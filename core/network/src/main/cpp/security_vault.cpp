#include <jni.h>
#include <string>
#include <vector>
#include <random>
#include <cstring>
#include <fcntl.h>
#include <unistd.h>
#include <cstdint>

// ─────────────────────────────────────────────────────────────────────────────
// Anti-Analysis / Memory Sanitization
// ─────────────────────────────────────────────────────────────────────────────

static inline void secure_wipe(void* v, size_t n) {
    volatile uint8_t* p = reinterpret_cast<volatile uint8_t*>(v);
    while (n--) *p++ = 0;
}

static bool isCompromised() {
    // 1. TracerPid check via /proc/self/status
    int fd = open("/proc/self/status", O_RDONLY);
    if (fd >= 0) {
        char buf[1024];
        ssize_t bytesRead = read(fd, buf, sizeof(buf) - 1);
        close(fd);
        if (bytesRead > 0) {
            buf[bytesRead] = '\0';
            const char* tracer = strstr(buf, "TracerPid:\t");
            if (tracer) {
                tracer += 11;
                while (*tracer == ' ' || *tracer == '\t') tracer++;
                if (*tracer != '0' && *tracer != '\n' && *tracer != '\0') {
                    return true;
                }
            }
        }
    }

    // 2. Scan /proc/self/maps for Frida artifacts & injected memory regions
    fd = open("/proc/self/maps", O_RDONLY);
    if (fd >= 0) {
        char buf[2048];
        ssize_t bytesRead;
        while ((bytesRead = read(fd, buf, sizeof(buf) - 1)) > 0) {
            buf[bytesRead] = '\0';
            if (strstr(buf, "frida") != nullptr ||
                strstr(buf, "gum-js") != nullptr ||
                strstr(buf, "gadget") != nullptr ||
                strstr(buf, "linjector") != nullptr) {
                close(fd);
                return true;
            }
        }
        close(fd);
    }

    // 3. Package sanity check via /proc/self/cmdline
    fd = open("/proc/self/cmdline", O_RDONLY);
    if (fd >= 0) {
        char cmdline[256];
        ssize_t bytesRead = read(fd, cmdline, sizeof(cmdline) - 1);
        close(fd);
        if (bytesRead > 0) {
            cmdline[bytesRead] = '\0';
            if (strstr(cmdline, "screentime") == nullptr) {
                return true;
            }
        }
    }

    return false;
}

// ─────────────────────────────────────────────────────────────────────────────
// Obfuscated Master Key & URLs
// ─────────────────────────────────────────────────────────────────────────────

// "W1nt3r_S3cur1ty_K3y_2026_@App!#9" (32 bytes) split across 4 non-linear mathematical parts
static const uint8_t K_PART0[8] = {0x6d, 0x0c, 0x2e, 0x37, 0x75, 0x3b, 0x13, 0x1c};
static const uint8_t K_PART1[8] = {0x4c, 0x19, 0x00, 0x02, 0x5a, 0x12, 0x18, 0x03};
static const uint8_t K_PART2[8] = {0xda, 0xab, 0xe6, 0xf9, 0x9f, 0x84, 0x89, 0xf4};
static const uint8_t K_PART3[8] = {0x1b, 0x0f, 0x13, 0x15, 0x18, 0x52, 0x25, 0x30};

static inline void assemble_master_key(uint8_t out[32]) {
    for (int i = 0; i < 8; ++i) out[i]      = static_cast<uint8_t>(K_PART0[i] ^ static_cast<uint8_t>(0x3A + i * 3));
    for (int i = 0; i < 8; ++i) out[8 + i]  = static_cast<uint8_t>(K_PART1[i] ^ static_cast<uint8_t>(0x7F - i * 5));
    for (int i = 0; i < 8; ++i) out[16 + i] = static_cast<uint8_t>(K_PART2[i] ^ static_cast<uint8_t>(0x91 + i * 7));
    for (int i = 0; i < 8; ++i) out[24 + i] = static_cast<uint8_t>(K_PART3[i] ^ static_cast<uint8_t>(0x44 ^ (i * 11)));
}

