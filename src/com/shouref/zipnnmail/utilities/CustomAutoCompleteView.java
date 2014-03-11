package com.shouref.zipnnmail.utilities;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * TODO: document your custom view class.
 */
public class CustomAutoCompleteView extends AutoCompleteTextView {

	public CustomAutoCompleteView(Context context) {
		super(context);
	}

	public CustomAutoCompleteView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomAutoCompleteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean enoughToFilter() {
		return true;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		if (focused && getText().length() > 0) {
			performFiltering(getText(), 0);
		}
	}

}
