package com.working.savch.was;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by savch on 15.05.2017.
 */

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        /*addSlide(firstFragment);
        addSlide(secondFragment);
        addSlide(thirdFragment);
        addSlide(fourthFragment);*/

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance(getString(R.string.slide1_title), getString(R.string.slide1_text), R.drawable.logo, Color.parseColor("#00BCD4")));
        addSlide(AppIntroFragment.newInstance(getString(R.string.slide2_title),  getString(R.string.slide2_text), R.drawable.intro_earnings, Color.parseColor("#00BCD4")));
        addSlide(AppIntroFragment.newInstance(getString(R.string.slide3_title),  getString(R.string.slide3_text), R.drawable.intro_categories, Color.parseColor("#00BCD4")));
        addSlide(AppIntroFragment.newInstance(getString(R.string.slide4_title),  getString(R.string.slide4_text), R.drawable.intro_history, Color.parseColor("#00BCD4")));
        addSlide(AppIntroFragment.newInstance(getString(R.string.slide5_title),  getString(R.string.slide5_text), R.drawable.logo, Color.parseColor("#00BCD4")));
        /*addSlide(AppIntroFragment.newInstance(getString(R.string.slide6_title),  getString(R.string.slide6_text), R.drawable.intro_enjoy, Color.parseColor("#3F51B5")));*/

        // OPTIONAL METHODS
        // Override bar/separator color.
        /*setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));*/

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(60);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        Intent intentMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intentMain);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        Intent intentMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intentMain);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
