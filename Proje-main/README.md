# Final Projesi

Emre Akgüzel 20180805016 (Backend) 
Bülent Tamay Batur 20200805010 (Gui) 
Hüseyin Kansız 20200805023 (Hardware)

Bitki bakım , gelişim otomasyon aracı;


Backend
---------- 
Tables
 -Users -> User_id , password , device_id 
 -Devicesettings -> device_id , irrigation_interval , light_interval , hum_interval , temp_interval , light_b , hum_b , temp_b 
 -Devicelog -> device_id , log1 , log2 , log 3 , log4 , log5, log6 , log7 , log8

 User id , device id ve password için prime key . device id log ve setting için  prime key. Uygulama açıldığında kullacının adına göre device id sini alıyor eğer kullanıcı database de varsa . Device id sine göre gui'de ki elemanlara log ve settings teki değerleri veriyor.Aynı şekilde profil ayarlarını ( şifre , user id ) ve device settings ' leri gui componentleri ile database aktarılacak şekilde hazırlandı. 

 Arduino tarafındaki ise sürekli çalışan listenerları , aynı anda değişiklik yapmayacak ve arduinodan gelen değerleri device id'sine göre database , database'den de arduinonun ölçüm yapması gereken interval ayarlarını alacak şekilde hazırlandı.
 
----------
Gui

- Login
- System
- Log
- Profile
- Settings

Login,System, Log, Profile ve Setting sayfaları için root ayarlanıp bunlar arasındaki geçiş, ekranın sağındaki butonlarla sağlandı. Her sayfa için bileşenlerinin id' sini içeren fxml dosyaları oluşturulup bu dosyalarda bileşenlerinin pozisyonları ayarlandı. Controller dosyalarıyla fxml'deki bileşenlerin id'sini kullanılıp bazı methodlar tanımlandı (rootları değiştiren butonlar da dahil). Styles.css kullanarak da fxml dosyalarındaki gerekli bileşenlerin boyutları, renkleri düzenlendi.

---------

Arduino / Hardware



Proje icin 1 adet arduino uno, 1er adet Ldr,Higrometer,Temprature sensor kullanılmıştır ek olarak ds1302 rtc modulu bulunmaktadır bu sayede kendi saatide vardır.Sensor verileri analog olarak alınıp arduino icinde islendikten sonra java appine gonderilmektedir.
Java app'i ise sürekli olarak arduinoyu Threadler ile dinlemekte ve İnterval vakti geldiğinde is arduinoya komut vermektedir.
 


