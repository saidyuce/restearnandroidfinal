package com.theoc.restapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theoc.restapp.HomeActivity;
import com.theoc.restapp.R;
import com.theoc.restapp.dataorganization.screendata.GetDataHomeScreen;
import com.theoc.restapp.helper.HomeDialog;
import com.theoc.restapp.helper.HomeDialogInterface;

public class ImageAdapter extends PagerAdapter {
    Context context;
    GetDataHomeScreen data;
    HomeDialogInterface homeDialogInterface;

    public ImageAdapter(Context context, GetDataHomeScreen data, HomeDialogInterface homeDialogInterface) {
        this.data = data;
        this.context = context;
        this.homeDialogInterface = homeDialogInterface;
    }

    @Override
    public int getCount() {
        return data.get_premium_size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Glide.with(context)
                .load(data.get_premium_image(position))
                .error(R.drawable.mypointsnargile)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .crossFade()
                .into(imageView);
        container.addView(imageView, 0);

        final int finalPosition = position;

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (homeDialogInterface != null) {
                    homeDialogInterface.show(data.get_premium_category(finalPosition),
                            data.get_premium_campaing_detail(finalPosition),
                            data.get_premium_id(finalPosition),
                            data.get_premium_icon(finalPosition),
                            data.get_premium_name(finalPosition),
                            data.get_premium_x(finalPosition),
                            data.get_premium_y(finalPosition),
                            data.get_premium_detail(finalPosition),
                            data.get_premium_large_image(finalPosition),
                            data.get_premium_face(finalPosition),
                            data.get_premium_twitter(finalPosition),
                            data.get_premium_instagram(finalPosition),
                            data.get_premium_site(finalPosition),
                            data.get_premium_tel(finalPosition));
                }
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}