package sa.ksu.swe444.hackwati.explor;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.R;
import sa.ksu.swe444.hackwati.storyAdapter;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PopularStories extends Fragment implements ClassificationAdapter.ClassificationAdapterListener {


    private RecyclerView recyclerView;
    private ClassificationAdapter adapter;
    private List<Classifications> classificationsList;
    private RecyclerView.LayoutManager mLayoutManager;

   //step 6
   private FirstFragmentListener listener;
    //step 7
    public void setListener(FirstFragmentListener listener){
        this.listener=listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_classification_recycler, container, false);

    recyclerView = view.findViewById(R.id.recycler_view);

        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        classificationsList = new ArrayList<>();

        adapter = new ClassificationAdapter(getActivity(), classificationsList);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);
        setClassifications();

    }

    private void setClassifications() {
        int[] drawable = {R.drawable.ic_menu_black_24dp, R.drawable.ic_power_settings_new_black_24dp, R.drawable.defult_thumbnail, R.drawable.ic_search_black_24dp, R.drawable.ic_mail,R.drawable.adventureclassification};// gray is some thing else

        Classifications classifications= new Classifications("جميع القصص",   R.drawable.bookss );
        classificationsList.add(classifications);
        classifications= new Classifications("الأكثر شهرة",  R.drawable.starr );
        classificationsList.add(classifications);
        classifications= new Classifications("قصص مغامرات",  R.drawable.adventureclassification );
        classificationsList.add(classifications);
        classifications= new Classifications("قصص خيال علمي",  R.drawable.alien );
        classificationsList.add(classifications);
        classifications= new Classifications("قصص تاريخيه",  R.drawable.historical);
        classificationsList.add(classifications);
        classifications= new Classifications("قصص أنبياء",  R.drawable.prophets );
        classificationsList.add(classifications);
        classifications= new Classifications("قصص حيوانات",  R.drawable.babyanimal );
        classificationsList.add(classifications);
        adapter.notifyDataSetChanged();

    }

    //8
    @Override
    public void onClassificationItemClick(String name) {
        listener.onClassificationItemSelected1(name);
        Log.d("here ","onClick on PopularStories"+name);

    }

    //step 5
    public interface FirstFragmentListener{
        void onClassificationItemSelected1(String name);
    }
}
