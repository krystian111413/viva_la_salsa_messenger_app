package com.zerter.teamconnect.AddPlan;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.zerter.teamconnect.R;

/**
 * Dialog to be sure to delete group
 */

@SuppressLint("ValidFragment")
public class ConfirmDialog extends DialogFragment{


    OnConfirm onConfirm;

    public ConfirmDialog(OnConfirm onConfirm) {
        this.onConfirm = onConfirm;
    }

    Button confirm, cancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_dialog,container,false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        confirm = view.findViewById(R.id.buttonConfirm);
        cancel = view.findViewById(R.id.buttonDismiss);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirm.onConfirm(true);
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