// "https://api.winterdating.com" (28 bytes)
static const uint8_t ENC_PROD_URL[28] = {
    0xfc, 0xe9, 0x92, 0x9f, 0x83, 0xc3, 0xed, 0xe4, 
    0xad, 0xa5, 0xb7, 0x09, 0x5f, 0x58, 0x54, 0x77, 
    0x61, 0x7f, 0x72, 0x7e, 0x14, 0x00, 0x1c, 0x1c, 
    0x52, 0x26, 0x21, 0x3a
};

// "https://connect-api-test-324708289864.us-central1.run.app" (57 bytes)
static const uint8_t ENC_DEV_URL[57] = {
    0x82, 0x87, 0x80, 0x8d, 0xb5, 0xf5, 0xff, 0xf6, 
    0x41, 0x44, 0x42, 0x5b, 0x5b, 0x64, 0x7c, 0x3c, 
    0x7b, 0x13, 0x0d, 0x40, 0x02, 0x1a, 0x33, 0x3d, 
    0x7f, 0x68, 0x6e, 0x91, 0x99, 0x87, 0x80, 0xb3, 
    0xb2, 0xaa, 0xac, 0xab, 0xd2, 0xc1, 0x85, 0x8a, 
    0xef, 0xa8, 0xa9, 0xbb, 0xaa, 0x55, 0x49, 0x5d, 
    0x0b, 0x2d, 0x76, 0x78, 0x78, 0x31, 0x01, 0x19, 
    0x02
};

// "203569877791-c1ke6b8kg28gg7og5ish10oo26b9ffsb.apps.googleusercontent.com" (72 bytes)
static const uint8_t ENC_GOOGLE_ID[72] = {
    0xcc, 0xf7, 0xfb, 0xe4, 0xec, 0x1a, 0x1c, 0x1a, 
    0x01, 0x08, 0x39, 0x38, 0x3f, 0x78, 0x2d, 0x0e, 
    0x0b, 0x41, 0x1a, 0x79, 0x21, 0x34, 0x66, 0x65, 
    0xc1, 0xc8, 0x87, 0xd6, 0xe5, 0xbe, 0xe5, 0xe6, 
    0xf6, 0xd6, 0xd8, 0x9e, 0x95, 0xf1, 0xf2, 0xaf, 
    0xef, 0xb9, 0x46, 0x5a, 0x50, 0x15, 0x5d, 0x75, 
    0x7e, 0x64, 0x36, 0x06, 0x05, 0x1c, 0x13, 0x11, 
    0x23, 0x3a, 0x23, 0x3c, 0xd0, 0xc8, 0xc3, 0xdb, 
    0xca, 0xe2, 0xe6, 0xe5, 0xb4, 0x80, 0x8b, 0x80
};

static std::string decode_string(const uint8_t* enc, size_t len, uint8_t seed) {
    std::string s;
    s.resize(len);
    for (size_t i = 0; i < len; ++i) {
        uint8_t mask = static_cast<uint8_t>((seed + (i * 7)) ^ 0xA3);
        s[i] = static_cast<char>(enc[i] ^ mask);
    }
    return s;
}

// ─────────────────────────────────────────────────────────────────────────────
// Native Cryptography (Pure C++ Rijndael AES-256-CBC)
// ─────────────────────────────────────────────────────────────────────────────

static const uint8_t sbox0[256] = {
    0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
    0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,
    0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,
    0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,
    0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84,
    0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf,
    0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8,
    0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2,
    0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
    0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb,
    0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79,
    0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08,
    0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a,
    0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e,
    0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf,
    0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16
};

