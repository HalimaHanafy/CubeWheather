package com.cubewheather.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cubewheather.DayDetailsActivity;
import com.cubewheather.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.MyViewHolder> {

    private List<com.cubewheather.models.forcast16.List> daysList;
    private String SELECTED_UNIT;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDayName, tvTemp, tvDesc;
        private ImageView ivDay;

        MyViewHolder(View view) {
            super(view);
            tvDayName = (TextView) view.findViewById(R.id.tv_day_name);
            tvDesc = (TextView) view.findViewById(R.id.tv_day_desc);
            tvTemp = (TextView) view.findViewById(R.id.tv_temp);
            ivDay= (ImageView) view.findViewById(R.id.iv_day);
        }
    }


    public DaysAdapter(List<com.cubewheather.models.forcast16.List> daysList, String SELECTED_UNIT) {
        this.daysList = daysList;
        this.SELECTED_UNIT=SELECTED_UNIT;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,int position) {

        Date time=new Date((long)daysList.get(position).getDt()*1000);
        String dateTime = getReadableDateString(time);

        holder.tvDayName.setText(dateTime);
        holder.tvDesc.setText(daysList.get(position).getWeather().get(0).getDescription());

        if (SELECTED_UNIT.equals(holder.itemView.getContext().getString(R.string.celsius))){
            holder.tvTemp.setText((daysList.get(position).getTemp().getDay()+
                    holder.itemView.getContext().getString(R.string.celsius)));
        }
        else {
            holder.tvTemp.setText((convertCelciusToFahrenheit((float) daysList.get(position).
                    getTemp().getDay())+holder.itemView.getContext().getString(R.string.fahrenheit)));
        }


        String dayIcon="http://openweathermap.org/img/w/"+
                daysList.get(position).getWeather().get(0).getIcon()+".png";

        Glide.with(holder.itemView.getContext()).load(dayIcon)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivDay);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dayDetailsIntent=new Intent(holder.itemView.getContext(), DayDetailsActivity.class);
                dayDetailsIntent.putExtra("detail", (Serializable) daysList.get(holder.getAdapterPosition()));
                holder.itemView.getContext().startActivity(dayDetailsIntent);
            }
        });
    }

        /* The date/time conversion code is going to be moved outside the asynctask later,
 * so for convenience we're breaking it out into its own method now.
 */
    private String getReadableDateString(Date time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEEE MMM dd",
                java.util.Locale.getDefault());
        return shortenedDateFormat.format(time);
    }

    // Converts to fahrenheit
    private float convertCelciusToFahrenheit(float celsius) {
        return Math.round(((celsius * 9 / 5) + 32));
    }

    @Override
    public int getItemCount() {
        return daysList.size();
    }
}
