package me.tangke.navigationbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

/**
 * 
 * @author Tank
 *
 */
public class NavigationBarButton extends NavigationBarItem {
	private ColorFilter mColorFilter;

	TextView text;

	NavigationBarButton(Context context, int id, TextView view, int gravity) {
		super(context, id, view, gravity);
		text = view;

		final Theme theme = context.getTheme();
		final Resources resources = context.getResources();
		TypedValue value = new TypedValue();

		theme.resolveAttribute(R.attr.colorPrimary, value, true);
		if (TypedValue.TYPE_REFERENCE == value.type) {
			mColorFilter = new PorterDuffColorFilter(
					resources.getColor(value.resourceId), Mode.SRC_IN);
		} else {
			mColorFilter = new PorterDuffColorFilter(value.data, Mode.SRC_IN);
		}
	}

	@Override
	public void setIcon(Drawable icon) {
		if (this.icon == icon) {
			return;
		}

		if (null != this.icon) {
//			this.icon.setColorFilter(null);
		}
		this.icon = icon;

		if (null != icon) {
//			icon.setColorFilter(mColorFilter);
		}

		Drawable left = null, right = null, top = null, bottom = null;
		switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
		case Gravity.LEFT:
			left = icon;
			break;
		case Gravity.RIGHT:
			right = icon;
			break;
		}

		switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
		case Gravity.TOP:
			top = icon;
			break;
		case Gravity.BOTTOM:
			bottom = icon;
			break;
		}

		text.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		text.setText(title);
	}
}
