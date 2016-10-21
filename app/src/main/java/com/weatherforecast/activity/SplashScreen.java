package com.weatherforecast.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.weatherforecast.R;
import com.weatherforecast.application.AppLevelVariables;
import com.weatherforecast.jsonparser.JsonParser;

/**
 * Created by Mansingh.Bhati on 10/20/2016.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        /**
         * Showing splash screen while parsing city list json file
         * data before launching the app Will use AsyncTask to parse
         */
        new PrefetchData().execute();
    }

    /**
     * Async Task to parse Json file
     */
    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            /*
             * Will make http call here This call will download required data
             * before launching the app
             */
            JsonParser jsonParser = new JsonParser(SplashScreen.this);
            AppLevelVariables.mCityList = jsonParser.readJSON();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
             /*
             * After completing json parsing
             *  will close this activity and launch main activity
             */
            Intent i = new Intent(SplashScreen.this, WeatherForeCast.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

            // close this activity
            finish();
        }
    }

}
