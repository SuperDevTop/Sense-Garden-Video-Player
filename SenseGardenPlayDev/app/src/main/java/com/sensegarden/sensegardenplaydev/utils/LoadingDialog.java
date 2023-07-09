package com.sensegarden.sensegardenplaydev.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.sensegarden.sensegardenplaydev.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;
    private ImageView imgRotate;
    private int count = 0;

    public LoadingDialog(Activity myactivity) {
        activity = myactivity;
    }

    public void start() {
        if (alertDialog == null || !alertDialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_loading, null);
            builder.setView(view);
            builder.setCancelable(false);

            imgRotate = view.findViewById(R.id.imgRotate);
            imgRotate.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.rotate));

            alertDialog = builder.create();
            alertDialog.show();
        } else
            count++;

    }

    public void dismiss() {
        if (count > 0)
            count--;
        else if (alertDialog != null)
            alertDialog.dismiss();
    }
}

