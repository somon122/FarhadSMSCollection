package com.farhad.sms_collection.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.farhad.sms_collection.R;

public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_open);

        OpeningFragment openingFragment = new OpeningFragment();
        openFragmentMethod(openingFragment);

    }

    private void openFragmentMethod(OpeningFragment openFragment) {

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.openHost, openFragment)
                .commit();

    }
}
