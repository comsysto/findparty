findparty
=========

For android-app development, you need to install Google Play Services via the Android SDK Manager. 
In Intellij, import Google Play Services Lib-Project <SDK-PATH>/extras/google/google_play_services/libproject/google-play-services-lib as new Module into project. Open Module Settings for Google Play Services Lib Project and add its lib/google-play-servies.jar as dependency and mark it for Export.

Open Module Settings for android-app module and add module dependency to Google Play Services Lib Project module.

Copy debug.keystore to ~/.android/debug.keystore
