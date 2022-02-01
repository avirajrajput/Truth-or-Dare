package com.manacher.hammer.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.manacher.hammer.R;

import java.util.List;

public class BottleAdapter extends BaseAdapter {

    private List<Drawable> list;
    public LayoutInflater layoutInflater;



    public BottleAdapter( Activity activity, List<Drawable> list){
        this.list = list;
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
        Drawable drawable = list.get(position);
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.bottle_view, parent, false);
            viewHolder.bottle = (ImageView) convertView.findViewById(R.id.bottle);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();

        }

        viewHolder.bottle.setImageDrawable(drawable);

        return convertView;
    }

    class ViewHolder{
        ImageView bottle;
    }
}
