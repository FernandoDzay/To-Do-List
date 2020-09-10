package com.fiuady.a2dolist;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int Hora = c.get(Calendar.HOUR_OF_DAY);
        int Minuto = c.get(Calendar.MINUTE);


        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), Hora, Minuto, android.text.format.DateFormat.is24HourFormat(getActivity()));
    }
}
