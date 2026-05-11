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
