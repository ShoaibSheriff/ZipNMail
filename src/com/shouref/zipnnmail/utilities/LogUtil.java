package com.shouref.zipnnmail.utilities;

import com.shouref.zipnnmail.Constants;

import android.util.Log;

public class LogUtil {

	private static boolean printLog = Constants.Log.PRINT_LOG;

	public static void inform(String field, String value) {
		if (printLog)
			Log.i(field, value);
	}

	public static void debug(String field, String value) {
		if (printLog)
			Log.d(field, value);
	}
}
