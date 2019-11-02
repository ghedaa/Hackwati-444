package sa.ksu.swe444.hackwati.list_adabter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.hackwati.AdminStoryActivity;
import sa.ksu.swe444.hackwati.Constants;
import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.ui.profileActivity.ProfileActivity;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private static final String TAG = "CustomAdapter";

    private List<CustomPojo> list_members = new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;
    private String userID;


    public CustomAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    //This method inflates view present in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.row_list_item_profile, parent, false);
        holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CustomPojo list_items = list_members.get(position);
        holder.user_name.setText(list_items.getName());
        Glide.with(context).load(list_items.getImg()).into(holder.icon);
        holder.subscribe.setText(list_items.isSubscribed());

    }

    //Setting the arraylist
    public void setListContent(ArrayList<CustomPojo> list_members) {
        this.list_members = list_members;
        notifyItemRangeChanged(0, list_members.size());

    }

    @Override
    public int getItemCount() {
        return list_members.size();
    }


    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView user_name;
        ImageView icon;
        Button subscribe;
        public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        public MyViewHolder(View itemView) {
            super(itemView);


            user_name = (TextView) itemView.findViewById(R.id.title1);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            subscribe = (Button) itemView.findViewById(R.id.subscribeBtn);
            subscribe.setVisibility(View.VISIBLE);



            subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (subscribe.getText().equals("اشتراك"))
                        subscribUser();
                    else if (subscribe.getText().equals("مشترك")) {
                        unsubscribUser();
                    } else
                        subscribe.setVisibility(View.INVISIBLE);
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    int clickedPosition = getAdapterPosition();
                    //todo intent action
                    if (clickedPosition != RecyclerView.NO_POSITION) {

                        CustomPojo clickedUser = list_members.get(clickedPosition);
                        userID = clickedUser.getId();


                        Intent intent = new Intent(context, ProfileActivity.class);
                        intent.putExtra(Constants.Keys.STORY_USER_ID, clickedUser.getId());
                        context.startActivity(intent);


                    }

                }

            });
        }


        public void subscribUser() {
            int clickedPosition = getAdapterPosition();
            //todo intent action
            if (clickedPosition != RecyclerView.NO_POSITION) {

                CustomPojo clickedUser = list_members.get(clickedPosition);
                userID = clickedUser.getId();
            }

            final DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
            washingtonRef.update("subscribedUsers", FieldValue.arrayUnion(userID)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    subscribe.setText("مشترك");


                }
            });


        }

        public void unsubscribUser() {
            int clickedPosition = getAdapterPosition();
            if (clickedPosition != RecyclerView.NO_POSITION) {

                CustomPojo clickedUser = list_members.get(clickedPosition);
                userID = clickedUser.getId();
            }
            final DocumentReference washingtonRef = firebaseFirestore.collection("users").document(userUid);
            washingtonRef.update("subscribedUsers", FieldValue.arrayRemove(userID)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    subscribe.setText("اشتراك");
                }
            });

        }


    }

}

