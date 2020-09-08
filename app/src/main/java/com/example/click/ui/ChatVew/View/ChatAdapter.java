package com.example.click.ui.ChatVew.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.click.R;
import com.example.click.ui.ChatVew.Model.ChatDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    private static int MSG_LEFT=0;
    private static int MSG_RIGHT=1;
    private Context context;
    private List<ChatDataModel> chatDataModels;

    FirebaseUser firebaseUser;

    public ChatAdapter(Context context,List<ChatDataModel> users) {
        this.chatDataModels = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType ==MSG_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_right, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_left, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        ChatDataModel chatDataModel = chatDataModels.get(position);
        holder.sendMessage.setText(chatDataModel.getMessage());

        if (position == chatDataModels.size()-1){
            if (chatDataModel.isIsseen()){
                holder.messageStatus.setText("Seen");
            }else {
                holder.messageStatus.setText("Delivered");
            }
        }else {
            holder.messageStatus.setVisibility(View.GONE);
        }

    }



    @Override
    public int getItemCount() {
        return chatDataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView sendMessage, messageStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            sendMessage = itemView.findViewById(R.id.tvMessage);
            messageStatus = itemView.findViewById(R.id.tvMessageStatus);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (chatDataModels.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_RIGHT;
        }else{
            return MSG_LEFT;
        }
    }
}

