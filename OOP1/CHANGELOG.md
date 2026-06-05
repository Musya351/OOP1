# CHANGELOG - AutopilotCar Project
## [PROMPT-0] - Project Initialization
- [CREATED] Struktur folder proyek
- [NOTE] Belum ada file Java. Dimulai dari package model
## [PROMPT-1] - Basic Model Classes
- [CREATED] src/main/java/autopilotcar/model/Tire.java -> menambahkan model ban dengan atribut dasar, accessor, dan representasi string
- [CREATED] src/main/java/autopilotcar/model/Wheel.java -> menambahkan model roda dengan composition ke Tire, accessor, dan representasi string
- [CREATED] src/main/java/autopilotcar/model/Brake.java -> menambahkan model rem dengan method simulasi apply() dan emergencyBrake()
- [CREATED] src/main/java/autopilotcar/model/Engine.java -> menambahkan model mesin dengan method simulasi start(), brake(), dan accelerate()
- [NOTE] Package model sudah berisi komponen dasar kendaraan; sesi berikutnya bisa lanjut ke relasi tingkat kendaraan atau package autopilot
## [PROMPT-2] - Remaining Model Classes
- [CREATED] src/main/java/autopilotcar/model/GearBoxType.java -> menambahkan tipe gearbox dengan properti nama, catatan, dan mode otomatis
- [CREATED] src/main/java/autopilotcar/model/GearBox.java -> menambahkan gearbox dengan aggregation ke GearBoxType dan logika shiftUp(), shiftDown(), autoShift()
- [CREATED] src/main/java/autopilotcar/model/Body.java -> menambahkan model bodi kendaraan dengan informasi pintu, warna, dan frame
- [CREATED] src/main/java/autopilotcar/model/Suspension.java -> menambahkan model suspensi beserta dependency sederhana ke Wheel
- [CREATED] src/main/java/autopilotcar/model/CarModel.java -> menambahkan model kendaraan dengan aggregation ke daftar Car
- [CREATED] src/main/java/autopilotcar/model/Car.java -> menambahkan class pusat mobil dengan seluruh komposisi komponen, simulasi pergerakan, dan getStatus()
- [NOTE] Class model inti sudah lengkap; relasi Car memakai 4 wheel wajib di constructor dan getStatus() sudah merangkum seluruh informasi mobil
## [PROMPT-3] - Autopilot Enums and Interface
- [CREATED] src/main/java/autopilotcar/autopilot/AutopilotMode.java -> menambahkan enum mode autopilot dengan JavaDoc singkat pada setiap nilai
- [CREATED] src/main/java/autopilotcar/autopilot/ObjectType.java -> menambahkan enum jenis objek terdeteksi dengan JavaDoc singkat pada setiap nilai
- [CREATED] src/main/java/autopilotcar/autopilot/Sensor.java -> menambahkan interface dasar sensor dengan method initialize(), readData(), dan isReady()
- [CREATED] src/main/java/autopilotcar/autopilot/DetectedObject.java -> menambahkan model objek terdeteksi lengkap dengan ObjectType, accessor, dan toString()
- [NOTE] Fondasi package autopilot sudah siap; sesi berikutnya bisa lanjut ke implementasi sensor konkret atau sistem kontrol autopilot
## [PROMPT-4] - Sensor Classes
- [CREATED] src/main/java/autopilotcar/autopilot/ObjectDetector.java -> menambahkan sensor deteksi objek dengan hasil scan acak, pencarian objek terdekat, dan status rintangan di depan
- [CREATED] src/main/java/autopilotcar/autopilot/SpeedSensor.java -> menambahkan sensor kecepatan dengan riwayat pembacaan, batas kecepatan, gear optimal, dan simulasi setCurrentSpeed()
- [NOTE] Sensor dasar autopilot sudah tersedia; ObjectDetector mengembalikan List<DetectedObject> saat readData() dan SpeedSensor menyimpan 10 histori kecepatan terbaru
## [PROMPT-5] - Autopilot System
- [CREATED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> menambahkan pusat logika autopilot untuk aktivasi, cruise control, penanganan obstacle, penyesuaian gigi, dan laporan status
- [NOTE] AutopilotSystem memakai dependency injection untuk ObjectDetector, SpeedSensor, GearBox, Brake, dan Engine; perpindahan mode selalu dicetak ke console
## [PROMPT-6] - Console UI
- [CREATED] src/main/java/autopilotcar/ui/ConsoleUI.java -> menambahkan menu konsol, validasi input numerik, status mobil, simulasi deteksi objek, dan kontrol autopilot
- [CREATED] src/main/java/autopilotcar/ui/CarSimulator.java -> menambahkan bootstrap simulasi untuk inisialisasi Car, setup AutopilotSystem, dan menjalankan ConsoleUI
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> menambahkan accessor current speed agar UI bisa sinkron dengan status kecepatan sistem tanpa parsing string
- [MODIFIED] src/main/java/autopilotcar/ui/ConsoleUI.java -> menyederhanakan sinkronisasi kecepatan mobil dengan memakai accessor dari AutopilotSystem
- [NOTE] Package UI sudah bisa menjalankan simulasi dasar via main(); input menu divalidasi agar tidak crash saat user memasukkan non-angka
## [PROMPT-7] - Testing and Finalization
- [CREATED] src/main/java/autopilotcar/Main.java -> menambahkan alternatif entry point di root package autopilotcar
- [CREATED] README.md -> menambahkan panduan compile, run, dan daftar fitur proyek
- [MODIFIED] src/main/java/autopilotcar/ui/ConsoleUI.java -> memperbaiki alert menu Cruise Control agar sesuai status sistem setelah update
- [NOTE] Review import selesai dan tidak ditemukan circular dependency antar package
- [NOTE] Simulasi alur menu 3 menemukan alert yang menyesatkan saat autopilot langsung masuk emergency stop; bug tersebut sudah diperbaiki
- [NOTE] File .class hasil compile dibersihkan dari src/main/java; output build sebaiknya ditempatkan di folder bin
- [NOTE] STATUS FINAL: struktur model, autopilot, UI, entry point alternatif, dan dokumentasi dasar proyek sudah lengkap
## [PROMPT-8] - README Compile Fix
- [MODIFIED] README.md -> memperbaiki instruksi compile agar sesuai dengan PowerShell dan posisi root folder proyek di Windows
- [NOTE] Pola `**/*.java` tidak diekspansi langsung oleh `javac` pada PowerShell; gunakan daftar file dari `Get-ChildItem`
## [PROMPT-9] - Menu State Autopilot
- [MODIFIED] src/main/java/autopilotcar/ui/ConsoleUI.java -> mengubah tampilan menu menjadi berbasis kondisi: menu awal saat mesin mati, menu mesin hidup, dan menu autopilot aktif
- [MODIFIED] src/main/java/autopilotcar/ui/ConsoleUI.java -> menu awal hanya menampilkan Hidupkan Mesin, Isi Bensin, dan Keluar; menu mesin hidup menampilkan Matikan Mesin, Status Mobil, dan Aktifkan Autopilot
- [MODIFIED] src/main/java/autopilotcar/ui/ConsoleUI.java -> menu autopilot aktif menampilkan Set Cruise Control, Simulasi Deteksi Objek, Emergency Stop Manual, dan Nonaktifkan Autopilot
- [MODIFIED] src/main/java/autopilotcar/model/Car.java -> menambahkan fuelLevel, accessor, validasi nilai bensin, dan method fillFuel() agar opsi Isi Bensin mengisi tangki hingga penuh
- [NOTE] Matikan Mesin mengembalikan program ke menu awal, sedangkan Nonaktifkan Autopilot dan Emergency Stop Manual mengembalikan program ke menu mesin hidup
## [PROMPT-10] - FuelTank Persistence and Consumption
- [CREATED] src/main/java/autopilotcar/model/FuelTank.java -> menambahkan class tangki bensin berkapasitas 40 liter dengan operasi isi penuh, konsumsi bensin, validasi nilai, status kosong, dan representasi string
- [MODIFIED] src/main/java/autopilotcar/model/Car.java -> mengganti fuelLevel sederhana menjadi komposisi FuelTank dan menambahkan konsumsi BBM berdasarkan waktu berjalan dengan rasio 10 detik per liter
- [MODIFIED] src/main/java/autopilotcar/model/Car.java -> mobil otomatis berhenti saat BBM habis tanpa mematikan mesin, menonaktifkan status autopilot mobil, mengembalikan kecepatan ke 0, dan gigi ke 0
- [MODIFIED] src/main/java/autopilotcar/ui/CarSimulator.java -> memuat data bensin terakhir dari file fuel-level.txt saat simulator membuat objek mobil
- [MODIFIED] src/main/java/autopilotcar/ui/ConsoleUI.java -> menyimpan data bensin terakhir ke fuel-level.txt saat program keluar, mesin dimatikan, bensin diisi, autopilot dinonaktifkan, emergency stop, dan setelah update konsumsi
- [MODIFIED] src/main/java/autopilotcar/ui/ConsoleUI.java -> menambahkan opsi Isi Bensin pada menu mesin hidup agar mobil yang berhenti karena BBM habis tetap bisa diisi tanpa mematikan mesin
- [MODIFIED] diagramClass/ClassDiagram.wsd -> menambahkan class FuelTank dan relasinya sebagai komposisi dari Car
- [NOTE] Jika file fuel-level.txt belum ada atau rusak, tangki bensin otomatis dibuat penuh sebagai nilai awal
- [NOTE] Implementasi file persistence memakai API Java 8 agar tetap kompatibel dengan JDK lama
## [PROMPT-11] - Realtime GUI Dashboard
- [CREATED] src/main/java/autopilotcar/ui/DashboardGUI.java -> menambahkan dashboard GUI berbasis Swing dengan input kontrol kendaraan dan panel status realtime
- [CREATED] src/main/java/autopilotcar/ui/GuiCarSimulator.java -> menambahkan entry point khusus untuk menjalankan dashboard GUI tanpa menghapus mode konsol
- [MODIFIED] src/main/java/autopilotcar/model/Car.java -> menambahkan state turnSignal dengan nilai Kiri, Kanan, atau Mati untuk mendukung tampilan lampu sen pada dashboard
- [MODIFIED] src/main/java/autopilotcar/model/Car.java -> menambahkan driveStraight() dan getTurnSignal() agar GUI bisa mematikan lampu sen saat mobil lurus
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> dashboard menampilkan gigi yang digunakan, kecepatan KM/Jam, mode berjalan, lampu sen, bensin, kondisi mesin, dan kondisi autopilot secara realtime melalui Swing Timer
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> input GUI menyediakan Hidupkan Mesin, Matikan Mesin, Isi Bensin, Aktifkan Autopilot, Set Cruise Control, Simulasi Deteksi Objek, Emergency Stop Manual, Nonaktifkan Autopilot, Tambah Kecepatan, Belok Kiri, Lurus, dan Belok Kanan
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> GUI menyimpan bensin terakhir ke fuel-level.txt saat dashboard refresh dan saat window ditutup
- [MODIFIED] README.md -> menambahkan instruksi run dashboard GUI realtime dan daftar fitur GUI
- [MODIFIED] diagramClass/ClassDiagram.wsd -> menambahkan DashboardGUI, GuiCarSimulator, relasi GUI, serta atribut/method lampu sen pada Car
- [NOTE] Mode konsol tetap tersedia melalui CarSimulator, sedangkan GUI dijalankan melalui GuiCarSimulator
