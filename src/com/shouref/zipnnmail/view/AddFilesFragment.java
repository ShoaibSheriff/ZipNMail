package com.shouref.zipnnmail.view;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shouref.zipnnmail.R;
import com.shouref.zipnnmail.adapter.AddFilesAdapter;

public class AddFilesFragment extends Fragment implements TabListener {

	AddFilesAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	ViewPagerActivity activity;

	public static Fragment newInstance(Context context) {
		AddFilesFragment f = new AddFilesFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_add_files, null);

		activity = (ViewPagerActivity) getActivity();
		final ActionBar actionBar = activity.getActionBar();

		mSectionsPagerAdapter = new AddFilesAdapter(getActivity().getSupportFragmentManager());

		mViewPager = (ViewPager) root.findViewById(R.id.viewPager_add_files);
		mViewPager.setPadding(0, 0, 10, 0);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (actionBar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS)
					actionBar.setSelectedNavigationItem(position);
			}
		});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
		return root;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		mViewPager.setCurrentItem(arg0.getPosition());
		if (arg0.getPosition() == 1) {
			if (activity.filesToAdd.size() == 0) {
				activity.setProgressBarIndeterminateVisibility(true);
				activity.initializeSearch();
				mSectionsPagerAdapter.updateFragment(1, activity.addFilesAdapter);
			}
		} else {
			activity.setProgressBarIndeterminateVisibility(false);
		}

	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

}