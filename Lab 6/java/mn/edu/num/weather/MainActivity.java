package mn.edu.num.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import mn.edu.num.weather.Modals.WeatherRVModal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import mn.edu.num.weather.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private TextView city_val, temp_val, desc_val, wind_speed_val;
    private RecyclerView today_recycle_view;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private final int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        city_val = findViewById(R.id.city_val);
        temp_val = findViewById(R.id.temp_val);
        wind_speed_val = findViewById(R.id.wind_speed_val);
        desc_val = findViewById(R.id.desc_val);
        today_recycle_view = findViewById(R.id.today_recycle_view);
        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModalArrayList);
        today_recycle_view.setAdapter(weatherRVAdapter);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        getWeatherInfo();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission granted...", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Please provide the permissions", Toast.LENGTH_SHORT).show();
        }
    }

    public void getWeatherInfo(){
        String city_name = "Ulaanbaatar";
        String url = "http://api.weatherapi.com/v1/forecast.json?key=e9552f9673ea4c9cb4694732220104&q=" + city_name + "&days=1&aqi=no&alerts=no";
        city_val.setText(city_name);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                weatherRVModalArrayList.clear();
                try{
                    String temp = response.getJSONObject("current").getString("temp_c");
                    temp_val.setText(temp + " °c");
                    String ws = response.getJSONObject("current").getString("wind_kph");
                    wind_speed_val.setText(ws + " Km/h");
                    int isDay = response.getJSONObject("current").getInt("is_day");

                    JSONObject curr_condition = response.getJSONObject("current").getJSONObject("condition");
                    String cond = curr_condition.getString("text");
                    desc_val.setText(cond);
                    //String condIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    //Picasso.get().load("http:".concat(condIcon)).into(ivImg);
                    if(isDay == 1){
                        Picasso.get().load("https://i.pinimg.com/564x/e2/76/39/e276395b56653bcd2de404b00ef1e3a7.jpg");
                    }else{
                        Picasso.get().load("https://i.pinimg.com/564x/cb/34/79/cb347977fc10cb7ed9d4293809f92fa5.jpg");
                    }

                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecastDay = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecastDay.getJSONArray("hour");

                    for(int i = 1; i < hourArray.length(); i = i + 3){
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temper = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String wind = hourObj.getString("wind_kph");
                        weatherRVModalArrayList.add(new WeatherRVModal(time, temper, img, wind));

                    }

                    weatherRVAdapter.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Not valid...", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}