package com.ypyg.shopmanager.view.wheel.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SplashArrayWheelAdapter<T> extends AbstractWheelTextAdapter {

	// items
	private T items[];

	/**
	 * Constructor
	 * 
	 * @param context
	 *            the current context
	 * @param items
	 *            the items
	 */
	public SplashArrayWheelAdapter(Context context, T items[]) {
		super(context);

		// setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
		this.items = items;
	}

	@Override
	public CharSequence getItemText(int index) {
		if (index >= 0 && index < items.length) {
			T item = items[index];
			if (item instanceof CharSequence) {
				return (CharSequence) item;
			}
			return item.toString();
		}
		return null;
	}
	@Override
	public View getItem(int index, View convertView,
			android.view.ViewGroup parent) {
		if (index >= 0 && index < getItemsCount()) {
			if (convertView == null) {
				convertView = getView(itemResourceId, parent);
			}
			TextView textView = getTextView(convertView, itemTextResourceId);
			if (textView != null) {
				CharSequence text = getItemText(index);
				if (text == null) {
					text = "";
				}
				textView.setText(text);
				configureTextView(textView, index);
			}
			return convertView;
		}
		return null;
	};
	@Override
	public int getItemsCount() {
		return items.length;
	}
	/**
	 * Loads view from resources
	 * 
	 * @param resource
	 *            the resource Id
	 * @return the loaded view or null if resource is not set
	 */
	private View getView(int resource, ViewGroup parent) {
		switch (resource) {
			case NO_RESOURCE :
				return null;
			case TEXT_VIEW_ITEM_RESOURCE :
				return new TextView(context);
			default :
				return inflater.inflate(resource, parent, false);
		}
	}
	private int textColor = Color.WHITE;
	private int textSize = DEFAULT_TEXT_SIZE;
	private Typeface textTypeface = null;
	/**
	 * Configures text view. Is called for the TEXT_VIEW_ITEM_RESOURCE views.
	 * 
	 * @param view
	 *            the text view to be configured
	 */
	protected void configureTextView(TextView view, int index) {
		if (itemResourceId == TEXT_VIEW_ITEM_RESOURCE) {
			view.setTextColor(textColor);
			view.setGravity(Gravity.CENTER);
			if (index == items.length / 2) {
				view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
			} else {
				view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
			}
			view.setLines(1);
		}
		if (textTypeface != null) {
			view.setTypeface(textTypeface);
		} else {
			// view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		}
	}
	public void setTypeface(Typeface typeface) {
		textTypeface = typeface;
	}
	/**
	 * Loads a text view from view
	 * 
	 * @param view
	 *            the text view or layout containing it
	 * @param textResource
	 *            the text resource Id in layout
	 * @return the loaded text view
	 */
	private TextView getTextView(View view, int textResource) {
		TextView text = null;
		try {
			if (textResource == NO_RESOURCE && view instanceof TextView) {
				text = (TextView) view;
			} else if (textResource != NO_RESOURCE) {
				text = (TextView) view.findViewById(textResource);
			}
		} catch (ClassCastException e) {
			Log.e("AbstractWheelAdapter",
					"You must supply a resource ID for a TextView");
			throw new IllegalStateException(
					"AbstractWheelAdapter requires the resource ID to be a TextView",
					e);
		}

		return text;
	}
}
