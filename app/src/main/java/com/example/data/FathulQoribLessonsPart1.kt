package com.example.data

import com.example.model.BlockType
import com.example.model.EditorialStatus
import com.example.model.Lesson
import com.example.model.LessonBlock

object FathulQoribLessonsPart1 {

    val lessons: List<Lesson> = listOf(
        // --- MUQADDIMAH ---
        Lesson(
            id = "ls_muq_1",
            bookId = "fathul_qorib",
            chapterId = "ch_muqaddimah",
            kitabTitle = "Muqaddimah",
            title = "Pembukaan Kitab & Penamaan Fathul Qorib",
            summary = "Khutbah pembukaan Syaikh Muhammad bin Qasim Al-Ghazi, keutamaan tafaqquh fid-din, dan dua nama kitab syarah atas Matan At-Taqrib.",
            sourcePage = 1,
            estimatedMinutes = 5,
            sequence = 1,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_tafaquh"),
            blocks = listOf(
                LessonBlock("b_m1_1", BlockType.HEADING, "Khutbah Syarih & Keutamaan Memahami Agama", 1, 1),
                LessonBlock(
                    "b_m1_2",
                    BlockType.ARABIC,
                    "الْحَمْدُ لِلَّهِ تَبَرُّكًا بِفَاتِحَةِ الْكِتَابِ، وَأَحْمَدُهُ أَنْ وَفَّقَ مَنْ أَرَادَ مِنْ عِبَادِهِ لِلتَّفَقُّهِ فِي الدِّينِ عَلَى وَفْقِ مُرَادِهِ، وَأُصَلِّي وَأُسَلِّمُ عَلَى أَفْضَلِ خَلْقِهِ مُحَمَّدٍ سَيِّدِ الْمُرْسَلِينَ الْقَائِلِ: مَنْ يُرِدِ اللَّهُ بِهِ خَيْرًا يُفَقِّهْهُ فِي الدِّينِ.",
                    2,
                    1
                ),
                LessonBlock(
                    "b_m1_3",
                    BlockType.TRANSLATION,
                    "Seluruh pujian hanya hak Allah, memulainya dengan hamdalah karena berharap berkah, karena merupakan permulaan setiap urusan yang penting, penutup setiap doa yang diijabah, dan akhir ungkapan orang-orang mukmin di surga. Aku memuji-Nya yang telah memberikan taufiq kepada setiap yang Dia kehendaki dari kalangan para hamba-Nya untuk tafaqquh di dalam agama sesuai dengan yang dikehendaki-Nya. Aku bersholawat dan memohonkan keselamatan bagi makhluk termulia, Muhammad penghulu para utusan, yang bersabda: \"Barangsiapa yang Allah kehendaki kebaikannya maka Dia Ta'ala akan memahamkannya pada agama.\" (HR. Bukhari dan Muslim).",
                    3,
                    1
                ),
                LessonBlock(
                    "b_m1_4",
                    BlockType.EXPLANATION,
                    "Pada halaman pertama, Syaikh Syamsuddin Abu Abdillah Muhammad bin Qasim Al-Ghazi menjelaskan bahwa naskah matan karya Imam Abu Syuja' terkadang dinamai At-Taqrib dan terkadang Ghayatul Ikhtishar. Oleh karena itu, syarah ini diberi dua nama: pertama Fathul Qorib Al-Mujib fi Syarhi Alfazh At-Taqrib, dan kedua Al-Qaul Al-Mukhtar fi Syarhi Ghayatil Ikhtishar.",
                    4,
                    1
                ),
                LessonBlock(
                    "b_m1_5",
                    BlockType.FOOTNOTE,
                    "Pengarang Matan: Al-Imam Abu Thayyib Ahmad bin Al-Husain bin Ahmad Al-Ashfahani yang terkenal dengan gelar Abu Syuja' Syihabul Millah wad-Din.",
                    5,
                    1
                )
            )
        ),
        Lesson(
            id = "ls_muq_2",
            bookId = "fathul_qorib",
            chapterId = "ch_muqaddimah",
            kitabTitle = "Muqaddimah",
            title = "Definisi Ilmu Fiqih & Biografi Imam Asy-Syafi'i",
            summary = "Pengertian fiqih secara bahasa dan istilah syariat, tujuan penyusunan ringkasan mazhab Syafi'i, serta tahun lahir dan wafat Imam Asy-Syafi'i.",
            sourcePage = 2,
            estimatedMinutes = 5,
            sequence = 2,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_tafaquh"),
            blocks = listOf(
                LessonBlock("b_m2_1", BlockType.HEADING, "Basmalah, Definisi Fiqih & Mazhab Imam Asy-Syafi'i", 1, 2),
                LessonBlock(
                    "b_m2_2",
                    BlockType.ARABIC,
                    "سَأَلَنِي بَعْضُ الْأَصْدِقَاءِ حَفِظَهُمُ اللَّهُ تَعَالَى أَنْ أَعْمَلَ مُخْتَصَرًا فِي الْفِقْهِ عَلَى مَذْهَبِ الْإِمَامِ الشَّافِعِيِّ رَحْمَةُ اللَّهِ عَلَيْهِ وَرِضْوَانُهُ، فِي غَايَةِ الِاخْتِصَارِ وَنِهَايَةِ الْإِيجَازِ.",
                    2,
                    2
                ),
                LessonBlock(
                    "b_m2_3",
                    BlockType.TRANSLATION,
                    "Sebagian sahabat-sahabatku memintaku — semoga Allah Ta'ala menjaga mereka — supaya aku membuat suatu ringkasan (sedikit lafadznya dan banyak maknanya) dalam ilmu fiqih menurut mazhab Al-Imam Asy-Syafi'i — semoga rahmat dan keridhoan Allah tercurah kepadanya — pada puncak ringkasan dan akhir rangkuman, guna mendekatkan pemahaman pelajar dan mempermudah para pemula untuk menghafalnya.",
                    3,
                    2
                ),
                LessonBlock(
                    "b_m2_4",
                    BlockType.EXPLANATION,
                    "Fiqih secara bahasa bermakna pemahaman. Secara istilah syara', fiqih adalah pengetahuan mengenai hukum-hukum syari'ah amaliyah yang diusahakan dari dalil-dalilnya yang terperinci. Adapun Al-Imam Abu Abdillah Muhammad bin Idris Asy-Syafi'i dilahirkan di Gaza pada tahun 150 H dan wafat pada hari Jumat akhir bulan Rajab tahun 204 H.",
                    4,
                    2
                ),
                LessonBlock(
                    "b_m2_5",
                    BlockType.QUOTE,
                    "\"Dan aku berharap hanya kepada Allah Yang Maha Suci lagi Maha Tinggi untuk mendapatkan taufiq pada kebenaran; sesungguhnya Dia atas segala sesuatu yang dikehendaki-Nya Maha Mampu dan terhadap hamba-hamba-Nya Maha Lembut lagi Maha Mengetahui.\"",
                    5,
                    3
                )
            )
        ),

        // --- KITAB THAHARAH ---
        Lesson(
            id = "ls_tha_1",
            bookId = "fathul_qorib",
            chapterId = "ch_thaharah_air",
            kitabTitle = "Kitab Thaharah",
            title = "Pengertian Thaharah & Tujuh Macam Sumber Air",
            summary = "Makna kitab dan bab, definisi thaharah menurut bahasa dan istilah syara', serta tujuh macam air yang sah untuk bersuci.",
            sourcePage = 4,
            estimatedMinutes = 6,
            sequence = 3,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_qayyid_lazim"),
            blocks = listOf(
                LessonBlock("b_t1_1", BlockType.HEADING, "Kitab Hukum-Hukum Thaharah & 7 Sumber Air", 1, 4),
                LessonBlock(
                    "b_t1_2",
                    BlockType.ARABIC,
                    "كِتَابُ أَحْكَامِ الطَّهَارَةِ: الْمِيَاهُ الَّتِي يَجُوزُ التَّطْهِيرُ بِهَا سَبْعُ مِيَاهٍ: مَاءُ السَّمَاءِ، وَمَاءُ الْبَحْرِ، وَمَاءُ النَّهْرِ، وَمَاءُ الْبِئْرِ، وَمَاءُ الْعَيْنِ، وَمَاءُ الثَّلْجِ، وَمَاءُ الْبَرَدِ.",
                    2,
                    4
                ),
                LessonBlock(
                    "b_t1_3",
                    BlockType.TRANSLATION,
                    "Kitab menjelaskan hukum-hukum thaharah. Air yang boleh (sah) digunakan untuk bersuci ada tujuh macam air: air langit (air hujan), air laut (asin), air sungai (tawar), air sumur, air sumber (mata air), air salju, dan air embun/es.",
                    3,
                    4
                ),
                LessonBlock(
                    "b_t1_4",
                    BlockType.EXPLANATION,
                    "Kata 'Kitab' secara bahasa berarti mengumpulkan, sedangkan secara istilah adalah nama bagi suatu jenis hukum tertentu; adapun 'Bab' adalah bagian khusus di bawah cakupan jenis hukum tersebut. Thaharah secara bahasa berarti bersih; secara syara' mencakup perbuatan yang dengannya sholat menjadi sah (wudlu, mandi, tayammum, dan menghilangkan najis). Ketujuh macam air di atas terangkum dalam ungkapan 'air yang turun dari langit atau keluar dari bumi' dalam sifat aslinya.",
                    4,
                    4
                ),
                LessonBlock(
                    "b_t1_5",
                    BlockType.FOOTNOTE,
                    "Referensi halaman 4-5: Pembahasan awal Kitab Thaharah menjadi fondasi seluruh ibadah karena sahnya sholat bergantung pada kesucian dari hadats dan najis.",
                    5,
                    5
                )
            )
        ),
        Lesson(
            id = "ls_tha_2",
            bookId = "fathul_qorib",
            chapterId = "ch_thaharah_air",
            kitabTitle = "Kitab Thaharah",
            title = "Pembagian Empat Jenis Air & Ukuran Dua Qullah",
            summary = "Klasifikasi air mutlak, air musyammas, air musta'mal & mutaghayyir, serta hukum air mutanajjis berdasarkan batas dua qullah.",
            sourcePage = 5,
            estimatedMinutes = 8,
            sequence = 4,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_qayyid_lazim", "gl_musyammas", "gl_mustamal", "gl_dua_qullah", "gl_qaul_jadid_qadim"),
            blocks = listOf(
                LessonBlock("b_t2_1", BlockType.HEADING, "Empat Kategori Hukum Air dalam Mazhab Syafi'i", 1, 5),
                LessonBlock(
                    "b_t2_2",
                    BlockType.ARABIC,
                    "ثُمَّ الْمِيَاهُ عَلَى أَرْبَعَةِ أَقْسَامٍ: طَاهِرٌ مُطَهِّرٌ غَيْرُ مَكْرُوهٍ وَهُوَ الْمَاءُ الْمُطْلَقُ، وَطَاهِرٌ مُطَهِّرٌ مَكْرُوهٌ وَهُوَ الْمَاءُ الْمُشَمَّسُ، وَطَاهِرٌ غَيْرُ مُطَهِّرٍ وَهُوَ الْمَاءُ الْمُسْتَعْمَلُ وَالْمُتَغَيِّرُ بِمَا خَالَطَهُ مِنَ الطَّاهِرَاتِ، وَمَاءٌ نَجِسٌ وَهُوَ الَّذِي حَلَّتْ فِيهِ نَجَاسَةٌ وَهُوَ دُونَ الْقُلَّتَيْنِ أَوْ كَانَ قُلَّتَيْنِ فَتَغَيَّرَ.",
                    2,
                    5
                ),
                LessonBlock(
                    "b_t2_3",
                    BlockType.TRANSLATION,
                    "Kemudian air terbagi menjadi empat bagian: (1) Suci zatnya, mensucikan lainnya, dan tidak makruh dipakai, yaitu air mutlak (bebas dari qayyid lazim); (2) Suci, mensucikan, namun makruh dipakai di badan, yaitu air musyammas; (3) Suci zatnya namun tidak mensucikan lainnya, yaitu air musta'mal dan air mutaghayyir yang berubah sifatnya karena tercampur benda suci; dan (4) Air mutanajjis, yaitu air kurang dari dua qullah yang kejatuhan najis, atau mencapai dua qullah namun berubah sifatnya.",
                    3,
                    6
                ),
                LessonBlock(
                    "b_t2_4",
                    BlockType.EXPLANATION,
                    "Air musyammas hanya makruh jika di daerah panas dalam wadah logam yang bisa berkarat selain emas dan perak, dan kemakruhannya hilang saat dingin (Imam Nawawi bahkan memilih tidak makruh secara mutlak). Air musta'mal adalah air yang telah dipakai menghilangkan hadats wajib atau najis tanpa berubah sifat dan tanpa bertambah ukurannya. Adapun air mutaghayyir kehilangan sifat mensucikannya jika perubahan karena benda suci yang larut (mukhalith) sampai menghilangkan kemutlakan nama air.",
                    4,
                    6
                ),
                LessonBlock(
                    "b_t2_5",
                    BlockType.EXPLANATION,
                    "Batas air banyak (dua qullah) menurut Qaul Jadid adalah kurang lebih 500 rithl Baghdad. Air dua qullah tidak menjadi najis karena najis yang jatuh kecuali berubah salah satu sifatnya (bau, rasa, atau warna). Sedangkan air kurang dari dua qullah menjadi najis seketika kejatuhan najis yang tidak dima'fu, baik berubah sifatnya maupun tidak.",
                    5,
                    7
                ),
                LessonBlock(
                    "b_t2_6",
                    BlockType.FOOTNOTE,
                    "Catatan istilah (hlm. 5-7): Mushannif mengecualikan air berubah akibat benda suci yang berdampingan tanpa larut (mujawir), seperti kayu cendana atau kapur barus padat, yang tetap suci mensucikan.",
                    6,
                    7
                )
            )
        ),
        Lesson(
            id = "ls_tha_3",
            bookId = "fathul_qorib",
            chapterId = "ch_wudlu_tayammum",
            kitabTitle = "Kitab Thaharah",
            title = "Bab Wudlu': Enam Fardlu Wudlu' & Batasannya",
            summary = "Perbedaan al-wudlu dan al-wadlu, serta rincian enam fardlu wudlu': niat, membasuh wajah, kedua tangan, mengusap kepala, membasuh kaki, dan tertib.",
            sourcePage = 11,
            estimatedMinutes = 7,
            sequence = 5,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_istinja"),
            blocks = listOf(
                LessonBlock("b_t3_1", BlockType.HEADING, "Fardlu-Fardlu Wudlu' Menurut Fathul Qorib", 1, 11),
                LessonBlock(
                    "b_t3_2",
                    BlockType.ARABIC,
                    "فَصْلٌ: وَفُرُوضُ الْوُضُوءِ سِتَّةُ أَشْيَاءَ: النِّيَّةُ عِنْدَ غَسْلِ الْوَجْهِ، وَغَسْلُ الْوَجْهِ، وَغَسْلُ الْيَدَيْنِ إِلَى الْمِرْفَقَيْنِ، وَمَسْحُ بَعْضِ الرَّأْسِ، وَغَسْلُ الرِّجْلَيْنِ إِلَى الْكَعْبَيْنِ، وَالتَّرْتِيبُ عَلَى مَا ذَكَرْنَاهُ.",
                    2,
                    11
                ),
                LessonBlock(
                    "b_t3_3",
                    BlockType.TRANSLATION,
                    "Fardlu-fardlunya wudlu' ada enam perkara: (1) Niat ketika membasuh wajah; (2) Membasuh seluruh wajah; (3) Membasuh kedua tangan hingga kedua siku; (4) Mengusap sebagian kepala; (5) Membasuh kedua kaki hingga kedua mata kaki; dan (6) Tertib sesuai urutan yang telah kami sebutkan.",
                    3,
                    12
                ),
                LessonBlock(
                    "b_t3_4",
                    BlockType.EXPLANATION,
                    "Lafadz 'al-wudlu' dengan dlammah pada huruf wau berarti pekerjaan bersuci, sedangkan 'al-wadlu' dengan fathah berarti air yang dipakai berwudlu'. Hakikat niat secara syara' adalah menyengaja sesuatu bersamaan dengan awal pelaksanaannya (saat membasuh bagian awal wajah), seperti niat menghilangkan hadats atau niat diperbolehkannya sholat. Jika sekadar niat membersihkan atau menyegarkan badan tanpa niat bersuci dari hadats, wudlunya tidak sah.",
                    4,
                    12
                ),
                LessonBlock(
                    "b_t3_5",
                    BlockType.EXPLANATION,
                    "Batasan panjang wajah adalah dari tempat tumbuhnya rambut kepala pada umumnya hingga ujung dua tulang rahang bawah (pertemuan janggut), dan lebarnya antara kedua telinga. Untuk jenggot lelaki yang lebat hingga kulit di baliknya tak terlihat, cukup membasuh bagian luarnya; namun bila tipis atau pada wanita/khuntsa, air wajib mengenai kulit di baliknya. Selain itu wajib membersihkan kotoran di bawah kuku yang dapat menghalangi masuknya air.",
                    5,
                    13
                )
            )
        ),
        Lesson(
            id = "ls_tha_4",
            bookId = "fathul_qorib",
            chapterId = "ch_wudlu_tayammum",
            kitabTitle = "Kitab Thaharah",
            title = "Bab Tayammum: Lima Syarat & Empat Fardlu",
            summary = "Pengertian tayammum, lima syarat sah tayammum termasuk masuk waktu dan debu berghubar, serta empat rukun pelaksanaannya.",
            sourcePage = 33,
            estimatedMinutes = 6,
            sequence = 6,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_tayammum"),
            blocks = listOf(
                LessonBlock("b_t4_1", BlockType.HEADING, "Syarat-Syarat dan Fardlu Tayammum", 1, 33),
                LessonBlock(
                    "b_t4_2",
                    BlockType.ARABIC,
                    "فَصْلٌ: وَشَرَائِطُ التَّيَمُّمِ خَمْسَةُ أَشْيَاءَ: وُجُودُ الْعُذْرِ بِسَفَرٍ أَوْ مَرَضٍ، وَدُخُولُ وَقْتِ الصَّلَاةِ، وَطَلَبُ الْمَاءِ، وَتَعَذُّرُ اسْتِعْمَالِهِ وَإِعْوَازُهُ بَعْدَ الطَّلَبِ، وَالتُّرَابُ الطَّاهِرُ لَهُ غُبَارٌ.",
                    2,
                    34
                ),
                LessonBlock(
                    "b_t4_3",
                    BlockType.TRANSLATION,
                    "Syarat-syarat tayammum ada lima perkara: (1) Adanya udzur karena bepergian atau sakit; (2) Telah masuk waktu sholat; (3) Mencari air setelah masuk waktu sholat; (4) Sulit menggunakan air atau tidak menemukannya setelah mencari; dan (5) Menggunakan debu suci mensucikan yang tidak basah dan memiliki ghubar (debu beterbangan).",
                    3,
                    34
                ),
                LessonBlock(
                    "b_t4_4",
                    BlockType.EXPLANATION,
                    "Tayammum secara bahasa berarti menyengaja, dan secara syara' adalah mendatangkan debu suci mensucikan ke wajah dan kedua tangan sebagai pengganti wudlu, mandi, atau membasuh anggota dengan syarat tertentu. Tidak sah bertayammum dengan gamping, remukan genteng, atau debu musta'mal. Fardlunya ada empat: niat (niat diperbolehkannya sholat), mengusap wajah, mengusap kedua tangan hingga siku, dan tertib.",
                    4,
                    35
                ),
                LessonBlock(
                    "b_t4_5",
                    BlockType.FOOTNOTE,
                    "Catatan Fathul Qorib (hlm. 35): Imam Nawawi dalam Syarh Al-Muhadzdzab mensyaratkan debu tidak tercampur pasir atau gamping, namun dalam Ar-Raudlah membolehkan pasir yang masih berdebu (ada ghubarnya).",
                    5,
                    35
                )
            )
        ),

        // --- KITAB SHOLAT ---
        Lesson(
            id = "ls_shl_1",
            bookId = "fathul_qorib",
            chapterId = "ch_sholat_rukun",
            kitabTitle = "Kitab Sholat",
            title = "Pengertian Sholat & Batas Waktu Sholat Lima Waktu",
            summary = "Definisi sholat menurut bahasa dan Imam Ar-Rafi'i, serta batas awal dan akhir waktu Dhuhur, Ashar, Maghrib, Isya', dan Subuh.",
            sourcePage = 50,
            estimatedMinutes = 7,
            sequence = 7,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_marhalah"),
            blocks = listOf(
                LessonBlock("b_s1_1", BlockType.HEADING, "Definisi Sholat & Waktu-Waktu Sholat Fardlu", 1, 50),
                LessonBlock(
                    "b_s1_2",
                    BlockType.ARABIC,
                    "كِتَابُ أَحْكَامِ الصَّلَاةِ: الصَّلَاةُ الْمَفْرُوضَةُ خَمْسٌ: الظُّهْرُ وَأَوَّلُ وَقْتِهَا زَوَالُ الشَّمْسِ، وَالْعَصْرُ وَأَوَّلُ وَقْتِهَا الزِّيَادَةُ عَلَى ظِلِّ الشَّيْءِ، وَالْمَغْرِبُ وَوَقْتُهَا وَاحِدٌ وَهُوَ غُرُوبُ الشَّمْسِ، وَالْعِشَاءُ، وَالصُّبْحُ.",
                    2,
                    50
                ),
                LessonBlock(
                    "b_s1_3",
                    BlockType.TRANSLATION,
                    "Sholat secara bahasa adalah doa. Secara syara' menurut Imam Ar-Rafi'i adalah ucapan dan perbuatan yang dimulai dengan takbir dan diakhiri dengan salam dengan syarat-syarat tertentu. Sholat yang difardlukan ada lima: Dhuhur (awal waktunya saat tergelincirnya matahari hingga bayangan benda seukuran bendanya), Ashar (dari bertambahnya bayangan melebihi bendanya hingga terbenam matahari), Maghrib (saat terbenamnya seluruh bulatan matahari), Isya', dan Subuh.",
                    3,
                    51
                ),
                LessonBlock(
                    "b_s1_4",
                    BlockType.EXPLANATION,
                    "Waktu sholat Ashar memiliki lima tingkatan: (1) Waktu fadlilah (awal waktu), (2) Waktu ikhtiyar (hingga bayangan dua kali benda), (3) Waktu jawaz tanpa makruh (hingga ishfirar/menguning), (4) Waktu jawaz disertai makruh (hingga terbenam matahari), dan (5) Waktu tahrim (menunda hingga tersisa waktu tidak cukup untuk sholat). Masing-masing sholat wajib dikerjakan sejak awal waktu dengan kewajiban yang luas (muwassa') hingga waktu tersisa hanya cukup melaksanakannya.",
                    4,
                    51
                )
            )
        ),
        Lesson(
            id = "ls_shl_2",
            bookId = "fathul_qorib",
            chapterId = "ch_sholat_rukun",
            kitabTitle = "Kitab Sholat",
            title = "Delapan Belas Rukun Sholat: Niat hingga Bacaan Al-Fatihah",
            summary = "Penjelasan empat rukun awal sholat dalam Fathul Qorib: niat di hati, berdiri bagi yang mampu, takbiratul ihram, dan bacaan Al-Fatihah secara tertib dan muwallah.",
            sourcePage = 59,
            estimatedMinutes = 8,
            sequence = 8,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_muwallah"),
            blocks = listOf(
                LessonBlock("b_s2_1", BlockType.HEADING, "Rukun-Rukun Sholat (18 Rukun)", 1, 59),
                LessonBlock(
                    "b_s2_2",
                    BlockType.ARABIC,
                    "فَصْلٌ: وَأَرْكَانُ الصَّلَاةِ ثَمَانِيَةَ عَشَرَ رُكْنًا: النِّيَّةُ، وَالْقِيَامُ مَعَ الْقُدْرَةِ، وَتَكْبِيرَةُ الْإِحْرَامِ، وَقِرَاءَةُ الْفَاتِحَةِ وَبِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ آيَةٌ مِنْهَا.",
                    2,
                    59
                ),
                LessonBlock(
                    "b_s2_3",
                    BlockType.TRANSLATION,
                    "Rukun-rukun sholat ada delapan belas rukun. Pertama adalah niat (di dalam hati); sholat fardlu wajib menyertakan niat fardlu, menyengaja pelaksanaannya, dan menentukan jenisnya seperti Subuh atau Dhuhur. Kedua adalah berdiri bagi yang mampu (bila tidak mampu, duduk iftirasy lebih utama). Ketiga takbiratul ihram dengan lafadz 'Allahu Akbar'. Keempat membaca Al-Fatihah, dan Bismillahirrahmanirrahim adalah satu ayat penuh darinya.",
                    3,
                    60
                ),
                LessonBlock(
                    "b_s2_4",
                    BlockType.EXPLANATION,
                    "Dalam takbiratul ihram, tidak sah mengganti dengan lafadz seperti 'Ar-Rahman Akbar' atau membalik susunan menjadi 'Akbarullahu'. Niat wajib dihadirkan bersamaan dengan takbiratul ihram (Imam Nawawi memilih cukup berbarengan secara 'urf). Saat membaca Al-Fatihah, wajib menjaga seluruh huruf dan tasydid, menjaga tertib urutan ayat, serta muwallah (bersambung terus-menerus tanpa diselingi dzikir lain kecuali ucapan 'amin' karena bacaan imam).",
                    4,
                    60
                ),
                LessonBlock(
                    "b_s2_5",
                    BlockType.FOOTNOTE,
                    "Ringkasan 18 Rukun (hlm. 59-64): Niat, berdiri, takbiratul ihram, Al-Fatihah, ruku', thuma'ninah ruku', i'tidal, thuma'ninah i'tidal, sujud, thuma'ninah sujud, duduk di antara dua sujud, thuma'ninah duduk, duduk akhir, tasyahhud akhir, sholawat Nabi, salam pertama, niat keluar sholat (menurut sebagian redaksi), dan tertib.",
                    5,
                    61
                )
            )
        ),
        Lesson(
            id = "ls_shl_3",
            bookId = "fathul_qorib",
            chapterId = "ch_sholat_rukun",
            kitabTitle = "Kitab Sholat",
            title = "Bab Sholat Jama'ah serta Qashar & Jama'",
            summary = "Hukum sholat berjama'ah bagi laki-laki merdeka dan mukim, serta syarat-syarat perjalanan jauh untuk mengqashar dan menjama' sholat.",
            sourcePage = 77,
            estimatedMinutes = 6,
            sequence = 9,
            verificationStatus = EditorialStatus.VERIFIED,
            glossaryIds = listOf("gl_marhalah"),
            blocks = listOf(
                LessonBlock("b_s3_1", BlockType.HEADING, "Sholat Jama'ah, Qashar dan Jama'", 1, 77),
                LessonBlock(
                    "b_s3_2",
                    BlockType.ARABIC,
                    "فَصْلٌ: وَصَلَاةُ الْجَمَاعَةِ سُنَّةٌ مُؤَكَّدَةٌ، وَيَجُوزُ لِلْمُسَافِرِ قَصْرُ الصَّلَاةِ الرُّبَاعِيَّةِ بِخَمْسِ شَرَائِطَ.",
                    2,
                    77
                ),
                LessonBlock(
                    "b_s3_3",
                    BlockType.TRANSLATION,
                    "Sholat berjama'ah hukumnya sunnah muakkad (atau fardlu kifayah menurut pendapat ashah bagi laki-laki mukim pada sholat lima waktu). Bagi musafir diperbolehkan mengqashar sholat yang empat rakaat (Dhuhur, Ashar, Isya') menjadi dua rakaat dengan lima syarat, di antaranya perjalanan bukan untuk maksiat dan jaraknya mencapai dua marhalah (± 16 farsakh).",
                    3,
                    80
                ),
                LessonBlock(
                    "b_s3_4",
                    BlockType.EXPLANATION,
                    "Dalam safar yang memenuhi syarat qashar, musafir juga boleh menjama' (mengumpulkan) Dhuhur dengan Ashar serta Maghrib dengan Isya', baik secara Jama' Taqdim maupun Jama' Ta'khir. Saat sholat jama'ah, makmum tidak boleh mendahului imam dalam takbiratul ihram dan wajib mengikuti gerakan imam serta mengetahui perpindahan gerakannya.",
                    4,
                    80
                )
            )
        )
    )
}
