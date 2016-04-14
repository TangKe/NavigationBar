package me.tangke.navigationbar;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Use to set the alpha of view below api level 11
 *
 * @author Tank
 */
class AlphaTransformAnimation extends Animation {
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
