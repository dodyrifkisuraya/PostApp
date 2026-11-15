# PostApp — Mini Android Application

Aplikasi Android sederhana yang dibangun menggunakan **Kotlin** dengan pola arsitektur **Model-View-Presenter (MVP)**, **Retrofit** untuk networking, dan **Room Database** untuk local storage.

---

## Fitur Utama

### 1. Halaman Login
* Menggunakan **RelativeLayout**.
* Form input `Username` dan `Password` dengan validasi non-empty langsung di sisi UI.
* Memberikan simulasi loading visual dan mencegah interaksi berulang saat proses validasi.
* Meneruskan username ke Dashboard untuk ditampilkan sebagai profil pengguna.

### 2. Halaman Dashboard
* Menggunakan **ConstraintLayout**.
* Menampilkan nama profil pengguna dari sesi login.
* Mengambil list data dari publik API: `https://jsonplaceholder.typicode.com/posts`.
* Menampilkan thumbnail gambar dummy dari Picsum (`https://picsum.photos/id/{id}/200/200`) menggunakan **Glide**.
* Menyediakan aksi bookmark pada setiap item dengan indikator status bintang secara realtime.
* Menangani state: *Loading*, *Empty State*, *Error handling*, dan *Success*.

### 3. Halaman Bookmark
* Menggunakan **LinearLayout**.
* Menyimpan postingan yang dibookmark ke local storage menggunakan **Room Database** (`post_app.db`).
* Sinkronisasi status bookmark dua arah: pelepasan bookmark di halaman ini otomatis memperbarui checklist status di Dashboard saat kembali (`onResume`).

### 4. Session Timeout (180 Detik Idle)
* Mendeteksi ketiadaan interaksi pengguna (tap, scroll, touch) selama **180 detik**.
* Deteksi interaksi terpusat di `BaseActivity` memanfaatkan callback `onUserInteraction()`.
* Jika waktu idle tercapai, sesi otomatis berakhir dan pengguna diarahkan kembali ke Halaman Login dengan membersihkan backstack aktivitas.

### 5. Lifecycle & Screen Rotation Handling
* Mempertahankan daftar postingan dan posisi scroll RecyclerView saat terjadi rotasi layar (*configuration change*) menggunakan `onSaveInstanceState` dan `Parcelable`.
* Menghindari pemanggilan ulang API (re-fetch) yang tidak perlu saat rotasi.

### 6. Dual Language (Localization)
* Mendukung 2 bahasa:
  * **Bahasa Indonesia** (`values/strings.xml`) sebagai default.
  * **Bahasa Inggris** (`values-en/strings.xml`) saat bahasa sistem perangkat berbahasa Inggris.

---

## Arsitektur

Aplikasi ini menggunakan pola **MVP (Model-View-Presenter)** yang dipisahkan ke dalam beberapa package utama:

* **`data`**: Berisi konfigurasi network Retrofit (`remote`), Room database & DAO (`local`), model data class, serta `PostRepository` sebagai jembatan data.
* **`presentation`**: Dibagi per-fitur (`login`, `dashboard`, `bookmark`), masing-masing memiliki pasangan `Activity`, `Contract`, dan `Presenter`. Tersedia juga `BaseActivity` untuk menangani idle timeout secara terpusat.
* **`utils`**: Helper untuk session timer dan insets window.

---

## Unit Testing

Layer Presenter pada ketiga fitur utama telah diuji secara menyeluruh dengan **100% lulus (10 skenario)** menggunakan JUnit dan Mockito:

* **`LoginPresenterTest`**:
  * `login_emptyUsername_showsUsernameError`
  * `login_emptyPassword_showsPasswordError`
  * `login_validCredentials_navigatesToDashboard`
* **`DashboardPresenterTest`**:
  * `loadPosts_success_showsPosts`
  * `loadPosts_empty_showsEmpty`
  * `loadPosts_error_showsError`
  * `toggleBookmark_success_updatesStatusAndShowsMessage`
* **`BookmarkPresenterTest`**:
  * `loadBookmarks_hasData_showsBookmarks`
  * `loadBookmarks_empty_showsEmpty`
  * `removeBookmark_callsRepositoryAndReloads`

Jalankan seluruh pengujian unit melalui perintah Gradle:
```bash
./gradlew testDebugUnitTest
```

---

## Petunjuk Build & Run

1. Clone repositori ini:
   ```bash
   git clone https://github.com/dodyrifki/PostApp.git
   ```
2. Buka folder proyek di Android Studio (`File > Open...`).
3. Tunggu proses **Gradle Sync** selesai secara otomatis.
4. Pilih target perangkat (Emulator atau Physical Device).
5. Klik tombol **Run** 
