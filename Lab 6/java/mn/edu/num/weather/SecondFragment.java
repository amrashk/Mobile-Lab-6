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
import mn.edu.num.weather.databinding.FragmentSecondBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    private TextView f2_city_val;
    private RecyclerView week_recycle_view;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        container.findViewById(R.id.desc_val);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        f2_city_val = view.findViewById(R.id.f2_city_val);
        week_recycle_view = view.findViewById(R.id.week_recycle_view);
        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(view.getContext(), weatherRVModalArrayList);
        week_recycle_view.setAdapter(weatherRVAdapter);

        getWeatherInfo();

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    public void getWeatherInfo(){
        String city_name = "Ulaanbaatar";
        String url = "http://api.weatherapi.com/v1/forecast.json?key=e9552f9673ea4c9cb4694732220104&q=" + city_name + "&days=8&aqi=no&alerts=no";
        f2_city_val.setText(city_name);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                weatherRVModalArrayList.clear();
                try{

                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONArray forecastDays = forecastObj.getJSONArray("forecastday");

                    for(int i = 0; i < forecastDays.length(); i++){
                        JSONObject dayObj = forecastDays.getJSONObject(i);
                        String time = dayObj.getString("date");
                        String temper = dayObj.getJSONObject("day").getString("avgtemp_c");
                        String img = dayObj.getJSONObject("day").getJSONObject("condition").getString("icon");
                        String wind = dayObj.getJSONObject("day").getString("maxwind_kph");
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