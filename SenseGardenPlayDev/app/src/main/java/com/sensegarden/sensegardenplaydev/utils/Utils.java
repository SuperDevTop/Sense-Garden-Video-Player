package com.sensegarden.sensegardenplaydev.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class Utils {

    private Context context;

    private ProgressDialog progressDialog;

    public Utils(Context context) {
        this.context = context;
    }

    public boolean isETEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    public String getETText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public String getTVText(TextView textView) {
        return textView.getText().toString().trim();
    }

    public void intent(Class c, Bundle bundle) {
        Intent intent = new Intent(context, c);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void showLoadingDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public boolean isEmailValid(EditText editText) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(getETText(editText)).matches();
    }

    public void displayToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void setRecycler(RecyclerView recycler, RecyclerView.Adapter adapter) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 1);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    public void setRecyclerHorizontal(RecyclerView recycler, RecyclerView.Adapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    public void setRecyclerTwoSpan(RecyclerView recycler, RecyclerView.Adapter adapter) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    public void setRecyclerThreeSpan(RecyclerView recycler, RecyclerView.Adapter adapter) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    public boolean isValidTime(String time) {
        String[] split = time.split(":");
        int hours = Integer.parseInt(split[0]);
        int minutes = Integer.parseInt(split[1]);
        int seconds = Integer.parseInt(split[2]);

        return minutes >= 0 && minutes < 60 && seconds >= 0 && seconds < 60 && hours >= 0;
    }

    public boolean isValidDate(String date) {
        if (date.isEmpty()) return false;

        String[] birthday = date.split("/");

        int year = Integer.parseInt(birthday[2]);
        int current_year = Calendar.getInstance().get(Calendar.YEAR);

        if (year >= current_year) return false;
        if (year <= 1900) return false;

        return true;
    }

}
