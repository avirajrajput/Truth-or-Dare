package com.manacher.hammer.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.models.MessageModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    private Activity activity;
    private List<MessageModel> messages;

    public ChatAdapter() {
    }

    public ChatAdapter(Activity activity, List<MessageModel> messages) {
        this.activity = activity;
        this.messages = messages;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ChatViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (messages.get(position).getUserId().equals(Util.USER.getUserId())){
            return 1;
        }else{
            return 0;
        }
    }

    public void addItem(MessageModel item) {
        this.messages.add(item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.own_message_view, parent, false);

        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_message_view, parent, false);

        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        final MessageModel item = messages.get(position);

        holder.text.setText(item.getText());

    }

    @Override
    public int getItemCount() {
//        if (((RoomActivity)activity).scrollView != null){
//            ((RoomActivity)activity).scrollView.fullScroll(View.FOCUS_DOWN);
//        }

        return messages.size();
    }
}