static const uint32_t te0[256] = {
    0xc66363a5, 0xf87c7c84, 0xee777799, 0xf67b7b8d, 0xfff2f20d, 0xd66b6bbd, 0xde6f6fb1, 0x91c5c554,
    0x60303050, 0x02010103, 0xce6767a9, 0x562b2b7d, 0xe7fefe19, 0xb5d7d762, 0x4dababe6, 0xec76769a,
    0x8fcaca45, 0x1f82829d, 0x89c9c940, 0xfa7d7d87, 0xeffafa15, 0xb25959eb, 0x8e4747c9, 0xfbf0f00b,
    0x41adadec, 0xb3d4d467, 0x5fa2a2fd, 0x45afafea, 0x239c9cbf, 0x53a4a4f7, 0xe4727296, 0x9bc0c05b,
    0x75b7b7c2, 0xe1fdfd1c, 0x3d9393ae, 0x4c26266a, 0x6c36365a, 0x7e3f3f41, 0xf5f7f702, 0x83cccc4f,
    0x6834345c, 0x51a5a5f4, 0xd1e5e534, 0xf9f1f108, 0xe2717193, 0xabd8d873, 0x62313153, 0x2a15153f,
    0x0804040c, 0x95c7c752, 0x46232365, 0x9dc3c35e, 0x30181828, 0x379696a1, 0x0a05050f, 0x2f9a9ab5,
    0x0e070709, 0x24121236, 0x1b80809b, 0xdfe2e23d, 0xcdebeb26, 0x4e272769, 0x7fb2b2cd, 0xea75759f,
    0x1209091b, 0x1d83839e, 0x582c2c74, 0x341a1a2e, 0x361b1b2d, 0xdc6e6eb2, 0xb45a5aee, 0x5ba0a0fb,
    0xa45252f6, 0x763b3b4d, 0xb7d6d661, 0x7db3b3ce, 0x5229297b, 0xdde3e33e, 0x5e2f2f71, 0x13848497,
    0xa65353f5, 0xb9d1d168, 0x00000000, 0xc1eded2c, 0x40202060, 0xe3fcfc1f, 0x79b1b1c8, 0xb65b5bed,
    0xd46a6abe, 0x8dcbcb46, 0x67bebed9, 0x7239394b, 0x944a4ade, 0x984c4cd4, 0xb05858e8, 0x85cfcf4a,
    0xbbd0d06b, 0xc5efef2a, 0x4faaaae5, 0xedfbfb16, 0x864343c5, 0x9a4d4dd7, 0x66333355, 0x11858594,
    0x8a4545cf, 0xe9f9f910, 0x04020206, 0xfe7f7f81, 0xa05050f0, 0x783c3c44, 0x259f9fba, 0x4ba8a8e3,
    0xa25151f3, 0x5da3a3fe, 0x804040c0, 0x058f8f8a, 0x3f9292ad, 0x219d9dbc, 0x70383848, 0xf1f5f504,
    0x63bcbcdf, 0x77b6b6c1, 0xafdada75, 0x42212163, 0x20101030, 0xe5ffff1a, 0xfdf3f30e, 0xbfd2d26d,
    0x81cdcd4c, 0x180c0c14, 0x26131335, 0xc3ecec2f, 0xbe5f5fe1, 0x359797a2, 0x884444cc, 0x2e171739,
    0x93c4c457, 0x55a7a7f2, 0xfc7e7e82, 0x7a3d3d47, 0xc86464ac, 0xba5d5de7, 0x3219192b, 0xe6737395,
    0xc06060a0, 0x19818198, 0x9e4f4fd1, 0xa3dcdc7f, 0x44222266, 0x542a2a7e, 0x3b9090ab, 0x0b888883,
    0x8c4646ca, 0xc7eeee29, 0x6bb8b8d3, 0x2814143c, 0xa7dede79, 0xbc5e5ee2, 0x160b0b1d, 0xaddbdb76,
    0xdbe0e03b, 0x64323256, 0x743a3a4e, 0x140a0a1e, 0x924949db, 0x0c06060a, 0x4824246c, 0xb85c5ce4,
    0x9fc2c25d, 0xbdd3d36e, 0x43acacef, 0xc46262a6, 0x399191a8, 0x319595a4, 0xd3e4e437, 0xf279798b,
    0xd5e7e732, 0x8bc8c843, 0x6e373759, 0xda6d6db7, 0x018d8d8c, 0xb1d5d564, 0x9c4e4ed2, 0x49a9a9e0,
    0xd86c6cb4, 0xac5656fa, 0xf3f4f407, 0xcfeaea25, 0xca6565af, 0xf47a7a8e, 0x47aeaee9, 0x10080818,
    0x6fbabad5, 0xf0787888, 0x4a25256f, 0x5c2e2e72, 0x381c1c24, 0x57a6a6f1, 0x73b4b4c7, 0x97c6c651,
    0xcbe8e823, 0xa1dddd7c, 0xe874749c, 0x3e1f1f21, 0x964b4bdd, 0x61bdbddc, 0x0d8b8b86, 0x0f8a8a85,
    0xe0707090, 0x7c3e3e42, 0x71b5b5c4, 0xcc6666aa, 0x904848d8, 0x06030305, 0xf7f6f601, 0x1c0e0e12,
    0xc26161a3, 0x6a35355f, 0xae5757f9, 0x69b9b9d0, 0x17868691, 0x99c1c158, 0x3a1d1d27, 0x279e9eb9,
    0xd9e1e138, 0xebf8f813, 0x2b9898b3, 0x22111133, 0xd26969bb, 0xa9d9d970, 0x078e8e89, 0x339494a7,
    0x2d9b9bb6, 0x3c1e1e22, 0x15878792, 0xc9e9e920, 0x87cece49, 0xaa5555ff, 0x50282878, 0xa5dfdf7a,
    0x038c8c8f, 0x59a1a1f8, 0x09898980, 0x1a0d0d17, 0x65bfbfda, 0xd7e6e631, 0x844242c6, 0xd06868b8,
    0x824141c3, 0x299999b0, 0x5a2d2d77, 0x1e0f0f11, 0x7bb0b0cb, 0xa85454fc, 0x6dbbbbd6, 0x2c16163a
};

