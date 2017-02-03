package socket.web.com.websocketintgration.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import socket.web.com.websocketintgration.R;
import socket.web.com.websocketintgration.models.ChatItem;

/**
 * Created by iva on 02.02.17.
 */

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatHolder> {


    private List<ChatItem> chatList;
    private Context context;

    public ChatRecyclerViewAdapter(Context context, List<ChatItem> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        ChatItem chatItem = chatList.get(position);
        if (chatItem.getUsername().isEmpty()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT;
            holder.message.setLayoutParams(params);
        }

        if (!chatItem.getPhotoUrl().isEmpty()) {
            Glide
                    .with(context)
                    .load(chatItem.getPhotoUrl())
                    .centerCrop()
                    .dontAnimate()
                    .crossFade()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.photo);
        }

        holder.message.setText(chatItem.getMessage());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void addNewItem(ChatItem item) {
        chatList.add(item);
        notifyDataSetChanged();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.username) TextView username;
        @BindView(R.id.photo) ImageView photo;
        @BindView(R.id.message) TextView message;

        public ChatHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
