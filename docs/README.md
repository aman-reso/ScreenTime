# Legal Documents

This folder contains the legal documents for the AppTime application:
- **Privacy Policy** (`privacy-policy.html`)
- **Terms & Conditions** (`terms-and-conditions.html`)
- **Index Page** (`index.html`) - Navigation page for all legal documents

## GitHub Pages Setup

### Option 1: Automatic Deployment with GitHub Actions (Recommended)

A GitHub Actions workflow (`.github/workflows/pages.yml`) has been configured to automatically deploy these pages whenever you push changes to the `docs` folder.

1. **Enable GitHub Pages in repository settings:**
   - Go to your GitHub repository on GitHub.com
   - Click on **Settings** (in the repository menu)
   - Scroll down to **Pages** (in the left sidebar)
   - Under **Source**, select **GitHub Actions**
   - Click **Save**

2. **Commit and push the files:**
   ```bash
   git add docs/
   git add .github/workflows/pages.yml
   git commit -m "Add legal documents and GitHub Pages workflow"
   git push origin main
   ```

3. **The workflow will automatically deploy your pages!**
   - Check the **Actions** tab in your repository to see the deployment status
   - Your pages will be available at: `https://[your-username].github.io/[repository-name]/`

### Option 2: Manual Deployment from Branch

If you prefer not to use GitHub Actions:

1. **Commit and push the files:**
   ```bash
   git add docs/
   git commit -m "Add legal documents"
   git push origin main
   ```

2. **Enable GitHub Pages:**
   - Go to your GitHub repository on GitHub.com
   - Click on **Settings** → **Pages**
   - Under **Source**, select **Deploy from a branch**
   - Select **main** (or your default branch) and **/docs** folder
   - Click **Save**

3. **Access your pages:**
   - Index page: `https://[your-username].github.io/[repository-name]/`
   - Privacy Policy: `https://[your-username].github.io/[repository-name]/privacy-policy.html`
   - Terms & Conditions: `https://[your-username].github.io/[repository-name]/terms-and-conditions.html`

## Custom Domain (Optional)

If you want to use a custom domain:
1. Add a `CNAME` file in the `docs` folder with your domain name
2. Configure DNS records for your domain to point to GitHub Pages
3. Update the workflow if using GitHub Actions

## Updating the Documents

Simply edit the HTML files in the `docs` folder and push the changes. GitHub Pages will automatically update within a few minutes (or immediately if using GitHub Actions).

## Files Structure

```
docs/
├── index.html                 # Navigation page
├── privacy-policy.html        # Privacy Policy document
└── terms-and-conditions.html  # Terms & Conditions document
```

