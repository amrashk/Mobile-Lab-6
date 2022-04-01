package mn.edu.num.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import mn.edu.num.weather.Modals.WeatherRVModal;
import mn.edu.num.weather.databinding.FragmentFirstBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private TextView city_val, temp_val, desc_val, wind_speed_val;
    private RecyclerView today_recycle_view;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        city_val = view.findViewById(R.id.city_val);
        temp_val = view.findViewById(R.id.temp_val);
        wind_speed_val = view.findViewById(R.id.wind_speed_val);
        desc_val = view.findViewById(R.id.desc_val);
        today_recycle_view = view.findViewById(R.id.today_recycle_view);
        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(view.getContext(), weatherRVModalArrayList);
        today_recycle_view.setAdapter(weatherRVAdapter);

        getWeatherInfo();

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }


    public void getWeatherInfo(){
        String city_name = "Ulaanbaatar";
        String url = "http://api.weatherapi.com/v1/forecast.json?key=e9552f9673ea4c9cb4694732220104&q=" + city_name + "&days=1&aqi=no&alerts=no";
        city_val.setText(city_name);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                Toast.makeText(getContext(), "Not valid...", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}