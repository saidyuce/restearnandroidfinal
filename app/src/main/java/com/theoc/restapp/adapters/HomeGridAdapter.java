package com.theoc.restapp.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.dataorganization.screendata.GetDataHomeScreen;
import com.theoc.restapp.R;
import com.theoc.restapp.helper.HomeDialog;

public class HomeGridAdapter extends BaseAdapter {

    GetDataHomeScreen data;
    Context context;

    public HomeGridAdapter (Context context, GetDataHomeScreen data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return (data.get_size() + 1) / 2;
    }

    @Override
    public Object getItem(int position) {
       return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.home_single_item, parent, false);
                holder = new ViewHolder();
                // birinci için
                holder.imageView = (ImageView) row.findViewById(R.id.placeholderImageView);
                holder.kampanyaTypeTextView = (TextView) row.findViewById(R.id.kampanyaTypeTextView);
                holder.kafeNameTextView = (TextView) row.findViewById(R.id.kafeNameTextView);
                holder.kafeLocationTextView = (TextView) row.findViewById(R.id.kafeLocationTextView);
                holder.main_frame = (CardView) row.findViewById(R.id.itemCV);
                // ikinci için
                holder.imageView2 = (ImageView) row.findViewById(R.id.placeholderImageView2);
                holder.kampanyaTypeTextView2 = (TextView) row.findViewById(R.id.kampanyaTypeTextView2);
                holder.kafeNameTextView2 = (TextView) row.findViewById(R.id.kafeNameTextView2);
                holder.kafeLocationTextView2 = (TextView) row.findViewById(R.id.kafeLocationTextView2);
                holder.main_frame2 = (CardView) row.findViewById(R.id.itemCV2);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            if (position != 0) {
                position = position * 2;
            }

            holder.kampanyaTypeTextView.setText(data.get_campaing_category(position));
            holder.kafeNameTextView.setText(data.get_cafe_name(position));
            holder.kafeLocationTextView.setText(data.get_cafe_distance(position));
            Glide.with(context)
                    .load(data.get_campaing_picture_url(position))
                    .error(R.drawable.mypointsnargile)
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.imageView);

        final int finalPosition = position;
        holder.main_frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeDialog dialog = new HomeDialog(context,
                            holder.kampanyaTypeTextView.getText().toString(),
                            data.get_campaing_detail(finalPosition),
                            data.get_cafe_id(finalPosition),
                            "",
                            data.get_cafe_name(finalPosition),
                            data.get_cafe_x(finalPosition),   // cafe_x çekilecek
                            data.get_cafe_y(finalPosition));  // cafe_y çekilecek
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();
                }
        });

            position = position + 1;

            holder.kampanyaTypeTextView2.setText(data.get_campaing_category(position));
            holder.kafeNameTextView2.setText(data.get_cafe_name(position));
            holder.kafeLocationTextView2.setText(data.get_cafe_distance(position));
            Glide.with(context)
                    .load(data.get_campaing_picture_url(position))
                    .error(R.drawable.mypointsnargile)
                    .placeholder(R.drawable.placeholder)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.imageView2);

        final int finalPosition1 = position;
        holder.main_frame2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeDialog dialog = new HomeDialog(context,
                            holder.kampanyaTypeTextView2.getText().toString(),
                            data.get_campaing_detail(finalPosition1),
                            data.get_cafe_id(finalPosition1),
                            "",
                            data.get_cafe_name(finalPosition1),
                            data.get_cafe_x(finalPosition1),   // cafe_x çekilecek
                            data.get_cafe_y(finalPosition1));  // cafe_y çekilecek
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();
                }
        });
        return row;
    }

    public static class ViewHolder {
        ImageView imageView;
        TextView kampanyaTypeTextView, kafeNameTextView,
                 kafeLocationTextView;
        ImageView imageView2;
        TextView kampanyaTypeTextView2, kafeNameTextView2,
                 kafeLocationTextView2;
        CardView main_frame;
        CardView main_frame2;
    }
}