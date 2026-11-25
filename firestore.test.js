const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_student";
const BOB_UID = "bob_editor";
const USTAZ_UID = "ustaz_reviewer";
const ADMIN_UID = "admin_bootstrapped";
const ADMIN_EMAIL = "apiepoment97@gmail.com";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated users cannot read or write any NgajiFiqih collections", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("progress").get());
  await assertFails(unauthDb.collection("editorialSubmissions").where("status", "==", "VERIFIED").get());
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Anon",
      role: "STUDENT",
      dailyTargetMinutes: 15,
      streakCount: 0,
      studyMinutesToday: 0,
      createdAt: new Date(Date.now() - 1000),
      updatedAt: new Date(Date.now() - 1000),
    })
  );
});

test("User profile: student create/update succeeds; self-escalation to ADMIN and cross-user read/write fail", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID, { email: "alice@example.com", email_verified: true }).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID, { email: "bob@example.com", email_verified: true }).firestore();
  const now = new Date(Date.now() - 5000);

  // Valid STUDENT creation
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Ahmad Santri",
      email: "alice@example.com",
      role: "STUDENT",
      dailyTargetMinutes: 20,
      streakCount: 1,
      studyMinutesToday: 0,
      createdAt: now,
      updatedAt: now,
    })
  );

  // Self-escalation to ADMIN on create fails
  await assertFails(
    bobDb.collection("users").doc(BOB_UID).set({
      userId: BOB_UID,
      displayName: "Bob Escalator",
      role: "ADMIN",
      dailyTargetMinutes: 20,
      streakCount: 0,
      studyMinutesToday: 0,
      createdAt: now,
      updatedAt: now,
    })
  );

  // Student updates own dailyTargetMinutes succeeds
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).update({
      dailyTargetMinutes: 30,
      studyMinutesToday: 10,
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Student cannot elevate own role on update
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      role: "SUPER_ADMIN",
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Cross-user PII read fails
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
});

test("Student subcollections: progress, bookmarks, notes, and quizAttempts enforce owner isolation & schema", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const now = new Date(Date.now() - 5000);

  // Progress create & partial update (no `id` field since Kotlin uses @DocumentId)
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("progress").doc("lesson_thaharah_1").set({
      userId: ALICE_UID,
      lessonId: "lesson_thaharah_1",
      chapterId: "bab_thaharah_air",
      status: "IN_PROGRESS",
      lastReadMode: "DUAL",
      studySeconds: 120,
      createdAt: now,
      updatedAt: now,
    })
  );
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("progress").doc("lesson_thaharah_1").update({
      status: "COMPLETED",
      studySeconds: 300,
      completedAt: new Date(Date.now() - 1000),
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Bookmark create, list, delete
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("bookmarks").doc("lesson_thaharah_1").set({
      userId: ALICE_UID,
      lessonId: "lesson_thaharah_1",
      chapterId: "bab_thaharah_air",
      lessonTitle: "Pengertian Thaharah & Pembagian Air",
      chapterTitle: "Kitab Thaharah",
      sourcePage: 5,
      createdAt: now,
      updatedAt: now,
    })
  );
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("bookmarks").get());

  // StudyNote create & reject tampered future timestamp
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("notes").doc("note_1").set({
      userId: ALICE_UID,
      lessonId: "lesson_thaharah_1",
      lessonTitle: "Pengertian Thaharah & Pembagian Air",
      highlightText: "Air mutlak adalah air suci lagi mensucikan",
      content: "Air dua qullah tidak mudah najis kecuali berubah sifatnya.",
      sourcePage: 6,
      createdAt: now,
      updatedAt: now,
    })
  );
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("notes").doc("note_future").set({
      userId: ALICE_UID,
      lessonId: "lesson_thaharah_1",
      lessonTitle: "Pengertian Thaharah & Pembagian Air",
      content: "Future note",
      sourcePage: 6,
      createdAt: new Date(Date.now() + 60000),
      updatedAt: new Date(Date.now() + 60000),
    })
  );

  // QuizAttempt create succeeds, update fails (immutable record)
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("quizAttempts").doc("attempt_1").set({
      userId: ALICE_UID,
      quizId: "quiz_thaharah",
      quizTitle: "Kuis Kitab Thaharah",
      chapterId: "bab_thaharah_air",
      score: 90,
      correctCount: 9,
      totalQuestions: 10,
      passed: true,
      durationSeconds: 145,
      createdAt: now,
    })
  );
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("quizAttempts").doc("attempt_1").update({
      score: 100,
    })
  );

  // Cross-user subcollection read fails
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("notes").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("progress").doc("lesson_thaharah_1").get());
});

