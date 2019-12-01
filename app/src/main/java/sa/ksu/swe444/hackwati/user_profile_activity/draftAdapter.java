package sa.ksu.swe444.hackwati.user_profile_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.Draft.ListenToStoryDraft;
import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.R;

 class draftAdap extends RecyclerView.Adapter<draftAdap.MyViewHolder> {
    private Context mContext;
    private List<Item> itemList;
    private String storyId,userId;
    public String  userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();



    public class MyViewHolder extends RecyclerView.ViewHolder   {
        public TextView title,channelName;
        public ImageView image, channelImage;



        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            channelName=itemView.findViewById(R.id.channelname);
            image=itemView.findViewById(R.id.storyimage);
            channelImage=itemView.findViewById(R.id.channelimage);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent); // to start another activity
*/



                    int clickedPosition = getAdapterPosition();
                    //todo intent action
                    if (clickedPosition != RecyclerView.NO_POSITION) {

                        Item clickedStory = itemList.get(clickedPosition);


                        storyId = clickedStory.getStoryId();

                        Context context = view.getContext();



                        Intent intent = new Intent(context, ListenToStoryDraft.class);
                        intent.putExtra(Constants.Keys.STORY_ID, storyId);
                        intent.putExtra(Constants.Keys.STORY_USER_ID, clickedStory.getUserId());
                        intent.putExtra(Constants.Keys.STORY_AUDIO,clickedStory.getSound() );
                        intent.putExtra(Constants.Keys.STORY_COVER ,clickedStory.getImage());
                        intent.putExtra(Constants.Keys.STORY_TITLE ,clickedStory.getTitle());
                        mContext.startActivity(intent);

                    }

                }
            });
        }


    }



    public draftAdap(Context mContext, List<Item> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    public draftAdap(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public draftAdap.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;
        if (userUid.equals("DUbp3gH497gydI7fJodUfRz9A2K3")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.admin_activit_item_layout, parent, false);
        }

        else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activit_main_item_layout, parent, false);}


        return new draftAdap.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull draftAdap.MyViewHolder holder, int position) {

        Item item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.channelName.setText(item.getChannelName());
        Glide.with(mContext).load(item.getImage()+"").into(holder.image);
        Glide.with(mContext).load(item.getChannelImage()+"").into(holder.channelImage);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
