/**
 *  This work is licensed under the Creative Commons Attribution-NonCommercial-
 *  NoDerivs 3.0 Unported License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to 
 *  Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 
 *  94041, USA.
 * 
 *  Use of this work is permitted only in accordance with license rights granted.
 *  Materials provided "AS IS"; no representations or warranties provided.
 * 
 *  Copyright © 2012 Marcus Parkkinen, Aki Käkelä, Fredrik Åhs.
 **/

package edu.chalmers.dat255.audiobookplayer.view;

import java.util.List;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Button;
import edu.chalmers.dat255.audiobookplayer.R;

/**
 * Preferences for the application.
 * 
 * @author Aki K�kel�
 * @version 0.1
 * 
 */
@TargetApi(11)
public class PreferencesActivity extends PreferenceActivity {

	private static final String TAG = "PreferencesActivity";

	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Add a button to the header list.
		if (hasHeaders()) {
			Button button = new Button(this);
			button.setText("Some action");
			setListFooter(button);
		}
	}

	/**
	 * Populate the activity with the top-level headers.
	 */
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}

	/**
	 * This fragment shows the preferences for the first header.
	 */
	public static class Prefs1Fragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Make sure default values are applied. In a real app, you would
			// want this in a shared function that is used to retrieve the
			// SharedPreferences wherever they are needed.
			// PreferenceManager.setDefaultValues(getActivity(),
			// R.xml.advanced_preferences, false);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences_fragment);
		}
	}
}
