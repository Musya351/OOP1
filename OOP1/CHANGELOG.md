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
## [PROMPT-12] - GUI State Button Menu
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> mengubah panel input GUI agar button yang tampil mengikuti state program: menu awal, menu mesin hidup, dan menu autopilot aktif
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> menu awal GUI hanya menampilkan Hidupkan Mesin, Isi Bensin, dan Keluar; tombol Keluar menyimpan bensin terakhir, menghentikan timer dashboard, menutup window, dan mematikan program
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> menu mesin hidup GUI menampilkan Matikan Mesin, Status Mobil, Tambah Kecepatan, Rem, Belok Kiri, Belok Kanan, Lurus, Mundur (Gigi R), dan Aktifkan Autopilot
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> menu autopilot aktif GUI menampilkan Set Cruise Control, Simulasi Deteksi Objek, Emergency Stop Manual, dan Nonaktifkan Autopilot
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> menambahkan dialog Status Mobil pada GUI yang menampilkan status Car dan AutopilotSystem
- [MODIFIED] src/main/java/autopilotcar/model/Car.java -> menambahkan reverseMode dan getGearDisplay() agar dashboard dapat menampilkan gigi R saat mobil mundur
- [MODIFIED] src/main/java/autopilotcar/model/Car.java -> menambahkan applyBrake() sebagai rem biasa yang mengurangi kecepatan bertahap dan mengembalikan gigi ke 0 saat mobil berhenti
- [MODIFIED] diagramClass/ClassDiagram.wsd -> memperbarui atribut dan method Car untuk reverseMode, applyBrake(), dan getGearDisplay()
- [NOTE] ConsoleUI tidak diubah sehingga mode konsol tetap memakai alur yang sudah ada
## [PROMPT-13] - Autopilot Cruise and Controlled Detection
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> menambahkan default cruise speed 50 KM/Jam saat autopilot diaktifkan
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> memisahkan update cruise otomatis dari simulasi deteksi objek agar rintangan hanya discan saat fitur Simulasi Deteksi Objek dijalankan
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> menambahkan simulateObjectDetection() sebagai jalur eksplisit untuk scan objek dan obstacle handling
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> mengubah emergency stop sistem menjadi penurunan bertahap 5 KM/Jam per detik sampai berhenti
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> update cruise tanpa scan obstacle akan mengembalikan mode COLLISION_AVOIDANCE ke CRUISE_CONTROL agar autopilot kembali melaju normal setelah simulasi obstacle selesai
- [MODIFIED] src/main/java/autopilotcar/ui/CarSimulator.java -> mengubah target speed awal AutopilotSystem dari 60 menjadi 50 KM/Jam
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> timer realtime GUI tetap memanggil update cruise biasa tanpa scan rintangan acak
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> saat Autopilot diaktifkan, GUI langsung menjalankan satu update cruise awal agar kecepatan mulai naik menuju 50 KM/Jam
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> tombol Simulasi Deteksi Objek memakai simulateObjectDetection() dan dijalankan pada thread terpisah agar dashboard tidak memicu obstacle otomatis setiap detik
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> Emergency Stop Manual memakai Swing Timer untuk mengurangi kecepatan 5 KM/Jam per detik sampai mobil berhenti
- [MODIFIED] src/main/java/autopilotcar/ui/ConsoleUI.java -> menu Simulasi Deteksi Objek memakai simulateObjectDetection(), sedangkan Set Cruise Control tetap memakai update cruise biasa
- [MODIFIED] src/main/java/autopilotcar/ui/ConsoleUI.java -> saat Autopilot diaktifkan dari console, sistem langsung menjalankan satu update cruise awal dan menyinkronkan speed mobil
- [MODIFIED] src/main/java/autopilotcar/ui/ConsoleUI.java -> Emergency Stop Manual console mengurangi kecepatan 5 KM/Jam per detik sebelum berhenti total
- [MODIFIED] src/main/java/autopilotcar/model/Car.java -> menambahkan overload applyBrake(float speedReduction) agar pengereman bertahap bisa dipakai oleh emergency stop manual
- [NOTE] Perubahan ini menjawab log console yang menunjukkan collision avoidance dan emergency stop terlalu sering muncul akibat scan objek otomatis pada update realtime
## [PROMPT-14] - Rare Normal Detection and Frequent Simulation
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> autopilot biasa kembali menjalankan deteksi objek otomatis, tetapi dengan peluang scan rendah agar rintangan jarang muncul
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> tombol/fitur Simulasi Deteksi Objek tetap memakai jalur scan khusus dengan frekuensi objek lebih tinggi
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> menambahkan Random dan NORMAL_DETECTION_CHANCE untuk membedakan frekuensi deteksi normal dan simulasi
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> obstacle non-urgent bertipe VEHICLE atau OBSTACLE tetap ditangani dengan collision avoidance, pengurangan kecepatan otomatis, dan log manuver menghindar
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> setelah obstacle terlewati, update cruise normal mengembalikan mode ke CRUISE_CONTROL sehingga kecepatan naik bertahap kembali ke target cruise
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> obstacle urgent seperti PEDESTRIAN atau UNKNOWN tetap mengeksekusi emergency stop bertahap
- [MODIFIED] src/main/java/autopilotcar/autopilot/ObjectDetector.java -> menambahkan scanFrequent() dan readFrequentData() agar simulasi tombol lebih sering menghasilkan objek/rintangan
- [MODIFIED] src/main/java/autopilotcar/autopilot/ObjectDetector.java -> scan frequent membuat 1 sampai 3 objek dengan jarak lebih dekat agar tombol Simulasi Deteksi Objek terasa lebih aktif daripada deteksi normal
- [MODIFIED] diagramClass/ClassDiagram.wsd -> menambahkan method simulateObjectDetection(), scanFrequent(), dan readFrequentData() ke diagram class
- [NOTE] Frekuensi deteksi normal dibuat jarang, sedangkan simulasi manual dibuat lebih sering; keduanya tetap memakai aturan urgent dan non-urgent yang sama
## [PROMPT-15] - Formatted GUI Status Popup
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> memperbaiki popup Status Mobil agar tidak lagi menampilkan dump object panjang dari getStatus()
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> menambahkan buildStatusReport() untuk menyusun informasi status dalam section Identitas, Kondisi Saat Ini, Bensin, Autopilot, dan Komponen Utama
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> menampilkan status pada JTextArea monospaced di dalam JScrollPane agar rapi, mudah dibaca, dan tetap nyaman saat isi laporan panjang
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> menambahkan appendStatusLine() untuk format label dan nilai status yang konsisten
- [NOTE] Popup Status Mobil sekarang menampilkan data penting seperti mesin, autopilot, mode, speed, gigi, lampu sen, bensin, target cruise, dan komponen utama secara terstruktur
## [PROMPT-16] - Autopilot Turn Signal and Timed Detection
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> menambahkan dependency opsional ke Car agar autopilot dapat mengatur lampu sen saat manuver menghindari obstacle
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> collision avoidance untuk VEHICLE dan OBSTACLE sekarang menyalakan lampu sen kiri atau kanan secara otomatis sebagai tanda menghindari rintangan
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> ketika obstacle sudah tidak terdeteksi dan mode kembali ke CRUISE_CONTROL, lampu sen dimatikan lewat driveStraight()
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> simulateObjectDetection() sekarang berjalan sebagai sesi 5 sampai 10 detik selama tidak ada kondisi urgent yang menghentikan mobil
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> obstacle urgent seperti PEDESTRIAN atau UNKNOWN tetap menjalankan emergency stop bertahap dan menghentikan sesi simulasi
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> TRAFFIC_SIGN tidak lagi mengubah target cruise control; sistem hanya memperlambat sementara lalu kembali mengejar target cruise semula
- [MODIFIED] src/main/java/autopilotcar/ui/CarSimulator.java -> mengirim referensi Car ke AutopilotSystem agar lampu sen autopilot bisa tersinkron dengan dashboard GUI
- [MODIFIED] diagramClass/ClassDiagram.wsd -> menambahkan atribut car pada AutopilotSystem untuk mencerminkan integrasi lampu sen autopilot
- [NOTE] Cruise control hanya berubah lewat input user atau default 50 KM/Jam saat autopilot diaktifkan
## [PROMPT-17] - Autopilot Activation Speed Sync
- [MODIFIED] src/main/java/autopilotcar/autopilot/AutopilotSystem.java -> saat autopilot diaktifkan, SpeedSensor langsung disinkronkan dengan currentSpeed dari Car
- [NOTE] Jika mobil sedang berjalan di atas cruise default 50 KM/Jam saat autopilot diaktifkan, cruise control akan menurunkan kecepatan bertahap 5 KM/Jam per update sampai mendekati target
- [NOTE] Jika mobil sedang berjalan di bawah cruise default 50 KM/Jam saat autopilot diaktifkan, cruise control akan menaikkan kecepatan bertahap 5 KM/Jam per update sampai mendekati target
- [NOTE] Perubahan ini menjaga transisi dari mode manual ke autopilot tetap memakai kecepatan aktual mobil, bukan nilai SpeedSensor lama
## [PROMPT-18] - Reverse Gear Speed Behavior
- [MODIFIED] src/main/java/autopilotcar/model/Car.java -> Mundur (Gigi R) sekarang hanya bisa digunakan ketika mobil dalam keadaan diam atau currentSpeed 0
- [MODIFIED] src/main/java/autopilotcar/model/Car.java -> ketika mobil sedang dalam reverseMode, moveForward() tidak lagi langsung pindah ke gigi maju; method ini menambah kecepatan mundur dan tetap mempertahankan gigi R
- [MODIFIED] src/main/java/autopilotcar/model/Car.java -> menambahkan isReverseMode() agar GUI bisa membedakan Tambah Kecepatan maju dan Tambah Kecepatan mundur
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> tombol Tambah Kecepatan menampilkan log Kecepatan mundur bertambah saat mobil sedang dalam Gigi R
- [MODIFIED] src/main/java/autopilotcar/ui/DashboardGUI.java -> tombol Mundur (Gigi R) menolak perpindahan ke reverse jika mobil belum diam dan menampilkan log penjelasan
- [MODIFIED] diagramClass/ClassDiagram.wsd -> menambahkan method isReverseMode() pada class Car
- [NOTE] Rem tetap mengurangi kecepatan mundur; ketika speed menjadi 0, reverseMode dimatikan dan gigi kembali 0
