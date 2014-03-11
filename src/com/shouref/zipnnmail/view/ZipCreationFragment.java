package com.shouref.zipnnmail.view;

import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.shouref.zipnnmail.R;
import com.shouref.zipnnmail.data.FileItem;
import com.shouref.zipnnmail.utilities.CustomAutoCompleteView;

public class ZipCreationFragment extends Fragment implements OnTouchListener {

	private AutoCompleteTextView et_zipName;
	private EditText et_zipPassword;
	private CheckBox cb_showPassword;

	private String[] fileNames;

	public static Fragment newInstance(Context context) {
		ZipCreationFragment f = new ZipCreationFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_zip_creation, null);

		final ViewPagerActivity activity = (ViewPagerActivity) getActivity();
		fileNames = getNameSuggestions(activity.files);

		et_zipName = (AutoCompleteTextView) root.findViewById(R.id.et_zip_name);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, fileNames);
		et_zipName.setAdapter(adapter);
		et_zipName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				activity.zipName = s.toString();
			}
		});

		et_zipPassword = (EditText) root.findViewById(R.id.et_password);
		et_zipPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				activity.zipPassword = s.toString();
			}
		});

		cb_showPassword = (CheckBox) root.findViewById(R.id.cb_showPassword);
		cb_showPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1)
					et_zipPassword.setInputType(InputType.TYPE_CLASS_TEXT);
				else
					et_zipPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

				et_zipPassword.setSelection(et_zipPassword.getText().length());

			}
		});
		return root;
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (menuVisible) {
			if (et_zipName != null && et_zipName.getText().length() == 0)
				et_zipName.showDropDown();
		}
	}

	private String[] getNameSuggestions(ArrayList<FileItem> files) {
		if (null != files) {
			String[] fileNames = new String[files.size()];

			for (int i = 0; i < fileNames.length; i++) {
				String realName = files.get(i).getFileName();
				fileNames[i] = FilenameUtils.removeExtension(realName) + ".zip";

			}

			return fileNames;
		}

		return null;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v instanceof CustomAutoCompleteView)
			((CustomAutoCompleteView) v).showDropDown();
		return false;
	}
}
