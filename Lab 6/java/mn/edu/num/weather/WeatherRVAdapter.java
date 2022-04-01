package mn.edu.num.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mn.edu.num.weather.Modals.WeatherRVModal;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;

    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModal> weatherRVModalArrayList) {
        this.context = context;
        this.weatherRVModalArrayList = weatherRVModalArrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position){
        WeatherRVModal modal = weatherRVModalArrayList.get(position);
        holder.tvTemp.setText(modal.getTemp() + " °c");
        Picasso.get().load("http:".concat(modal.getIcon())).into(holder.ivIcon);
        holder.tvWindSpeed.setText(modal.getWind_speed() + " Km/h");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try{
            Date t = input.parse(modal.getTime());
            holder.tvTime.setText(output.format(t));
        }catch(ParseException e){
            holder.tvTime.setText(modal.getTime());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount(){
        return weatherRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTime, tvTemp, tvWindSpeed;
        private ImageView ivIcon;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            tvWindSpeed = itemView.findViewById(R.id.tvWindSpeed);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}