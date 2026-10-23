# Multi-Platform Ambient Sounds App
Some time ago, I used to rely heavily on an app that would let you layer up nice sounds to make a soothing ambience for sleep. After a while, it got filled with ads, and more and more features got pulled away until it was entirely paywalled - it gradually became a shadow of it's former self and I've missed it ever since.

> This app aims to fill it's place, and **this one's staying free forever.** 

Just load up the app, hit play and move the sliders around to make the overall soundscape you want. It supports background play so you can use it alongside other audio if needed and it's relatively simplistic so at this stage there's no special permissions needed. 

---

### Desktop
![screenshots/desktop.png]

### Mobile
![screenshots/mobile.png]
![screenshots/background-play.png]



**Runs on:** 
 - Android
 - Desktop
 - Web

\*I have plans to build IOS support too, but I need to get a dev environment and test device set up for that first.

---

## Installing
I'm in the process of getting this on the play store and other marketplaces - for now, getting it running is more of a manual process. 

**Android**
You can download and install the APK. You'll likely need to enable software sources in order for your phone to allow the installation.
[Download APK](release/Ambient%20Sounds.apk)

**Manual Build**
To build on Linux, clone the repo and cd into the root of it. Then run: 
``` bash
./kotlin --help
```
for more info. 

To build on Android, you can do: 
``` bash
./kotlin build -p android -v release
```


---

## Thank You!
I hope you like it. It's still early days but I'll be adding to this where I can. 