package com.shouref.zipnnmail.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.shouref.zipnnmail.R;

/**
 * A dummy fragment representing a section of the app, but that simply displays dummy text.
 */
public class AddFilesChildFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private ListView listAddFiles;
	private BaseAdapter adapter;

	public AddFilesChildFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_dummy, container, false);
		listAddFiles = (ListView) rootView.findViewById(R.id.ls_add_files);
		adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, new String[] { "Old1" });
		listAddFiles.setAdapter(adapter);
		return rootView;
	}

	public void setAdapter(BaseAdapter adapter) {
		if (listAddFiles != null)
			listAddFiles.setAdapter(adapter);

	}

	public void showFastScrolls(boolean b) {
		listAddFiles.setFastScrollEnabled(b);
		listAddFiles.setFastScrollAlwaysVisible(b);

	}
}
