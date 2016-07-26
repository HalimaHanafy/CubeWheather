package com.cubewheather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cubewheather.models.forcast16.List;
import com.cubewheather.utils.SharedPrefUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by halimahanafy on 26/07/16.
 */

public class DayDetailsActivity extends AppCompatActivity {

    private List selectedDayList;
    private TextView tvTempDay,tvTempMin,tvTempMax,tvDesc,tvTempNight,tvTempEvening,tvTempMorning
            ,tvDateTime;
    private ImageView ivDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_details);

        initToolbar();
        initViews();
        retreiveDataFromIntentAndPutItOnView();

    }

    private void retreiveDataFromIntentAndPutItOnView() {
        Intent intent = getIntent();
        selectedDayList= (List) intent.getSerializableExtra("detail");

        if (SharedPrefUtil.getInstance(getApplicationContext())
                .read("UnitSelected", "Unknown").equals("Unknown")){
            tvTempDay.setText(selectedDayList.getTemp().getDay()+"");
            tvTempMin.setText("-"+selectedDayList.getTemp().getMin()+getString(R.string.celsius));
            tvTempMax.setText("+"+selectedDayList.getTemp().getMax()+getString(R.string.celsius));
            tvTempMorning.setText(""+selectedDayList.getTemp().getMorn()+getString(R.string.celsius));
            tvTempNight.setText(""+selectedDayList.getTemp().getNight()+getString(R.string.celsius));
            tvTempEvening.setText(""+selectedDayList.getTemp().getEve()+getString(R.string.celsius));
            tvTempDay.setText(""+selectedDayList.getTemp().getDay()+getString(R.string.celsius));


        }
        else if (SharedPrefUtil.getInstance(getApplicationContext())
                .read("UnitSelected", "Unknown").equals(getString(R.string.fahrenheit))){

            tvTempDay.setText(convertCelciusToFahrenheit(selectedDayList.getTemp().getDay())+"");
            tvTempMin.setText("-"+convertCelciusToFahrenheit(selectedDayList.getTemp().getMin())
                    +getString(R.string.fahrenheit));
            tvTempMax.setText("+"+convertCelciusToFahrenheit(selectedDayList.getTemp().getMax())
                    +getString(R.string.fahrenheit));
            tvTempMorning.setText(""+convertCelciusToFahrenheit(selectedDayList.getTemp().getMorn())
                    +getString(R.string.fahrenheit));
            tvTempNight.setText(""+convertCelciusToFahrenheit(selectedDayList.getTemp().getNight())
                    +getString(R.string.fahrenheit));
            tvTempEvening.setText(""+convertCelciusToFahrenheit(selectedDayList.getTemp().getEve())
                    +getString(R.string.fahrenheit));
            tvTempDay.setText(""+convertCelciusToFahrenheit(selectedDayList.getTemp().getDay())
                    +getString(R.string.fahrenheit));


        }
        else if (SharedPrefUtil.getInstance(getApplicationContext())
                .read("UnitSelected", "Unknown").equals(getString(R.string.celsius))) {

            tvTempDay.setText(selectedDayList.getTemp().getDay()+"");
            tvTempMin.setText("-"+selectedDayList.getTemp().getMin()+getString(R.string.celsius));
            tvTempMax.setText("+"+selectedDayList.getTemp().getMax()+getString(R.string.celsius));
            tvTempMorning.setText(""+selectedDayList.getTemp().getMorn()+getString(R.string.celsius));
            tvTempNight.setText(""+selectedDayList.getTemp().getNight()+getString(R.string.celsius));
            tvTempEvening.setText(""+selectedDayList.getTemp().getEve()+getString(R.string.celsius));
            tvTempDay.setText(""+selectedDayList.getTemp().getDay()+getString(R.string.celsius));
        }

        String dayIcon="http://openweathermap.org/img/w/"+
                selectedDayList.getWeather().get(0).getIcon()+".png";

        Glide.with(getBaseContext()).load(dayIcon)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivDay);

        tvDesc.setText(""+selectedDayList.getWeather().get(0).getDescription());

        Date time=new Date((long)selectedDayList.getDt()*1000);
        tvDateTime.setText(getReadableDateString(time));

    }

    private void initViews() {
        tvTempMax= (TextView) findViewById(R.id.tv_max_temp);
        tvTempMin= (TextView) findViewById(R.id.tv_min_temp);
        tvTempNight= (TextView) findViewById(R.id.tv_night_temp);
        tvTempMorning= (TextView) findViewById(R.id.tv_morning_temp);
        tvTempEvening= (TextView) findViewById(R.id.tv_evening_temp);
        tvTempDay= (TextView) findViewById(R.id.tv_temp_day);
        tvDesc= (TextView) findViewById(R.id.tv_desc);
        tvDateTime= (TextView) findViewById(R.id.tv_datetime);
        ivDay= (ImageView) findViewById(R.id.iv_day);



    }

    private String getReadableDateString(Date time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEEE MMM dd yyyy");
        return shortenedDateFormat.format(time);
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Converts to fahrenheit
    private float convertCelciusToFahrenheit(double celsius) {
        return Math.round(((celsius * 9 / 5) + 32));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
            DayDetailsActivity.this.finish();
        }
        return true;

    }
}