static inline uint32_t rotr(uint32_t val, int shift) {
    return (val >> shift) | (val << (32 - shift));
}

#define TE0(x) (te0[(x)])
#define TE1(x) (rotr(te0[(x)], 8))
#define TE2(x) (rotr(te0[(x)], 16))
#define TE3(x) (rotr(te0[(x)], 24))

static inline uint32_t get_u32_be(const uint8_t* p) {
    return (static_cast<uint32_t>(p[0]) << 24) |
           (static_cast<uint32_t>(p[1]) << 16) |
           (static_cast<uint32_t>(p[2]) << 8)  |
            static_cast<uint32_t>(p[3]);
}

static inline void put_u32_be(uint8_t* p, uint32_t v) {
    p[0] = static_cast<uint8_t>(v >> 24);
    p[1] = static_cast<uint8_t>(v >> 16);
    p[2] = static_cast<uint8_t>(v >> 8);
    p[3] = static_cast<uint8_t>(v);
}

static const uint8_t rcon[15] = {
    0x00, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36, 0x6C, 0xD8, 0xAB, 0x4D
};

static void aes256_expand_key(const uint8_t key[32], uint32_t xk[60]) {
    for (int i = 0; i < 8; ++i) {
        xk[i] = get_u32_be(key + i * 4);
    }
    for (int i = 8; i < 60; ++i) {
        uint32_t temp = xk[i - 1];
        if (i % 8 == 0) {
            uint8_t b0 = static_cast<uint8_t>(temp >> 24);
            uint8_t b1 = static_cast<uint8_t>(temp >> 16);
            uint8_t b2 = static_cast<uint8_t>(temp >> 8);
            uint8_t b3 = static_cast<uint8_t>(temp);
            temp = (static_cast<uint32_t>(sbox0[b1] ^ rcon[i / 8]) << 24) |
                   (static_cast<uint32_t>(sbox0[b2]) << 16) |
                   (static_cast<uint32_t>(sbox0[b3]) << 8)  |
                    static_cast<uint32_t>(sbox0[b0]);
        } else if (i % 8 == 4) {
            uint8_t b0 = static_cast<uint8_t>(temp >> 24);
            uint8_t b1 = static_cast<uint8_t>(temp >> 16);
            uint8_t b2 = static_cast<uint8_t>(temp >> 8);
            uint8_t b3 = static_cast<uint8_t>(temp);
            temp = (static_cast<uint32_t>(sbox0[b0]) << 24) |
                   (static_cast<uint32_t>(sbox0[b1]) << 16) |
                   (static_cast<uint32_t>(sbox0[b2]) << 8)  |
                    static_cast<uint32_t>(sbox0[b3]);
        }
        xk[i] = xk[i - 8] ^ temp;
    }
}

