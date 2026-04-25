# Frontend Refactoring Plan — Luminous Pro Design System

## Tujuan
Dokumen ini adalah panduan tingkat tinggi (High-Level Plan) untuk merombak UI aplikasi RtakeBooth agar selaras dengan **Luminous Pro Design System**. Transformasi ini mengubah paradigma tata letak dari gaya konvensional "kiri-ke-kanan" menjadi layout berbasis Kanvas sentral yang modern dan terfokus.

## Perubahan Arsitektur & Struktur UI

### 1. Global Layout (Sudah Diimplementasikan)
*   **Navigasi Pindah ke Atas:** Sidebar kiri dihapus sepenuhnya. Seluruh menu navigasi utama (General, Capture, Sharing, dll) kini berada di `TopBar` yang dapat di-scroll secara horizontal jika menu terlalu banyak.
*   **Launch Booth & Kiosk Mode:** Terdapat tombol `Launch Booth` di TopBar untuk memicu Kiosk Mode (mode *full-screen* Booth Player saat event berlangsung).
*   **Theme & Design Tokens:** Warna dasar menggunakan *Dark Mode* (`#121212`) dengan panel warna `Surface` (`#1E1E1E`), elemen interaktif warna `Surface Variant` (`#2D2D2D`), dan warna utama Pink Luminous (`#E91E63`). Font utama diseragamkan ke `Inter` atau *sans-serif* sejenis.

### 2. Tipe Layout Layar (Screen Layouts)
Aplikasi kini dibagi menjadi dua tipe layar yang membutuhkan penanganan berbeda:

#### Tipe A: Visual/Editor Screens (Module 1, 6, 11)
*Layar yang membutuhkan pratinjau visual (Canvas).*
*   **Sub-Navigasi (Khusus Editor):** Diletakkan secara sentral tepat di bawah TopBar.
*   **Workspace Kiri (Canvas):** Mengisi sisa ruang kosong secara maksimal (`weight(1f)`). Elemen visual diletakkan di tengah area ini.
*   **Sidebar Kanan (Properties):** Panel statis (`width = 320.dp`) yang memuat seluruh *toggle*, *slider*, dan input konfigurasi yang sebelumnya ada di sidebar kiri/kanan.

#### Tipe B: Non-Visual Settings (Module 2, 3, 4, 5, 8, 10, Event List)
*Layar pengaturan murni tanpa pratinjau kanvas (seperti Pengaturan Umum, Kamera, Printer).*
*   **Tata Letak Sentral:** Form input tidak ditepi melainkan **diletakkan di tengah** (Centered Layout) dengan ukuran *container* maksimal yang wajar (misalnya `MaxWidth = 800.dp`).
*   **Keseragaman Elemen:** Form dibungkus dalam `ElevatedCard` atau komponen berlatar `SurfaceVariant` dengan sudut melengkung.

---

## Aturan Desain (Luminous Pro Tokens)
Saat me-refactor komponen, patuhi token warna berikut yang sudah ada di `MaterialTheme.colorScheme`:
*   `primary`: #E91E63 (Tombol utama, switch toggle ON, border elemen aktif)
*   `background`: #121212 (Latar belakang utama aplikasi/kanvas)
*   `surface`: #1E1E1E (Latar panel kanan, latar sub-nav)
*   `surfaceVariant`: #2D2D2D (Latar untuk form field, card konfigurasi)
*   `onSurface`: #E5E2E1 (Teks utama)
*   `outline`: #474747 (Border halus, switch toggle OFF)
