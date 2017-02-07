package socket.web.com.websocketintgration.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import socket.web.com.websocketintgration.R;
import socket.web.com.websocketintgration.models.ChatItem;
import socket.web.com.websocketintgration.utils.Utils;

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
    public void onBindViewHolder(final ChatHolder holder, int position) {
        ChatItem chatItem = chatList.get(position);
        if (chatItem.getUsername().equals(Utils.getMyUsername())) {
           holder.messageContainer.setGravity(Gravity.RIGHT);
        }

        if (!chatItem.getMessage().isEmpty()) {
            holder.message.setText(chatItem.getMessage());
        } else {
            holder.message.setVisibility(View.GONE);
        }

        if (!chatItem.getPhotoUrl().isEmpty()) {
            holder.progressPhoto.setVisibility(View.VISIBLE);
            Glide
                    .with(context)
                    .load(chatItem.getPhotoUrl())
                    .dontAnimate()
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressPhoto.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressPhoto.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.photo);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void addNewItem(ChatItem item) {
        chatList.add(item);
        notifyItemInserted(chatList.size());
    }

    public class ChatHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.username) TextView username;
        @BindView(R.id.photo) ImageView photo;
        @BindView(R.id.message) TextView message;
        @BindView(R.id.messageContainer) LinearLayout messageContainer;
        @BindView(R.id.progressPhoto) ProgressBar progressPhoto;

        public ChatHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
