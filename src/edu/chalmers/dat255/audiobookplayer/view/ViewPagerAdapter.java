package edu.chalmers.dat255.audiobookplayer.view;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Adapter for the ViewPager.
 * 
 * @author Aki Käkelä
 * @version 0.6
 * 
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;

	public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}

	@Override
	public Fragment getItem(int index) {
		return fragments.get(index);
	}

}
