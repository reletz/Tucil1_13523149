IQ Puzzler Pro Solver

Deskripsi Singkat

Program ini merupakan solver untuk permainan IQ Puzzler Pro, yang memungkinkan pengguna untuk:
Mengunggah file test case dalam format .txt.
Menampilkan hasil solusi dalam bentuk grid warna sesuai karakter blok.
Menampilkan statistik pencarian, termasuk waktu eksekusi dan jumlah percobaan solusi.
Menyimpan hasil solusi ke dalam file .txt atau .png.
Berjalan dalam mode GUI atau CLI.
Dibangun menggunakan Java dan JavaFX untuk tampilan grafis.

Requirement

Sebelum menjalankan program, pastikan:

Java Development Kit (JDK) 21 atau lebih baru telah terinstal.

JavaFX SDK 21.0.6 telah diunduh dan tersedia di C:/Program Files/javafx-sdk-21.0.6/.

Folder struktur berikut telah dibuat:
==
.
├── bin
│   ├── Block.class
│   ├── Board.class
│   ├── Data.class
│   ├── GUI.class
│   ├── Handler.class
│   └── Solver.class
├── doc
│   └── Laporan Tucil1_13523149.pdf
├── README.md
├── src
│   ├── Block.java
│   ├── Board.java
│   ├── Data.java
│   ├── Handler.java
│   ├── Main.java
│   └── Solver.java
└── test
    ├── input
    └── output
==

🛠 Instalasi

Unduh dan pasang JDK 21+ (Jika belum terinstal)

Download JDK

Unduh JavaFX SDK 21.0.6

Download JavaFX

Ekstrak JavaFX SDK ke C:/Program Files/javafx-sdk-21.0.6/

▶️ Cara Menjalankan Program

1. Kompilasi Program

Gunakan perintah berikut untuk mengompilasi semua file Java ke dalam folder bin:

javac --module-path "C:/Program Files/javafx-sdk-21.0.6/lib" --add-modules javafx.controls,javafx.fxml,javafx.swing -d bin src/*.java

2. Jalankan Program dalam Mode GUI

Untuk menjalankan program dengan Graphical User Interface (GUI), gunakan perintah:

java --module-path "C:/Program Files/javafx-sdk-21.0.6/lib" --add-modules javafx.controls,javafx.fxml,javafx.swing -cp bin GUI

3. Jalankan Program dalam Mode CLI

Jika ingin menggunakan Command Line Interface (CLI), jalankan:

java -cp bin GUI

Saat berjalan dalam mode CLI, Anda akan diminta untuk memasukkan nama file test case yang berada di test/input/.

Struktur File .txt untuk Test Case
Test case harus disimpan di folder test/input/ dalam format berikut:

N M P
S
Blok 1
Blok 2
...
Blok P

Dimana:

N = jumlah baris papan permainan

M = jumlah kolom papan permainan

P = jumlah blok

S = tipe board (DEFAULT, CUSTOM, PYRAMID)

Blok = representasi blok dalam bentuk karakter

Contoh test case:

5 5 2
DEFAULT
A
AA
B B

Fitur Utama

Bisa dijalankan dalam mode GUI atau CLI
Memproses file .txt sebagai input dan membaca test case otomatis
Menampilkan hasil pencarian solusi dalam bentuk grid warna dan teks
Menyimpan solusi ke .txt atau .png
Menggunakan algoritma pencarian solusi optimal

Catatan
Jika program tidak bisa menemukan folder test/input/, pastikan folder benar-benar ada di dalam proyek.
Jika mengalami error terkait JavaFX, pastikan bahwa --module-path sudah menunjuk ke lokasi yang benar.
