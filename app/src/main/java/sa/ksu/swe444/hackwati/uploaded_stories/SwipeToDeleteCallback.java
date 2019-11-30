package sa.ksu.swe444.hackwati.uploaded_stories;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.type.Color;

import sa.ksu.swe444.hackwati.R;


public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private UploadedStoriesAdapter mAdapter;
    private Drawable icon;
    private ColorDrawable background ;
    private CoordinatorLayout coordinatorLayout;
    private int position;

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        showDialogWithOkButton("هل أنت متأكد من حذف القصة ؟", viewHolder);
        position = viewHolder.getAdapterPosition();


    }

    @SuppressLint("ResourceAsColor")
    public SwipeToDeleteCallback(UploadedStoriesAdapter adapter) {
        super(0,ItemTouchHelper.LEFT );
        mAdapter = adapter;
        icon = ContextCompat.getDrawable(mAdapter.getContext(),
                R.drawable.ic_trash);
        /*
        * change colors to RED
        *
        */
        background = new ColorDrawable(ContextCompat.getColor(mAdapter.getContext(),R.color.red_delete));
        this.coordinatorLayout = coordinatorLayout;

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;
        if (dX > 0) { // Swiping to the right
            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());

        } else if (dX < 0) { // Swiping to the left
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(c);
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            int iconRight = itemView.getLeft() + iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);

        icon.draw(c);
    }

    public void showDialogWithOkButton(String msg, final RecyclerView.ViewHolder viewHolder) {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mAdapter.getContext());
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("متأكد", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //int position = mAdapter.getAdapterPosition();
                        mAdapter.deleteItem(position);
                        mAdapter.notifyDataSetChanged();




                    }
                }).setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                      //  mAdapter.undoDelete();
                        dialog.dismiss();
                        mAdapter.notifyDataSetChanged();
                       // mAdapter.notify();
            }
        });
        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.show();
    }
}// SwipeToDeleteCallback
