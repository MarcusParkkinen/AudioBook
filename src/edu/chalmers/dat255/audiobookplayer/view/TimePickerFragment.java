package edu.chalmers.dat255.audiobookplayer.view;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TimePicker;

/**
 * @author Aki Käkelä
 * 
 */
public class TimePickerFragment extends DialogFragment implements
		OnTimeSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, true);
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Log.d("TimePicker", "Time set");
	}

}
