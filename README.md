Android Revealator
========
[![Release](https://jitpack.io/v/Jaouan/Revealator.svg)](https://jitpack.io/#Jaouan/Revealator) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Revealator-green.svg?style=true)](https://android-arsenal.com/details/1/3788) [![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)

An helper to circle reveal/unreveal a view easily, with translations and childs animations.
The libraries is Android 15+ compatible.

![demo](art/demo3.gif) ![demo](art/demo2.gif) ![demo](art/demo1.gif)

Installation
--------
Gradle

```java
repositories {
  	maven { url "https://jitpack.io" }
}
```

```java
compile 'com.github.jaouan:revealator:1.2.1'
```

Usage
--------

Just put the view you want to animate in a `io.codetail.widget.RevealFrameLayout`...

```xml
<io.codetail.widget.RevealFrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <FrameLayout
            android:id="@+id/the_awesome_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@color/colorAccent"
            android:visibility="invisible">

                      <TextView
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:text="Lorem ipsum" />


        </FrameLayout>

    </io.codetail.widget.RevealFrameLayout>
```

... and use the Revealator's magic to reveal...
```java
Revealator.reveal( theAwesomeViewYouWantToReveal )
    .from( theInitiatorViewYouWantToTranslate )
    .withCurvedTranslation()
    //.withCurvedTranslation(curvePoint)
    .withChildsAnimation()
    //.withDelayBetweenChildAnimation(...)
    //.withChildAnimationDuration(...)
    //.withTranslateDuration(...)
    //.withHideFromViewAtTranslateInterpolatedTime(...)
    //.withRevealDuration(...)
    //.withEndAction(...)
    .start();
```

... or unreveal.
```java
Revealator.unreveal( theAwesomeViewYouWantToUnreveal )
    .to( theInitiatorViewYouWantToTranslateBack )
    .withCurvedTranslation()
    //.withCurvedTranslation(curvePoint)
    //.withUnrevealDuration(...)
    //.withTranslateDuration(...)
    //.withShowFromViewInterpolatedDuration(...)
    //.withEndAction(...)
    .start();
```

References
========

This project uses :
 - [ozodrukh's CircularReveal](https://github.com/ozodrukh/CircularReveal) for Android 4 compatibility.
 - [guohai's ArcTranslateAnimation](https://gist.github.com/guohai/2293628) for curved translation.

License
========

[Apache License Version 2.0](LICENSE)
