package com.example.click.ui.Discover.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.click.R;
import com.example.click.ui.ChatVew.Model.ChatDataModel;
import com.example.click.ui.ChatVew.View.ChatActivity;
import com.example.click.ui.profile.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ViewHolder> {


    private Context context;
    private List<User> usersList;
    String lastMessage;

    public DiscoverAdapter(Context context,List<User> users) {
        this.usersList = users;
        this.context = context;
    }

    @NonNull
    @Override
    public DiscoverAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_user_list, parent, false);

        return new DiscoverAdapter.ViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull DiscoverAdapter.ViewHolder holder, int position) {

        User user=usersList.get(position);
        Log.d("11111111", "onBindViewHolder: "+usersList.size()+"");
        holder.user_name.setText(user.getUserName());
        holder.tvBio.setText(user.getBio());
        holder.tvCountryDiscover.setText(user.getCountry());

        // Online State
        if (user.getStatus().equals("online")){

            holder.user_status.setImageResource(R.drawable.online_24dp);
        }else
        if (user.getStatus().equals("offline")){
            holder.user_status.setImageResource(R.drawable.offline_24dp);
        }else {
            holder.user_status.setImageResource(R.drawable.offline_24dp);
        }

        //Profile Picture
        if (user.getImageUrl()!=null){
            if (!user.getImageUrl().equals("default")){
                Glide.with(context).load(user.getImageUrl()).into(holder.user_image);
            }else {
                holder.user_image.setImageResource(R.drawable.ic_profile_24dp);
            }

        }

        // Open Chat View
        holder.itemView.setOnClickListener(view -> {
            Intent intent= new Intent(context, ChatActivity.class);
            intent.putExtra("userId",user.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        CircleImageView user_image;
        TextView user_name,tvCountryDiscover,tvBio;
        CircleImageView user_status;


        public ViewHolder(View itemView) {
            super(itemView);
            user_image = itemView.findViewById(R.id.ivUserListImgDiscover);
            user_name = itemView.findViewById(R.id.tvListUserNameDiscover);
            tvBio = itemView.findViewById(R.id.tvListBioDiscover);
            user_status = itemView.findViewById(R.id.tvUserStatusDiscover);
            tvCountryDiscover = itemView.findViewById(R.id.tvCountryDiscover);

        }
    }


    private void lastMessage(final String userid,final TextView textView){
        lastMessage="default";

        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatDataModel chatDataModel = snapshot.getValue(ChatDataModel.class);

                    if (chatDataModel.getReceiver().equals(firebaseUser.getUid()) && chatDataModel.getSender().equals(userid) ||
                            chatDataModel.getReceiver().equals(userid) && chatDataModel.getSender().equals(firebaseUser.getUid())){
                        lastMessage= chatDataModel.getMessage();
                    }
                }

                switch (lastMessage){

                    case "default" :
                        textView.setText("");
                        break;
                    default:
                        textView.setText(lastMessage);
                        break;
                }

                lastMessage="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
