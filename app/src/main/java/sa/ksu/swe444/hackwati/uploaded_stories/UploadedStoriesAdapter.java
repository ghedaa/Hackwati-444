package sa.ksu.swe444.hackwati.uploaded_stories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.R;


public class UploadedStoriesAdapter extends RecyclerView.Adapter<UploadedStoriesAdapter.MyViewHolder> {
    private static final String TAG = "CustomAdapter";

    private List<Item> storyList = new ArrayList<>();
    private LayoutInflater inflater;
    private Item mRecentlyDeletedItem;
    View view;
    UploadedStoriesAdapter.MyViewHolder holder;
    private Context context;
    private int mRecentlyDeletedItemPosition;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    int position;


    public UploadedStoriesAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }




    //This method inflates view present in the RecyclerView
    @Override
    public UploadedStoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.uploaded_stories, parent, false);
        holder = new UploadedStoriesAdapter.MyViewHolder(view);
        return holder;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item list_items = storyList.get(position);
        holder.user_name.setText(list_items.getTitle());
        Glide.with(context).load(list_items.getImage()).into(holder.icon);
        holder.subscribe.setText(list_items.getStatus());

        if (list_items.getStatus().equals(Constants.Keys.REJECTED)) {

            ViewCompat.setBackgroundTintList(holder.subscribe, ContextCompat.getColorStateList(context, R.color.pink_hak2));
            holder.subscribe.setText("مرفوض");
        } else if (list_items.getStatus().equals(Constants.Keys.PROCESSING)) {

            ViewCompat.setBackgroundTintList(holder.subscribe, ContextCompat.getColorStateList(context, R.color.orange_hak));
            holder.subscribe.setText("مرسل");
        } else if (list_items.getStatus().equals(Constants.Keys.PUBLISHED)) {

            ViewCompat.setBackgroundTintList(holder.subscribe, ContextCompat.getColorStateList(context, R.color.green_hak));
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

    public Context getContext() {
        return context;
    }


    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView user_name;
        ImageView icon;
        Button subscribe;
        RelativeLayout relativeLayout;
        private ImageView menu;
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

        public int getP(){
                    return getItemViewType();
        }
    }//MyViewHolder class


    public void deleteStory(String id, String status, int position) {
        String storyCollection = null;

        if (status.equals(Constants.Keys.REJECTED)) {
            storyCollection = "rejectedStories";
        } else if (status.equals(Constants.Keys.PROCESSING)) {
            storyCollection = "stories";
        } else if (status.equals(Constants.Keys.PUBLISHED)) {
            storyCollection = "publishedStories";
        }


        firebaseFirestore.collection(storyCollection).document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        notifyItemRemoved(position);

    }

    public void undoDelete() {
        storyList.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = storyList.get(position);
        mRecentlyDeletedItemPosition = position;
        storyList.remove(position);
        notifyItemRemoved(position);
        String status =mRecentlyDeletedItem.getStatus();
        String id =mRecentlyDeletedItem.getStoryId();
        deleteStory(id,status,position);

    }

}//end class



