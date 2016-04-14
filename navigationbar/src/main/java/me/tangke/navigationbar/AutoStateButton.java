package me.tangke.navigationbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Arrays;

/**
 * Add pressed state to Button, when user pressed on this Button, this Button will become
 * transparent
 *
 * @author Tank
 */
class AutoStateButton extends Button {
    private AlphaTransformAnimation mAlphaAnimation = new AlphaTransformAnimation(
            1.0f);
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
            mAlphaAnimation.setAlpha(1.0f);
        } else {
            mAlphaAnimation.setAlpha(mPressedAlpha);
        }
        ViewGroup parent = (ViewGroup) getParent();
        if (null != parent) {
            parent.invalidate();
        }
        return state;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            setAnimation(mAlphaAnimation);
        } else {
            clearAnimation();
        }
    }

    /**
     * Set the alpha when pressed
     *
     * @param alpha from 0 to 1, 0 means full transparent, 1 means opaque
     */
    public void setPressedAlpha(float alpha) {
        mPressedAlpha = alpha;
        invalidate();
    }

    public float getPressedAlpha() {
        return mPressedAlpha;
    }
}
