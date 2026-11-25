package com.example.data

import com.example.model.Book
import com.example.model.Chapter
import com.example.model.EditorialStatus
import com.example.model.GlossaryTerm
import com.example.model.KitabCategory
import com.example.model.KitabSection

object FathulQoribCatalog {

    val mainBook = Book(
        id = "fathul_qorib",
        title = "Fathul Qorib Al-Mujib",
        arabicTitle = "فتح القريب المجيب في شرح ألفاظ التقريب",
        author = "Syaikh Syamsuddin Abu Abdillah Muhammad bin Qasim Al-Ghazi",
        originalMatanAuthor = "Al-Imam Abu Syuja' Ahmad bin Al-Husain Al-Ashfahani",
        publisher = "Pondok Pesantren Al-Khoirot Malang",
        totalPages = 380,
        description = "Syarah otoritatif atas kitab Matan At-Taqrib (Ghayatul Ikhtishar) dalam mazhab Syafi'i. Mencakup bab ibadah (thaharah, sholat, zakat, puasa, haji), muamalah, waris, munakahat, jinayat, hingga qadla'.",
        status = EditorialStatus.VERIFIED
    )

    val kitabSections: List<KitabSection> = listOf(
        KitabSection(
            id = "kitab_muqaddimah",
            bookId = "fathul_qorib",
            title = "Muqaddimah",
            arabicTitle = "مقدمة الكتاب",
            category = KitabCategory.IBADAH,
            startPage = 1,
            endPage = 4,
            sequence = 1,
            summary = "Pembukaan mushannif dan syarih, keutamaan tafaqquh fiddin, biografi ringkas Imam Asy-Syafi'i, serta pengantar penamaan kitab At-Taqrib dan Fathul Qorib.",
            babTitles = listOf("Khutbah Kitab & Biografi Penulis", "Definisi Ilmu Fiqih & Tujuan Penyusunan")
        ),
        KitabSection(
            id = "kitab_thaharah",
            bookId = "fathul_qorib",
            title = "Kitab Hukum-Hukum Thaharah",
            arabicTitle = "كتاب أحكام الطهارة",
            category = KitabCategory.IBADAH,
            startPage = 4,
            endPage = 49,
            sequence = 2,
            summary = "Pembahasan bersuci dari hadats dan najis: tujuh macam air, pembagian empat jenis air, menyamak kulit, perabot emas perak, siwak, wudlu, istinja', mandi wajib, mengusap muza, tayammum, najis, serta haidl, nifas, dan istihadlah.",
            babTitles = listOf(
                "Pengertian Thaharah & 7 Macam Air (hlm. 4-5)",
                "Pembagian 4 Jenis Air: Mutlak, Musyammas, Musta'mal, Mutanajjis (hlm. 5-8)",
                "Bab Dibagh (Menyamak Kulit), Perabot Emas Perak & Siwak (hlm. 8-11)",
                "Bab Wudlu': Fardlu, Kesunnahan, Istinja' & Pembatal Wudlu' (hlm. 11-24)",
                "Bab Mandi Wajib, Mengusap Muza & Tayammum (hlm. 25-38)",
                "Bab Najis serta Haidl, Nifas & Istihadlah (hlm. 39-49)"
            )
        ),
        KitabSection(
            id = "kitab_sholat",
            bookId = "fathul_qorib",
            title = "Kitab Hukum-Hukum Sholat",
            arabicTitle = "كتاب أحكام الصلاة",
            category = KitabCategory.IBADAH,
            startPage = 50,
            endPage = 105,
            sequence = 3,
            summary = "Pembahasan sholat fardlu lima waktu, waktu-waktu sholat, syarat wajib dan syarat sah, delapan belas rukun sholat, sunnah ab'adl & hai'at, pembatal sholat, jama'ah, qashar-jama', Jum'at, Hari Raya, gerhana, istisqa', khauf, pakaian, hingga jenazah.",
            babTitles = listOf(
                "Definisi Sholat & Waktu Sholat Lima Waktu (hlm. 50-54)",
                "Syarat Wajib & Syarat Sah Sholat (hlm. 54-58)",
                "18 Rukun Sholat & Kesunnahan Sholat (hlm. 59-69)",
                "Perkara yang Membatalkan & Waktu Makruh Sholat (hlm. 71-76)",
                "Sholat Jama'ah, Qashar-Jama', Jum'at & Hari Raya (hlm. 77-90)",
                "Sholat Gerhana, Istisqa', Khauf, Pakaian & Jenazah (hlm. 91-105)"
            )
        ),
        KitabSection(
            id = "kitab_zakat",
            bookId = "fathul_qorib",
            title = "Kitab Hukum-Hukum Zakat",
            arabicTitle = "كتاب أحكام الزكاة",
            category = KitabCategory.IBADAH,
            startPage = 106,
            endPage = 123,
            sequence = 4,
            summary = "Kewajiban zakat pada lima jenis harta: binatang ternak (onta, sapi, kambing), emas dan perak, hasil pertanian, buah-buahan, barang dagangan, zakat fitrah, serta delapan golongan mustahik zakat.",
            babTitles = listOf(
                "Harta Wajib Zakat & Syarat Wajib Zakat (hlm. 106-110)",
                "Zakat Ternak, Emas Perak, Pertanian & Dagangan (hlm. 110-118)",
                "Zakat Fitrah & Delapan Golongan Penerima Zakat (hlm. 119-123)"
            )
        ),
        KitabSection(
            id = "kitab_puasa",
            bookId = "fathul_qorib",
            title = "Kitab Hukum-Hukum Berpuasa",
            arabicTitle = "كتاب أحكام الصيام",
            category = KitabCategory.IBADAH,
            startPage = 124,
            endPage = 131,
            sequence = 5,
            summary = "Syarat wajib puasa Ramadhan, empat fardlu puasa, sepuluh perkara yang membatalkan puasa, hari-hari haram berpuasa, kafarat jima', serta hukum i'tikaf di masjid.",
            babTitles = listOf(
                "Syarat Wajib, Fardlu & Hal yang Membatalkan Puasa (hlm. 124-129)",
                "Hari Haram Puasa & Bab I'tikaf (hlm. 130-131)"
            )
        ),
        KitabSection(
            id = "kitab_haji",
            bookId = "fathul_qorib",
            title = "Kitab Hukum-Hukum Haji",
            arabicTitle = "كتاب أحكام الحج",
            category = KitabCategory.IBADAH,
            startPage = 132,
            endPage = 150,
            sequence = 6,
            summary = "Tujuh syarat wajib haji (istitha'ah), rukun haji dan umrah, wajib haji, larangan dalam ihram, serta rincian dam (denda) pelanggaran ihram.",
            babTitles = listOf(
                "Syarat Wajib Haji & Rukun Haji-Umrah (hlm. 132-140)",
                "Wajib Haji, Sunnah Haji & Dam dalam Ihram (hlm. 141-150)"
            )
        ),
        KitabSection(
            id = "kitab_muamalah",
            bookId = "fathul_qorib",
            title = "Kitab Jual Beli & Muamalah",
            arabicTitle = "كتاب البيوع وغيرها من المعاملات",
            category = KitabCategory.MUAMALAH,
            startPage = 151,
            endPage = 216,
            sequence = 7,
            summary = "Klasifikasi akad jual beli, larangan riba pada emas-perak dan makanan, khiyar, akad salam, rahn (gadai), hajr, shuluh, hawalah, dlaman, syirkah, wakalah, iqrar, 'ariyyah, ghasab, syuf'ah, qiradl, musaqah, ijarah, ju'alah, wakaf, hibah, luqathah, dan wadi'ah.",
            babTitles = listOf(
                "Hukum Jual Beli & Syarat Akad Sah (hlm. 151-153)",
                "Bab Riba & Khiyar (hlm. 154-158)",
                "Salam, Pegadaian, Hajr, Syirkah, Wakalah & Qiradl (hlm. 159-191)",
                "Sewa (Ijarah), Wakaf, Hibah, Luqathah & Wadi'ah (hlm. 194-216)"
            )
        ),
        KitabSection(
            id = "kitab_faraidl",
            bookId = "fathul_qorib",
            title = "Kitab Faraidl (Waris) & Wasiat",
            arabicTitle = "كتاب الفرائض والوصايا",
            category = KitabCategory.MUAMALAH,
            startPage = 217,
            endPage = 226,
            sequence = 8,
            summary = "Daftar ahli waris laki-laki (10/15) dan perempuan (7/10), ahli waris yang tidak gugur, tujuh penghalang waris, bagian-bagian pasti (furudhul muqaddarah), serta hukum wasiat.",
            babTitles = listOf(
                "Ahli Waris Laki-Laki, Perempuan & Penghalang Waris (hlm. 217-219)",
                "Bagian-Bagian Pasti (1/2, 1/4, 1/8, 2/3, 1/3, 1/6) & Wasiat (hlm. 220-226)"
            )
        ),
        KitabSection(
            id = "kitab_nikah",
            bookId = "fathul_qorib",
            title = "Kitab Hukum-Hukum Nikah",
            arabicTitle = "كتاب أحكام النكاح",
            category = KitabCategory.MUNAKAHAT,
            startPage = 227,
            endPage = 282,
            sequence = 9,
            summary = "Hukum nikah, khitbah dan hukum memandang lawan jenis, syarat sah nikah (wali & dua saksi adil), mahram, mas kawin, walimah, khulu', talak, rujuk, ila', dhihar, li'an, 'iddah, radla', nafaqah, dan hadlanah.",
            babTitles = listOf(
                "Hukum Nikah, Pandangan & Syarat Sah Nikah (hlm. 227-235)",
                "Wanita Mahram, Mas Kawin, Walimah & Nusyuz (hlm. 236-249)",
                "Khulu', Talak, Talak Raj'i, Ila', Dhihar & Li'an (hlm. 250-265)",
                "Bab 'Iddah, Radla' (Susuan), Nafaqah & Hadlanah (hlm. 266-282)"
            )
        ),
        KitabSection(
            id = "kitab_jinayat",
            bookId = "fathul_qorib",
            title = "Kitab Hukum-Hukum Jinayat",
            arabicTitle = "كتاب الجنايات",
            category = KitabCategory.JINAYAT_QADLA,
            startPage = 283,
            endPage = 298,
            sequence = 10,
            summary = "Pembagian pembunuhan (sengaja, semi sengaja, tersalah), syarat qishash, ketentuan diyat (mughalladhah & mukhaffafah), dan qasamah.",
            babTitles = listOf("Klasifikasi Pembunuhan & Qishash (hlm. 283-289)", "Bab Diyat & Qasamah (hlm. 290-298)")
        ),
        KitabSection(
            id = "kitab_hudud",
            bookId = "fathul_qorib",
            title = "Kitab Hukum-Hukum Had (Hudud)",
            arabicTitle = "كتاب الحدود",
            category = KitabCategory.JINAYAT_QADLA,
            startPage = 299,
            endPage = 314,
            sequence = 11,
            summary = "Ketentuan hukuman had: zina, qadzaf (menuduh zina), minuman keras, pencurian, begal jalanan, shiyal (membela diri), bughat, murtad, dan orang meninggalkan sholat.",
            babTitles = listOf("Had Zina, Qadzaf & Khamr (hlm. 299-304)", "Pencurian, Membela Diri, Bughat & Murtad (hlm. 305-314)")
        ),
        KitabSection(
            id = "kitab_jihad",
            bookId = "fathul_qorib",
            title = "Kitab Hukum-Hukum Jihad",
            arabicTitle = "كتاب الجهاد",
            category = KitabCategory.JINAYAT_QADLA,
            startPage = 315,
            endPage = 328,
            sequence = 12,
            summary = "Syarat kewajiban jihad, etika peperangan dalam Islam, pembagian salab & ghanimah, harta fai', dan ketentuan jizyah bagi ahli dzimmah.",
            babTitles = listOf("Hukum Jihad, Ghanimah, Fai' & Jizyah (hlm. 315-328)")
        ),
        KitabSection(
            id = "kitab_buruan_kurban",
            bookId = "fathul_qorib",
            title = "Kitab Buruan, Kurban & Aqiqah",
            arabicTitle = "كتاب الصيد والذبائح والأضحية والعقيقة",
            category = KitabCategory.IBADAH,
            startPage = 329,
            endPage = 342,
            sequence = 13,
            summary = "Hukum berburu dan menyembelih binatang halal, kriteria hewan halal-haram, hukum kurban (udhhiyah) pada Idul Adha, serta ketentuan aqiqah bayi.",
            babTitles = listOf("Penyembelihan & Binatang Halal (hlm. 329-334)", "Bab Kurban & Bab Aqiqah (hlm. 335-342)")
        ),
        KitabSection(
            id = "kitab_lomba_sumpah",
            bookId = "fathul_qorib",
            title = "Kitab Perlombaan, Sumpah & Nadzar",
            arabicTitle = "كتاب السبق والرمي والأيمان والنذور",
            category = KitabCategory.MUAMALAH,
            startPage = 343,
            endPage = 350,
            sequence = 14,
            summary = "Hukum perlombaan ketangkasan, keabsahan sumpah beserta kafaratnya, dan macam-macam nadzar beserta kewajiban menunaikannya.",
            babTitles = listOf("Perlombaan, Sumpah & Nadzar (hlm. 343-350)")
        ),
        KitabSection(
            id = "kitab_qadla",
            bookId = "fathul_qorib",
            title = "Kitab Qadla' & Persaksian",
            arabicTitle = "كتاب الأقضية والشهادات",
            category = KitabCategory.JINAYAT_QADLA,
            startPage = 351,
            endPage = 369,
            sequence = 15,
            summary = "Lima belas syarat menjadi qadli (hakim), adab persidangan, pembagian hak (qismah), alat bukti (bayyinah), syarat saksi adil, serta hak-hak Allah dan hak adami.",
            babTitles = listOf("Syarat Hakim (Qadli) & Etika Mengadili (hlm. 351-359)", "Bayyinah, Syarat Saksi & Huquq (hlm. 360-369)")
        ),
        KitabSection(
            id = "kitab_itqu",
            bookId = "fathul_qorib",
            title = "Kitab Memerdekakan Budak & Ummu Walad",
            arabicTitle = "كتاب العتق وأمهات الأولاد",
            category = KitabCategory.MUNAKAHAT,
            startPage = 370,
            endPage = 380,
            sequence = 16,
            summary = "Hukum memerdekakan budak ('itqu), hak wala', budak mudabbar, akad kitabah (mukatab), dan hukum ummu walad sebagai penutup kitab Fathul Qorib.",
            babTitles = listOf("Memerdekakan Budak, Wala', Mudabbar, Kitabah & Ummu Walad (hlm. 370-380)")
        )
    )

