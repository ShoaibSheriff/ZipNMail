package com.shouref.zipnnmail.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shouref.zipnnmail.view.AddFilesFragment;
import com.shouref.zipnnmail.view.FilesListFragment;
import com.shouref.zipnnmail.view.ZipCreationFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
	private Context context;
	private int totalPage = 3;

	private Fragment addFilesFragment, filesListFragment, zipCreationFragment;

	public ViewPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment f = new Fragment();
		switch (position) {
		case 0:
			if (addFilesFragment == null)
				addFilesFragment = AddFilesFragment.newInstance(context);
			f = addFilesFragment;
			break;
		case 1:
			if (filesListFragment == null)
				filesListFragment = FilesListFragment.newInstance(context);
			f = filesListFragment;
			break;
		case 2:
			if (zipCreationFragment == null)
				zipCreationFragment = ZipCreationFragment.newInstance(context);
			f = zipCreationFragment;
			break;
		}
		return f;
	}

	@Override
	public int getCount() {
		return totalPage;
	}

}
