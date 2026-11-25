package com.example.data

import com.example.model.BlockType
import com.example.model.EditorialStatus
import com.example.model.Lesson
import com.example.model.LessonBlock

object FathulQoribLessonsPart2 {

    val lessons: List<Lesson> = listOf(
        // --- KITAB ZAKAT ---
        Lesson(
            id = "ls_zak_1",
            bookId = "fathul_qorib",
            chapterId = "ch_zakat",
            kitabTitle = "Kitab Zakat",
            title = "Lima Jenis Harta Wajib Zakat & Syarat-Syaratnya",
            summary = "Pengelompokan lima harta wajib zakat dalam Fathul Qorib: hewan ternak, emas-perak, hasil pertanian, buah-buahan, dan barang dagangan.",
            sourcePage = 106,
            estimatedMinutes = 7,
            sequence = 10,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_nishab_haul", "gl_qaul_jadid_qadim"),
            blocks = listOf(
                LessonBlock("b_z1_1", BlockType.HEADING, "Harta yang Wajib Dizakati & Syarat Kepemilikan", 1, 106),
                LessonBlock(
                    "b_z1_2",
                    BlockType.ARABIC,
                    "كِتَابُ أَحْكَامِ الزَّكَاةِ: تَجِبُ الزَّكَاةُ فِي خَمْسَةِ أَشْيَاءَ: وَهِيَ الْمَوَاشِي، وَالْأَثْمَانُ، وَالزُّرُوعُ، وَالثِّمَارُ، وَعُرُوضُ التِّجَارَةِ.",
                    2,
                    106
                ),
                LessonBlock(
                    "b_z1_3",
                    BlockType.TRANSLATION,
                    "Zakat wajib dikeluarkan pada lima jenis harta: (1) Binatang ternak (onta, sapi, dan kambing), (2) Atsman (mata uang emas dan perak, baik sudah dicetak maupun belum), (3) Zuru' (hasil pertanian bahan makanan pokok seperti gandum, beras, jagung, dan kacang), (4) Buah-buahan (kurma kering dan anggur kering), serta (5) Barang dagangan (tijarah).",
                    3,
                    108
                ),
                LessonBlock(
                    "b_z1_4",
                    BlockType.EXPLANATION,
                    "Syarat wajib zakat ternak ada enam: Islam, merdeka, milik sempurna (milik yang lemah seperti barang dibeli namun belum diterima tidak wajib dizakati menurut Qaul Qadim, sedangkan menurut Qaul Jadid wajib), mencapai satu nishab, mencapai satu tahun (haul), dan saum (digembalakan di rumput mubah lebih dari separuh tahun). Untuk hasil pertanian, syaratnya ditanam manusia, tahan disimpan sebagai makanan pokok, dan mencapai nishab 5 wasaq tanpa kulit.",
                    4,
                    107
                )
            )
        ),
        Lesson(
            id = "ls_zak_2",
            bookId = "fathul_qorib",
            chapterId = "ch_zakat",
            kitabTitle = "Kitab Zakat",
            title = "Zakat Fitrah & Delapan Golongan Penerima Zakat",
            summary = "Tiga syarat wajib zakat fitrah, ukuran satu sha' makanan pokok, dan penjelasan delapan golongan mustahik zakat.",
            sourcePage = 119,
            estimatedMinutes = 7,
            sequence = 11,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_sha"),
            blocks = listOf(
                LessonBlock("b_z2_1", BlockType.HEADING, "Zakat Fitrah & 8 Asnaf Mustahik Zakat", 1, 119),
                LessonBlock(
                    "b_z2_2",
                    BlockType.ARABIC,
                    "فَصْلٌ: وَتَجِبُ زَكَاةُ الْفِطْرِ بِثَلَاثَةِ شَرَائِطَ: الْإِسْلَامِ، وَبِغُرُوبِ الشَّمْسِ مِنْ آخِرِ يَوْمٍ مِنْ شَهْرِ رَمَضَانَ، وَوُجُودِ الْفَضْلِ عَنْ قُوتِهِ وَقُوتِ عِيَالِهِ فِي ذَلِكَ الْيَوْمِ.",
                    2,
                    119
                ),
                LessonBlock(
                    "b_z2_3",
                    BlockType.TRANSLATION,
                    "Wajib mengeluarkan zakat fitrah dengan tiga syarat: (1) Islam; (2) Menemui terbenamnya matahari pada hari terakhir bulan Ramadhan (sehingga wajib atas orang yang wafat setelah maghrib akhir Ramadhan, tidak atas bayi yang lahir setelah terbenam matahari); dan (3) Memiliki kelebihan dari makanan pokok untuk dirinya dan keluarga yang wajib dinafkahinya pada siang dan malam hari raya Idul Fitri. Ukurannya satu sha' (5 1/3 rithl Irak) makanan pokok daerahnya.",
                    3,
                    120
                ),
                LessonBlock(
                    "b_z2_4",
                    BlockType.EXPLANATION,
                    "Pada halaman 121, dijelaskan delapan golongan penerima zakat (QS. At-Taubah: 60): (1) Fakir (tidak punya harta dan pekerjaan yang mencukupi), (2) Miskin (punya harta/pekerjaan namun belum mencukupi kebutuhan), (3) Amil (petugas pengumpul dan penyalur zakat yang diangkat imam), (4) Muallaf qulubuhum, (5) Riqab (budak mukatab sah), (6) Gharim (orang berhutang untuk kemaslahatan atau kebutuhan mubah), (7) Fi sabilillah, dan (8) Ibnu sabil (musafir kehabisan bekal).",
                    4,
                    121
                )
            )
        ),

        // --- KITAB PUASA & HAJI ---
        Lesson(
            id = "ls_pua_1",
            bookId = "fathul_qorib",
            chapterId = "ch_puasa_haji",
            kitabTitle = "Kitab Puasa",
            title = "Fardlu Puasa & Sepuluh Perkara Pembatal Puasa",
            summary = "Syarat wajib puasa Ramadhan, empat fardlu puasa, serta rincian hal-hal yang membatalkan puasa termasuk hukum makan karena lupa.",
            sourcePage = 124,
            estimatedMinutes = 7,
            sequence = 12,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_jauf"),
            blocks = listOf(
                LessonBlock("b_p1_1", BlockType.HEADING, "Fardlu Puasa & Hal yang Membatalkan Puasa", 1, 124),
                LessonBlock(
                    "b_p1_2",
                    BlockType.ARABIC,
                    "كِتَابُ أَحْكَامِ الصِّيَامِ: وَفَرَائِضُ الصَّوْمِ أَرْبَعَةُ أَشْيَاءَ: النِّيَّةُ، وَالْإِمْسَاكُ عَنِ الْأَكْلِ وَالشُّرْبِ، وَالْجِمَاعِ، وَتَعَمُّدِ الْقَيْءِ.",
                    2,
                    124
                ),
                LessonBlock(
                    "b_p1_3",
                    BlockType.TRANSLATION,
                    "Fardlu-fardlu puasa ada empat perkara: (1) Niat pada malam hari untuk puasa fardlu; (2) Menahan diri dari makan dan minum walaupun sedikit apabila ada unsur kesengajaan; (3) Menahan diri dari melakukan jima' dengan sengaja; dan (4) Menahan diri dari muntah dengan sengaja. Apabila seseorang makan atau minum dalam keadaan lupa, puasanya tidak batal.",
                    3,
                    125
                ),
                LessonBlock(
                    "b_p1_4",
                    BlockType.EXPLANATION,
                    "Hal-hal yang membatalkan puasa ada sepuluh perkara: (1-2) sampainya benda secara sengaja ke dalam rongga dalam tubuh (jauf) baik lubang terbuka maupun luka tembus, (3) huqnah (suntikan obat lewat qubul atau dubur), (4) muntah disengaja, (5) wathi sengaja di farji, (6) inzal (keluar mani sebab bersentuhan kulit tanpa jima'), (7) haidl, (8) nifas, (9) gila, dan (10) murtad. Pada bab berikutnya (hlm. 131), dijelaskan pula kesunnahan i'tikaf di masjid.",
                    4,
                    125
                )
            )
        ),
        Lesson(
            id = "ls_haj_1",
            bookId = "fathul_qorib",
            chapterId = "ch_puasa_haji",
            kitabTitle = "Kitab Haji",
            title = "Syarat Wajib & Empat Rukun Haji",
            summary = "Tujuh syarat istitha'ah kewajiban haji, empat rukun haji (ihram, wukuf di Arafah, thawaf, sa'i), dan perbedaannya dengan wajib haji.",
            sourcePage = 132,
            estimatedMinutes = 7,
            sequence = 13,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_marhalah"),
            blocks = listOf(
                LessonBlock("b_h1_1", BlockType.HEADING, "Syarat Wajib Haji & Rukun-Rukun Haji", 1, 132),
                LessonBlock(
                    "b_h1_2",
                    BlockType.ARABIC,
                    "كِتَابُ أَحْكَامِ الْحَجِّ: وَشَرَائِطُ وُجُوبِ الْحَجِّ سَبْعَةُ أَشْيَاءَ: الْإِسْلَامُ، وَالْبُلُوغُ، وَالْعَقْلُ، وَالْحُرِّيَّةُ، وَوُجُودُ الزَّادِ وَالرَّاحِلَةِ، وَتَخْلِيَةُ الطَّرِيقِ، وَإِمْكَانُ الْمَسِيرِ.",
                    2,
                    133
                ),
                LessonBlock(
                    "b_h1_3",
                    BlockType.TRANSLATION,
                    "Haji secara bahasa adalah menyengaja; secara syara' adalah menyengaja Baitul Haram guna melaksanakan ibadah. Syarat wajib haji ada tujuh: Islam, baligh, berakal, merdeka, adanya bekal dan kendaraan yang layak (jika jaraknya dua marhalah atau lebih dan berlebih dari hutang serta nafkah keluarga), amannya jalan, dan memungkinkan menempuh perjalanan.",
                    3,
                    133
                ),
                LessonBlock(
                    "b_h1_4",
                    BlockType.EXPLANATION,
                    "Rukun haji ada empat (atau lima dengan tertib dan mencukur rambut/tahallul): (1) Ihram disertai niat masuk ibadah haji, (2) Wukuf di Arafah walau sebentar setelah tergelincir matahari tanggal 9 Dzulhijjah hingga terbit fajar 10 Dzulhijjah, (3) Thawaf ifadhah di Baitullah, dan (4) Sa'i antara Shafa dan Marwah. Rukun haji tidak bisa diganti dengan dam apabila ditinggalkan, berbeda dengan wajib haji.",
                    4,
                    134
                )
            )
        ),

        // --- KITAB JUAL BELI & MUAMALAH ---
        Lesson(
            id = "ls_mua_1",
            bookId = "fathul_qorib",
            chapterId = "ch_muamalah",
            kitabTitle = "Kitab Jual Beli",
            title = "Klasifikasi Jual Beli, Larangan Riba & Khiyar",
            summary = "Tiga kategori benda dalam jual beli, aturan ketat barang ribawi (emas, perak, makanan), dan tiga macam khiyar dalam transaksi.",
            sourcePage = 151,
            estimatedMinutes = 8,
            sequence = 14,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_riba_naqdan", "gl_khiyar"),
            blocks = listOf(
                LessonBlock("b_mu1_1", BlockType.HEADING, "Hukum Jual Beli, Riba & Khiyar", 1, 154),
                LessonBlock(
                    "b_mu1_2",
                    BlockType.ARABIC,
                    "فَصْلٌ: وَالرِّبَا حَرَامٌ، وَلَا يَكُونُ إِلَّا فِي الذَّهَبِ وَالْفِضَّةِ وَالْمَطْعُومَاتِ، وَلَا يَجُوزُ بَيْعُ الذَّهَبِ بِالذَّهَبِ وَلَا الْفِضَّةِ بِالْفِضَّةِ إِلَّا مُتَمَاثِلًا نَقْدًا.",
                    2,
                    154
                ),
                LessonBlock(
                    "b_mu1_3",
                    BlockType.TRANSLATION,
                    "Akad riba hukumnya haram, dan riba hanya terjadi pada emas, perak, dan makanan (baik makanan penguat badan, camilan, maupun obat-obatan). Tidak boleh menjual emas dengan emas atau perak dengan perak — baik sudah dicetak maupun belum — kecuali ukurannya sama dan naqdan (serah terima tunai secara langsung sebelum berpisah). Jika menjual emas dengan perak boleh berbeda ukuran namun wajib kontan sebelum berpisah.",
                    3,
                    155
                ),
                LessonBlock(
                    "b_mu1_4",
                    BlockType.EXPLANATION,
                    "Transaksi yang mengandung gharar/penipuan dilarang, seperti menjual barang yang belum diterima atau menjual burung yang masih terbang di angkasa. Pada Bab Khiyar (hlm. 156), penjual dan pembeli memiliki khiyar majlis selama belum berpisah badan secara 'urf, boleh menyepakati khiyar syarat maksimal tiga hari, serta memiliki khiyar aib jika ditemukan cacat tersembunyi.",
                    4,
                    156
                )
            )
        ),

        // --- KITAB FARAIDL & NIKAH ---
        Lesson(
            id = "ls_far_1",
            bookId = "fathul_qorib",
            chapterId = "ch_faraidl_nikah",
            kitabTitle = "Kitab Faraidl & Wasiat",
            title = "Ahli Waris Utama & Tujuh Penghalang Warisan",
            summary = "Golongan ahli waris laki-laki dan perempuan, lima ahli waris yang pasti menerima warisan, serta penghalang mewaris.",
            sourcePage = 217,
            estimatedMinutes = 7,
            sequence = 15,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_furudhul_muqaddarah"),
            blocks = listOf(
                LessonBlock("b_f1_1", BlockType.HEADING, "Kitab Faraidl (Waris): Ahli Waris & Penghalangnya", 1, 217),
                LessonBlock(
                    "b_f1_2",
                    BlockType.ARABIC,
                    "كِتَابُ الْفَرَائِضِ وَالْوَصَايَا: الْوَارِثُونَ مِنَ الرِّجَالِ عَشَرَةٌ، وَالْوَارِثَاتُ مِنَ النِّسَاءِ سَبْعٌ، وَمَنْ لَا يَسْقُطُ بِحَالٍ خَمْسَةٌ: الزَّوْجَانِ وَالْأَبَوَانِ وَوَلَدُ الصُّلْبِ.",
                    2,
                    217
                ),
                LessonBlock(
                    "b_f1_3",
                    BlockType.TRANSLATION,
                    "Golongan ahli waris dari pihak laki-laki yang disepakati ada sepuluh secara ringkas (lima belas secara rinci), dan bila seluruhnya berkumpul yang mendapat warisan hanya ayah, anak laki-laki, dan suami. Ahli waris perempuan ada tujuh secara ringkas (sepuluh secara rinci). Adapun lima orang yang pasti mendapat warisan dan tidak pernah gugur dalam keadaan apa pun adalah suami atau istri (zaujain), ayah dan ibu (abawain), serta anak kandung.",
                    3,
                    218
                ),
                LessonBlock(
                    "b_f1_4",
                    BlockType.EXPLANATION,
                    "Tujuh orang yang terhalang dan sama sekali tidak bisa mewaris (hlm. 218-219): budak, mudabbar, ummu walad, mukatab, pembunuh (tidak bisa mewaris orang yang ia bunuh), orang murtad/zindiq, serta pemeluk dua agama yang berbeda (muslim tidak mewarisi kafir dan sebaliknya). Pada halaman 220 dirinci bagian pasti: 1/2, 1/4, 1/8, 2/3, 1/3, dan 1/6.",
                    4,
                    219
                )
            )
        ),
        Lesson(
            id = "ls_nik_1",
            bookId = "fathul_qorib",
            chapterId = "ch_faraidl_nikah",
            kitabTitle = "Kitab Nikah",
            title = "Hukum Nikah, Pandangan Khitbah & Syarat Sah Akad",
            summary = "Anjuran nikah bagi yang mampu nafkah dan mahar, tujuh hukum memandang lawan jenis, serta rukun-syarat wali dan dua saksi adil.",
            sourcePage = 227,
            estimatedMinutes = 8,
            sequence = 16,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = emptyList(),
            blocks = listOf(
                LessonBlock("b_n1_1", BlockType.HEADING, "Hukum Nikah & Syarat Sah Akad Nikah", 1, 227),
                LessonBlock(
                    "b_n1_2",
                    BlockType.ARABIC,
                    "كِتَابُ أَحْكَامِ النِّكَاحِ: النِّكَاحُ مُسْتَحَبٌّ لِمَنْ يَحْتَاجُ إِلَيْهِ، وَلَا يَصِحُّ عَقْدُ النِّكَاحِ إِلَّا بِوَلِيٍّ وَشَاهِدَيْ عَدْلٍ.",
                    2,
                    228
                ),
                LessonBlock(
                    "b_n1_3",
                    BlockType.TRANSLATION,
                    "Nikah disunnahkan bagi orang yang membutuhkannya sebab dorongan kuat dalam dirinya dan ia memiliki biaya seperti mas kawin dan nafkah. Jika tidak memiliki biaya, maka tidak disunnahkan baginya menikah. Akad nikah hukumnya tidak sah kecuali disertai dengan wali laki-laki yang adil dan hadirnya dua orang saksi yang adil.",
                    3,
                    232
                ),
                LessonBlock(
                    "b_n1_4",
                    BlockType.EXPLANATION,
                    "Fathul Qorib halaman 230 merinci tujuh hukum memandang lawan jenis: haram memandang bukan mahram tanpa hajat; boleh memandang wajah dan kedua telapak tangan luar-dalam bagi laki-laki yang hendak melamar/menikahi wanita tersebut; serta boleh dalam batas kebutuhan medis, persaksian, dan muamalah. Seorang wanita tidak dapat menikahkan dirinya sendiri maupun orang lain tanpa wali.",
                    4,
                    230
                )
            )
        ),

        // --- KITAB KURBAN, QADLA' & PENUTUP ---
        Lesson(
            id = "ls_qad_1",
            bookId = "fathul_qorib",
            chapterId = "ch_kurban_qadla",
            kitabTitle = "Kitab Kurban, Qadla' & Penutup",
            title = "Kurban, Syarat Qadli (Hakim) & Bab Ummu Walad",
            summary = "Hukum sunnah muakkad berkurban, syarat-syarat seorang hakim (qadli) dalam Islam, hingga penutup kitab Fathul Qorib.",
            sourcePage = 335,
            estimatedMinutes = 7,
            sequence = 17,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_qadli", "gl_ummu_walad"),
            blocks = listOf(
                LessonBlock("b_q1_1", BlockType.HEADING, "Kurban (Udhhiyah), Hakim Adil & Penutup Kitab", 1, 335),
                LessonBlock(
                    "b_q1_2",
                    BlockType.ARABIC,
                    "فَصْلٌ: وَالْأُضْحِيَّةُ سُنَّةٌ مُؤَكَّدَةٌ، وَشَرَائِطُ الْقَضَاءِ خَمْسَ عَشْرَةَ خَصْلَةً: الْإِسْلَامُ، وَالْبُلُوغُ، وَالْعَقْلُ، وَالْحُرِّيَّةُ، وَالذُّكُورَةُ، وَالْعَدَالَةُ، وَمَعْرِفَةُ أَحْكَامِ الْكِتَابِ وَالسُّنَّةِ.",
                    2,
                    351
                ),
                LessonBlock(
                    "b_q1_3",
                    BlockType.TRANSLATION,
                    "Berkurban pada hari raya Idul Adha dan hari-hari tasyriq hukumnya sunnah muakkad dengan hewan ternak yang bebas dari cacat yang mengurangi daging. Adapun syarat seorang hakim (qadli) ada lima belas perkara, di antaranya: Islam, baligh, berakal, merdeka, laki-laki, adil, dan menguasai hukum Al-Qur'an dan As-Sunnah.",
                    3,
                    351
                ),
                LessonBlock(
                    "b_q1_4",
                    BlockType.EXPLANATION,
                    "Pada bab-bab akhir (hlm. 370-380), Fathul Qorib membahas pembebasan budak ('itqu), hak wala', akad kitabah, dan ditutup dengan Bab Ummu Walad pada halaman 377–380. Syaikh Muhammad bin Qasim Al-Ghazi menutup syarahnya dengan memuji Allah dan memohon manfaat atas kitab ini bagi para penuntut ilmu syariat.",
                    4,
                    377
                )
            )
        )
    )
}
