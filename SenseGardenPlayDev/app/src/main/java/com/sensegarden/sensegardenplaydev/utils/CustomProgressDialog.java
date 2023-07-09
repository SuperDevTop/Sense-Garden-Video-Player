package com.sensegarden.sensegardenplaydev.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class CustomProgressDialog {
    private ProgressDialog progressDialog = null;

    public void show(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismiss() {
        progressDialog.dismiss();
    }

}
