# Bromi 

This game was written as a project for a seminar of the major Information Science and Language Technology at the Heinrich-Heine-Universität Düsseldorf. 

What is being presented here is a very simple language training App written in Java, for the purpose of practicing how to make an Android App on my own.


## Features ##

### Done (mandatory for project):
- [x] Create, delete and view your own user profile (only 1 profile).
- [x] View your statistics on your profile with some basic data collected from playing levels.
- [x] 8 sets/levels, with a total of 5 single choice translation questions each, try to train your language skills in practice mode. Only German-English is available currently.
- [x] The correct answer to a vocabulary is added to a set of wrong answers that are randomized every time you play.
- [x] View results of a level with a result screen to see if you passed.
- [x] Track your progress with a simple XP and level up system.

### Optional/TODO:

These are features that could be added to the game if I decide to take this project further in my free time.

- [ ] More languages (as implied ingame already)
- [ ] Longer levels
- [ ] Language unlock system
- [ ] Level unlock system
- [ ] Achievements system
- [ ] More profile statistics
- [ ] A challenge mode
- [ ] An endless mode
- [ ] Randomize entire levels daily
- [ ] Optimize layout for all kinds of smartphone screen resolution
- [ ] Add wrongly answered vocabulary to a user-directed custom level
- [ ] Add exams


## Getting Started

These instructions will get you a copy of the project up and running.


### APK for your Android smartphone

The simplest way to get this app running is to download the APK file to your Android smartphone. Note, however, that the layout and design of the game is guaranteed to 
show unintented behaviour, because I did not deal with programming layouts for different screen resolutions yet. The results I have seen on my own smartphone show the
disappearing of buttons, because they are outside of the visible screen, as well as undesired button cropping or button overlapping. Some of the text also disappears
outside of the viewable screen.

If you still wish to give the app a try this way, feel free to download the APK from my private Dropbox [here](https://www.dropbox.com/s/uoiuzkbdgr097h0/app-release.apk?dl=0).

Make sure your device's settings allows the installation of APK files.


### Running the Code on Your Computer

#### Prerequisites:

If you wish to run this game from your local PC, there are a few things you must have downloaded and set up beforehand:

* [Java SE Development Kit 8+ (JDK 8+)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Java Runtime Evironment](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Android Studio 2.1+](https://developer.android.com/studio/index.html), including the Android SDK and Android Emulator VMs (Android Studio will download the Android SDK and android virtual machines on its own.) 
* A PC that supports Intel Virtualization Technology

 If you are on Windows, you must add a variable to your system's PATH environment for the code to be compilable. Search for `cmd` on your PC, open it, and enter the following:

```
set PATH=C:\jdk1.7.0_75\bin;%PATH%
```

Press enter and then type the following command:

```
set JAVA_HOME=C:\jdk1.7.0_75
```

Press enter again to finish the process.

`C:\...` is the complete directory path to `jdk1.7.0_75` and `jdk1.7.0_75\bin`. They are located wherever you installed the JDK and JRE. Depending on what version of JDK you use, you must adjust `jdk1.7.0_75` to that version.

If you are on Linux, the system will know how to compile the code once you have installed the JDK and JRE.


#### Running the Code

Once you have installed Android Studio and set it up, you can fork/pull/download the project and open it in there. Next, click on `Run` in the toolbar of Android Studio (the green arrow). After that, you will be asked to
set up a virtual machine for the android emulator. Click on `Create New Emulator`. Then, on the next window, from the `Phone` tab, choose `3.7" WVGA (Nexus One)` (depending on what it is named). The whole project was being emulated on this VM and is thus recommended.
Click on `Next`. Now you have to choose what version of Android you want to emulate. For this project, choose and download `Marshmallow` with `API Levl 23` and `x86`, which is Android 6.0. Click on `Next`. All you have to do now is give the VM a name and click on `Finish`.
Click on `Run` again and choose the emulator you just created. Android Studio will set up the Virtual Machine and start it whenever it's finished. This can take a few minutes. When it's done, the app will start immediately after and you
can play the game. Note that the VM can run the app slower than a phone, so lag or slow loading times can occur on the emulator.

If you receive an Error while setting up the VM, check if your BIOS settings enables Intel VT and AMD-V Virtualization. Restart your computer and press whatever button leads you to the BIOS setup menu. Look around for an option
called  `Intel Virtualization Technology`, `Intel VT-x and AMD-V`, `Virtualization Extensions` or something similar and enable it. If your computer does not support virtualization, you cannot run the code on an emulator and thus
not test it on your computer at all. Try the APK method instead.


## Versioning

*Bromi ver. 0.10 (early alpha)


## Authors

* **Marco Kluin** - [GitHub](https://github.com/xayfuu)


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


## Acknowledgments

* Stackoverflow
* Google's Android Documentation
* Tutorialspoint
* [Android Button Maker](http://angrytools.com/android/button/)
* My music playlists on YouTube/SoundCloud
* A handful of friends for playtesting the App



*template inspired by [https://gist.github.com/PurpleBooth/109311bb0361f32d87a2](https://gist.github.com/PurpleBooth/109311bb0361f32d87a2)*