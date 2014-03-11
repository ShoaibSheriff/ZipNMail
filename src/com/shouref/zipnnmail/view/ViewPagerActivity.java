package com.shouref.zipnnmail.view;

import java.io.File;
import java.util.ArrayList;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.shouref.zipnnmail.Constants;
import com.shouref.zipnnmail.R;
import com.shouref.zipnnmail.adapter.AddFilesAlphabetizedAdapter;
import com.shouref.zipnnmail.adapter.FilesListAdapter;
import com.shouref.zipnnmail.adapter.ViewPagerAdapter;
import com.shouref.zipnnmail.data.FileItem;
import com.shouref.zipnnmail.utilities.LogUtil;

public class ViewPagerActivity extends FragmentActivity implements OnPageChangeListener, OnClickListener {

	// Main Tabs
	private ViewPager viewPager;
	private ViewPagerAdapter viewPagerAdapter;

	protected ArrayList<FileItem> files = new ArrayList<FileItem>();
	protected String[] fileNames;
	protected FilesListAdapter listAdapter;

	protected ArrayList<FileItem> filesToAdd = new ArrayList<FileItem>();
	public AddFilesAlphabetizedAdapter addFilesAdapter;

	public String zipName;
	public String zipPassword;

	private Uri zipUri;
	private String zipPath;

	private long last_back_press_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Crashlytics.start(this);

