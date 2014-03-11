package com.shouref.zipnnmail.data;

import java.io.File;
import java.io.Serializable;

import org.apache.commons.io.FilenameUtils;

import com.shouref.zipnnmail.Constants;

public class FileItem implements Serializable {

	private static final long serialVersionUID = -5179198239311155493L;

	public static String[] sizeUnits = { Constants.FileSizeUnits.BYTES, Constants.FileSizeUnits.KILO_BYTES, Constants.FileSizeUnits.MEGA_BYTES,
			Constants.FileSizeUnits.GIGA_BYTES };

	private String fileName;
	private double fileSize;
	private String fileSizeUnit = sizeUnits[0];
	private String filePath;

	public FileItem(File file) {
		super();
		this.setFile(file);
	}

	public FileItem(String fileName, double fileSize, String filePath) {
		this.fileName = fileName;
		this.fileSize = getNormalizedFileSize(fileSize);
		this.fileSizeUnit = getFileSizeUnit(fileSize);
		this.filePath = filePath;
	}

	private void setFile(File file) {
		this.fileName = file.getName();
		this.fileSize = getNormalizedFileSize(file.length());
		this.fileSizeUnit = getFileSizeUnit(file.length());
		this.filePath = file.getAbsolutePath();
	}

	public String getFileSizeUnit() {
		return fileSizeUnit;
	}

	public String getFileName() {
		return fileName;
	}

	public double getFileSize() {
		return fileSize;
	}

	public String getFormattedFileSize() {
		return String.format("%.2f", getFileSize()) + getFileSizeUnit();
	}

	public String getFilePath() {
		return filePath;
	}

	public String getFileFolder() {
		return getFilePath().replaceAll(getFileName(), "");
	}

	public String getFileExtension() {
		return FilenameUtils.getExtension(getFileName());
	}

	private double getNormalizedFileSize(double fileSize) {
		while (fileSize > 1024.0) {
			fileSize /= 1024.0;
		}
		return fileSize;
	}

	private String getFileSizeUnit(double fileSize) {
		int fileSizeTypeCount = 0;
		while (fileSize > 1024.0) {
			fileSize /= 1024.0;
			fileSizeTypeCount++;
		}
		return sizeUnits[fileSizeTypeCount];
	}
}
