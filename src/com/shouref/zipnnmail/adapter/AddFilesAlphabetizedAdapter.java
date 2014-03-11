package com.shouref.zipnnmail.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.shouref.zipnnmail.R;

public class AddFilesAlphabetizedAdapter extends SimpleCursorAdapter implements SectionIndexer {

	private static final int TYPE_HEADER = 1;
	private static final int TYPE_NORMAL = 0;

	private static final int TYPE_COUNT = 2;

	private Context context;
	private AlphabetIndexer indexer;

	private int[] usedSectionNumbers;

	private Map<Integer, Integer> sectionToOffset;
	private Map<Integer, Integer> sectionToPosition;

	public AddFilesAlphabetizedAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
		super(context, layout, c, from, to, 0);

		indexer = new AlphabetIndexer(c, c.getColumnIndexOrThrow("Files"), "0ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		this.context = context;
		sectionToPosition = new TreeMap<Integer, Integer>();
		sectionToOffset = new HashMap<Integer, Integer>();

		final int count = super.getCount();

		int i;
		for (i = count - 1; i >= 0; i--) {
			sectionToPosition.put(indexer.getSectionForPosition(i), i);
		}

		Log.i("KeySet Size : ", sectionToPosition.keySet().size() + "");

		i = 0;
		usedSectionNumbers = new int[sectionToPosition.keySet().size()];

		for (Integer section : sectionToPosition.keySet()) {
			sectionToOffset.put(section, i);
			usedSectionNumbers[i] = section;
			i++;
		}

		for (Integer section : sectionToPosition.keySet()) {
			sectionToPosition.put(section, sectionToPosition.get(section) + sectionToOffset.get(section));
		}

		System.out.println("Finished arrangening files");
	}

	@Override
	public int getCount() {
		if (super.getCount() != 0) {
			return super.getCount() + usedSectionNumbers.length;
		}

		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (getItemViewType(position) == TYPE_NORMAL) {
			return super.getItem(position - sectionToOffset.get(getSectionForPosition(position)) - 1);
		}

		return null;
	}

	public int getRealIndex(int position) {
		return position - sectionToOffset.get(getSectionForPosition(position)) - 1;
	}

	@Override
	public int getPositionForSection(int section) {
		if (!sectionToOffset.containsKey(section)) {
			int i = 0;
			int maxLength = usedSectionNumbers.length;

			while (i < maxLength && section > usedSectionNumbers[i]) {
				i++;
			}
			if (i == maxLength)
				return getCount();

			return indexer.getPositionForSection(usedSectionNumbers[i]) + sectionToOffset.get(usedSectionNumbers[i]);
		}

		return indexer.getPositionForSection(section) + sectionToOffset.get(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		int i = 0;
		int maxLength = usedSectionNumbers.length;

		while (i < maxLength && position >= sectionToPosition.get(usedSectionNumbers[i])) {
			i++;
		}
		return usedSectionNumbers[i - 1];
	}

	@Override
	public Object[] getSections() {

		String[] sections = new String[indexer.getSections().length];
		for (int i = 0; i < indexer.getSections().length; i++)
			if (indexer.getSections()[i].toString().equalsIgnoreCase("0"))
				sections[i] = "0-9";
			else
				sections[i] = indexer.getSections()[i].toString();

		return indexer.getSections();
	}

	@Override
	public int getItemViewType(int position) {
		if (position == getPositionForSection(getSectionForPosition(position))) {
			return TYPE_HEADER;
		}
		return TYPE_NORMAL;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	// return the header view, if it's in a section header position
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int type = getItemViewType(position);
		if (type == TYPE_HEADER) {
			if (convertView == null) {
				convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header, parent, false);
			}
			((TextView) convertView.findViewById(R.id.header)).setText((String) getSections()[getSectionForPosition(position)]);
			return convertView;
		}
		return super.getView(position - sectionToOffset.get(getSectionForPosition(position)) - 1, convertView, parent);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		if (getItemViewType(position) == TYPE_HEADER) {
			return false;
		}
		return true;
	}
}