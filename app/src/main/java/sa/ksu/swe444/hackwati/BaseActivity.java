package sa.ksu.swe444.hackwati;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.hardware.camera2.params.MeteringRectangle;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.Locale;


public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }//end onCreate()()





    //2, progress Dialog
    public void showProgressDialog(boolean isCancelable) {
        try {
            if (progressDialog == null || progressDialog.isShowing()){

                progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loding");
            progressDialog.setCancelable(isCancelable);
            progressDialog.show();
        }//end if

        } catch (Exception e) {
            e.getStackTrace();
        } //end catch

    }// end showProgressDialog

    //3
    public void hideProgressDialog() {
        if (progressDialog != null)
            if (progressDialog.isShowing() && !isDestroyed())
                progressDialog.dismiss();
    }//end hideProgressDialog()

    public void showDialog(String msg) {

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(msg);
        //alertDialog.setIcon(R.drawable.elm_logo);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });// BUTTON_POSITIVE
        alertDialog.show();
    }




}//end class
