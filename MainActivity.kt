package com.example.fitness_veritabani

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Uye(
    val uyeId: Int,
    val ad: String,
    val soyad: String,
    val boy: Double,
    val kilo: Double,
    val uyelikTipi: String
)

data class AntrenmanProgrami(
    val programAdi: String,
    val seviye: String,
    val aciklama: String,
    val baslangicTarihi: Date,
    val bitisTarihi: Date?
)

data class BeslenmeListesi(
    val listeAdi: String,
    val diyetisyenAdi: String,
    val gunlukKaloriHedefi: Int,
    val aciklama: String,
    val gecerlilikBitis: Date?
)

/** Bir üyeye ve onun antrenman/beslenme kayıtlarına karşılık gelen tek bir sayfa. */
data class UyeSayfasi(
    val uye: Uye,
    val antrenmanProgrami: AntrenmanProgrami,
    val beslenmeListesi: BeslenmeListesi
)

class MainActivity : AppCompatActivity() {

    private lateinit var ivAvatar: ImageView
    private lateinit var tvAdSoyad: TextView
    private lateinit var tvBoyKilo: TextView
    private lateinit var chipUyelikTipi: Chip

    private lateinit var chipSeviye: Chip
    private lateinit var tvProgramAdi: TextView
    private lateinit var tvAntrenmanAciklama: TextView
    private lateinit var tvAntrenmanTarih: TextView

    private lateinit var chipKalori: Chip
    private lateinit var tvListeAdi: TextView
    private lateinit var chipDiyetisyen: Chip
    private lateinit var tvBeslenmeAciklama: TextView
    private lateinit var tvBeslenmeGecerlilik: TextView

    private lateinit var btnOncekiUye: MaterialButton
    private lateinit var btnSonrakiUye: MaterialButton
    private lateinit var tvSayfaBilgisi: TextView

    private val tarihFormat = SimpleDateFormat("dd MMM yyyy", Locale("tr"))
    private val sqlTarihFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    private val sayfalar: List<UyeSayfasi> by lazy { fitnessSayfalariniOlustur() }
    private var mevcutSayfaIndeksi = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

        bindViews()

        btnOncekiUye.setOnClickListener { sayfayaGit(mevcutSayfaIndeksi - 1) }
        btnSonrakiUye.setOnClickListener { sayfayaGit(mevcutSayfaIndeksi + 1) }