    val chapters: List<Chapter> = listOf(
        Chapter(
            id = "ch_muqaddimah",
            kitabId = "kitab_muqaddimah",
            bookId = "fathul_qorib",
            title = "Muqaddimah & Pengantar Kitab",
            subtitle = "Sejarah Matan Taqrib & Definisi Ilmu Fiqih",
            category = KitabCategory.IBADAH,
            startPage = 1,
            endPage = 4,
            sequence = 1
        ),
        Chapter(
            id = "ch_thaharah_air",
            kitabId = "kitab_thaharah",
            bookId = "fathul_qorib",
            title = "Thaharah & Pembagian Air",
            subtitle = "Tujuh sumber air, air mutlak, musyammas, musta'mal, dan mutanajjis",
            category = KitabCategory.IBADAH,
            startPage = 4,
            endPage = 8,
            sequence = 2
        ),
        Chapter(
            id = "ch_wudlu_tayammum",
            kitabId = "kitab_thaharah",
            bookId = "fathul_qorib",
            title = "Bab Wudlu', Tayammum & Najis",
            subtitle = "Fardlu wudlu, pembatal wudlu, syarat tayammum, dan klasifikasi najis",
            category = KitabCategory.IBADAH,
            startPage = 11,
            endPage = 44,
            sequence = 3
        ),
        Chapter(
            id = "ch_sholat_rukun",
            kitabId = "kitab_sholat",
            bookId = "fathul_qorib",
            title = "Waktu, Syarat & Rukun Sholat",
            subtitle = "Waktu lima sholat fardlu, syarat sah, 18 rukun, dan jama'ah",
            category = KitabCategory.IBADAH,
            startPage = 50,
            endPage = 82,
            sequence = 4
        ),
        Chapter(
            id = "ch_zakat",
            kitabId = "kitab_zakat",
            bookId = "fathul_qorib",
            title = "Zakat Harta, Fitrah & Mustahik",
            subtitle = "Lima harta wajib zakat, zakat fitrah satu sha', dan 8 asnaf",
            category = KitabCategory.IBADAH,
            startPage = 106,
            endPage = 123,
            sequence = 5
        ),
        Chapter(
            id = "ch_puasa_haji",
            kitabId = "kitab_puasa",
            bookId = "fathul_qorib",
            title = "Puasa Ramadhan, I'tikaf & Haji",
            subtitle = "Fardlu & pembatal puasa, syarat wajib haji, dan rukun haji",
            category = KitabCategory.IBADAH,
            startPage = 124,
            endPage = 145,
            sequence = 6
        ),
        Chapter(
            id = "ch_muamalah",
            kitabId = "kitab_muamalah",
            bookId = "fathul_qorib",
            title = "Jual Beli, Riba & Khiyar",
            subtitle = "Akad sah jual beli, barang ribawi, khiyar majlis & aib",
            category = KitabCategory.MUAMALAH,
            startPage = 151,
            endPage = 162,
            sequence = 7
        ),
        Chapter(
            id = "ch_faraidl_nikah",
            kitabId = "kitab_nikah",
            bookId = "fathul_qorib",
            title = "Waris (Faraidl) & Hukum Nikah",
            subtitle = "Ahli waris utama, penghalang waris, syarat sah nikah & mahram",
            category = KitabCategory.MUNAKAHAT,
            startPage = 217,
            endPage = 252,
            sequence = 8
        ),
        Chapter(
            id = "ch_kurban_qadla",
            kitabId = "kitab_qadla",
            bookId = "fathul_qorib",
            title = "Kurban, Aqiqah, Qadla' & Penutup Kitab",
            subtitle = "Ketentuan kurban, aqiqah, syarat hakim (qadli), dan bab ummu walad",
            category = KitabCategory.JINAYAT_QADLA,
            startPage = 329,
            endPage = 380,
            sequence = 9
        )
    )