		if (isTablet())
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		else
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_view_pager);

		viewPager = (ViewPager) findViewById(R.id.viewPager_main);
		viewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setOnPageChangeListener(this);

		int mode = getMode();
		viewPager.setCurrentItem(mode);
		initActionBar(mode);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private int getMode() {
		String action = getIntent().getAction();
		String type = getIntent().getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			return 1;

		} else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
			return 1;

		} else if (Intent.ACTION_MAIN.equals(action)) {
			return 1;
		}

		return 1;
	}

	@Override
	public void onBackPressed() {
		long current_time = 0;

		switch (viewPager.getCurrentItem()) {
		case 0:
			viewPager.setCurrentItem(1, true);
			break;
		case 1:
			current_time = System.currentTimeMillis();
			if (current_time - last_back_press_time > Constants.ExitTimer.TIMER_DURATION) {
				Toast.makeText(this, R.string.exit_message, Toast.LENGTH_SHORT).show();
				last_back_press_time = System.currentTimeMillis();
				return;
			} else {
				finish();
			}
			break;
		case 2:
			viewPager.setCurrentItem(1, true);
			break;
		}

	}

	public boolean onPrepareOptionsMenu(Menu menu) {

		menu.clear();
		switch (viewPager.getCurrentItem()) {
		case 0:
			getMenuInflater().inflate(R.menu.add_file, menu);
			break;

		case 1:
			getMenuInflater().inflate(R.menu.files_list, menu);
			if (viewPagerAdapter.getItem(1) instanceof FilesListFragment)
				if (((FilesListFragment) viewPagerAdapter.getItem(1)).isFileListEmpty(this))
					menu.getItem(2).setEnabled(false);
				else
					menu.getItem(2).setEnabled(true);
			break;
		case 2:
			getMenuInflater().inflate(R.menu.create_zip, menu);
			if (zipPath != null && new File(zipPath).exists()) {
				menu.getItem(0).setEnabled(true);
				setProgressBarIndeterminateVisibility(false);
			} else
				menu.getItem(0).setEnabled(false);
			System.out.println("To allow menu :" + (zipUri != null && new File(zipPath).exists()));
			break;
		}

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.files_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			viewPager.setCurrentItem(1, true);
			break;
		case R.id.action_add_selected:
			viewPager.setCurrentItem(1, true);
			break;

		case R.id.action_next:
			viewPager.setCurrentItem(2, true);
			break;

		case R.id.action_add_files:
			viewPager.setCurrentItem(0, true);
			break;
		case R.id.action_attach_to_mail:
			launchMailClientChooser(zipUri);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void initActionBar(int position) {
		final ActionBar actionBar = getActionBar();
		switch (position) {
		case 0:
			getActionBar().setSubtitle("Add files To zip");
			if (actionBar.getTabCount() > 2) {
				actionBar.removeTabAt(3);
				actionBar.removeTabAt(2);
			}
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.setDisplayHomeAsUpEnabled(false);
			break;
		case 1:
			getActionBar().setSubtitle("Files To zip");
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			actionBar.setDisplayHomeAsUpEnabled(false);
			break;
		case 2:
			getActionBar().setSubtitle("Create Zip");
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			actionBar.setDisplayHomeAsUpEnabled(true);
			break;
		}

		setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		invalidateOptionsMenu();

		final ActionBar actionBar = getActionBar();
		switch (position) {
		case 0:
			getActionBar().setSubtitle("Add files To zip");
			if (actionBar.getTabCount() > 2) {
				for (int idx = actionBar.getTabCount() - 1; idx >= 2; idx--)
					actionBar.removeTabAt(idx);
			}
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.setDisplayHomeAsUpEnabled(true);
			break;
		case 1:
			getActionBar().setSubtitle("Files To zip");
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			actionBar.setDisplayHomeAsUpEnabled(false);
			break;
		case 2:
			getActionBar().setSubtitle("Create Zip");
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			actionBar.setDisplayHomeAsUpEnabled(true);
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case android.R.id.empty:
			viewPager.setCurrentItem(0, true);
			break;
		case R.id.btn_create_zip:
			setProgressBarIndeterminateVisibility(true);
			zipPath = createZip(zipName, zipPassword);
			zipUri = getFileUri(zipPath);
			invalidateOptionsMenu();
			break;
		}

	}

	void handleSendImage(Intent intent) {
		Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (imageUri != null) {
			System.out.println("Uri : " + imageUri);
			File file = new File(getRealPathFromURI(imageUri));

			System.out.println("File path : " + file);
			if (file.exists()) {
				FileItem li = new FileItem(file);
				System.out.println("List item : " + li);

				listAdapter.addItem(li);
			}
		}

	}

	void handleSendMultipleImages(Intent intent) {
		ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
		for (Uri uri : imageUris) {
			if (uri != null) {
				File file = new File(getRealPathFromURI(uri));
				if (file.exists()) {
					FileItem li = new FileItem(file);
					listAdapter.addItem(li);
				}
			}
		}
	}

	public String getRealPathFromURI(Uri contentUri) {

		if (!contentUri.toString().contains("content"))
			return contentUri.getPath();

		String[] proj = { MediaStore.Files.FileColumns.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public ArrayList<FileItem> getAllFiles(Context context) {

		ArrayList<FileItem> files = new ArrayList<FileItem>();

		ContentResolver cr = context.getContentResolver();

		Uri uri = MediaStore.Files.getContentUri("external");

		String[] projection = { MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.DATA };
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = " lower(" + MediaStore.Files.FileColumns.DISPLAY_NAME + ") ASC ";

		Cursor cursorAllFiles = cr.query(uri, projection, selection, selectionArgs, sortOrder);

		if (cursorAllFiles != null) {
			while (cursorAllFiles.moveToNext()) {

				String fileName = cursorAllFiles.getString(cursorAllFiles.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
				double fileSize = cursorAllFiles.getDouble(cursorAllFiles.getColumnIndex(MediaStore.Files.FileColumns.SIZE));
				String filePath = cursorAllFiles.getString(cursorAllFiles.getColumnIndex(MediaStore.Files.FileColumns.DATA));

				if (fileName != null) {
					FileItem li = new FileItem(fileName, fileSize, filePath);
					files.add(li);
				}
			}
		} else {
			Log.w("Encrypt It", "Files Cursor null");
		}

		return files;
	}

	public String createZip(String zipName, String zipPassword) {
		File file;

		if (zipName.length() > 0)
			file = new File(getExternalCacheDir(), zipName);
		else
			file = new File(getExternalCacheDir(), "Zip_" + String.valueOf(System.currentTimeMillis()));

		if (file.exists())
			file.delete();

		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(file);
			ArrayList<File> filesToAdd = new ArrayList<File>();
			for (FileItem li : files)
				filesToAdd.add(new File(li.getFilePath()));

			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

			if (zipPassword != null && zipPassword.length() > 0) {
				parameters.setEncryptFiles(true);
				parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
				parameters.setPassword(zipPassword);
			}

			zipFile.createZipFile(filesToAdd, parameters);
		} catch (ZipException e) {
			e.printStackTrace();
			Crashlytics.logException(e);
		} finally {
			if (zipFile != null && zipFile.isValidZipFile()) {
				LogUtil.debug("Zip created at", zipFile.getFile().getAbsolutePath());
				invalidateOptionsMenu();
				return zipFile.getFile().getAbsolutePath();
			}

		}
		return null;

	}

	private Uri getFileUri(String zipPath) {
		LogUtil.debug("Zip Uri", "file://" + zipPath.replaceAll("sdcard0", "sdcard").replaceAll("/storage", ""));
		return Uri.parse("file://" + zipPath.replaceAll("sdcard0", "sdcard").replaceAll("/storage", ""));

	}

	public void initializeSearch() {
		filesToAdd = getAllFiles(this);

		String[] columns = new String[] { "_id", "Files" };
		MatrixCursor matrixCursor = new MatrixCursor(columns);

		for (int i = 0; i < filesToAdd.size(); i++)
			matrixCursor.addRow(new Object[] { i, filesToAdd.get(i).getFileName() });

		addFilesAdapter = new AddFilesAlphabetizedAdapter(this, android.R.layout.simple_list_item_1, matrixCursor,
				new String[] { "Files" }, new int[] { android.R.id.text1 });

		setProgressBarIndeterminateVisibility(false);
	}

	public boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		return (xlarge || large);
	}

	private boolean isTablet() {
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);

		int width = displayMetrics.widthPixels / displayMetrics.densityDpi;
		int height = displayMetrics.heightPixels / displayMetrics.densityDpi;

		double screenDiagonal = Math.sqrt(width * width + height * height);
		System.out.println(screenDiagonal);
		return (screenDiagonal >= 5.0);
	}

	private void launchMailClientChooser(Uri zipFileUri) {
		String recepientEmail = "";
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setData(Uri.parse("mailto:" + recepientEmail));
		intent.putExtra(Intent.EXTRA_STREAM, zipFileUri);
		startActivity(intent);
	}

}
