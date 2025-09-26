Vacation Scheduler Android App

This project gave me the chance to learn hands-on how to build an Android application from the ground up using Java and Android Studio. It was created as part of my mobile application development course, and the goal was to develop a working vacation planner that allows users to create, edit, and manage vacations along with any related excursions.

Through this project, I learned how to use the Room database to store user data locally, how to build a multi-screen mobile app, how to validate user input, and how to add features like alerts and sharing. I also got experience building and signing an APK for deployment.
What the App Does

Users can add, update, and delete vacations

Each vacation has a title, hotel name, start date, and end date

Excursions can be added to any vacation with a title and date

The app validates that the vacation dates are in the correct format and that the end date comes after the start date

Excursion dates must fall within the vacation dates

Alerts can be set for vacation start and end dates, as well as for individual excursions

Vacation details can be shared through email, SMS, or by copying to the clipboard

All data is saved locally using the Room persistence library

The app does not require an internet connection

Compatibility

This app is built for Android devices running version 8.0 (API level 26) or higher. I used the latest tools available at the time, including:

Android Studio Hedgehog

Java 11

Room 2.6.1

compileSdkVersion 36

targetSdkVersion 36

minSdkVersion 26

How to Run the App

1. Clone the Git repository:
git clone https://gitlab.com/your-username/vacation-scheduler.git

2. Open the project in Android Studio (Hedgehog or newer)

3. Build and run the app using an Android emulator or a physical device running Android 8.0 or later

How to Use It

When the app launches, you'll land on the home screen. From there, you can navigate to the vacation list, where you’ll see all vacations you've added.

To add a vacation, tap the "+" button. You'll be taken to a screen where you can enter the vacation title, hotel name, and the start and end dates. You can also add excursions from this screen. Once you're done, use the menu in the top right corner to save the vacation.

You can set alerts for the vacation's start and end dates using the same menu. You can also share the vacation details if needed.

To delete a vacation, go back to the list, select the vacation, and use the menu to delete it. Note that vacations can’t be deleted if they still have excursions tied to them.

To add an excursion, go to the vacation's detail screen and tap the "+" button. You'll enter a title and date for the excursion. Excursion dates must fall between the vacation's start and end dates. You can also set an alert for the excursion date using the menu.
Deployment Info

The signed APK was generated using Android Studio’s "Generate Signed APK" tool. The APK can be found at:

app/release/app-release.apk

Screenshots of the signing process and the final APK are included in the submission, along with the full project files and Git history.
Here is my git repository Link
https://gitlab.com/wgu-gitlab-environment/student-repos/ntrimb10/d308-mobile-application-development-android.git


## Scalability Features
- **Pagination**: Loads data in chunks (10 items/page)
- **Database Indexing**: Faster search on vacation titles
- **Async Operations**: All database calls run off UI thread
- 