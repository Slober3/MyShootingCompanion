package eu.a7ol.myshootingcompanion.myshootingcompanion;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

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
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}