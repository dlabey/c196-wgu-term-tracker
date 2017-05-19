# WGU Term Tracker

This is the project for Mobile Application Development - C196. This project overall addresses the 
competency for `C. STUDENT SCHEDULER AND PROGRESS TRACKING APPLICATION`. All the flows outlined in 
the project task are addressed, as well as the ability to add deletion checking, optional notes, 
notes with photos, and the ability to get notifications. Noteable code is commented.

## Implementation Details

### Android Version Compatibility

WGU Term Tracker was build to be compatible with API/SDK version 25 and higher. This was chosen 
because this is a new project and to take use of the latest features and design standards, 
especially material UI. It was primarily developed on a Nexus 5 and Google Pixel with that version.

### Notifications

Notifications/Alerts are optional and by default on. The user can change the preferences for them 
in the preferences activity where they can set them on or off and also the number of days before 
their dates to notify (e.g. start date, end date, due date). The code that creates these 
notifications are in the CourseInputActivity class and the AssessmentInputActivity class. Courses 
have a notification for their start date and due date whereas Assessments have a notification for 
their due date. The NotificationScheduler class schedules the notifications and the
NotificationPublisher class publishes them.

### Data Storage

The application uses SQLite and SharedPreferences for data storage. SharedPreferences store the 
user's preferences whereas SQLite stores everything else. The classes that manage these along with
their contracts are in the data package.

### Declarative UI

The part of the UI that is declarative are the NonScrollListViews along with the items that make 
them up. A non-scroll list view had to be made so it could be set inside an already scrollable 
ScrollView. 

### Notes

Notes can be either text, photos, or both. In order to use photos the user will be prompted for 
permission to access their external content. A user can choose either a gallery photo or take one 
directly from the camera. This was done with a chooser intent.

### Sharing

Sharing is done in the NoteViewActivity and uses the phones messaging application. It shares the 
text if there is no photo present, otherwise it shares the photo.
