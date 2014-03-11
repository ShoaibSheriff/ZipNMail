package com.shouref.zipnnmail.utilities;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {

	private static AnimationUtil AnimationUtil;

	public static AnimationUtil getInstance() {
		if (null == AnimationUtil)
			AnimationUtil = new AnimationUtil();

		return AnimationUtil;
	}

	private AnimationUtil() {
	}

	public Animation createTranslateAnimation(long offset) {
		TranslateAnimation animation = new TranslateAnimation(100, 0, 0, 0);
		animation.setDuration(100);
		animation.setStartOffset(offset);
		return animation;

	}
}
