# 📱 Cara Membuat File APK — Aish Financial Tracker

Folder ini adalah **proyek Android lengkap** yang membungkus aplikasi Aish Financial Tracker menjadi APK asli. Aplikasi HTML kamu sudah ada di dalam (`app/src/main/assets/index.html`), lengkap dengan data tersimpan permanen & offline.

Ada **3 cara** membuat APK-nya. Pilih yang paling cocok. **Cara A paling direkomendasikan** karena tidak perlu install software apa pun.

---

## ✅ CARA A — Build Otomatis di GitHub (Gratis, Tanpa Install Apa Pun)

Proyek ini sudah dilengkapi robot build (GitHub Actions). Kamu tinggal upload, robotnya yang membuat APK.

**Langkah:**

1. Buat akun di **github.com** (gratis) bila belum punya.
2. Klik **New repository** → beri nama (mis. `aish-financial-tracker`) → **Create repository**.
3. Di halaman repo baru, klik **"uploading an existing file"** → **drag semua isi folder `aish-android` ini** ke sana (termasuk folder `app`, `gradle`, `.github`, dan file `gradlew`, `settings.gradle`, dll). Lalu **Commit changes**.
   - Penting: yang di-upload adalah **isi** folder `aish-android`, bukan folder-nya. File `settings.gradle` harus berada di root repo.
4. Buka tab **Actions** di repo → akan muncul workflow **"Build APK"** yang berjalan otomatis (atau klik **Run workflow**).
5. Tunggu ± 3–5 menit sampai muncul centang hijau ✓.
6. Klik run tersebut → scroll ke bagian **Artifacts** → unduh **`AishFinancialTracker-debug-apk`**.
7. Ekstrak file zip-nya → di dalamnya ada **`app-debug.apk`**. Itu APK kamu! 🎉

APK ini bisa langsung dipasang di HP Android dan dijual. (Ini APK "debug" — ditandatangani kunci debug bawaan, tetap bisa di-install. Untuk versi "release" bertanda tangan sendiri, lihat bagian bawah.)

---

## ✅ CARA B — Layanan Web-to-APK (Paling Mudah untuk Non-Teknis)

Karena aplikasi ini satu file HTML mandiri, kamu bisa memakai layanan pembungkus tanpa coding:

- **Median.co** (dulu GoNative), **WebToApp.design**, atau **Appsgeyser** (sebagian gratis).

**Langkah singkat:**
1. Host file `index.html` agar punya alamat web (URL). Cara cepat & gratis: buka **app.netlify.com/drop** lalu drag file `index.html` ke sana → dapat URL.
2. Buka layanan web-to-APK, masukkan URL tersebut, atur nama & ikon aplikasi.
3. Unduh APK yang dihasilkan.

Catatan: sebagian layanan gratis menyisipkan watermark/iklan; versi berbayar biasanya menghilangkannya.

---

## ✅ CARA C — Android Studio (Di Komputer Sendiri)

Untuk kontrol penuh (dan build versi rilis).

1. Install **Android Studio** (gratis, dari developer.android.com).
2. **File → Open** → pilih folder `aish-android` ini.
3. Tunggu proses **Gradle sync** selesai (Android Studio otomatis mengunduh SDK & dependency yang dibutuhkan).
4. Menu **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
5. Setelah selesai, klik **locate** → APK ada di:
   `app/build/outputs/apk/debug/app-debug.apk`

Atau lewat terminal di dalam folder ini:
```bash
./gradlew assembleDebug
```

---

## 🔐 (Opsional) Membuat APK Rilis Bertanda Tangan

APK "release" bertanda tangan cocok untuk distribusi jangka panjang / Play Store.

1. Buat keystore (sekali saja, simpan baik-baik):
   ```bash
   keytool -genkey -v -keystore aish.keystore -alias aish -keyalg RSA -keysize 2048 -validity 10000
   ```
2. Tambahkan konfigurasi berikut di dalam blok `android { }` pada `app/build.gradle`:
   ```gradle
   signingConfigs {
       release {
           storeFile file("aish.keystore")
           storePassword "PASSWORD_KAMU"
           keyAlias "aish"
           keyPassword "PASSWORD_KAMU"
       }
   }
   buildTypes {
       release {
           signingConfig signingConfigs.release
           minifyEnabled false
       }
   }
   ```
3. Build:
   ```bash
   ./gradlew assembleRelease
   ```
   APK ada di `app/build/outputs/apk/release/app-release.apk`.

---

## ℹ️ Info Teknis Aplikasi

- **Package / App ID:** `id.aish.financialtracker`
- **Nama aplikasi:** Aish Financial Tracker
- **Minimal Android:** 7.0 (API 24)
- **Target Android:** 14 (API 34)
- **Izin (permissions):** tidak ada — aplikasi tidak meminta akses internet, kamera, atau data pribadi. 100% offline & privat.
- **Penyimpanan data:** localStorage di dalam WebView (tersimpan permanen di HP pengguna).
- **Isi aplikasi:** `app/src/main/assets/index.html` (untuk update tampilan/fitur, ganti file ini lalu build ulang).

> Catatan kecil: font khusus (Plus Jakarta Sans/Sora) dimuat dari internet saat online; ketika offline, aplikasi otomatis memakai font bawaan Android (Roboto) — tampilan tetap rapi & semua fitur berjalan normal.

Butuh bantuan build? Bilang saja, nanti dipandu langkah demi langkah. 💙
