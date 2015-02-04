package me.tangke.navigationbar;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 用于兼容低版本设置view的alpha
 * 
 * @author Tank
 * 
 */
public class AlphaTransformAnimation extends Animation {
	private float mAlpha;

	public AlphaTransformAnimation(float alpha) {
		setDuration(0);
		setFillAfter(true);
		mAlpha = alpha;
	}

	public void setAlpha(float alpha) {
		mAlpha = alpha;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		t.setAlpha(mAlpha);
	}
}