test("Editorial CMS: enforces separation of duties (editor cannot self-approve, Ustaz/Reviewer approves others' REVIEW drafts)", async () => {
  const now = new Date(Date.now() - 5000);
  await testEnv.withSecurityRulesDisabled(async (context) => {
    const db = context.firestore();
    await db.collection("users").doc(BOB_UID).set({
      userId: BOB_UID,
      displayName: "Ustaz Editor Bob",
      role: "CONTENT_EDITOR",
      dailyTargetMinutes: 20,
      streakCount: 0,
      studyMinutesToday: 0,
      createdAt: now,
      updatedAt: now,
    });
    await db.collection("users").doc(USTAZ_UID).set({
      userId: USTAZ_UID,
      displayName: "KH Reviewer",
      role: "REVIEWER_USTAZ",
      dailyTargetMinutes: 20,
      streakCount: 0,
      studyMinutesToday: 0,
      createdAt: now,
      updatedAt: now,
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const ustazDb = testEnv.authenticatedContext(USTAZ_UID).firestore();
  const adminDb = testEnv.authenticatedContext(ADMIN_UID, { email: ADMIN_EMAIL, email_verified: true }).firestore();

  // Student cannot create editorial submissions
  await assertFails(
    aliceDb.collection("editorialSubmissions").doc("sub_student").set({
      authorId: ALICE_UID,
      authorName: "Ahmad",
      chapterId: "bab_thaharah",
      chapterTitle: "Kitab Thaharah",
      lessonTitle: "Draft Student",
      arabicText: "المياه",
      translation: "Air",
      explanation: "Penjelasan",
      sourcePage: 5,
      status: "DRAFT",
      createdAt: now,
      updatedAt: now,
    })
  );

  // Content Editor creates DRAFT and submits to REVIEW
  await assertSucceeds(
    bobDb.collection("editorialSubmissions").doc("sub_1").set({
      authorId: BOB_UID,
      authorName: "Ustaz Editor Bob",
      chapterId: "bab_thaharah",
      chapterTitle: "Kitab Thaharah",
      lessonTitle: "Macam-Macam Air Suci Mensucikan",
      arabicText: "المياه التي يجوز التطهير بها سبع مياه",
      translation: "Air yang boleh digunakan bersuci ada tujuh macam.",
      explanation: "Pembagian air dalam Fathul Qorib halaman 6.",
      sourcePage: 6,
      status: "DRAFT",
      createdAt: now,
      updatedAt: now,
    })
  );
  await assertSucceeds(
    bobDb.collection("editorialSubmissions").doc("sub_1").update({
      status: "REVIEW",
      updatedAt: new Date(Date.now() - 2000),
    })
  );

  // Editor CANNOT approve their own submission
  await assertFails(
    bobDb.collection("editorialSubmissions").doc("sub_1").update({
      status: "VERIFIED",
      reviewerId: BOB_UID,
      reviewerName: "Ustaz Editor Bob",
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Even a Reviewer/Admin CANNOT self-approve their own submission
  await assertSucceeds(
    ustazDb.collection("editorialSubmissions").doc("sub_ustaz_own").set({
      authorId: USTAZ_UID,
      authorName: "KH Reviewer",
      chapterId: "bab_sholat",
      chapterTitle: "Kitab Sholat",
      lessonTitle: "Syarat Wajib Sholat",
      arabicText: "شرائط وجوب الصلاة",
      translation: "Syarat wajib sholat ada tiga",
      explanation: "Lihat Fathul Qorib hlm. 51.",
      sourcePage: 51,
      status: "REVIEW",
      createdAt: now,
      updatedAt: now,
    })
  );
  await assertFails(
    ustazDb.collection("editorialSubmissions").doc("sub_ustaz_own").update({
      status: "VERIFIED",
      reviewerId: USTAZ_UID,
      reviewerName: "KH Reviewer",
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Reviewer approves Bob's submission (authorId != reviewerId)
  await assertSucceeds(
    ustazDb.collection("editorialSubmissions").doc("sub_1").update({
      status: "VERIFIED",
      reviewerId: USTAZ_UID,
      reviewerName: "KH Reviewer",
      reviewNote: "Sesuai teks matan dan syarah Fathul Qorib hlm. 6.",
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Student can query VERIFIED submissions, but unfiltered query fails
  await assertSucceeds(
    aliceDb.collection("editorialSubmissions").where("status", "==", "VERIFIED").get()
  );
  await assertFails(aliceDb.collection("editorialSubmissions").get());

  // Bootstrapped Admin can list all submissions
  await assertSucceeds(adminDb.collection("editorialSubmissions").get());
});
