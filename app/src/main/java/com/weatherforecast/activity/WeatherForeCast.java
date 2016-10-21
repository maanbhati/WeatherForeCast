package com.weatherforecast.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.weatherforecast.R;
import com.weatherforecast.application.AppLevelVariables;
import com.weatherforecast.jsonparser.JsonParser;
import com.weatherforecast.model.CityList;
import com.weatherforecast.model.CityWeatherResult;
import com.weatherforecast.model.DayDataModel;
import com.weatherforecast.model.DayDetailDataModel;
import com.weatherforecast.model.ListDataModel;
import com.weatherforecast.model.Searchdata;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mansingh.Bhati on 10/20/2016.
 */
public class WeatherForeCast extends AppCompatActivity {
    /**
     * Keep track of the weather searching  task to ensure we can cancel it if requested.
     */
    private WeatherInfoTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mCityName;
    private EditText mCountryCode;
    private View mProgressView;
    private View mWeatherFormView;
    private CityList mCityList;
    private ListDataModel mListDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        // parseCityList();
        mCityList = AppLevelVariables.mCityList;

        mCityName = (AutoCompleteTextView) findViewById(R.id.city);
        mCountryCode = (EditText) findViewById(R.id.countrycode);
        mWeatherFormView = findViewById(R.id.weather_form);
        mProgressView = findViewById(R.id.login_progress);

        /*Weather search button handle */
        Button mWeatherSearchButton = (Button) findViewById(R.id.search_weather_button);
        mWeatherSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    searchWeatherForecastInfo();
                } else {
                    //Showing alert dialog
                    showAlertDialog();
                }
            }
        });
        /*Weather search button handle ends */

        /*Country AutoCompleteTextView Section*/
        List<String> countries = getCityNames();
        mCityName.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries));
        /*Country AutoCompleteTextView Section ends*/

        /*AutoCompleteTextView  mCityName handle  */
        mCityName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = mCityName.getText().toString().trim();
                String countryName = getCountryCode(cityName);
                if (countryName != null) {
                    mCountryCode.setText(countryName);
                } else {
                    mCountryCode.setText("");
                }
            }
        });
        /*AutoCompleteTextView  mCityName handle  ends*/
    }

    /**
     * Return country code by city name
     */
    private String getCountryCode(String cityName) {
        try {
            for (Searchdata searchdataModel : mCityList.getSearchdata()) {
                if (searchdataModel.getName().trim().equalsIgnoreCase(cityName))
                    return searchdataModel.getCountry();
            }
        } catch (Exception ex) {
            System.out.println("Exception :" + ex.getMessage());
        }
        return null;
    }

    /**
     * Return city name from mCityListModel class object.
     */
    private List<String> getCityNames() {
        List<String> cityNameList = new ArrayList<>();
        if (mCityList.getSearchdata() != null) {
            for (Searchdata searchdataModel : mCityList.getSearchdata()) {
                if (searchdataModel != null)
                    cityNameList.add(searchdataModel.getName().trim());
            }
        }
        return cityNameList;
    }

    /**
     * Checks network availability
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Show alert dialog
     */
    private void showAlertDialog() {
        new AlertDialog.Builder(WeatherForeCast.this)
                .setTitle(getResources().getString(R.string.alert))
                .setMessage(getResources().getString(R.string.check_network))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    /**
     * Attempts to search weather forecast data by city name.
     */
    private void searchWeatherForecastInfo() {
        if (mAuthTask != null) {
            return;
        }
        mCityName.clearFocus();
        mCountryCode.clearFocus();
        String cityName = mCityName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(cityName)) {
            mCityName.setError(getString(R.string.error_field_required));
            focusView = mCityName;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new WeatherInfoTask();
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Represents an asynchronous weather search task on behalf of
     * the city.
     */
    public class WeatherInfoTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                return getDataFromServer();
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                Intent intent = new Intent(WeatherForeCast.this, WeatherForeCastList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("ListDataModel", mListDataModel);
                startActivity(intent);
            } else {
                mCityName.setError(getString(R.string.error_incorrect_city_name));
                mCityName.requestFocus();
            }
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Get the weather json data from server by using {"http://api.openweathermap.org/data/2.5/forecast"} API
     */
    private boolean getDataFromServer() {
        String queryString = mCityName.getText().toString().trim() + "," + mCountryCode.getText().toString().trim();
        String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + queryString.trim() + "&mode=json&appid=" + getResources().getString(R.string.api_key).trim();
        HttpURLConnection urlConnection = null;
        try {
            StringBuilder json = new StringBuilder();
            URL urlObj = new URL(url);
            urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.setRequestMethod("GET");
            int code = urlConnection.getResponseCode();
            InputStream responseStream = new BufferedInputStream(urlConnection.getInputStream());
            if (code == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
                returnSerializableData(json.toString());
                return true;
            } else {
                Log.i("WeatherForeCast", getResources().getString(R.string.error_http_url_connection));
                return false;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Returns ListDataModel class object to use in WeatherForeCastList activity class.
     */
    private ListDataModel returnSerializableData(String jsonString) {
        mListDataModel = new ListDataModel();
        try {
            /* Fetch data section*/
            CityWeatherResult cityWeatherResult = new JsonParser(WeatherForeCast.this).readServerJSON(jsonString);
            List<DayDetailDataModel> dayDetailDataModels = null;
            List<DayDataModel> dayDataModels = new ArrayList<DayDataModel>();
            DayDataModel dayDataModel = null;
            DayDetailDataModel dayDetailDataModel;
            mListDataModel.setCityName(cityWeatherResult.city.name);
            CityWeatherResult.List[] listData = cityWeatherResult.list;
            String previousDate = "";
            for (CityWeatherResult.List data : listData) {
                String resultDate = data.dt_txt.substring(0, 10);
                String resultTime = data.dt_txt.substring(11, 18);
                if (previousDate.equals("") || (!previousDate.equals(resultDate))) {
                    dayDataModel = new DayDataModel();
                    dayDetailDataModels = new ArrayList<DayDetailDataModel>();
                }
                dayDetailDataModel = new DayDetailDataModel();
                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                dayDataModel.setCurrentDate(inputFormat.parse(data.dt_txt));

                dayDetailDataModel.setMaxTemperature(Double.toString(data.main.temp_max));
                dayDetailDataModel.setMinTemperature(Double.toString(data.main.temp_min));
                dayDetailDataModel.setTemperature(Double.toString(data.main.temp));
                dayDetailDataModel.setTime(resultTime);
                dayDetailDataModel.setWeather(data.weather[0].main);
                dayDetailDataModel.setWindSpeed(Double.toString(data.wind.speed));
                dayDetailDataModels.add(dayDetailDataModel);
                if (previousDate.equals("") || (!previousDate.equals(resultDate))) {
                    dayDataModel.setDayDetailDataModels(dayDetailDataModels);
                    dayDataModels.add(dayDataModel);
                    mListDataModel.setDayDataModels(dayDataModels);
                }
                previousDate = resultDate;
            }
            /* Fetch data section end*/
        } catch (Exception ex) {
            System.out.println("returnSerializableData Exception :" + ex.getMessage());
        }
        return mListDataModel;
    }

    /**
     * Shows the progress UI and hides the weather search form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mWeatherFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mWeatherFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mWeatherFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mWeatherFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
