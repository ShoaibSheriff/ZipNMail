package com.shouref.zipnnmail.view;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.shouref.zipnnmail.R;
import com.shouref.zipnnmail.adapter.FilesListAdapter;
import com.shouref.zipnnmail.data.FileItem;

public class FilesListFragment extends Fragment {

	private ListView listFiles;

	private ActionMode mActionMode;

	public static Fragment newInstance(Context context) {
		FilesListFragment f = new FilesListFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_files_list, null);

		final ViewPagerActivity activity = (ViewPagerActivity) getActivity();

		listFiles = (ListView) root.findViewById(R.id.ls_files);
		listFiles.setEmptyView(root.findViewById(android.R.id.empty));

		activity.listAdapter = new FilesListAdapter(this.getActivity(), activity.files);
		listFiles.setAdapter(activity.listAdapter);

		listFiles.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				onListItemSelect(activity, position);
				return true;
			}
		});

		listFiles.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (activity.listAdapter.getSelectedCount() > 0) {
					onListItemSelect(activity, position);
					return;
				}

				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				File file = new File(((FileItem) activity.listAdapter.getItem(position)).getFilePath());
				String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
						((FileItem) activity.listAdapter.getItem(position)).getFileExtension());
				intent.setDataAndType(Uri.fromFile(file), mime);

				if (getActivity().getPackageManager().queryIntentActivities(intent, 0).size() == 0) {
					Toast.makeText(getActivity(), "Cant open this file", Toast.LENGTH_SHORT).show();
					return;
				}

				Intent chooser = Intent.createChooser(intent, "Open with");
				startActivity(chooser);

			}
		});

		Intent intent = getActivity().getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			activity.handleSendImage(intent); // Handle single image being sent
			getActivity().invalidateOptionsMenu();

		} else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
			activity.handleSendMultipleImages(intent); // Handle multiple images being sent
			getActivity().invalidateOptionsMenu();

		} else {
			// Handle other intents, such as being started from the home screen
		}

		return root;
	}

	public boolean isFileListEmpty(ViewPagerActivity activity) {
		return activity.listAdapter == null ? true : activity.listAdapter.getCount() == 0;
	}

	private void onListItemSelect(ViewPagerActivity activity, int position) {
		activity.listAdapter.toggleSelection(position);
		boolean hasCheckedItems = activity.listAdapter.getSelectedCount() > 0;

		if (hasCheckedItems && mActionMode == null)
			// there are some selected items, start the actionMode
			mActionMode = getActivity().startActionMode(new ActionModeCallback(activity));
		else if (!hasCheckedItems && mActionMode != null)
			// there no selected items, finish the actionMode
			mActionMode.finish();

		if (mActionMode != null)
			mActionMode.setTitle(String.valueOf(activity.listAdapter.getSelectedCount()) + " selected");
	}

	private class ActionModeCallback implements ActionMode.Callback {

		private ViewPagerActivity activity;

		public ActionModeCallback(ViewPagerActivity activity) {
			this.activity = activity;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.getMenuInflater().inflate(R.menu.context_menu, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			switch (item.getItemId()) {
			case R.id.menu_delete:
				SparseBooleanArray selected = activity.listAdapter.getSelectedIds();
				for (int i = (selected.size() - 1); i >= 0; i--) {
					if (selected.valueAt(i)) {
						FileItem selectedItem = (FileItem) activity.listAdapter.getItem(selected.keyAt(i));
						activity.listAdapter.remove(selectedItem);
					}
				}
				mode.finish();
				return true;

			case R.id.menu_select_all:
				activity.listAdapter.selectAll();
			default:
				return false;
			}

		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			activity.listAdapter.removeSelection();
			getActivity().invalidateOptionsMenu();
			mActionMode = null;
		}
	}

}
