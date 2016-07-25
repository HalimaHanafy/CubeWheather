package com.cubewheather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cubewheather.utils.SharedPrefUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * check if this id the first time to open the app
         */
        if (SharedPrefUtil.getInstance(getApplicationContext()).read("isFirstTimeToRunApp", true)) {
            /**
             * dialog opens only in first time to run the app to select weather location
             */
            showDialogFirstTime();
            Toast.makeText(this, "ShowDialog", Toast.LENGTH_SHORT).show();
        }
        else
        {
            /**
             * fetch loction wheather
             */
        }
    }

    private void showDialogFirstTime() {
            SharedPrefUtil.getInstance(getApplicationContext()).write("isFirstTimeToRunApp", false);
    }
}
