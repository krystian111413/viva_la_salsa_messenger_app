package com.zerter.teamconnect.AddPlan;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TimePicker;

import com.zerter.teamconnect.Controlers.MyButton;
import com.zerter.teamconnect.R;

@SuppressLint("ValidFragment")
public class DialogTime extends DialogFragment {

    public DialogTime(OnResult onResult) {
        this.onResult = onResult;
    }

    MyButton okButton;
    OnResult onResult;
    TimePicker timePicker;
    String time;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_time,container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        timePicker = view.findViewById(R.id.timePicker);
        okButton = view.findViewById(R.id.OKTime);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String hour = String.valueOf(hourOfDay);
                if (hourOfDay<10)
                    hour = "0" + hourOfDay;

                String min = String.valueOf(minute);
                if (minute<10)
                    min = "0"+minute;

                time = hour + ":" + min;
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResult.onResult(time);
                dismiss();
            }
        });
    }
}
