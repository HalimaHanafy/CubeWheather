package com.cubewheather;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cubewheather.adapters.DaysAdapter;
import com.cubewheather.custom.DividerItemDecoration;
import com.cubewheather.models.forcast16.WeatherForecastResponseModel;
import com.cubewheather.utils.NetworkUtils;
import com.cubewheather.utils.SharedPrefUtil;
import com.cubewheather.webservice.RestClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        Callback<WeatherForecastResponseModel> {

    private RecyclerView recyclerView;
    private TextView tvTempDay,tvTempMin,tvTempMax,tvDesc,tvTempNight,tvTempEvening,tvTempMorning
            ,tvCityName,tvDateTime;
    private ImageView ivDay;
    private String SELECTED_UNIT;
    private NetworkUtils networkUtils;
    private LinearLayout llContent;
    private Button btnTryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
//        check if this it is the first time to open the app
        if (SharedPrefUtil.getInstance(getApplicationContext()).read("isFirstTimeToRunApp", true)) {
//          dialog opens only in first time to run the app to select weather location
            showDialogChooseCity();
        } else {
//            fetch location weather
            callWeatherServiceForecast(SharedPrefUtil.getInstance(getApplicationContext())
                    .read("CityChoosed", "UnKnown"));
        }
    }

    private void showDialogChooseCity() {
        final AlertDialog.Builder locationDialog = new AlertDialog.Builder(MainActivity.this);
        locationDialog.setTitle("Choose City");
        locationDialog.setMessage("City Name (hint: Mansourah,Egypt):");
        locationDialog.setCancelable(false);

        final EditText locationEditText = new EditText(MainActivity.this);
        locationEditText.setSingleLine();
        locationEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        locationEditText.setText(R.string.default_search_city);

        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (16 * scale + 0.5f); // this converts 16dp to pixels
        locationDialog.setView(locationEditText, padding, 0, padding, 0);
        locationDialog.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String mSearch = locationEditText.getText().toString();
                if(mSearch.trim().equals("")) {
                    Toast.makeText(MainActivity.this, "Mansourah,Egypt)", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPrefUtil.getInstance(getApplicationContext()).write("isFirstTimeToRunApp", false);
                    callWeatherServiceForecast(locationEditText.getText().toString());
                    SharedPrefUtil.getInstance(getApplicationContext())
                            .write("CityChoosed", mSearch);
                    dialog.dismiss();
                }

            }
        });
        locationDialog.show();
    }
    
    private void callWeatherServiceForecast(String s) {
//        check network connectivity
        if (networkUtils.isConnectingToInternet()) {
            Call<WeatherForecastResponseModel> call = RestClient.getInstance().getSalonService()
                    .getWeatherForecast(s);
            call.enqueue(this);
        }
        else {
            //get data from shared
            llContent.setVisibility(View.GONE);
            btnTryAgain.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tvTempMax= (TextView) findViewById(R.id.tv_max_temp);
        tvTempMin= (TextView) findViewById(R.id.tv_min_temp);
        tvTempNight= (TextView) findViewById(R.id.tv_night_temp);
        tvTempMorning= (TextView) findViewById(R.id.tv_morning_temp);
        tvTempEvening= (TextView) findViewById(R.id.tv_evening_temp);
        tvTempDay= (TextView) findViewById(R.id.tv_temp_day);
        tvCityName= (TextView) findViewById(R.id.tv_city);
        tvDesc= (TextView) findViewById(R.id.tv_desc);
        tvDateTime= (TextView) findViewById(R.id.tv_datetime);
        ivDay= (ImageView) findViewById(R.id.iv_day);
        llContent= (LinearLayout) findViewById(R.id.ll_content);
        btnTryAgain= (Button) findViewById(R.id.btn_tryagain);
        llContent.setVisibility(View.GONE);
        SELECTED_UNIT=getString(R.string.celsius);
        networkUtils = new NetworkUtils(this);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SharedPrefUtil.getInstance(getApplicationContext())
                        .read("UnitSelected", "Unknown").equals("Unknown")) {
                    showDialogChooseCity();
                }
                else {
                    callWeatherServiceForecast(SharedPrefUtil.getInstance(getApplicationContext())
                            .read("CityChoosed", "UnKnown"));
                }
            }
        });

    }

    private void setupDaysListonRecyclerview(List<com.cubewheather.models.forcast16.List> listDays,
                                             String SELECTED_UNIT) {

        DaysAdapter mAdapter = new DaysAdapter(listDays,SELECTED_UNIT);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
            this.startActivity(settingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Call<WeatherForecastResponseModel> call, Response
            <WeatherForecastResponseModel> response) {

        if (response.isSuccess()&&!response.body().equals(null)) {
            llContent.setVisibility(View.VISIBLE);
            btnTryAgain.setVisibility(View.GONE);

            WeatherForecastResponseModel forecastResponseModel = response.body();

            //save model






            if(forecastResponseModel.getCod().equals("200")) {
                String cityName = forecastResponseModel.getCity().getName()+","+
                        forecastResponseModel.getCity().getCountry();
                SharedPrefUtil.getInstance(getApplicationContext())
                        .write("CityChoosed", cityName);
                List<com.cubewheather.models.forcast16.List> daysList =
                        forecastResponseModel.getList();

//        today obj
                com.cubewheather.models.forcast16.List todayWeatherObj = new
                        com.cubewheather.models.forcast16
                        .List(daysList.get(0).getDt(),
                        daysList.get(0).getTemp(),
                        daysList.get(0).getPressure(),
                        daysList.get(0).getHumidity(),
                        daysList.get(0).getWeather(),
                        daysList.get(0).getSpeed(),
                        daysList.get(0).getDeg(),
                        daysList.get(0).getClouds(),
                        daysList.get(0).getRain()
                );

                tvCityName.setText(forecastResponseModel.getCity().getName() + "," +
                        forecastResponseModel.getCity().getCountry());
                tvDesc.setText((todayWeatherObj.getWeather().get(0).getMain() + ""));


                if (SharedPrefUtil.getInstance(getApplicationContext())
                        .read("UnitSelected", "Unknown").equals("Unknown")) {

                    SELECTED_UNIT = getString(R.string.celsius);
                    tvTempDay.setText((todayWeatherObj.getTemp().getDay() + ""));
                    tvTempMin.setText("-" + todayWeatherObj.getTemp().getMin() + getString(R.string.celsius));
                    tvTempMax.setText("+" + todayWeatherObj.getTemp().getMax() + getString(R.string.celsius));
                    tvTempMorning.setText("" + todayWeatherObj.getTemp().getMorn() + getString(R.string.celsius));
                    tvTempNight.setText("" + todayWeatherObj.getTemp().getNight() + getString(R.string.celsius));
                    tvTempEvening.setText("" + todayWeatherObj.getTemp().getEve() + getString(R.string.celsius));
                } else if (SharedPrefUtil.getInstance(getApplicationContext())
                        .read("UnitSelected", "Unknown").equals(getString(R.string.fahrenheit))) {
//            SELECTED_UNIT=getString(R.string.fahrenheit);

                    SELECTED_UNIT = getString(R.string.fahrenheit);
                    tvTempDay.setText((convertCelciusToFahrenheit((float) todayWeatherObj.getTemp()
                            .getDay()) + ""));
                    tvTempMin.setText("-" + convertCelciusToFahrenheit((float) todayWeatherObj
                            .getTemp().getMin()) + getString(R.string.fahrenheit));
                    tvTempMax.setText("+" + convertCelciusToFahrenheit((float) todayWeatherObj
                            .getTemp().getMax()) + getString(R.string.fahrenheit));
                    tvTempMorning.setText("" + convertCelciusToFahrenheit((float) todayWeatherObj
                            .getTemp().getMorn()) + getString(R.string.fahrenheit));
                    tvTempNight.setText("" + convertCelciusToFahrenheit((float) todayWeatherObj
                            .getTemp().getNight()) + getString(R.string.fahrenheit));
                    tvTempEvening.setText("" + convertCelciusToFahrenheit((float) todayWeatherObj
                            .getTemp().getEve()) + getString(R.string.fahrenheit));


                } else if (SharedPrefUtil.getInstance(getApplicationContext())
                        .read("UnitSelected", "Unknown").equals(getString(R.string.celsius))) {

                    SELECTED_UNIT = getString(R.string.celsius);
                    tvTempDay.setText((todayWeatherObj.getTemp().getDay() + ""));
                    tvTempMin.setText("-" + todayWeatherObj.getTemp().getMin() + getString(R.string.celsius));
                    tvTempMax.setText("+" + todayWeatherObj.getTemp().getMax() + getString(R.string.celsius));
                    tvTempMorning.setText("" + todayWeatherObj.getTemp().getMorn() + getString(R.string.celsius));
                    tvTempNight.setText("" + todayWeatherObj.getTemp().getNight() + getString(R.string.celsius));
                    tvTempEvening.setText("" + todayWeatherObj.getTemp().getEve() + getString(R.string.celsius));

                }

                String image = todayWeatherObj.getWeather().get(0).getIcon();

                switch (image){
                    case "01d":
                        ivDay.setImageResource(R.drawable.clearsky);
                        break;
                    case "02d":
                        ivDay.setImageResource(R.drawable.ic_fewclouds);
                        break;
                    case "03d":
                        ivDay.setImageResource(R.drawable.ic_scatteredclouds);
                        break;
                    case "09d":
                        ivDay.setImageResource(R.drawable.ic_brokenclouds);
                        break;
                    case "10d":
                        ivDay.setImageResource(R.drawable.ic_showerrain);
                        break;
                    case "11d":
                        ivDay.setImageResource(R.drawable.ic_rain);
                        break;
                    case "13d":
                        ivDay.setImageResource(R.drawable.ic_snow);
                        break;
                    case "50d":
                        ivDay.setImageResource(R.drawable.ic_mist);
                        break;
                    default:
                        ivDay.setImageResource(R.drawable.ic_sunny);
                        break;
                }

                Date time = new Date((long) todayWeatherObj.getDt() * 1000);
                tvDateTime.setText(getReadableDateString(time));


                List<com.cubewheather.models.forcast16.List> restWeekDaysList = new ArrayList<>();
                for (int i = 1; i < daysList.size(); i++) {
                    restWeekDaysList.add(daysList.get(i));
                }
                Toast.makeText(this, "restWeekDaysList=  " + restWeekDaysList.size(),
                        Toast.LENGTH_SHORT).show();
                setupDaysListonRecyclerview(restWeekDaysList, SELECTED_UNIT);
            }
        }
    }

    // Converts to fahrenheit
    private float convertCelciusToFahrenheit(float celsius) {
        return Math.round(((celsius * 9 / 5) + 32));
    }


    private String getReadableDateString(Date time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEEE MMM dd yyyy",
                java.util.Locale.getDefault());
        return shortenedDateFormat.format(time);
    }

    @Override
    public void onFailure(Call<WeatherForecastResponseModel> call, Throwable t) {
        Log.d("TAG", "onFailure: " + t.getLocalizedMessage());
        llContent.setVisibility(View.GONE);
        btnTryAgain.setVisibility(View.VISIBLE);
    }
}