static void aes256_encrypt_block(const uint8_t src[16], const uint32_t xk[60], uint8_t dst[16]) {
    uint32_t s0 = get_u32_be(src + 0)  ^ xk[0];
    uint32_t s1 = get_u32_be(src + 4)  ^ xk[1];
    uint32_t s2 = get_u32_be(src + 8)  ^ xk[2];
    uint32_t s3 = get_u32_be(src + 12) ^ xk[3];

    int k = 4;
    uint32_t t0, t1, t2, t3;
    for (int r = 0; r < 13; ++r) {
        t0 = xk[k + 0] ^ TE0(static_cast<uint8_t>(s0 >> 24)) ^ TE1(static_cast<uint8_t>(s1 >> 16)) ^ TE2(static_cast<uint8_t>(s2 >> 8)) ^ TE3(static_cast<uint8_t>(s3));
        t1 = xk[k + 1] ^ TE0(static_cast<uint8_t>(s1 >> 24)) ^ TE1(static_cast<uint8_t>(s2 >> 16)) ^ TE2(static_cast<uint8_t>(s3 >> 8)) ^ TE3(static_cast<uint8_t>(s0));
        t2 = xk[k + 2] ^ TE0(static_cast<uint8_t>(s2 >> 24)) ^ TE1(static_cast<uint8_t>(s3 >> 16)) ^ TE2(static_cast<uint8_t>(s0 >> 8)) ^ TE3(static_cast<uint8_t>(s1));
        t3 = xk[k + 3] ^ TE0(static_cast<uint8_t>(s3 >> 24)) ^ TE1(static_cast<uint8_t>(s0 >> 16)) ^ TE2(static_cast<uint8_t>(s1 >> 8)) ^ TE3(static_cast<uint8_t>(s2));
        k += 4;
        s0 = t0; s1 = t1; s2 = t2; s3 = t3;
    }

    // Last round uses S-box directly without MixColumns
    s0 = (static_cast<uint32_t>(sbox0[t0 >> 24]) << 24) |
         (static_cast<uint32_t>(sbox0[(t1 >> 16) & 0xFF]) << 16) |
         (static_cast<uint32_t>(sbox0[(t2 >> 8) & 0xFF]) << 8) |
          static_cast<uint32_t>(sbox0[t3 & 0xFF]);
    s1 = (static_cast<uint32_t>(sbox0[t1 >> 24]) << 24) |
         (static_cast<uint32_t>(sbox0[(t2 >> 16) & 0xFF]) << 16) |
         (static_cast<uint32_t>(sbox0[(t3 >> 8) & 0xFF]) << 8) |
          static_cast<uint32_t>(sbox0[t0 & 0xFF]);
    s2 = (static_cast<uint32_t>(sbox0[t2 >> 24]) << 24) |
         (static_cast<uint32_t>(sbox0[(t3 >> 16) & 0xFF]) << 16) |
         (static_cast<uint32_t>(sbox0[(t0 >> 8) & 0xFF]) << 8) |
          static_cast<uint32_t>(sbox0[t1 & 0xFF]);
    s3 = (static_cast<uint32_t>(sbox0[t3 >> 24]) << 24) |
         (static_cast<uint32_t>(sbox0[(t0 >> 16) & 0xFF]) << 16) |
         (static_cast<uint32_t>(sbox0[(t1 >> 8) & 0xFF]) << 8) |
          static_cast<uint32_t>(sbox0[t2 & 0xFF]);

    s0 ^= xk[k + 0];
    s1 ^= xk[k + 1];
    s2 ^= xk[k + 2];
    s3 ^= xk[k + 3];

    put_u32_be(dst + 0, s0);
    put_u32_be(dst + 4, s1);
    put_u32_be(dst + 8, s2);
    put_u32_be(dst + 12, s3);
}

