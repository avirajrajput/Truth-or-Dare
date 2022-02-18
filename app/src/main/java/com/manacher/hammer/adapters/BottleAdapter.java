package com.manacher.hammer.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.manacher.hammer.Activities.SplashActivity;
import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.models.Bottle;

import java.util.List;

public class BottleAdapter extends BaseAdapter {

    private List<Bottle> list;
    public LayoutInflater layoutInflater;
    private Activity activity;

    public BottleAdapter( Activity activity, List<Bottle> list){
        this.list = list;
        this.activity = activity;
        this.layoutInflater =(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bottle bottle = list.get(position);
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.bottle_view, parent, false);
            viewHolder.bottle = convertView.findViewById(R.id.bottle);
            viewHolder.lock = convertView.findViewById(R.id.lock);
            viewHolder.selected = convertView.findViewById(R.id.selected);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Glide.with(activity)
                .load(bottle.getUrl())
                .into(viewHolder.bottle);


        if(Util.USER.getOwnBottles().contains(position)){
            bottle.setLock(false);
        }

        if(bottle.isLock()){
            viewHolder.lock.setVisibility(View.VISIBLE);
        }else{
            viewHolder.lock.setVisibility(View.GONE);
        }



        if(SplashActivity.SELECTED_BOTTLE == position){
            viewHolder.selected.setVisibility(View.VISIBLE);
        }else{
            viewHolder.selected.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder{
        ImageView bottle;
        LinearLayout lock;
        RelativeLayout selected;
    }
}
