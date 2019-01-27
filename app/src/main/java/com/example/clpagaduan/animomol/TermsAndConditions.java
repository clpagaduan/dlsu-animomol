package com.example.clpagaduan.animomol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

public class TermsAndConditions extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Terms and Conditions")
                .setMessage("1. I understand that the personal information I have inputted is protected under the Data Privacy Act.\n" +
                        "2. I am responsible and liable for all actions taken within the premises of the application.\n" +
                        "3. All personal information may be used by the application to generate important analytics and improve filtering of room preferences.\n" +
                        "4. I am subject under the CyberCrime Prevention Act of 2012, therefore, making me responsible for any malicious intents taken on the application and its information.\n" +
                        "5. Animomol's administrators have the authority to suspend or remove account access should it be deemed necessary. ")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
