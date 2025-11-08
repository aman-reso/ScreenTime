# Privacy Policy

This folder contains the privacy policy for the AppTime application.

## GitHub Pages Setup

To host this privacy policy on GitHub Pages:

1. **Commit and push the files to your GitHub repository:**
   ```bash
   git add docs/privacy-policy.html
   git commit -m "Add privacy policy HTML page"
   git push origin main
   ```
   (Replace `main` with your default branch name if different)

2. **Enable GitHub Pages:**
   - Go to your GitHub repository on GitHub.com
   - Click on **Settings** (in the repository menu)
   - Scroll down to **Pages** (in the left sidebar)
   - Under **Source**, select **Deploy from a branch**
   - Select **main** (or your default branch) and **/docs** folder
   - Click **Save**

3. **Access your privacy policy:**
   - Your privacy policy will be available at:
     `https://[your-username].github.io/[repository-name]/privacy-policy.html`
   - For example: `https://amankumar.github.io/ScreenTime/privacy-policy.html`

## Custom Domain (Optional)

If you want to use a custom domain:
1. Add a `CNAME` file in the `docs` folder with your domain name
2. Configure DNS records for your domain to point to GitHub Pages

## Updating the Privacy Policy

Simply edit `privacy-policy.html` and push the changes. GitHub Pages will automatically update within a few minutes.

