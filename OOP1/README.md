# AutopilotCar

Proyek simulasi mobil dengan sistem autopilot berbasis Java OOP.

## Cara Compile

```bash
cd OOP1
$files = Get-ChildItem src/main/java -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d bin $files
```

## Cara Run

```bash
cd OOP1
java -cp bin autopilotcar.ui.CarSimulator
```

Alternatif entry point:

```bash
cd OOP1
java -cp bin autopilotcar.Main
```

## Fitur Tersedia

- Model kendaraan lengkap: engine, gearbox, body, brake, wheel, tire, suspension, dan car
- Sistem autopilot dengan mode `CRUISE_CONTROL`, `COLLISION_AVOIDANCE`, `EMERGENCY_STOP`, `MANUAL_OVERRIDE`, dan `LANE_KEEPING`
- Sensor kecepatan dan deteksi objek
- Simulasi penanganan obstacle berdasarkan jenis objek
- Penyesuaian gigi otomatis berdasarkan kecepatan
- Antarmuka konsol untuk melihat status mobil, mengaktifkan autopilot, mengatur cruise control, simulasi deteksi objek, emergency stop manual, dan menonaktifkan autopilot
- Entry point alternatif melalui `autopilotcar.Main`
