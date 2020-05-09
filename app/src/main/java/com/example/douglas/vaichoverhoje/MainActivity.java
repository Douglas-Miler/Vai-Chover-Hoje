package com.example.douglas.vaichoverhoje;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String SEVEN_DAYS_WEATHER_CONDITION = "https://apiadvisor.climatempo.com.br/api/v1/forecast/locale/3477/days/15?token=fe15f8e3da1752649e88fe308c324550";
    private static final String CURRENT_WEATHER_CONDITION = "https://apiadvisor.climatempo.com.br/api/v1/weather/locale/3477/current?token=fe15f8e3da1752649e88fe308c324550";
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final SimpleDateFormat FORMAT_INPUT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat FORMAT_OUTPUT = new SimpleDateFormat("EEE, d MMM");
    private Weather weather = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.my_toolbar));
        new WeatherAsyncTask().execute(SEVEN_DAYS_WEATHER_CONDITION, CURRENT_WEATHER_CONDITION);
    }


    private void updateUi(ArrayList<Weather> weathers, Weather weather){
        if(weathers != null && weather != null){
            ((ListView)findViewById(R.id.listView)).setAdapter(new WeatherAdapter(this, 0, weathers));

            ((TextView)findViewById(R.id.currentDate)).setText(weather.getDate());
            ((TextView)findViewById(R.id.currentTemperature)).setText(weather.getCurrentTemp()+"°");
            ((TextView)findViewById(R.id.morningMin)).setText(weather.getMinMorningTemp()+"°");
            ((TextView)findViewById(R.id.morningMax)).setText(weather.getMaxMorningTemp()+"°");
            ((TextView)findViewById(R.id.afternoonMin)).setText(weather.getMinAfternoonTemp()+"°");
            ((TextView)findViewById(R.id.afternoonMax)).setText(weather.getMaxAfternoonTemp()+"°");
            ((TextView)findViewById(R.id.nightMin)).setText(weather.getMinNightTemp()+"°");
            ((TextView)findViewById(R.id.nightMax)).setText(weather.getMaxNightTemp()+"°");
        }else{
            Toast.makeText(this, "Error occurred", Toast.LENGTH_LONG).show();
            super.finish();
        }
    }

    private class WeatherAsyncTask extends AsyncTask<String, Void, ArrayList<Weather>>{
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ArrayList<Weather> doInBackground(String... urls) {
            URL url = createUrl(urls[0]);
            URL url1 = createUrl(urls[1]);

            String jsonResponse = null;
            String jsonResponse1 = null;

            if(url != null){
                jsonResponse = makeHttpRequest(url);
                jsonResponse1 = makeHttpRequest(url1);
            }

            weather = getWeatherFromJson(jsonResponse1);
            return extractWeathersFromJson(jsonResponse);
        }

        private String convertDate(Date date){
            return FORMAT_OUTPUT.format(date);
        }

        private Weather getWeatherFromJson(String jsonResponse){
            Weather weather = null;
            if(!TextUtils.isEmpty(jsonResponse) || jsonResponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    JSONObject data = jsonObject.getJSONObject("data");
                    int currentTemp = data.getInt("temperature");
                    weather = new Weather(currentTemp, -1, -1, -1, 1,
                            -1, -1, -1, -1, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return weather;
        }

        @Override
        protected void onPostExecute(ArrayList<Weather> weathers) {
            updateUi(weathers, weather);
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            findViewById(R.id.content).setVisibility(View.VISIBLE);

        }

        private ArrayList<Weather> extractWeathersFromJson(String jsonResponse){
            ArrayList<Weather> weathers = null;
            if(!TextUtils.isEmpty(jsonResponse) || jsonResponse != null)
            {
                try {
                    weathers = new ArrayList<>();
                    JSONObject baseObject = new JSONObject(jsonResponse);
                    JSONArray data = baseObject.getJSONArray("data");
                    JSONObject day = data.getJSONObject(0);
                    JSONObject temperature = day.getJSONObject("temperature");

                    weather.setMinMorningTemp(temperature.getJSONObject("morning").getInt("min"));
                    weather.setMaxMorningTemp(temperature.getJSONObject("morning").getInt("max"));
                    weather.setMinAfternoonTemp(temperature.getJSONObject("afternoon").getInt("min"));
                    weather.setMaxAfternoonTemp(temperature.getJSONObject("afternoon").getInt("max"));
                    weather.setMinNightTemp(temperature.getJSONObject("night").getInt("min"));
                    weather.setMaxNightTemp(temperature.getJSONObject("night").getInt("max"));
                    weather.setMinTemp(temperature.getInt("min"));
                    weather.setMaxTemp(temperature.getInt("max"));
                    weather.setDate(convertDate(FORMAT_INPUT.parse(day.getString("date"))));

                    for(int index = 1; index < data.length(); index++){
                        day = data.getJSONObject(index);
                        temperature = day.getJSONObject("temperature");

                        //There's no current temperature
                        weathers.add(new Weather(-1,
                                temperature.getJSONObject("morning").getInt("min"),
                                temperature.getJSONObject("morning").getInt("max"),
                                temperature.getJSONObject("afternoon").getInt("min"),
                                temperature.getJSONObject("afternoon").getInt("max"),
                                temperature.getJSONObject("night").getInt("min"),
                                temperature.getJSONObject("night").getInt("max"),
                                temperature.getInt("min"),
                                temperature.getInt("max"),
                                convertDate(FORMAT_INPUT.parse(day.getString("date")))));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "JSONException got caught in extractWeathersFromJson()");
                }
            }

            return weathers;
        }

        private String makeHttpRequest(URL url){
            HttpURLConnection connection = null;
            String jsonResponse = null;

            try{
                connection = (HttpURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(3000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(3000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod("GET");
                // Open communications link (network traffic occurs here).
                connection.connect();
                int responseCode = connection.getResponseCode();
                if(responseCode != HttpURLConnection.HTTP_OK){
                    Log.e(LOG_TAG, "Response code: " + responseCode);
                    throw new IOException("HTTP error code: " + responseCode);
                }

                jsonResponse = readFromInputStream(connection.getInputStream());
            }catch (Exception e){
                e.printStackTrace();
                Log.e(LOG_TAG, "IOException got caught in makeHttpRequest()");
            }finally {
                if(connection != null)
                    connection.disconnect();
            }

            return jsonResponse;
        }

        //returns empty or not
        private String readFromInputStream(InputStream stream) throws IOException{
            StringBuilder output = new StringBuilder();
            if(stream != null){
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                try {
                    String line = bufferedReader.readLine();
                    while (line != null){
                        output.append(line);
                        line = bufferedReader.readLine();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "IOException got caught in readFromInputStream()");
                }finally {
                    bufferedReader.close();
                }
            }

            return output.toString();
        }

        //returns null or not
        private URL createUrl(String stringUrl){
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "MalformedURLException got caught in createUrl()", e);
            }
            return url;
        }
    }
}
