# API Keys & Secrets Setup Guide

⚠️ **IMPORTANT**: This file contains sensitive setup instructions. **DO NOT** commit your actual API keys to the repository.

## 🔐 Security Setup

### 1. Google OAuth2 Setup

1. **Go to Google Cloud Console**: https://console.cloud.google.com/
2. **Create/Select Project** → APIs & Credentials → OAuth 2.0 Client IDs
3. **Configure OAuth Client**:
   - **Application type**: Web application
   - **Authorized JavaScript origins**: `http://localhost:8080`
   - **Authorized redirect URIs**: `http://localhost:8080/login/oauth2/code/google`
4. **Copy credentials** → Client ID and Client Secret

### 2. PayPal Setup

1. **Go to PayPal Developer**: https://developer.paypal.com/
2. **Dashboard** → My Apps & Credentials
3. **Create App** or select existing
4. **Copy credentials** → Client ID and Client Secret
5. **Mode**: Use `sandbox` for testing, `live` for production

### 3. Configure Application

1. **Copy the example file**:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

2. **Edit `application.properties`**:
   ```properties
   # Replace with your actual Google credentials
   spring.security.oauth2.client.registration.google.client-id=YOUR_ACTUAL_GOOGLE_CLIENT_ID
   spring.security.oauth2.client.registration.google.client-secret=YOUR_ACTUAL_GOOGLE_CLIENT_SECRET

   # Replace with your actual PayPal credentials
   paypal.client-id=YOUR_ACTUAL_PAYPAL_CLIENT_ID
   paypal.client-secret=YOUR_ACTUAL_PAYPAL_CLIENT_SECRET
   ```

## 🚫 What NOT to Do

- ❌ **Never commit** `application.properties` with real API keys
- ❌ **Never share** API keys in public repositories
- ❌ **Never use** production API keys for development

## ✅ What IS Safe

- ✅ **application.properties.example** - Contains placeholders only
- ✅ **README-API-SETUP.md** - Setup instructions
- ✅ **.gitignore** - Excludes sensitive files

## 🔧 Development Workflow

1. **Clone repository**
2. **Copy example config**: `cp application.properties.example application.properties`
3. **Add your API keys** to the copied file
4. **Run application**: `mvn spring-boot:run`
5. **Test features** with your credentials

## 🛡️ Security Best Practices

- Use **environment variables** for production deployments
- Rotate API keys regularly
- Use **IP restrictions** when possible
- Monitor API usage for unusual activity

## 📞 Need Help?

If you encounter issues:
1. Check that your API keys are correct
2. Verify Google Cloud Console and PayPal configurations
3. Ensure `application.properties` is not in `.gitignore` (it should be)
4. Check application logs for detailed error messages