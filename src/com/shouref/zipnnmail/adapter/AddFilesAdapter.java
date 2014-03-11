package com.shouref.zipnnmail.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.BaseAdapter;

import com.shouref.zipnnmail.view.AddFilesChildFragment;

public class AddFilesAdapter extends FragmentPagerAdapter {

	private AddFilesChildFragment pickFragment, searchFragment;

	public AddFilesAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			if (pickFragment == null)
				pickFragment = new AddFilesChildFragment();
			return pickFragment;
		case 1:
			if (searchFragment == null)
				searchFragment = new AddFilesChildFragment();
			return searchFragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return "Pick File";
		case 1:
			return "Search File";
		}
		return null;
	}

	public void updateFragment(int position, BaseAdapter adapter) {
		switch (position) {
		case 0:
			if (pickFragment != null) {
				pickFragment.setAdapter(adapter);
			}
			break;
		case 1:
			if (searchFragment != null) {
				searchFragment.setAdapter(adapter);
				searchFragment.showFastScrolls(true);
			}
			break;
		}

	}

}
