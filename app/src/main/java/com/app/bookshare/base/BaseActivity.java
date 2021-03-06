package com.app.bookshare.base;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.app.bookshare.BR;
import com.app.bookshare.callbacks.DialogListener;

import cn.pedant.SweetAlert.SweetAlertDialog;


public abstract class BaseActivity<P , T extends ViewDataBinding> extends AppCompatActivity {


    protected T viewDataBinding;
    protected P presenter;
    private SweetAlertDialog dialog;
    private DialogListener dialogListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initBuilding();

    }

    private void initBuilding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutResourceId());
        presenter = createPresenter();
        viewDataBinding.setVariable(BR.presenter, presenter);
    }



    protected abstract int getLayoutResourceId();

    protected abstract P createPresenter();

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public void showProgressDialog(String title) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#FF9D0C"));
        dialog.setTitleText(title);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void hideProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void openActivity(Class<?> calledActivity) {
        Intent myIntent = new Intent(this, calledActivity);
        this.startActivity(myIntent);
    }

    public void openActivityWithNoAnimationAndClearTask(Class<?> calledActivity) {
        Intent myIntent = new Intent(this, calledActivity);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(myIntent);
    }

    public void openActivityWithFinish(Class<?> calledActivity) {
        Intent myIntent = new Intent(this, calledActivity);
        this.startActivity(myIntent);
       this.finish();
    }

    public void openActivityWithClearTask(Class<?> calledActivity) {
        Intent intent = new Intent(this, calledActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


    public void showDialogClickListeners(String title, String message, String positiveText, String negativeText) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveText, (dialogInterface, i) -> {
                    if (dialogListener != null) {
                        dialogListener.onPositiveClick();
                    }
                    dialogInterface.dismiss();
                })
                .setNegativeButton(negativeText, (dialogInterface, i) -> {
                    if (dialogListener != null) {
                        dialogListener.onNegativeClick();
                    }
                    dialogInterface.dismiss();
                });

        alert.show();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
