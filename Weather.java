package com.example.ashud.weather;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Weather extends AppCompatActivity {
        //hi
    Button enterButton;
    EditText enterZipCode;
    TextView textView;
    TextView themeQuote,dayOneHi,dayOneLo,dayTwoHi,dayTwoLo,dayThreeHi,dayThreeLo,dayFourLo,dayFourHi,dayFiveHi,dayFiveLo,currentTemperature,displayTemp;
    TextView timefour,timeone, timetwo,timethree,timefive;
    String userZip;
    String apiRequest;
    ImageView one,two,three, four,five,mainImage;
    ConstraintLayout layout;
    double[] temp_min = new double[5], temp_max = new double[5];
    String[] weatherDescription = new String[5], date = new String[5];
    double currentTemp;
    String currentLocation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        enterButton = (Button)findViewById(R.id.enterButton);
        enterZipCode = (EditText)findViewById(R.id.enterZipcode);
        textView = (TextView) findViewById(R.id.textView);
        dayOneHi = (TextView)findViewById(R.id.dayOneHi);
        dayOneLo = (TextView)findViewById(R.id.dayOneLo);
        dayTwoHi = (TextView)findViewById(R.id.day2Hi);
        dayTwoLo = (TextView)findViewById(R.id.day2Lo);
        dayThreeHi = (TextView)findViewById(R.id.day3Hi);
        dayThreeLo = (TextView)findViewById(R.id.day3Lo);
        dayFourHi = (TextView)findViewById(R.id.day4Hi);
        dayFourLo = (TextView)findViewById(R.id.day4Lo);
        dayFiveHi = (TextView)findViewById(R.id.day5Hi);
        dayFiveLo = (TextView)findViewById(R.id.day5Lo);
        currentTemperature = (TextView)findViewById(R.id.currentTemp);
        displayTemp = (TextView)findViewById(R.id.displayTemp);
        themeQuote = (TextView)findViewById(R.id.themequote);
        one = (ImageView)findViewById(R.id.imageView);
        two = (ImageView)findViewById(R.id.imageView2);
        three = (ImageView)findViewById(R.id.imageView3);
        four = (ImageView)findViewById(R.id.imageView4);
        five = (ImageView)findViewById(R.id.imageView5);
        mainImage = (ImageView)findViewById(R.id.mainImage);
        layout = (ConstraintLayout)findViewById(R.id.root);
        timeone = (TextView)findViewById(R.id.timeone);
        timetwo = (TextView)findViewById(R.id.timetwo);
        timethree = (TextView)findViewById(R.id.timethree);
        timefour = (TextView)findViewById(R.id.timefour);
        timefive = (TextView)findViewById(R.id.timefive);

        //apiRequest = "http://api.openweathermap.org/data/2.5/forecast?zip=08852,us&APPID=f92c483877f31dd6e8fe267389253549";




        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userZip = enterZipCode.getText().toString();
                if(userZip.equals("")){
                    Toast message = Toast.makeText(Weather.this,"Please Enter a zipcode.",Toast.LENGTH_SHORT);
                    message.show();
                }
                else {
                    // apiRequest = "http://api.openweathermap.org/data/2.5/forecast?zip=" + userZip + "&units=imperial&type=accurate&mode=json&APPID=f92c483877f31dd6e8fe267389253549";
                    apiRequest = "http://api.openweathermap.org/data/2.5/forecast?zip=" + userZip + ",us&APPID=f92c483877f31dd6e8fe267389253549";
                    Log.d("LEBRON", apiRequest);
                    AsyncThread thread = new AsyncThread();
                    thread.execute();
                }
            }
        });





    }

    public class AsyncThread extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(apiRequest);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String jsonText = bufferedReader.readLine();
                JSONObject json = new JSONObject(jsonText);

                JSONArray list = json.getJSONArray("list");
                for(int i = 0; i < 5; i++) {
                    JSONObject currentObject = list.getJSONObject(i);
                    JSONObject main = currentObject.getJSONObject("main");
                    temp_min[i] = main.getDouble("temp_min");
                    temp_max[i] = main.getDouble("temp_max");

                    JSONArray weather = currentObject.getJSONArray("weather");
                    weatherDescription[i] = weather.getJSONObject(0).getString("description");

                    String currentTime = currentObject.getString("dt_txt").split(" ")[1];
                    TimeZone tz = TimeZone.getTimeZone("EST");
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                    sdf.setTimeZone(tz);

                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
                        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date d = formatter.parse(currentTime);
                        date[i] = sdf.format(d);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            try{
                URL url = new URL(apiRequest);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String jsonText = bufferedReader.readLine();
                JSONObject json = new JSONObject(jsonText);

                JSONArray list = json.getJSONArray("list");
                JSONObject currentObject = list.getJSONObject(0);
                JSONObject main = currentObject.getJSONObject("main");
                currentTemp = main.getDouble("temp");

                JSONObject city = json.getJSONObject("city");
                //JSONObject name = city.getJSONObject("name");
                currentLocation = city.getString("name");



            }catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String all = new String();

            for (int i = 0; i < 5; i++) {
                all += temp_min[i] + " " + temp_max[i] + " " + weatherDescription[i] + " " + date[i] + '\n';
            }

            textView.setText(currentLocation);



            //sets the high and low temps
            dayOneHi.setText(Math.round(setFahrenheit(temp_max[0]))+"°F");
            dayOneLo.setText(Math.round(setFahrenheit(temp_min[0]))+"°F");
            dayTwoHi.setText(Math.round(setFahrenheit(temp_max[1]))+"°F");
            dayTwoLo.setText(Math.round(setFahrenheit(temp_min[1]))+"°F");
            dayThreeHi.setText(Math.round(setFahrenheit(temp_max[2]))+"°F");
            dayThreeLo.setText(Math.round(setFahrenheit(temp_min[2]))+"°F");
            dayFourHi.setText(Math.round(setFahrenheit(temp_max[3]))+"°F");
            dayFourLo.setText(Math.round(setFahrenheit(temp_min[3]))+"°F");
            dayFiveHi.setText(Math.round(setFahrenheit(temp_max[4]))+"°F");
            dayFiveLo.setText(Math.round(setFahrenheit(temp_min[4]))+"°F");
            //times
            if(date[0].substring(0,1).equals("0"))
                timeone.setText(date[0].substring(1));
            else
                timeone.setText(date[0]);
            if(date[1].substring(0,1).equals("0"))
                timetwo.setText(date[1].substring(1));
            else
                timetwo.setText(date[1]);
            if(date[2].substring(0,1).equals("0"))
                timethree.setText(date[2].substring(1));
            else
                timethree.setText(date[2]);
            if(date[3].substring(0,1).equals("0"))
                timefour.setText(date[3].substring(1));
            else
                timefour.setText(date[3]);
            if(date[4].equals("12:00:00"))
                timefive.setText("7:00");
            if(date[4].substring(0,1).equals("0"))
                timefive.setText(date[4].substring(1));
            else
                timefive.setText(date[4]);


            //sets the current temp
            //currentTemperature.setText("Right now:"+setFahrenheit(currentTemp));
            displayTemp.setText(""+Math.round(setFahrenheit(currentTemp))+"°F");

            //first hour
            for(int i = 0;i<5;i++){
                if(weatherDescription[0].contains("clear sky")) {
                    one.setImageResource(R.drawable.clearsky);
                    themeQuote.setText("Good day for some quidditch");
                    mainImage.setImageResource(R.drawable.clearsky);
                }
                if(weatherDescription[0].contains("clouds")) {
                    one.setImageResource(R.drawable.fewclouds);
                    themeQuote.setText("Good day for some quidditch.");
                    mainImage.setImageResource(R.drawable.fewclouds);
                }
                if(weatherDescription[0].contains("rain")) {
                    one.setImageResource(R.drawable.rain);
                    themeQuote.setText("Can't visit Hagrid today!");
                    mainImage.setImageResource(R.drawable.rain);
                }
                if(weatherDescription[0].equals("scattered clouds")) {
                    one.setImageResource(R.drawable.scatteredclouds);
                    themeQuote.setText("Perfect day to ride BuckBeak");
                    mainImage.setImageResource(R.drawable.scatteredclouds);
                }
                if(weatherDescription[0].equals("shower rain")) {
                    one.setImageResource(R.drawable.showerrain);
                    themeQuote.setText("Harry ain't catchin the snitch today!");
                   mainImage.setImageResource(R.drawable.showerrain);
                }
                if(weatherDescription[0].equals("Snow")) {
                    themeQuote.setText("Good day to visit Hogsmeade");
                    one.setImageResource(R.drawable.snow);
                    mainImage.setImageResource(R.drawable.snow);

                }
                if(weatherDescription[0].equals("light snow")) {
                    one.setImageResource(R.drawable.snow);
                    themeQuote.setText("Dementors are near.");
                    mainImage.setImageResource(R.drawable.snow);
                }

            }
            //secondhour
            for(int i = 0;i<5;i++){
                if(weatherDescription[1].equals("clear sky"))
                    two.setImageResource(R.drawable.clearsky);
                if(weatherDescription[1].contains("clouds"))
                    two.setImageResource(R.drawable.fewclouds);
                if(weatherDescription[1].contains("rain"))
                    two.setImageResource(R.drawable.rain);
                if(weatherDescription[1].equals("scattered clouds"))
                    two.setImageResource(R.drawable.scatteredclouds);
                if(weatherDescription[1].equals("shower rain"))
                    two.setImageResource(R.drawable.showerrain);
                if(weatherDescription[1].equals("snow"))
                    two.setImageResource(R.drawable.snow);
                if(weatherDescription[1].equals("light snow")) {
                    two.setImageResource(R.drawable.snow);
                    //themeQuote.setText("Dementors are near.");
                }
            }
            //thirdhour
            for(int i = 0;i<5;i++){
                if(weatherDescription[2].equals("clear sky"))
                    three.setImageResource(R.drawable.clearsky);
                if(weatherDescription[2].contains("clouds"))
                    three.setImageResource(R.drawable.fewclouds);
                if(weatherDescription[2].contains("rain"))
                    three.setImageResource(R.drawable.rain);
                if(weatherDescription[2].equals("scattered clouds"))
                    three.setImageResource(R.drawable.scatteredclouds);
                if(weatherDescription[2].equals("shower rain"))
                    three.setImageResource(R.drawable.showerrain);
                if(weatherDescription[2].equals("snow"))
                    three.setImageResource(R.drawable.snow);
                if(weatherDescription[2].equals("light snow"))
                    three.setImageResource(R.drawable.snow);
            }
            //fourthhour
            for(int i = 0;i<5;i++){
                if(weatherDescription[3].equals("clear sky"))
                    four.setImageResource(R.drawable.clearsky);
                if(weatherDescription[3].contains("few clouds"))
                    four.setImageResource(R.drawable.fewclouds);
                if(weatherDescription[3].contains("rain"))
                    four.setImageResource(R.drawable.rain);
                if(weatherDescription[3].equals("scattered clouds"))
                    four.setImageResource(R.drawable.scatteredclouds);
                if(weatherDescription[3].equals("shower rain"))
                    four.setImageResource(R.drawable.showerrain);
                if(weatherDescription[3].equals("snow"))
                    four.setImageResource(R.drawable.snow);
                if(weatherDescription[3].equals("light snow"))
                    four.setImageResource(R.drawable.snow);
            }
            //5thhour
            for(int i = 0;i<5;i++){
                if(weatherDescription[4].equals("clear sky"))
                    five.setImageResource(R.drawable.clearsky);
                if(weatherDescription[4].contains("clouds"))
                    five.setImageResource(R.drawable.fewclouds);
                if(weatherDescription[4].contains("rain"))
                    five.setImageResource(R.drawable.rain);
                if(weatherDescription[4].equals("scattered clouds"))
                    five.setImageResource(R.drawable.scatteredclouds);
                if(weatherDescription[4].equals("shower rain"))
                    five.setImageResource(R.drawable.showerrain);
                if(weatherDescription[4].equals("snow"))
                    five.setImageResource(R.drawable.snow);
                if(weatherDescription[4].equals("light snow"))
                    five.setImageResource(R.drawable.snow);
            }



        }
    }

    double kelvinTemp;
    double celsiusTemp;
    double fahrenheitTemp;
    public double setFahrenheit(double kelvin){
        kelvinTemp = kelvin;
        celsiusTemp = kelvinTemp-273;
        fahrenheitTemp = ((9.0/5)*celsiusTemp) +32;
        DecimalFormat numberFormat = new DecimalFormat(("#"));


        //return Double.parseDouble(numberFormat.format(fahrenheitTemp));

        return fahrenheitTemp;


    }





}
