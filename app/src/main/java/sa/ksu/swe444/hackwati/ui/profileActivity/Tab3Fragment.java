package sa.ksu.swe444.hackwati.ui.profileActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.hackwati.Item;
import sa.ksu.swe444.hackwati.R;


public class Tab3Fragment extends Fragment {

    private RecyclerView recyclerView;
    private smallerStoryAdapter adapter;
    private List<Item> itemList;


    public Tab3Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.profile_fragment_two, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        itemList = new ArrayList<>();
        adapter = new smallerStoryAdapter(getActivity(),itemList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Tab3Fragment.GridSpacingItemDecoration
                (10, dpToPx(10), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        itemList();

        return view;
    }

    private void itemList() {
        int[] covers = new int[]{
                R.drawable.gray,
                R.drawable.gray,
                R.drawable.gray,
                R.drawable.gray,
        };


        Item a =new Item("انبتهي يا جود",covers[0]);
        itemList.add(a);
        a =new Item("انبتهي يا جود",covers[1]);
        itemList.add(a);
        a =new Item("انبتهي يا جود",covers[2]);
        itemList.add(a);
        a =new Item("انبتهي يا جود",covers[3]);
        itemList.add(a);

        Item a1 = new Item("أخي زيد", covers[0]);
        itemList.add(a1);

        a1 = new Item("ليس بعد !", covers[1]);
        itemList.add(a1);

        a1 = new Item("جود ودراجتها الجديدة", covers[2]);
        itemList.add(a1);

        a1 = new Item("انبتهي يا جود", covers[3]);
        itemList.add(a1);






        adapter.notifyDataSetChanged();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }
    }
}






