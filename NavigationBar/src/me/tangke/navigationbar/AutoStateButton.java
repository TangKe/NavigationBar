package me.tangke.navigationbar;

import java.util.Arrays;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 在点按状态自动调整自身透明度的{@link Button}
 * 
 * @author Tank
 * 
 */
class AutoStateButton extends Button {
	private AlphaTransformAnimation mAlphaAnimation = new AlphaTransformAnimation(
			1.0f);;
	private float mPressedAlpha;

	public AutoStateButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.AutoStateButton, defStyleAttr, 0);
		mPressedAlpha = a.getFloat(R.styleable.AutoStateButton_pressedAplha,
				0.5f);
		a.recycle();

		setAnimation(mAlphaAnimation);
	}

	public AutoStateButton(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.autoStateButtonStyle);
	}

	public AutoStateButton(Context context) {
		this(context, null);
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		int[] state = super.onCreateDrawableState(extraSpace);

		if (null == mAlphaAnimation) {
			return state;
		}
		if (0 > Arrays.binarySearch(state, android.R.attr.state_pressed)) {
			mAlphaAnimation.setAlpha(255);
		} else {
			mAlphaAnimation.setAlpha(mPressedAlpha);
		}
		ViewGroup parent = (ViewGroup) getParent();
		if (null != parent) {
			parent.invalidate();
		}
		return state;
	}
}
