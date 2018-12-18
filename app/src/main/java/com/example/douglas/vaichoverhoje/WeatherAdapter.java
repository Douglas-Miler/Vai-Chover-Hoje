package com.example.douglas.vaichoverhoje;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends ArrayAdapter {

    public WeatherAdapter(Context context, int id, ArrayList<Weather> weathers){
        super(context, id, weathers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Weather weather = (Weather) getItem(position);

        TextView date = listItemView.findViewById(R.id.date);
        date.setText(new StringBuilder(weather.getDate()));

        TextView maxTemp = listItemView.findViewById(R.id.maxTemp);
        maxTemp.setText(new StringBuilder().append(weather.getMaxTemp()).append("°"));

        TextView minTemp = listItemView.findViewById(R.id.minTemp);
        minTemp.setText(new StringBuilder().append(weather.getMinTemp()).append("°"));

        return listItemView;
    }
}
