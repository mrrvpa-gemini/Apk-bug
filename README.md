<<<<<<< HEAD
# Apk-bug
Sc
=======
# 🦑 Manta'X WhatsApp Manager

WhatsApp Manager dengan integrasi Baileys + Myfunc. Build otomatis via GitHub Actions!

## 🚀 Quick Start

### 1. Push ke GitHub
```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/USERNAME/manta-x-whatsapp.git
git push -u origin main
```

### 2. GitHub Actions otomatis build APK
Setiap push ke `main`, GitHub Actions akan:
- Build Debug APK
- Build Release APK
- Upload ke GitHub Releases

### 3. Download APK
Buka tab **Actions** di repo GitHub, klik workflow run terbaru, download APK dari artifacts.

## 📁 Struktur Project

```
manta-x-whatsapp/
├── 📱 ANDROID-APK/          # Android Kotlin Project
├── 🖥️ BACKEND-SERVER/       # Node.js Express API
├── 🤖 TELEGRAM-BOT/         # Telegram Bot
├── 🗄️ DATABASE/             # MySQL Schemas
└── 📄 DOCS/                 # Documentation
```

## 🔧 Setup Backend

```bash
cd BACKEND-SERVER
cp .env.example .env
# Edit .env dengan konfigurasi kamu
npm install
npm start
```

## 🤖 Setup Telegram Bot

```bash
cd TELEGRAM-BOT
npm install
node bot.js
```

## 📝 Konfigurasi

### Android (ApiClient.kt)
Ganti `BASE_URL` di `ANDROID-APK/app/src/main/java/com/manta/whatsapp/services/ApiClient.kt`

### Backend (.env)
```env
PORT=3000
DB_HOST=localhost
DB_NAME=mantax_db
DB_USER=root
DB_PASS=password
JWT_SECRET=your-secret-key
TELEGRAM_BOT_TOKEN=8774991694:AAH3mLJMMsw5IjinO3pfvo9mWPyTINoLkeE
TELEGRAM_OWNER_ID=8469659582
```

## 🔐 Signing Release APK

Untuk sign release APK, tambahkan secrets di GitHub:
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

## 📜 License

Private - Manta'X Team
>>>>>>> 44265cf (🦑 Manta'X WhatsApp Manager - Initial Release)
