package com.shouref.zipnnmail.utilities;

import com.shouref.zipnnmail.Constants;
import com.shouref.zipnnmail.R;

public class FileIconUtil {

	private static FileIconUtil fileIconUtil;

	public static FileIconUtil getInstance() {
		if (null == fileIconUtil)
			fileIconUtil = new FileIconUtil();

		return fileIconUtil;
	}

	private FileIconUtil() {
	}

	public int getIcon(String fileExtension) {
		if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_MP3))
			return R.drawable.mp3;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_FLAC))
			return R.drawable.fla;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_JPEG))
			return R.drawable.jpg;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_TEXT))
			return R.drawable.txt;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_3GP))
			return R.drawable._3gp;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_AVI))
			return R.drawable.avi;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_MP4))
			return R.drawable.mp4;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_ZIP))
			return R.drawable.zip;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_ZIPX))
			return R.drawable.zip;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_RAR))
			return R.drawable.rar;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_DOC))
			return R.drawable.doc;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_DOCX))
			return R.drawable.docx;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_PPT))
			return R.drawable.ppt;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_PPTX))
			return R.drawable.pptx;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_PSD))
			return R.drawable.psd;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_PDF))
			return R.drawable.pdf;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_HTML))
			return R.drawable.html;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_XL))
			return R.drawable.xls;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_XLS))
			return R.drawable.xlsx;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_CSS))
			return R.drawable.cfm;
		else if (fileExtension
				.equalsIgnoreCase(Constants.FileExtensions.FILE_EXTENSION_DB))
			return R.drawable.sql;
		else
			return R.drawable.file;
	}
}
