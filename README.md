findparty
=========

### Android App
For *android-app* development, you need to install Google Play Services via the Android SDK Manager. 

##### Include Google Play Services Lib Project 

In Intellij, import Google Play Services Lib-Project `<SDK-PATH>/extras/google/google_play_services/libproject/google-play-services-lib` as new module into the project. Open *Module Settings* for Google Play Services Lib Project and add its `lib/google-play-servies.jar` as dependency and mark it for export.

Open *Module Settings* for *android-app* module and add a new module dependency to Google Play Services Lib Project module.

For more information on installing Google Play Services, see: [Setup Google Play Services](http://developer.android.com/google/play-services/setup.html)

##### Keystore

In order to properly access the Google Apis in this project, you need to copy the keystore file `debug.keystore` to `~/.android/debug.keystore`.
