package com.example.data

import com.example.model.AnswerOption
import com.example.model.QuestionType
import com.example.model.Quiz
import com.example.model.QuizQuestion

object FathulQoribContent {
    val allLessons by lazy { FathulQoribLessonsPart1.lessons + FathulQoribLessonsPart2.lessons }

    fun lessonsForChapter(chapterId: String) =
        allLessons.filter { it.chapterId == chapterId }.sortedBy { it.sequence }

    fun lessonById(lessonId: String) = allLessons.firstOrNull { it.id == lessonId }

    fun chapterById(chapterId: String) = FathulQoribCatalog.chapters.firstOrNull { it.id == chapterId }
}

object FathulQoribQuizzes {

    val quizzes: List<Quiz> = listOf(
        Quiz(
            id = "quiz_thaharah",
            chapterId = "ch_thaharah_air",
            title = "Kuis Kitab Thaharah & Air",
            subtitle = "Menguji pemahaman 7 sumber air, 4 jenis air, wudlu', dan tayammum",
            passingScore = 70,
            estimatedMinutes = 5,
            questions = listOf(
                QuizQuestion(
                    id = "q_th_1",
                    quizId = "quiz_thaharah",
                    question = "Berapakah macam sumber air yang boleh digunakan untuk bersuci menurut Matan Taqrib & Fathul Qorib?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Empat macam air", false),
                        AnswerOption("b", "Lima macam air", false),
                        AnswerOption("c", "Tujuh macam air", true),
                        AnswerOption("d", "Sembilan macam air", false)
                    ),
                    explanation = "Dalam Fathul Qorib hlm. 4 dijelaskan bahwa air yang sah untuk bersuci ada tujuh: air hujan, air laut, air sungai, air sumur, air mata air, air salju, dan air embun/es.",
                    sourcePage = 4
                ),
                QuizQuestion(
                    id = "q_th_2",
                    quizId = "quiz_thaharah",
                    question = "Bagaimana hukum menggunakan air musyammas menurut syara' yang dijelaskan pada halaman 5?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Haram dipakai untuk pakaian maupun badan", false),
                        AnswerOption("b", "Suci mensucikan namun makruh digunakan pada badan (di daerah panas dalam wadah logam berkarat)", true),
                        AnswerOption("c", "Suci tetapi tidak mensucikan seperti air musta'mal", false),
                        AnswerOption("d", "Mutanajjis apabila belum mencapai dua qullah", false)
                    ),
                    explanation = "Fathul Qorib hlm. 5 menerangkan air musyammas termasuk kategori suci mensucikan yang makruh dipakai pada badan di daerah panas dengan wadah logam selain emas/perak, dan tidak makruh pada pakaian.",
                    sourcePage = 5
                ),
                QuizQuestion(
                    id = "q_th_3",
                    quizId = "quiz_thaharah",
                    question = "Air yang kurang dari dua qullah tetap suci dan tidak menjadi najis meskipun kejatuhan najis selama tidak berubah bau, warna, atau rasanya.",
                    questionType = QuestionType.TRUE_FALSE,
                    options = listOf(
                        AnswerOption("t", "Benar", false),
                        AnswerOption("f", "Salah", true)
                    ),
                    explanation = "Salah. Pada Fathul Qorib hlm. 7 dijelaskan bahwa air kurang dari dua qullah menjadi najis seketika kejatuhan najis yang tidak dima'fu, baik berubah sifatnya maupun tidak.",
                    sourcePage = 7
                ),
                QuizQuestion(
                    id = "q_th_4",
                    quizId = "quiz_thaharah",
                    question = "Kapan waktu pelaksanaan niat wudlu' yang benar menurut Fathul Qorib?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Sebelum mencuci kedua telapak tangan", false),
                        AnswerOption("b", "Bersamaan dengan membasuh bagian awal dari wajah", true),
                        AnswerOption("c", "Setelah selesai mengusap kepala", false),
                        AnswerOption("d", "Ketika hendak membasuh kedua kaki", false)
                    ),
                    explanation = "Dalam Fathul Qorib hlm. 12 ditegaskan hakikat niat secara syara' adalah menyengaja sesuatu bersamaan dengan awal pelaksanaannya, yakni saat membasuh awal bagian wajah.",
                    sourcePage = 12
                ),
                QuizQuestion(
                    id = "q_th_5",
                    quizId = "quiz_thaharah",
                    question = "Manakah bahan yang SAH digunakan untuk bertayammum menurut pendapat yang mu'tamad dalam Fathul Qorib?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Debu suci mensucikan yang kering dan memiliki ghubar (debu)", true),
                        AnswerOption("b", "Debu musta'mal yang sudah dipakai orang lain", false),
                        AnswerOption("c", "Serbuk gamping dan remukan genteng", false),
                        AnswerOption("d", "Pasir basah tanpa unsur debu", false)
                    ),
                    explanation = "Fathul Qorib hlm. 34-35 mensyaratkan debu suci mensucikan yang tidak basah dan memiliki ghubar, serta mengecualikan gamping, remukan genteng, dan debu musta'mal.",
                    sourcePage = 35
                )
            )
        ),
        Quiz(
            id = "quiz_sholat",
            chapterId = "ch_sholat_rukun",
            title = "Kuis Kitab Sholat & Rukunnya",
            subtitle = "Waktu sholat fardlu, 18 rukun sholat, dan ketentuan qashar-jama'",
            passingScore = 70,
            estimatedMinutes = 5,
            questions = listOf(
                QuizQuestion(
                    id = "q_sh_1",
                    quizId = "quiz_sholat",
                    question = "Kapankah awal masuknya waktu sholat Dhuhur menurut Fathul Qorib?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Saat matahari tepat berada di titik kulminasi atas", false),
                        AnswerOption("b", "Saat tergelincirnya (zawal) matahari dari tengah langit ke arah barat", true),
                        AnswerOption("c", "Saat bayangan benda dua kali lipat panjang bendanya", false),
                        AnswerOption("d", "Saat matahari terbenam seluruh bulatannya", false)
                    ),
                    explanation = "Fathul Qorib hlm. 50 menjelaskan awal waktu sholat Dhuhur adalah tergelincirnya matahari dari tengah langit yang diketahui dengan bergesernya bayangan ke arah timur.",
                    sourcePage = 50
                ),
                QuizQuestion(
                    id = "q_sh_2",
                    quizId = "quiz_sholat",
                    question = "Berapa jumlah keseluruhan rukun sholat yang disebutkan dalam Matan Taqrib & Fathul Qorib?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "13 rukun", false),
                        AnswerOption("b", "15 rukun", false),
                        AnswerOption("c", "18 rukun", true),
                        AnswerOption("d", "21 rukun", false)
                    ),
                    explanation = "Pada awal Bab Rukun-Rukun Sholat hlm. 59 disebutkan secara eksplisit: 'Rukun-rukun sholat ada delapan belas rukun' (mencakup thuma'ninah di tiap posisi).",
                    sourcePage = 59
                ),
                QuizQuestion(
                    id = "q_sh_3",
                    quizId = "quiz_sholat",
                    question = "Bacaan Bismillahirrahmanirrahim termasuk satu ayat penuh dari Surat Al-Fatihah sehingga wajib dibaca saat sholat.",
                    questionType = QuestionType.TRUE_FALSE,
                    options = listOf(
                        AnswerOption("t", "Benar", true),
                        AnswerOption("f", "Salah", false)
                    ),
                    explanation = "Benar. Fathul Qorib hlm. 60 menegaskan Bismillahirrahmanirrahim adalah satu ayat penuh dari Surat Al-Fatihah dan tidak boleh tertinggal satu huruf atau tasydid pun.",
                    sourcePage = 60
                ),
                QuizQuestion(
                    id = "q_sh_4",
                    quizId = "quiz_sholat",
                    question = "Manakah ucapan takbiratul ihram yang sah dalam sholat menurut Fathul Qorib?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Ar-Rahmanu Akbar", false),
                        AnswerOption("b", "Akbarullahu", false),
                        AnswerOption("c", "Allahu Akbar", true),
                        AnswerOption("d", "Alhamdulillah", false)
                    ),
                    explanation = "Fathul Qorib hlm. 59-60 menjelaskan takbiratul ihram bagi yang mampu wajib mengucapkan 'Allahu Akbar', dan tidak sah mendahulukan khabar ('Akbarullahu') atau mengganti lafadznya.",
                    sourcePage = 60
                ),
                QuizQuestion(
                    id = "q_sh_5",
                    quizId = "quiz_sholat",
                    question = "Berapa jarak minimal perjalanan safar agar musafir diperbolehkan mengqashar sholat empat rakaat?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Satu marhalah", false),
                        AnswerOption("b", "Dua marhalah", true),
                        AnswerOption("c", "Lima farsakh", false),
                        AnswerOption("d", "Tiga hari tanpa batas jarak", false)
                    ),
                    explanation = "Fathul Qorib hlm. 80 menetapkan jarak safar yang membolehkan qashar adalah dua marhalah (± 16 farsakh) dan perjalanan bukan untuk maksiat.",
                    sourcePage = 80
                )
            )
        ),
        Quiz(
            id = "quiz_zakat",
            chapterId = "ch_zakat",
            title = "Kuis Kitab Zakat & Asnaf",
            subtitle = "Jenis harta wajib zakat, zakat fitrah, dan delapan golongan penerima",
            passingScore = 70,
            estimatedMinutes = 5,
            questions = listOf(
                QuizQuestion(
                    id = "q_zk_1",
                    quizId = "quiz_zakat",
                    question = "Manakah kelompok yang termasuk lima harta wajib zakat dalam Fathul Qorib?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Ternak (onta, sapi, kambing), emas-perak, hasil pertanian, buah-buahan, dan barang dagangan", true),
                        AnswerOption("b", "Kuda, rumah tinggal, emas, beras, dan perabot", false),
                        AnswerOption("c", "Semua jenis sayur-mayur, rempah, dan kendaraan pribadi", false),
                        AnswerOption("d", "Emas dan perak saja", false)
                    ),
                    explanation = "Fathul Qorib hlm. 106-110 merinci lima harta wajib zakat: mawasyi (onta, sapi, kambing), atsman (emas-perak), zuru' (makanan pokok), tsimaar (kurma & anggur kering), dan barang dagangan.",
                    sourcePage = 106
                ),
                QuizQuestion(
                    id = "q_zk_2",
                    quizId = "quiz_zakat",
                    question = "Berapakah batas nishab zakat hasil pertanian (zuru') tanpa kulit menurut Fathul Qorib?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Satu sha'", false),
                        AnswerOption("b", "Lima wasaq", true),
                        AnswerOption("c", "Dua puluh mitsqal", false),
                        AnswerOption("d", "Dua ratus dirham", false)
                    ),
                    explanation = "Fathul Qorib hlm. 108 menjelaskan syarat ketiga zakat hasil pertanian adalah mencapai satu nishab, yaitu lima wasaq tanpa kulit.",
                    sourcePage = 108
                ),
                QuizQuestion(
                    id = "q_zk_3",
                    quizId = "quiz_zakat",
                    question = "Bayi yang baru dilahirkan setelah matahari terbenam pada malam hari raya Idul Fitri tetap wajib dibayarkan zakat fitrahnya.",
                    questionType = QuestionType.TRUE_FALSE,
                    options = listOf(
                        AnswerOption("t", "Benar", false),
                        AnswerOption("f", "Salah", true)
                    ),
                    explanation = "Salah. Fathul Qorib hlm. 119 menjelaskan sebab wajib zakat fitrah adalah terbenamnya matahari di hari terakhir Ramadhan, sehingga tidak wajib atas anak yang lahir setelah maghrib tersebut.",
                    sourcePage = 119
                ),
                QuizQuestion(
                    id = "q_zk_4",
                    quizId = "quiz_zakat",
                    question = "Berapakah ukuran baku zakat fitrah menurut Fathul Qorib?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Satu mud makanan pokok", false),
                        AnswerOption("b", "Satu sha' (5 1/3 rithl Irak) makanan pokok daerah setempat", true),
                        AnswerOption("c", "Lima wasaq makanan pokok", false),
                        AnswerOption("d", "2,5% dari total penghasilan bulanan", false)
                    ),
                    explanation = "Fathul Qorib hlm. 120 menyatakan wajib mengeluarkan satu sha' makanan pokok daerahnya, yakni 5 rithl lebih 1/3 rithl negara Irak.",
                    sourcePage = 120
                ),
                QuizQuestion(
                    id = "q_zk_5",
                    quizId = "quiz_zakat",
                    question = "Apa perbedaan antara Fakir dan Miskin dalam Bab Golongan Penerima Zakat?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Fakir tidak punya harta/pekerjaan mencukupi sama sekali, sedangkan miskin punya harta/pekerjaan namun belum mencukupi kebutuhannya", true),
                        AnswerOption("b", "Miskin lebih parah keadaannya daripada fakir", false),
                        AnswerOption("c", "Fakir adalah musafir, sedangkan miskin adalah orang berhutang", false),
                        AnswerOption("d", "Keduanya bermakna sama persis tanpa perbedaan kondisi", false)
                    ),
                    explanation = "Fathul Qorib hlm. 121 membedakan fakir sebagai orang yang tidak punya harta dan pekerjaan mencukupi, sedangkan miskin memiliki harta/pekerjaan namun masih kurang (misal butuh 10 dirham hanya punya 7 dirham).",
                    sourcePage = 121
                )
            )
        ),
        Quiz(
            id = "quiz_puasa_haji",
            chapterId = "ch_puasa_haji",
            title = "Kuis Puasa Ramadhan & Haji",
            subtitle = "Fardlu dan pembatal puasa serta syarat & rukun ibadah haji",
            passingScore = 70,
            estimatedMinutes = 5,
            questions = listOf(
                QuizQuestion(
                    id = "q_ph_1",
                    quizId = "quiz_puasa_haji",
                    question = "Apakah hukum seseorang yang makan atau minum karena lupa saat berpuasa Ramadhan menurut Fathul Qorib?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Puasanya batal dan wajib qadla'", false),
                        AnswerOption("b", "Puasanya tidak batal", true),
                        AnswerOption("c", "Puasanya batal dan terkena kafarat", false),
                        AnswerOption("d", "Hanya sah jika dilakukan sebelum dzuhur", false)
                    ),
                    explanation = "Fathul Qorib hlm. 125 menjelaskan makan, minum, atau jima' dalam keadaan lupa tidak membatalkan puasa.",
                    sourcePage = 125
                ),
                QuizQuestion(
                    id = "q_ph_2",
                    quizId = "quiz_puasa_haji",
                    question = "Menyuntikkan obat melalui salah satu dari dua jalan (qubul atau dubur) yang disebut al-huqnah membatalkan puasa.",
                    questionType = QuestionType.TRUE_FALSE,
                    options = listOf(
                        AnswerOption("t", "Benar", true),
                        AnswerOption("f", "Salah", false)
                    ),
                    explanation = "Benar. Fathul Qorib hlm. 125 mencantumkan al-huqnah melalui qubul atau dubur sebagai perkara ketiga yang membatalkan puasa.",
                    sourcePage = 125
                ),
                QuizQuestion(
                    id = "q_ph_3",
                    quizId = "quiz_puasa_haji",
                    question = "Muntah yang terjadi secara terpaksa (tidak disengaja) tidak membatalkan puasa.",
                    questionType = QuestionType.TRUE_FALSE,
                    options = listOf(
                        AnswerOption("t", "Benar", true),
                        AnswerOption("f", "Salah", false)
                    ),
                    explanation = "Benar. Fathul Qorib hlm. 125 menegaskan yang membatalkan puasa adalah muntah dengan sengaja; bila terpaksa muntah maka puasanya tidak batal.",
                    sourcePage = 125
                ),
                QuizQuestion(
                    id = "q_ph_4",
                    quizId = "quiz_puasa_haji",
                    question = "Kapankah waktu pelaksanaan rukun wukuf di Arafah dimulai?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Sejak terbit matahari tanggal 8 Dzulhijjah", false),
                        AnswerOption("b", "Setelah tergelincirnya matahari pada hari Arafah (9 Dzulhijjah) hingga terbit fajar 10 Dzulhijjah", true),
                        AnswerOption("c", "Setelah sholat Idul Adha 10 Dzulhijjah", false),
                        AnswerOption("d", "Sepanjang hari tasyriq 11-13 Dzulhijjah", false)
                    ),
                    explanation = "Fathul Qorib hlm. 134 menerangkan waktu wukuf dimulai setelah tergelincirnya matahari tanggal 9 Dzulhijjah dan berlanjut hingga terbit fajar hari raya kurban 10 Dzulhijjah.",
                    sourcePage = 134
                ),
                QuizQuestion(
                    id = "q_ph_5",
                    quizId = "quiz_puasa_haji",
                    question = "Manakah syarat harta dan kendaraan dalam kewajiban haji?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Cukup memiliki ongkos perjalanan meskipun masih punya hutang jatuh tempo", false),
                        AnswerOption("b", "Bekal dan kendaraan harus melebihi dari hutang dan biaya nafkah keluarga yang ditinggalkan", true),
                        AnswerOption("c", "Wajib memiliki kendaraan pribadi meskipun jarak ke Makkah kurang dari dua marhalah", false),
                        AnswerOption("d", "Tidak disyaratkan keamanan perjalanan", false)
                    ),
                    explanation = "Fathul Qorib hlm. 133 mensyaratkan bekal dan kendaraan harus berlebih dari hutang, nafkah orang yang wajib ditanggung selama perjalanan, serta rumah yang layak.",
                    sourcePage = 133
                )
            )
        ),
        Quiz(
            id = "quiz_muamalah",
            chapterId = "ch_muamalah",
            title = "Kuis Muamalah: Jual Beli, Riba & Khiyar",
            subtitle = "Menguji pemahaman ketentuan akad sah, barang ribawi, dan hak khiyar",
            passingScore = 70,
            estimatedMinutes = 5,
            questions = listOf(
                QuizQuestion(
                    id = "q_mu_1",
                    quizId = "quiz_muamalah",
                    question = "Pada komoditas apa sajakah akad riba dapat terjadi menurut Fathul Qorib?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Hanya pada emas, perak, dan bahan makanan (penguat badan, camilan, obat)", true),
                        AnswerOption("b", "Pada pakaian dan hewan ternak saja", false),
                        AnswerOption("c", "Pada properti tanah dan bangunan", false),
                        AnswerOption("d", "Hanya pada mata uang kertas modern", false)
                    ),
                    explanation = "Fathul Qorib hlm. 154 menegaskan akad riba hanya terjadi pada emas, perak, dan makanan (penguat badan, camilan, atau obat-obatan).",
                    sourcePage = 154
                ),
                QuizQuestion(
                    id = "q_mu_2",
                    quizId = "quiz_muamalah",
                    question = "Menjual emas dengan perak dalam ukuran berbeda hukumnya diperbolehkan dengan syarat serah terima tunai (naqdan) sebelum berpisah.",
                    questionType = QuestionType.TRUE_FALSE,
                    options = listOf(
                        AnswerOption("t", "Benar", true),
                        AnswerOption("f", "Salah", false)
                    ),
                    explanation = "Benar. Fathul Qorib hlm. 155 membolehkan menjual emas dengan perak (berbeda jenis) dengan takaran berbeda asal kontan dan diserahterimakan sebelum berpisah.",
                    sourcePage = 155
                ),
                QuizQuestion(
                    id = "q_mu_3",
                    quizId = "quiz_muamalah",
                    question = "Bolehkah seseorang menjual kembali barang yang baru dibelinya sebelum ia menerimanya (qabdl) dari penjual pertama?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Sah apabila pembelinya sepakat", false),
                        AnswerOption("b", "Tidak sah menjual barang yang belum diterima, baik kepada penjual awal maupun orang lain", true),
                        AnswerOption("c", "Sah khusus untuk barang dagangan makanan", false),
                        AnswerOption("d", "Makruh namun akadnya tetap berlaku", false)
                    ),
                    explanation = "Fathul Qorib hlm. 155 menyatakan tidak sah menjual barang yang telah dibeli kecuali setelah menerimanya.",
                    sourcePage = 155
                ),
                QuizQuestion(
                    id = "q_mu_4",
                    quizId = "quiz_muamalah",
                    question = "Berapakah batas maksimal waktu khiyar syarat yang disepakati dalam akad jual beli?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Satu hari", false),
                        AnswerOption("b", "Tiga hari", true),
                        AnswerOption("c", "Tujuh hari", false),
                        AnswerOption("d", "Tiga puluh hari", false)
                    ),
                    explanation = "Dalam Bab Khiyar Fathul Qorib hlm. 156 dijelaskan bahwa khiyar syarat dapat ditetapkan oleh penjual maupun pembeli selama maksimal tiga hari.",
                    sourcePage = 156
                ),
                QuizQuestion(
                    id = "q_mu_5",
                    quizId = "quiz_muamalah",
                    question = "Menjual satu jenis daging hewan yang dibeli dengan hewan hidup (seperti daging kambing dibeli dengan seekor kambing) diperbolehkan secara mutlak.",
                    questionType = QuestionType.TRUE_FALSE,
                    options = listOf(
                        AnswerOption("t", "Benar", false),
                        AnswerOption("f", "Salah", true)
                    ),
                    explanation = "Salah. Fathul Qorib hlm. 155 menyatakan tidak boleh menjual daging yang dibeli dengan binatang, baik sejenis maupun berlainan jenis dari hewan halal.",
                    sourcePage = 155
                )
            )
        ),
        Quiz(
            id = "quiz_faraidl_nikah",
            chapterId = "ch_faraidl_nikah",
            title = "Kuis Waris (Faraidl) & Nikah",
            subtitle = "Ahli waris utama, penghalang waris, serta syarat sah dan pandangan dalam nikah",
            passingScore = 70,
            estimatedMinutes = 5,
            questions = listOf(
                QuizQuestion(
                    id = "q_fn_1",
                    quizId = "quiz_faraidl_nikah",
                    question = "Siapakah lima ahli waris yang pasti mendapat warisan dan tidak pernah gugur dalam keadaan apa pun?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Suami/istri, ayah/ibu, dan anak kandung", true),
                        AnswerOption("b", "Kakek, paman, sepupu, saudara seayah, dan cucu", false),
                        AnswerOption("c", "Hanya anak laki-laki dan ayah saja", false),
                        AnswerOption("d", "Seluruh sepuluh golongan ahli waris laki-laki", false)
                    ),
                    explanation = "Fathul Qorib hlm. 218 menyebutkan lima orang yang tidak gugur hak warisnya dalam berbagai keadaan: zaujain (suami dan istri), abawain (ayah dan ibu), dan anak kandung.",
                    sourcePage = 218
                ),
                QuizQuestion(
                    id = "q_fn_2",
                    quizId = "quiz_faraidl_nikah",
                    question = "Seorang muslim tidak dapat mewarisi harta kerabatnya yang kafir, dan sebaliknya orang kafir tidak mewarisi muslim.",
                    questionType = QuestionType.TRUE_FALSE,
                    options = listOf(
                        AnswerOption("t", "Benar", true),
                        AnswerOption("f", "Salah", false)
                    ),
                    explanation = "Benar. Fathul Qorib hlm. 219 mencantumkan perbedaan dua agama sebagai salah satu dari tujuh penghalang mewaris.",
                    sourcePage = 219
                ),
                QuizQuestion(
                    id = "q_fn_3",
                    quizId = "quiz_faraidl_nikah",
                    question = "Bagian badan manakah yang diperbolehkan dilihat seorang laki-laki ketika hendak melamar wanita yang ingin dinikahinya?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Wajah dan kedua telapak tangan luar-dalam", true),
                        AnswerOption("b", "Hanya suara tanpa melihat wajah", false),
                        AnswerOption("c", "Seluruh rambut dan leher", false),
                        AnswerOption("d", "Haram melihat sama sekali sebelum akad", false)
                    ),
                    explanation = "Fathul Qorib hlm. 230 menjelaskan laki-laki yang hendak menikahi seorang wanita diperbolehkan melihat wajah dan kedua telapak tangan luar-dalam wanita tersebut.",
                    sourcePage = 230
                ),
                QuizQuestion(
                    id = "q_fn_4",
                    quizId = "quiz_faraidl_nikah",
                    question = "Seorang wanita dewasa dapat menikahkan dirinya sendiri atau wanita lain tanpa kehadiran wali.",
                    questionType = QuestionType.TRUE_FALSE,
                    options = listOf(
                        AnswerOption("t", "Benar", false),
                        AnswerOption("f", "Salah", true)
                    ),
                    explanation = "Salah. Fathul Qorib hlm. 232 menegaskan akad nikah tidak sah kecuali disertai wali laki-laki yang adil dan dua saksi yang adil; wanita tidak bisa menikahkan dirinya sendiri maupun orang lain.",
                    sourcePage = 232
                ),
                QuizQuestion(
                    id = "q_fn_5",
                    quizId = "quiz_faraidl_nikah",
                    question = "Dalam kasus seluruh ahli waris laki-laki (10 golongan) berkumpul bersama, siapakah yang tetap mendapat bagian warisan?",
                    questionType = QuestionType.MULTIPLE_CHOICE,
                    options = listOf(
                        AnswerOption("a", "Ayah, anak laki-laki, dan suami", true),
                        AnswerOption("b", "Kakek, paman, dan saudara laki-laki", false),
                        AnswerOption("c", "Anak laki-laki saja", false),
                        AnswerOption("d", "Kesepuluh golongan dibagi rata", false)
                    ),
                    explanation = "Fathul Qorib hlm. 217-218 menerangkan jika seluruh golongan ahli waris laki-laki berkumpul, yang berhak menerima warisan hanya tiga orang: ayah, anak laki-laki, dan suami.",
                    sourcePage = 217
                )
            )
        )
    )

    fun quizById(quizId: String): Quiz? = quizzes.firstOrNull { it.id == quizId }
}
