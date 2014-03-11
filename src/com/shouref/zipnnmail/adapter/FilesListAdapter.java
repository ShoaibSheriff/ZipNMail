package com.shouref.zipnnmail.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shouref.zipnnmail.R;
import com.shouref.zipnnmail.data.FileItem;
import com.shouref.zipnnmail.utilities.FileIconUtil;

public class FilesListAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private ArrayList<FileItem> data;
	private FileIconUtil fileIconUtil;
	private SparseBooleanArray mSelectedItemsIds = new SparseBooleanArray();;
	private Typeface font;

	private Paint mPaint = new Paint();
	Rect bounds = new Rect();

	public FilesListAdapter(Context context, ArrayList<FileItem> data) {
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
		this.fileIconUtil = FileIconUtil.getInstance();

		this.font = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.path_font_file));
		this.mPaint.setTextSize(18);
		this.mPaint.setTypeface(font);
	}

	public void addItem(FileItem listItem) {
		this.data.add(listItem);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void removeItem(int position) {
		data.remove(position);
		notifyDataSetChanged();
	}

	public void remove(FileItem object) {
		data.remove(object);
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.layout_file_item, null);

			holder = new ViewHolder();
			holder.fileName = (TextView) convertView.findViewById(R.id.txt_file_name);
			holder.fileName.setTypeface(font);
			holder.fileSize = (TextView) convertView.findViewById(R.id.txt_file_size);
			holder.filePath = (TextView) convertView.findViewById(R.id.txt_file_path);
			holder.fileIcon = (ImageView) convertView.findViewById(R.id.img_file_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FileItem fi = (FileItem) getItem(position);
		holder.fileName.setHeight(getTextHeight(fi.getFileName()));
		holder.fileName.setText(fi.getFileName());
		holder.fileSize.setText(fi.getFormattedFileSize());
		holder.filePath.setText(fi.getFileFolder());
		holder.fileIcon.setImageResource(fileIconUtil.getIcon(fi.getFileExtension()));

		convertView.setBackgroundColor(mSelectedItemsIds.get(position) ? layoutInflater.getContext().getResources().getColor(R.color.focus_blue)
				: Color.TRANSPARENT);

		return convertView;
	}

	public void toggleSelection(int position) {
		selectView(position, !mSelectedItemsIds.get(position));
	}

	public void removeSelection() {
		mSelectedItemsIds = new SparseBooleanArray();
		notifyDataSetChanged();
	}

	public void selectView(int position, boolean value) {
		if (value)
			mSelectedItemsIds.put(position, value);
		else
			mSelectedItemsIds.delete(position);

		notifyDataSetChanged();
	}

	public int getSelectedCount() {
		return mSelectedItemsIds.size();
	}

	public SparseBooleanArray getSelectedIds() {
		return mSelectedItemsIds;
	}

	public void selectAll() {
		for (int i = 0; i < getCount(); i++)
			mSelectedItemsIds.put(i, true);
		notifyDataSetChanged();

	}

	private int getTextHeight(String text) {
		mPaint.getTextBounds(text, 0, text.length(), bounds);
		return bounds.height();
	}

	static class ViewHolder {
		private TextView fileName;
		private TextView fileSize;
		private TextView filePath;
		private ImageView fileIcon;
	}

}
