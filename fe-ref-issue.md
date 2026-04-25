# Frontend Refactoring Issues — Luminous Pro Transition

> Dokumen ini berisi instruksi bertahap (step-by-step) untuk merombak UI aplikasi menuju Luminous Pro Design System. Didesain agar mudah dipahami oleh AI Assistant, selesaikan issue ini secara berurutan.

---

## Issue 1 — Refactor Layout Utama Editor (Tipe A)
**Target:** `org/rtakebooth/app/ui/editor/EditorScreen.kt` & `EditorProperties.kt`
**Tujuan:** Mengimplementasikan "Left Canvas + Right Sidebar" untuk Module 1.

*   **Langkah 1:** Di `EditorScreen.kt`, pastikan layout dasarnya adalah sebuah `Row`.
*   **Langkah 2:** Buat elemen pertama di `Row` tersebut menjadi area Kanvas (`EditorCanvas`) dengan modifier `Modifier.weight(1f)`.
*   **Langkah 3:** Hapus Toolbar yang melayang, gabungkan fungsinya ke dalam kanvas atau ke sub-navigasi di bagian atas jika relevan.
*   **Langkah 4:** Buat elemen kedua di `Row` menjadi Sidebar Kanan (`EditorProperties`) dengan `Modifier.width(320.dp).background(MaterialTheme.colorScheme.surface)`.
*   **Langkah 5:** Jika ada menu pilihan (seperti Welcome, Capture, Sharing screen), buat `SubNavBar` yang diletakkan di atas `Row` menggunakan komponen `ScrollableRow` dengan *styling* teks *uppercase* dan warna primer.

---

## Issue 2 — Refactor Template/Preset Editor (Module 11)
**Target:** `org/rtakebooth/app/ui/template/TemplateEditorScreen.kt` & komponen pendukung.
**Tujuan:** Menyesuaikan Template Editor yang baru saja dibuat agar sesuai dengan standar Luminous Pro.

*   **Langkah 1:** Di `TemplateEditorScreen.kt`, ubah susunan **3-Panel UI** (kiri-tengah-kanan) menjadi **2-Panel UI** (Tengah-Kanan) sama seperti Issue 1.
*   **Langkah 2:** Gabungkan alat-alat penambah komponen (Add Text, Add Photo Placeholder, dll) yang tadinya di Sidebar Kiri ke bagian atas Sidebar Kanan atau jadikan *Floating Toolbar* transparan di atas kanvas.
*   **Langkah 3:** Pastikan warna Sidebar Kanan menggunakan `surface` dan panel konfigurasinya terbungkus rapi.

---

## Issue 3 — Refactor Photo Layout Editor (Module 6)
**Target:** `org/rtakebooth/app/ui/setup/LayoutScreen.kt`
**Tujuan:** Menggunakan layout Tipe A untuk pengaturan pemrosesan foto akhir.

*   **Langkah 1:** Ubah `LayoutScreen.kt` agar memiliki struktur Canvas di kiri (`weight(1f)`) dan Panel Properti di kanan (`width(320.dp)`).
*   **Langkah 2:** Pindahkan sub-tab menu (Effects, AI Portrait, BG Removal, Survey) ke *Sub-Navigasi Horizontal* tepat di bawah `TopBar`.
*   **Langkah 3:** Form masing-masing tab ditampilkan di Sidebar Kanan. Untuk sementara, Canvas Kiri bisa diisi dengan ilustrasi statis gambar kamera atau tulisan "Preview Area" sebelum logika preview FFmpeg dibuat.

---

## Issue 4 — Refactor Non-Visual Settings (Tipe B)
**Target:** Modul 2 (General), 3 (Capture), 4 (Camera), 5 (Attendant), 8 (Print).
**Tujuan:** Membuat form input berada di tengah layar tanpa Sidebar.

*   **Langkah 1:** Buka `GeneralScreen.kt`, `CaptureScreen.kt`, `CameraScreen.kt`, `AttendantScreen.kt`, `PrintScreen.kt`.
*   **Langkah 2:** Bungkus konten form di masing-masing file tersebut dengan `Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter)`.
*   **Langkah 3:** Di dalam `Box`, gunakan `Column` dengan `Modifier.widthIn(max = 800.dp).padding(32.dp)` agar form tidak terlalu melebar pada monitor resolusi tinggi.
*   **Langkah 4:** Pastikan setiap grup input dibungkus dengan kartu/panel warna `surfaceVariant` (`#2D2D2D`).

---

## Issue 5 — Refactor UI Components (Toggle, TextField, Button)
**Target:** `org/rtakebooth/app/ui/components/FormComponents.kt` (dan komponen dasar lainnya).
**Tujuan:** Memoles elemen form dasar agar menyamai estetika Luminous Pro.

*   **Langkah 1:** Perbarui komponen *Toggle Switch* agar menggunakan warna pink `primary` saat aktif, dan warna abu-abu gelap `outline` saat nonaktif.
*   **Langkah 2:** Perbarui desain komponen `TextField` bawaan (jika ada *custom text field*) agar memiliki latar belakang `surfaceVariant` dan border berubah menjadi pink saat *fokus*.
*   **Langkah 3:** Seragamkan *Border Radius* (`shape = RoundedCornerShape(8.dp)` atau `12.dp`) untuk semua komponen kartu form dan tombol.

---

## Catatan Kritis untuk AI Selanjutnya
> Jangan ubah `AppTheme.kt` dan `TopBar.kt` lagi karena sudah dikonfigurasi. Fokus Anda hanya pada merombak layar-layar spesifik di atas dengan mengikuti **Layout Tipe A** atau **Layout Tipe B** sesuai instruksi. Selesaikan satu issue per *commit*.
