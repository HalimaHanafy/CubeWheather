package com.cubewheather;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cubewheather.utils.SharedPrefUtil;

/**
 * Created by halimahanafy on 25/07/16.
 */

public class SettingsActivity extends AppCompatActivity {

    LinearLayout llCity;
    TextView tvCity;
    LinearLayout llUnit;
    TextView tvUnit ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initToolbar();
        initViews();

        llCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder locationDialog = new AlertDialog.Builder(SettingsActivity.this);
                locationDialog.setTitle("Change Location");
                locationDialog.setMessage("Change City Name:");

                final EditText locationEditText = new EditText(SettingsActivity.this);
                locationEditText.setSingleLine();
                locationEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                locationEditText.setText(SharedPrefUtil.getInstance(getApplicationContext())
                        .read("CityChoosed","UnKnown"));
                float scale = getResources().getDisplayMetrics().density;
                int padding = (int) (16 * scale + 0.5f); // this converts 16dp to pixels

                locationDialog.setView(locationEditText, padding, 0, padding, 0);

                locationDialog.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String mSearch = locationEditText.getText().toString();
                        SharedPrefUtil.getInstance(getApplicationContext())
                                .write("CityChoosed",mSearch);
                        tvCity.setText(locationEditText.getText().toString());
                        dialog.dismiss();
                    }
                });
                locationDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                locationDialog.show();

            }
        });



        llUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set selected radio button
                int selectedUnitIndex=0;
                if (SharedPrefUtil.getInstance(getApplicationContext())
                        .read("UnitSelected", "Unknown").equals(getString(R.string.celsius))) {
                    selectedUnitIndex = 0;
                }else if(SharedPrefUtil.getInstance(getApplicationContext())
                        .read("UnitSelected", "Unknown").equals(getString(R.string.fahrenheit))) {
                    selectedUnitIndex=1;
                }
                else {
                    //no thing selected
                    selectedUnitIndex=-1;
                }
                //open dialog to choose unit
                new MaterialDialog.Builder(SettingsActivity.this)
                        .title("Change Temp Unit")
                        .items(R.array.items)
                        .itemsCallbackSingleChoice(selectedUnitIndex, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                String selectedUnit =text.toString();
                                SharedPrefUtil.getInstance(getApplicationContext())
                                        .write("UnitSelected",selectedUnit);
                                tvUnit.setText(selectedUnit);
                                return true;

                            }
                        })
                        .positiveText("Choose")
                        .show();

            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent backIntent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(backIntent);
    }


    private void initViews() {

        llCity = (LinearLayout) findViewById(R.id.ll_city);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvCity.setText(SharedPrefUtil.getInstance(getApplicationContext())
                .read("CityChoosed", "UnKnown"));
        llUnit = (LinearLayout) findViewById(R.id.ll_unit);
        tvUnit = (TextView) findViewById(R.id.tv_unit);
        if (SharedPrefUtil.getInstance(getApplicationContext())
                .read("UnitSelected", "Unknown").equals("Unknown")) {

            tvUnit.setText(getResources().getString(R.string.celsius));
        }else {
            tvUnit.setText(SharedPrefUtil.getInstance(getApplicationContext())
                    .read("UnitSelected", "UnKnown"));

        }

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
            Intent backIntent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(backIntent);
        }
        return true;

    }
}
