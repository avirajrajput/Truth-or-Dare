package com.manacher.hammer.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.manacher.hammer.R;
import com.manacher.hammer.models.Player;

import java.util.List;

public class PlayersAdapter extends BaseAdapter {

    private List<Player> list;
    public LayoutInflater layoutInflater;
    public Button continueButton;

    public PlayersAdapter(Activity activity, List<Player> list, Button continueButton){
        this.list = list;
        this.layoutInflater =(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.continueButton = continueButton;
    }

    public void addPlayer(Player player){
        list.add(player);
        notifyDataSetChanged();
    }

    public void delete(int position){
        list.remove(position);
        notifyDataSetChanged();
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
        Player player = list.get(position);
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.number_player_view, parent, false);
            viewHolder.number = (TextView) convertView.findViewById(R.id.number);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.delete = (Button) convertView.findViewById(R.id.delete);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.number.setText(String.valueOf(position + 1));
        viewHolder.name.setText(player.getName());

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
                if(list.isEmpty()){
                    continueButton.setVisibility(View.GONE);
                }else{
                    continueButton.setVisibility(View.VISIBLE);
                }
            }
        });

        if(list.isEmpty()){
            continueButton.setVisibility(View.GONE);
        }else{
            continueButton.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class ViewHolder{
        TextView number;
        TextView name;
        Button delete;
    }
}
