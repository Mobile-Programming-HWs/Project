# MicroMaster

Android course management app for the final Mobile Programming project.

## What It Does

- Registers local users as teachers, TAs, or students with basic name, email, and password validation.
- Lets teachers create courses and add homework links.
- Lets students enroll in courses.
- Lets TAs request course access. Only approved TAs can add homework.
- Shows user profile details and related courses.

## Setup

Install:

- Android Studio
- JDK 11
- Android SDK Platform 32

Build from PowerShell:

```powershell
cd "C:\Users\imanm\Downloads\GitHub\Mobile-Programming-HWs\Project"
.\gradlew.bat :app:assembleDebug
```

The debug APK is created at:

```text
app\build\outputs\apk\debug\app-debug.apk
```

## Run

Use Android Studio, or install on a connected device:

```powershell
adb devices
.\gradlew.bat :app:installDebug
```

## Implementation Notes

- Login and registration: `MainActivity`, `RegisterActivity`
- Course list: `CoursesListActivity`
- Course details and homework: `CourseActivity`, `AddHomeworkActivity`
- Profile screen: `ProfileActivity`
- Local database: Room entities and DAOs under `com.sharif.micromaster`

The app stores users, courses, enrollments, TA records, homework links, and login state in Room. Homework links can point to online files and are downloaded through Android DownloadManager.
