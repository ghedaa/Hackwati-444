package sa.ksu.swe444.hackwati.uploaded_stories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.Color;

import java.util.ArrayList;
import java.util.List;


import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.InnerStoryActivity;
import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.R;




public class UploadedStoriesAdapter extends RecyclerView.Adapter<UploadedStoriesAdapter.MyViewHolder> {
    private static final String TAG = "CustomAdapter";

    private List<Item> storyList = new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    UploadedStoriesAdapter.MyViewHolder holder;
    private Context context;
    private String userID;


    public UploadedStoriesAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    //This method inflates view present in the RecyclerView
    @Override
    public UploadedStoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_list_item_profile, parent, false);
        holder = new UploadedStoriesAdapter.MyViewHolder(view);
        return holder;
    }



    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item list_items = storyList.get(position);
        holder.user_name.setText(list_items.getTitle());
        Glide.with(context).load(list_items.getImage()).into(holder.icon);
       // holder.subscribe.setText(list_items.getStatus());
        if(list_items.getStatus().equals( Constants.Keys.REJECTED)){
            holder.subscribe.setBackgroundColor(R.color.pink_hak2);
            holder.subscribe.setText("مرفوض");
        }
        else if(list_items.getStatus().equals( Constants.Keys.PROCESSING)){
            holder.subscribe.setBackgroundColor(R.color.orange_hak);
            holder.subscribe.setText("مرسل");
        }
        else if(list_items.getStatus().equals( Constants.Keys.PUBLISHED)) {
            holder.subscribe.setBackgroundColor(R.color.green_hak);
            holder.subscribe.setText("منشور");
        }


    }

    //Setting the arraylist
    public void setListContent(List<Item> list_members) {
        this.storyList = list_members;
        notifyItemRangeChanged(0, list_members.size());

    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }


    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView user_name;
        ImageView icon;
        Button subscribe;
        RelativeLayout relativeLayout;
        public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        public MyViewHolder(View itemView) {
            super(itemView);


            user_name = (TextView) itemView.findViewById(R.id.title1);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            subscribe = (Button) itemView.findViewById(R.id.subscribeBtn);
            relativeLayout = itemView.findViewById(R.id.LR);
            subscribe.setVisibility(View.VISIBLE);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ListenToUserStory.class);
                    intent.putExtra(Constants.Keys.STORY_AUDIO, storyList.get(getAdapterPosition()).getSound());
                    intent.putExtra(Constants.Keys.STORY_TITLE, storyList.get(getAdapterPosition()).getTitle());
                    intent.putExtra(Constants.Keys.STORY_ID, storyList.get(getAdapterPosition()).getStoryId());
                    intent.putExtra(Constants.Keys.STORY_COVER, storyList.get(getAdapterPosition()).getImage());
                    context.startActivity(intent);
                }
            });

        }


        }//MyViewHolder class



    }//end class



