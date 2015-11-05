package com.ypyg.shopmanager.activity.shop;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.BaseActivity;
import com.ypyg.shopmanager.event.BaseEvent;
import com.ypyg.shopmanager.view.kenburnsview.AlphaForegroundColorSpan;
import com.ypyg.shopmanager.view.kenburnsview.KenBurnsView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

public class ActivityShopManager extends BaseActivity {
	private String imageTag = "ActivityShopManager";
	private int mActionBarTitleColor;
	private int mActionBarHeight;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;
	private ListView mListView;
	private KenBurnsView mHeaderPicture;
	private ImageView mHeaderLogo;
	private View mHeader;
	private View mPlaceHolderView;
	private AccelerateDecelerateInterpolator mSmoothInterpolator;

	private RectF mRect1 = new RectF();
	private RectF mRect2 = new RectF();

	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	private SpannableString mSpannableString;

	private TypedValue mTypedValue = new TypedValue();
	public static int IMAGEVIEWS_LENGTH = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSmoothInterpolator = new AccelerateDecelerateInterpolator();
		mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
		mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();

		setContentView(R.layout.activity_shop_manager);

		mListView = (ListView) findViewById(R.id.listview);
		mHeader = findViewById(R.id.header);
		mHeaderPicture = (KenBurnsView) findViewById(R.id.header_picture);
		mHeaderPicture.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.picture1));
		// mHeaderPicture.setResourceIds(R.drawable.picture0,R.drawable.picture1);

		mHeaderLogo = (ImageView) findViewById(R.id.header_logo);

		mActionBarTitleColor = getResources().getColor(R.color.white);

		mSpannableString = new SpannableString("店铺名称");
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(mActionBarTitleColor);

		setupActionBar();
		setupListView();

	}

	private void setupListView() {
		mPlaceHolderView = getLayoutInflater().inflate(R.layout.view_header_placeholder, mListView, false);
		mListView.addHeaderView(mPlaceHolderView);
		mListView.setAdapter(new ShopManagerAdapter(mContext, imageTag));
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int scrollY = getScrollY();
				// sticky actionbar
				mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
				// header_logo --> actionbar icon
				float ratio = clamp(mHeader.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
				interpolate(mHeaderLogo, getActionBarIconView(), mSmoothInterpolator.getInterpolation(ratio));
				// actionbar title alpha
				// getActionBarTitleView().setAlpha(clamp(5.0F * ratio - 4.0F,
				// 0.0F, 1.0F));
				// ---------------------------------
				// better way thanks to @cyrilmottier
				setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
			}
		});
	}

	private void setTitleAlpha(float alpha) {
		mAlphaForegroundColorSpan.setAlpha(alpha);
		mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getActionBar().setTitle(mSpannableString);
	}

	public static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(value, max));
	}

	private void interpolate(View view1, View view2, float interpolation) {
		getOnScreenRect(mRect1, view1);
		getOnScreenRect(mRect2, view2);

		float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
		float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
		float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
		float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

		view1.setTranslationX(translationX);
		view1.setTranslationY(translationY - mHeader.getTranslationY());
		view1.setScaleX(scaleX);
		view1.setScaleY(scaleY);
	}

	private RectF getOnScreenRect(RectF rect, View view) {
		rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
		return rect;
	}

	public int getScrollY() {
		View c = mListView.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = mListView.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mPlaceHolderView.getHeight();
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	@SuppressLint("NewApi")
	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.ic_transparent);
		actionBar.setDisplayHomeAsUpEnabled(true);
		if (android.os.Build.VERSION.SDK_INT >= 18) {
			actionBar.setHomeAsUpIndicator(R.drawable.back_unpressed_ico);
		} else {
			ChangeActionBarHomeUpDrawable(this, R.drawable.back_unpressed_ico);
		}

		// getActionBarTitleView().setAlpha(0f);
	}

	public void back(View v) {
		this.finish();
	}

	protected void onEventMainThread(BaseEvent event) {

	}

	private ImageView getActionBarIconView() {
		return (ImageView) findViewById(android.R.id.home);
	}

	public int getActionBarHeight() {
		if (mActionBarHeight != 0) {
			return mActionBarHeight;
		}
		getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
		mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());
		return mActionBarHeight;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:// 点击返回图标事件
			this.finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * android改变 actionbar的 返回箭头的方法 rid 图片id
	 */
	public void ChangeActionBarHomeUpDrawable(Activity activity, int rid) {
		Drawable homeUp = activity.getResources().getDrawable(rid);
		final View home = activity.findViewById(android.R.id.home);
		if (home == null) {
			// Action bar doesn't have a known configuration, an OEM messed with
			// things.
			return;
		}

		final ViewGroup parent = (ViewGroup) home.getParent();
		final int childCount = parent.getChildCount();
		if (childCount != 2) {
			// No idea which one will be the right one, an OEM messed with
			// things.
			return;
		}

		final View first = parent.getChildAt(0);
		final View second = parent.getChildAt(1);
		final View up = first.getId() == android.R.id.home ? second : first;

		if (up instanceof ImageView) {
			// Jackpot! (Probably...)
			ImageView upIndicatorView = (ImageView) up;
			upIndicatorView.setImageDrawable(homeUp);
		}
	}

	public void toEdit(View v) {
		switch (v.getId()) {
		case R.id.LinearLayout1:
			startActivity(new Intent(mContext, ActivityEditShopName.class));
			break;

		case R.id.LinearLayout2:

			break;
		case R.id.LinearLayout3:

			break;
		case R.id.LinearLayout4:

			break;
		case R.id.LinearLayout5:

			break;
		case R.id.LinearLayout6:

			break;
		case R.id.LinearLayout7:

			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		clearImgMemory();
	}

	/**
	 * 清空图片的内存
	 */
	private void clearImgMemory() {

		Drawable d = mHeaderPicture.getDrawable();
		if (d != null && d instanceof BitmapDrawable) {
			Bitmap bmp = ((BitmapDrawable) d).getBitmap();
			bmp.recycle();
			bmp = null;
		}
		mHeaderPicture.setImageBitmap(null);
		if (d != null) {
			d.setCallback(null);

		}
	}
}
