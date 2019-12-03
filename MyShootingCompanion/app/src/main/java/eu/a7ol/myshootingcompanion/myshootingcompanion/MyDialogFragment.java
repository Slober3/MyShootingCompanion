package eu.a7ol.myshootingcompanion.myshootingcompanion;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class MyDialogFragment extends DialogFragment {
    long lasttime;
    ArrayList<DataModel> data;

    @SuppressLint("ValidFragment")
    public MyDialogFragment(long lstTime, ArrayList<DataModel> data) {
        this.lasttime = lstTime;
        this.data = data;
    }

    public ArrayList<DataModel> getData() {
        return data;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Completed!");
        builder.setMessage("You took " + Long.toString((lasttime / 1000)) + " seconds. \nOr " + Long.toString((lasttime)) + " milliseconds.\n\nPress start again to log your next run!");
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                data.clear();


            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // You don't have to do anything here if you just
                // want it dismissed when clicked
            }
        }).setNeutralButton("Add Points", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialog, int id) {
                // You don't have to do anything here if you just
                Context context = getContext();
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                builder2.setTitle("Enter your points");
                builder2.setMessage("Please enter your points\nYou should do this in one number\nDo not add spaces.");

                final EditText input2 = new EditText(getContext());

                input2.setInputType(InputType.TYPE_CLASS_NUMBER);
                input2.setRawInputType(Configuration.KEYBOARD_12KEY);
                input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});

                builder2.setView(input2);

                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text2 = input2.getText().toString();
                    }
                });
                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder2.show();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}