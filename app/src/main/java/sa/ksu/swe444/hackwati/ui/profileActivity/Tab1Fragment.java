package sa.ksu.swe444.hackwati.ui.profileActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sa.ksu.swe444.hackwati.R;


public class Tab1Fragment extends Fragment {

LinearLayout linear;
    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CustomPojo> namesList;


    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.profile_fragment_one, container, false);

        recyclerView =v.findViewById(R.id.recycleView);
        namesList=new ArrayList<>();
        mAdapter = new CustomAdapter(getActivity());

        //We set the array to the adapter
        mAdapter.setListContent(namesList);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);



       /* namesList();*/

        return  v;
    }


   /* private void namesList() {

        // this is data fro recycler view
        CustomPojo s1 = new CustomPojo();
        s1.setName("الاسم");
        s1.setContent("أسماء الجديع");
        s1.setImg(R.drawable.ic_person_black_24dp);
        namesList.add(s1);

        CustomPojo s2 = new CustomPojo();
        s2.setName("البريد الإلكتروني");
        s2.setContent("iasmju@gmail.com" );
        s2.setImg(R.drawable.ic_email_black_24dp);
        namesList.add(s2);

        CustomPojo s3 = new CustomPojo();
        s3.setName("تاريخ الميلاد");
        s3.setContent("10/10/1997");
        s3.setImg(R.drawable.ic_date_range_black_24dp);
        namesList.add(s3);

        CustomPojo s4 = new CustomPojo();
        s4.setName("الجنس");
        s4.setContent("أنثى" );
        s4.setImg(R.drawable.ic_people_outline_black_24dp);
        namesList.add(s4);

        CustomPojo s5 = new CustomPojo();
        s5.setName("عدد المشتركين");
        s5.setContent("1400" );
        s5.setImg(R.drawable.ic_filter_9_plus_black_24dp);
        namesList.add(s5);



        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();



    }
*/

}

