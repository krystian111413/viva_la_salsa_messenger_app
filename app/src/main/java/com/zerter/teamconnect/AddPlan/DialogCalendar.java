package com.zerter.teamconnect.AddPlan;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CalendarView;

import com.zerter.teamconnect.Controlers.MyButton;
import com.zerter.teamconnect.R;

/**
 *
 */

@SuppressLint("ValidFragment")
public class DialogCalendar extends DialogFragment {

    public DialogCalendar(OnResult onResult) {
        this.onResult = onResult;
    }

    CalendarView calendarView;
    MyButton okButton;
    OnResult onResult;

    String date;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_calendar,container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        calendarView = view.findViewById(R.id.calendarView);
        okButton = view.findViewById(R.id.calendarOK);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String d = String.valueOf(dayOfMonth);
                if (dayOfMonth<10)
                    d = "0"+dayOfMonth;

                String m = String.valueOf(month + 1);
                if (month<9)
                    m = "0"+m;

                date = year + "-" + m + "-" + d;
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResult.onResult(date);
                calendarView.getDate();
                dismiss();
            }
        });
    }

}