    val glossaryTerms: List<GlossaryTerm> = listOf(
        GlossaryTerm(
            id = "gl_tafaquh",
            term = "Tafaqquh fid-Din",
            arabicTerm = "التَّفَقُّهُ فِي الدِّينِ",
            definition = "Mendalami dan memahami hukum-hukum syariat Islam secara sungguh-sungguh berdasarkan dalil-dalilnya yang terperinci.",
            sourcePage = 1
        ),
        GlossaryTerm(
            id = "gl_qayyid_lazim",
            term = "Qayyid Lazim & Qayyid Munfak",
            arabicTerm = "قَيْدٌ لَازِمٌ وَقَيْدٌ مُنْفَكٌّ",
            definition = "Qayyid lazim adalah ikatan nama yang melekat permanen pada air sehingga tidak lagi disebut air mutlak (seperti air mawar atau air kelapa). Qayyid munfak adalah keterangan tambahan yang bisa lepas (seperti 'air sumur' atau 'air hujan') sehingga status air mutlaknya tetap terjaga.",
            sourcePage = 5
        ),
        GlossaryTerm(
            id = "gl_musyammas",
            term = "Air Musyammas",
            arabicTerm = "المَاءُ المُشَمَّسُ",
            definition = "Air suci mensucikan yang dipanaskan oleh terik sinar matahari di daerah beriklim panas dalam wadah logam yang dapat berkarat (selain emas dan perak). Hukumnya makruh digunakan pada badan, bukan pada pakaian.",
            sourcePage = 5
        ),
        GlossaryTerm(
            id = "gl_mustamal",
            term = "Air Musta'mal",
            arabicTerm = "المَاءُ المُسْتَعْمَلُ",
            definition = "Air sedikit (kurang dari dua qullah) yang telah terpakai untuk mengangkat hadats (basuhan wajib) atau menghilangkan najis tanpa berubah sifat dan ukurannya; hukumnya suci namun tidak mensucikan.",
            sourcePage = 6
        ),
        GlossaryTerm(
            id = "gl_dua_qullah",
            term = "Dua Qullah",
            arabicTerm = "القُلَّتَانِ",
            definition = "Batas volume air banyak (± 500 rithl Baghdad atau sekitar 216 liter dalam takaran kubus ± 60 cm). Air dua qullah tidak menjadi najis sekadar kejatuhan najis kecuali berubah rasa, warna, atau baunya.",
            sourcePage = 7
        ),
        GlossaryTerm(
            id = "gl_qaul_jadid_qadim",
            term = "Qaul Qadim & Qaul Jadid",
            arabicTerm = "القَوْلُ القَدِيمُ وَالقَوْلُ الجَدِيدُ",
            definition = "Qaul Qadim adalah fatwa Imam Asy-Syafi'i saat berada di Irak; Qaul Jadid adalah fatwa beliau setelah pindah dan menetap di Mesir. Dalam mazhab Syafi'i, Qaul Jadid umumnya didahulukan kecuali pada masalah-masalah tertentu.",
            sourcePage = 7
        ),
        GlossaryTerm(
            id = "gl_istinja",
            term = "Istinja'",
            arabicTerm = "الِاسْتِنْجَاءُ",
            definition = "Bersuci dari kotoran yang keluar melalui qubul atau dubur menggunakan air suci mensucikan, batu suci (minimal tiga usapan), atau benda padat yang memenuhi syarat.",
            sourcePage = 19
        ),
        GlossaryTerm(
            id = "gl_tayammum",
            term = "Tayammum",
            arabicTerm = "التَّيَمُّمُ",
            definition = "Secara bahasa berarti menyengaja; secara syara' adalah mengusapkan debu suci yang mensucikan dan berdebu (ghubar) pada wajah dan kedua tangan sebagai pengganti wudlu atau mandi karena udzur.",
            sourcePage = 33
        ),
        GlossaryTerm(
            id = "gl_muwallah",
            term = "Muwallah (Muwalah)",
            arabicTerm = "المُوَالَاةُ",
            definition = "Membaca atau mengerjakan rangkaian ibadah secara berkesinambungan tanpa jeda panjang yang memutus kesinambungan, seperti bacaan ayat-ayat surat Al-Fatihah dalam sholat.",
            sourcePage = 60
        ),
        GlossaryTerm(
            id = "gl_nishab_haul",
            term = "Nishab & Haul",
            arabicTerm = "النِّصَابُ وَالحَوْلُ",
            definition = "Nishab adalah batas minimal jumlah harta yang mewajibkan zakat (misal 20 mitsqal emas atau 5 wasaq hasil pertanian). Haul adalah berlalunya satu tahun penuh kepemilikan harta secara sempurna.",
            sourcePage = 107
        ),
        GlossaryTerm(
            id = "gl_sha",
            term = "Satu Sha'",
            arabicTerm = "الصَّاعُ",
            definition = "Takaran baku zakat fitrah sebesar 4 mud atau 5 1/3 rithl Irak (± 2,5 - 2,75 kg makanan pokok daerah setempat).",
            sourcePage = 120
        ),
        GlossaryTerm(
            id = "gl_jauf",
            term = "Jauf",
            arabicTerm = "الجَوْفُ",
            definition = "Rongga bagian dalam tubuh manusia (seperti lambung, tenggorokan bagian dalam, rongga kepala) yang menjadi batas batalnya puasa jika benda masuk ke dalamnya secara sengaja.",
            sourcePage = 125
        ),
        GlossaryTerm(
            id = "gl_marhalah",
            term = "Dua Marhalah",
            arabicTerm = "المَرْحَلَتَانِ",
            definition = "Jarak tempuh perjalanan jauh yang menjadi batas kebolehan sholat qashar-jama' dan syarat kendaraan pada ibadah haji (± 80-89 km).",
            sourcePage = 80
        ),
        GlossaryTerm(
            id = "gl_khiyar",
            term = "Khiyar",
            arabicTerm = "الخِيَارُ",
            definition = "Hak pilih bagi penjual atau pembeli untuk melanjutkan atau membatalkan akad jual beli, meliputi khiyar majlis, khiyar syarat (maksimal 3 hari), dan khiyar aib (cacat barang).",
            sourcePage = 156
        ),
        GlossaryTerm(
            id = "gl_riba_naqdan",
            term = "Riba & Naqdan",
            arabicTerm = "الرِّبَا وَالنَّقْدُ",
            definition = "Riba adalah tambahan atau penukaran barang sejenis (emas, perak, makanan) tanpa kesamaan takaran atau tanpa serah terima tunai langsung (naqdan) sebelum berpisah dari majelis akad.",
            sourcePage = 154
        ),
        GlossaryTerm(
            id = "gl_furudhul_muqaddarah",
            term = "Furudhul Muqaddarah",
            arabicTerm = "الفُرُوضُ المُقَدَّرَةُ",
            definition = "Enam bagian warisan pasti yang telah ditetapkan Al-Qur'an, yaitu setengah (1/2), seperempat (1/4), seperdelapan (1/8), dua pertiga (2/3), sepertiga (1/3), dan seperenam (1/6).",
            sourcePage = 220
        ),
        GlossaryTerm(
            id = "gl_qadli",
            term = "Qadli & Bayyinah",
            arabicTerm = "القَاضِي وَالبَيِّنَةُ",
            definition = "Qadli adalah hakim syar'i yang memutus sengketa secara adil. Bayyinah adalah alat bukti atau saksi yang sah untuk menguatkan gugatan di persidangan.",
            sourcePage = 351
        ),
        GlossaryTerm(
            id = "gl_ummu_walad",
            term = "Ummu Walad",
            arabicTerm = "أُمُّ الوَلَدِ",
            definition = "Istilah fiqih klasik dalam bab penutup Fathul Qorib bagi budak wanita yang melahirkan anak dari tuannya sehingga ia merdeka secara otomatis setelah wafatnya sang tuan.",
            sourcePage = 377
        )
    )

    fun glossaryById(id: String): GlossaryTerm? = glossaryTerms.firstOrNull { it.id == id }
}
