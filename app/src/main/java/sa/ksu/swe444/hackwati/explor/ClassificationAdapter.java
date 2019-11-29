package sa.ksu.swe444.hackwati.explor;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.StoryActivity;

import static sa.ksu.swe444.hackwati.R.color.pink_hak;

public class ClassificationAdapter extends RecyclerView.Adapter<ClassificationAdapter.MyViewHolder> {
    private Context mContext;
    private List<Classifications> classificationsList;
     MediaPlayer mp;

    //step2
    private ClassificationAdapterListener listener;
    //step3
    public void setListener(ClassificationAdapterListener listener){
        this.listener=listener;
    }

    public ClassificationAdapter(Context mContext, List<Classifications> classificationsList) {
        this.mContext = mContext;
        this.classificationsList = classificationsList;
    }

    public ClassificationAdapter(List<Classifications> classificationsList) {
        this.classificationsList = classificationsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.classification_title);
            image=itemView.findViewById(R.id.classification_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mp = MediaPlayer.create(mContext, R.raw.zapsplat_cartoon_bubble);
                    mp.start();

                    String s= classificationsList.get(getAdapterPosition()).getClassificationTitle();
                    //step4
                    listener.onClassificationItemClick(classificationsList.get(getAdapterPosition()).getClassificationTitle());
                    Log.d("here","onClick on ClassificationAdapter"+s);
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.classification_cardview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Classifications classifications = classificationsList.get(position);

        holder.title.setText(classifications.getClassificationTitle());
        Glide.with(mContext).load(classifications.getClassificationPic()+"").into(holder.image);

    }

    @Override
    public int getItemCount() {
        return classificationsList.size();    }

    //step1
    public interface ClassificationAdapterListener{
        void onClassificationItemClick(String name);

    }
}
