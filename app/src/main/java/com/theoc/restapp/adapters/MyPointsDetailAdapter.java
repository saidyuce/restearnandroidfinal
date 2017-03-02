package com.theoc.restapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.theoc.restapp.R;
import com.theoc.restapp.dataorganization.screendata.GetDataPointsDetail;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPointsDetailAdapter extends BaseAdapter {

    Context context;
    GetDataPointsDetail data;

    public MyPointsDetailAdapter (Context context, GetDataPointsDetail data) {
        this.context = context;
        this.data = data;

    }

    @Override
    public int getCount() {
        return data.get_size();
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.my_points_detail_single_item, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) row.findViewById(R.id.textView9);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        String text = data.get_prizes(position).replace(",", "\nveya\n");
        holder.textView.setText(text);
        return row;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    public static class ViewHolder {
        TextView textView, ORTextView;
    }
}