        // Uygulama, "Profilim" ekranı olduğu için varsayılan olarak son eklenen
        // üye (Arda Toprak, UyeID = 6) ile açılır; Önceki ile diğer üyelere gidilebilir.
        sayfayaGit(sayfalar.lastIndex)
    }

    private fun tarih(yyyyMMdd: String): Date = sqlTarihFormat.parse(yyyyMMdd)!!

    /** fitness_database.sql içindeki Uyeler / AntrenmanProgramlari / BeslenmeListeleri INSERT'lerinin karşılığı. */
    private fun fitnessSayfalariniOlustur(): List<UyeSayfasi> = listOf(
        UyeSayfasi(
            uye = Uye(1, "Ahmet", "Yılmaz", 178.50, 82.30, "Premium"),
            antrenmanProgrami = AntrenmanProgrami(
                programAdi = "Kas Kütlesi Artırma Programı",
                seviye = "Orta",
                aciklama = "Haftada 4 gün ağırlık antrenmanı",
                baslangicTarihi = tarih("2026-01-05"),
                bitisTarihi = tarih("2026-04-05")
            ),
            beslenmeListesi = BeslenmeListesi(
                listeAdi = "Kilo Alma Beslenme Planı",
                diyetisyenAdi = "Dyt. Aylin Yıldız",
                gunlukKaloriHedefi = 3200,
                aciklama = "Yüksek proteinli beslenme planı",
                gecerlilikBitis = tarih("2026-04-05")
            )
        ),
        UyeSayfasi(
            uye = Uye(2, "Elif", "Kaya", 165.00, 58.40, "Standart"),
            antrenmanProgrami = AntrenmanProgrami(
                programAdi = "Yağ Yakım Programı",
                seviye = "Baslangic",
                aciklama = "Kardiyo ağırlıklı başlangıç programı",
                baslangicTarihi = tarih("2026-02-01"),
                bitisTarihi = null
            ),
            beslenmeListesi = BeslenmeListesi(
                listeAdi = "Yağ Yakım Beslenme Planı",
                diyetisyenAdi = "Dyt. Aylin Yıldız",
                gunlukKaloriHedefi = 1600,
                aciklama = "Düşük karbonhidratlı plan",
                gecerlilikBitis = null
            )
        ),
        UyeSayfasi(
            uye = Uye(3, "Mehmet", "Demir", 182.00, 90.10, "VIP"),
            antrenmanProgrami = AntrenmanProgrami(
                programAdi = "İleri Seviye Güç Programı",
                seviye = "Ileri",
                aciklama = "Powerlifting odaklı program",
                baslangicTarihi = tarih("2025-11-15"),
                bitisTarihi = tarih("2026-05-15")
            ),
            beslenmeListesi = BeslenmeListesi(
                listeAdi = "Performans Beslenme Planı",
                diyetisyenAdi = "Dyt. Onur Çelik",
                gunlukKaloriHedefi = 3500,
                aciklama = "Antrenman öncesi/sonrası beslenme",
                gecerlilikBitis = tarih("2026-05-15")
            )
        ),
        UyeSayfasi(
            uye = Uye(4, "Zeynep", "Şahin", 170.20, 63.00, "Standart"),
            antrenmanProgrami = AntrenmanProgrami(
                programAdi = "Genel Fitness Programı",
                seviye = "Baslangic",
                aciklama = "Genel kondisyon ve esneklik",
                baslangicTarihi = tarih("2026-03-01"),
                bitisTarihi = null
            ),
            beslenmeListesi = BeslenmeListesi(
                listeAdi = "Dengeli Beslenme Planı",
                diyetisyenAdi = "Dyt. Onur Çelik",
                gunlukKaloriHedefi = 2000,
                aciklama = "Genel sağlıklı beslenme",
                gecerlilikBitis = null
            )
        ),
        UyeSayfasi(
            uye = Uye(5, "Can", "Öztürk", 175.00, 76.80, "Premium"),
            antrenmanProgrami = AntrenmanProgrami(
                programAdi = "Kas Kütlesi Artırma Programı",
                seviye = "Orta",
                aciklama = "Üst-alt vücut bölgesel program",
                baslangicTarihi = tarih("2026-01-20"),
                bitisTarihi = tarih("2026-04-20")
            ),
            beslenmeListesi = BeslenmeListesi(
                listeAdi = "Kilo Alma Beslenme Planı",
                diyetisyenAdi = "Dyt. Aylin Yıldız",
                gunlukKaloriHedefi = 3000,
                aciklama = "Kas kütlesi için kalori artışı",
                gecerlilikBitis = tarih("2026-04-20")
            )
        ),
        UyeSayfasi(
            uye = Uye(6, "Arda", "Toprak", 176.00, 77.75, "Standart"),
            antrenmanProgrami = AntrenmanProgrami(
                programAdi = "Yağ Yakma ve Kas Kütlesi Programı",
                seviye = "Orta",
                aciklama = "1. Faz (ilk 8 hafta): kardiyo ağırlıklı yağ yakım antrenmanı. " +
                    "2. Faz (sonraki 8 hafta): ağırlık antrenmanı ile kas kütlesi artırma.",
                baslangicTarihi = tarih("2026-07-14"),
                bitisTarihi = tarih("2026-11-10")
            ),
            beslenmeListesi = BeslenmeListesi(
                listeAdi = "Özel Yeşillik Salata Diyeti",
                diyetisyenAdi = "Dyt. Aylin Yıldız",
                gunlukKaloriHedefi = 1200,
                aciklama = "Sadece marul, havuç, roka, mor lahana ve soğandan oluşan özel salata diyeti.",
                gecerlilikBitis = tarih("2026-11-10")
            )
        )
    )

    private fun bindViews() {
        ivAvatar = findViewById(R.id.ivAvatar)
        tvAdSoyad = findViewById(R.id.tvAdSoyad)
        tvBoyKilo = findViewById(R.id.tvBoyKilo)
        chipUyelikTipi = findViewById(R.id.chipUyelikTipi)

        chipSeviye = findViewById(R.id.chipSeviye)
        tvProgramAdi = findViewById(R.id.tvProgramAdi)
        tvAntrenmanAciklama = findViewById(R.id.tvAntrenmanAciklama)
        tvAntrenmanTarih = findViewById(R.id.tvAntrenmanTarih)

        chipKalori = findViewById(R.id.chipKalori)
        tvListeAdi = findViewById(R.id.tvListeAdi)
        chipDiyetisyen = findViewById(R.id.chipDiyetisyen)
        tvBeslenmeAciklama = findViewById(R.id.tvBeslenmeAciklama)
        tvBeslenmeGecerlilik = findViewById(R.id.tvBeslenmeGecerlilik)

        btnOncekiUye = findViewById(R.id.btnOncekiUye)
        btnSonrakiUye = findViewById(R.id.btnSonrakiUye)
        tvSayfaBilgisi = findViewById(R.id.tvSayfaBilgisi)

        val avatarBg = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(Color.parseColor("#EEF0FA"))
        }
        ivAvatar.background = avatarBg
    }

    /** Sayfa numarası her zaman Uyeler.UyeID değerine eşittir (1..sayfalar.size). */
    private fun sayfayaGit(indeks: Int) {
        mevcutSayfaIndeksi = indeks.coerceIn(0, sayfalar.lastIndex)
        val sayfa = sayfalar[mevcutSayfaIndeksi]

        doldurUyeKarti(sayfa.uye)
        doldurAntrenmanKarti(sayfa.antrenmanProgrami)
        doldurBeslenmeKarti(sayfa.beslenmeListesi)

        tvSayfaBilgisi.text = "Üye Sayfası ${sayfa.uye.uyeId} / ${sayfalar.size}  (UyeID: ${sayfa.uye.uyeId})"
        btnOncekiUye.isEnabled = mevcutSayfaIndeksi > 0
        btnSonrakiUye.isEnabled = mevcutSayfaIndeksi < sayfalar.lastIndex
    }

    private fun doldurUyeKarti(uye: Uye) {
        tvAdSoyad.text = "${uye.ad} ${uye.soyad}"
        tvBoyKilo.text = "${uye.boy.toInt()} cm  •  ${uye.kilo} kg"
        chipUyelikTipi.text = "${uye.uyelikTipi} Üyesi"
    }

    private fun doldurAntrenmanKarti(program: AntrenmanProgrami) {
        chipSeviye.text = program.seviye
        tvProgramAdi.text = program.programAdi
        tvAntrenmanAciklama.text = program.aciklama

        val baslangic = tarihFormat.format(program.baslangicTarihi)
        val bitis = program.bitisTarihi?.let { tarihFormat.format(it) } ?: "Devam ediyor"
        tvAntrenmanTarih.text = "$baslangic → $bitis"
    }

    private fun doldurBeslenmeKarti(liste: BeslenmeListesi) {
        chipKalori.text = "${liste.gunlukKaloriHedefi} kcal"
        tvListeAdi.text = liste.listeAdi
        chipDiyetisyen.text = liste.diyetisyenAdi
        tvBeslenmeAciklama.text = liste.aciklama
        tvBeslenmeGecerlilik.text = liste.gecerlilikBitis?.let { "Geçerlilik bitişi: ${tarihFormat.format(it)}" }
            ?: "Geçerlilik bitişi: Süresiz"
    }
}
