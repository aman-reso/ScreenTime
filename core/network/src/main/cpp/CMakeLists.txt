cmake_minimum_required(VERSION 3.22.1)

project("security_vault")

add_library(
    security_vault
    SHARED
    security_vault.cpp
)

find_library(
    log-lib
    log
)

set_target_properties(
    security_vault
    PROPERTIES
    C_VISIBILITY_PRESET hidden
    CXX_VISIBILITY_PRESET hidden
    VISIBILITY_INLINES_HIDDEN ON
)

target_compile_options(
    security_vault
    PRIVATE
    -fvisibility=hidden
    -fvisibility-inlines-hidden
    -O2
    -fno-exceptions
    -fno-rtti
)

target_link_libraries(
    security_vault
    ${log-lib}
)

target_link_options(
    security_vault
    PRIVATE
    "-Wl,-z,max-page-size=16384"
    "-Wl,--exclude-libs,ALL"
)