// ─────────────────────────────────────────────────────────────────────────────
// Native Base64 Encoder (pure C++, NO JNI call to android.util.Base64)
// ─────────────────────────────────────────────────────────────────────────────

static const char B64_TABLE[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

static std::string native_base64_encode(const uint8_t* data, size_t len) {
    std::string out;
    out.reserve(((len + 2) / 3) * 4);
    for (size_t i = 0; i < len; i += 3) {
        uint32_t val = (static_cast<uint32_t>(data[i]) << 16);
        if (i + 1 < len) val |= (static_cast<uint32_t>(data[i + 1]) << 8);
        if (i + 2 < len) val |= static_cast<uint32_t>(data[i + 2]);

        out.push_back(B64_TABLE[(val >> 18) & 0x3F]);
        out.push_back(B64_TABLE[(val >> 12) & 0x3F]);
        out.push_back((i + 1 < len) ? B64_TABLE[(val >> 6) & 0x3F] : '=');
        out.push_back((i + 2 < len) ? B64_TABLE[val & 0x3F] : '=');
    }
    return out;
}

static void get_random_bytes(uint8_t* buffer, size_t length) {
    int fd = open("/dev/urandom", O_RDONLY);
    if (fd >= 0) {
        ssize_t res = read(fd, buffer, length);
        close(fd);
        if (res == static_cast<ssize_t>(length)) return;
    }
    std::random_device rd;
    for (size_t i = 0; i < length; ++i) {
        buffer[i] = static_cast<uint8_t>(rd() & 0xFF);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Native Methods (Static & Hidden)
// ─────────────────────────────────────────────────────────────────────────────

static __attribute__((visibility("hidden"))) jstring nativeMint(
    JNIEnv* env,
    jclass /* clazz */,
    jlong timestamp
) {
    if (isCompromised()) {
        return env->NewStringUTF("");
    }

    // 1. Generate 16-byte cryptographically secure IV & 8-byte Nonce
    uint8_t iv[16];
    get_random_bytes(iv, sizeof(iv));

    uint8_t nonceBytes[8];
    get_random_bytes(nonceBytes, sizeof(nonceBytes));
    char nonceHex[17];
    for (int i = 0; i < 8; ++i) {
        snprintf(nonceHex + (i * 2), 3, "%02x", nonceBytes[i]);
    }
    nonceHex[16] = '\0';

    // 2. Format: "<timestamp>:<nonceHex>"
    std::string payload = std::to_string(timestamp) + ":" + std::string(nonceHex);
    size_t payloadLen = payload.size();

    // 3. PKCS#7 Padding
    size_t padLen = 16 - (payloadLen % 16);
    size_t totalLen = payloadLen + padLen;
    std::vector<uint8_t> padded(totalLen);
    std::memcpy(padded.data(), payload.data(), payloadLen);
    std::memset(padded.data() + payloadLen, static_cast<uint8_t>(padLen), padLen);

    // 4. Assemble Master Key in stack memory & Expand Key
    uint8_t masterKey[32];
    assemble_master_key(masterKey);

    uint32_t xk[60];
    aes256_expand_key(masterKey, xk);

    // Secure wipe raw key immediately after expansion
    secure_wipe(masterKey, sizeof(masterKey));

    // 5. AES-256-CBC Encrypt
    std::vector<uint8_t> combined(16 + totalLen);
    std::memcpy(combined.data(), iv, 16);

    uint8_t prev[16];
    std::memcpy(prev, iv, 16);

    for (size_t offset = 0; offset < totalLen; offset += 16) {
        uint8_t blockIn[16];
        for (int i = 0; i < 16; ++i) {
            blockIn[i] = padded[offset + i] ^ prev[i];
        }
        uint8_t blockOut[16];
        aes256_encrypt_block(blockIn, xk, blockOut);
        std::memcpy(combined.data() + 16 + offset, blockOut, 16);
        std::memcpy(prev, blockOut, 16);
    }

    // Secure wipe round keys and intermediate vectors
    secure_wipe(xk, sizeof(xk));
    secure_wipe(padded.data(), padded.size());

    // 6. Native Base64 Encode
    std::string tokenBase64 = native_base64_encode(combined.data(), combined.size());
    secure_wipe(combined.data(), combined.size());

    return env->NewStringUTF(tokenBase64.c_str());
}

static __attribute__((visibility("hidden"))) jstring nativeUrl(
    JNIEnv* env,
    jclass /* clazz */,
    jboolean isDebug
) {
    if (isDebug) {
        std::string url = decode_string(ENC_DEV_URL, sizeof(ENC_DEV_URL), 0x49);
        return env->NewStringUTF(url.c_str());
    } else {
        std::string url = decode_string(ENC_PROD_URL, sizeof(ENC_PROD_URL), 0x37);
        return env->NewStringUTF(url.c_str());
    }
}

static __attribute__((visibility("hidden"))) jstring nativeBill(
    JNIEnv* env,
    jclass /* clazz */
) {
    std::string url = decode_string(ENC_DEV_URL, sizeof(ENC_DEV_URL), 0x49);
    return env->NewStringUTF(url.c_str());
}

static __attribute__((visibility("hidden"))) jstring nativeGid(
    JNIEnv* env,
    jclass /* clazz */
) {
    std::string gid = decode_string(ENC_GOOGLE_ID, sizeof(ENC_GOOGLE_ID), 0x5D);
    return env->NewStringUTF(gid.c_str());
}

// ─────────────────────────────────────────────────────────────────────────────
// Dynamic JNI Registration (NO exported Java_ symbols in .dynsym)
// ─────────────────────────────────────────────────────────────────────────────

static const JNINativeMethod gVaultMethods[] = {
    {const_cast<char*>("nvMint"), const_cast<char*>("(J)Ljava/lang/String;"), reinterpret_cast<void*>(nativeMint)},
    {const_cast<char*>("nvUrl"),  const_cast<char*>("(Z)Ljava/lang/String;"), reinterpret_cast<void*>(nativeUrl)},
    {const_cast<char*>("nvBill"), const_cast<char*>("()Ljava/lang/String;"),  reinterpret_cast<void*>(nativeBill)},
    {const_cast<char*>("nvGid"),  const_cast<char*>("()Ljava/lang/String;"),  reinterpret_cast<void*>(nativeGid)}
};

// "com/app/screentime/core/network/security/NativeSecurityVault" (60 bytes)
static const uint8_t ENC_CLASS_NAME[60] = {
    0x2b, 0x27, 0x25, 0x67, 0x29, 0x38, 0x38, 0x67, 
    0x3b, 0x2b, 0x3a, 0x2d, 0x2d, 0x26, 0x3c, 0x21, 
    0x25, 0x2d, 0x67, 0x2b, 0x27, 0x3a, 0x2d, 0x67, 
    0x26, 0x2d, 0x3c, 0x3f, 0x27, 0x3a, 0x23, 0x67, 
    0x3b, 0x2d, 0x2b, 0x3d, 0x3a, 0x21, 0x3c, 0x31, 
    0x67, 0x06, 0x29, 0x3c, 0x21, 0x3e, 0x2d, 0x1b, 
    0x2d, 0x2b, 0x3d, 0x3a, 0x21, 0x3c, 0x31, 0x1e, 
    0x29, 0x3d, 0x24, 0x3c
};

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* /* reserved */) {
    JNIEnv* env = nullptr;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    // Dynamic reconstruction of target class name
    char className[sizeof(ENC_CLASS_NAME) + 1];
    for (size_t i = 0; i < sizeof(ENC_CLASS_NAME); ++i) {
        className[i] = static_cast<char>(ENC_CLASS_NAME[i] ^ 0x48);
    }
    className[sizeof(ENC_CLASS_NAME)] = '\0';

    jclass clazz = env->FindClass(className);
    secure_wipe(className, sizeof(className));

    if (!clazz) {
        return JNI_ERR;
    }

    if (env->RegisterNatives(
            clazz,
            gVaultMethods,
            sizeof(gVaultMethods) / sizeof(gVaultMethods[0])
        ) < 0) {
        return JNI_ERR;
    }

    return JNI_VERSION_1_6;
}
