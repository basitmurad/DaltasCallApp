package com.example.daltascallapp.UserModels;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogeText extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Reporting The Caller");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Report The User");
        builder.setTitle("Are You Want To Report User");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();

                Toast.makeText(getActivity(), "Report Successful", Toast.LENGTH_SHORT).show();



            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getActivity(), "Report Cancel", Toast.LENGTH_SHORT).show();

            }
        });


        return builder.create();
    }


}
