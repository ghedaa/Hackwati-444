package sa.ksu.swe444.hackwati.user_profile_activity;

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

import java.util.List;

import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.MainActivity;
import sa.ksu.swe444.hackwati.R;

public class smallerStoryAdapter extends RecyclerView.Adapter<smallerStoryAdapter.MyViewHolder> {
    private Context mContext;
    private List<Item> itemList;


    public class MyViewHolder extends RecyclerView.ViewHolder   {
        public TextView title;
        public ImageView image;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.small_title);
            image=itemView.findViewById(R.id.small_storyimage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public smallerStoryAdapter(Context mContext, List<Item> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    public smallerStoryAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stories_smaller_item, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.title.setText(item.getTitle());
        // loading album cover using Glide library
       Glide.with(mContext).load(item.getImage()).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
