package com.eknowlabs.myapplication;

import android.app.Application;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Avay on 3/2/2016.
 */
public class ErrorDialogFragment extends DialogFragment {

    private Dialog mDialog;

    public ErrorDialogFragment() {
        super();
        mDialog = null;
    }

    public void setDialog(Dialog dialog) {
        mDialog = dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDialog;
    }
}
