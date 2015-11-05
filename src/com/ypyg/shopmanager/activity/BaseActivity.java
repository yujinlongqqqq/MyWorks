package com.ypyg.shopmanager.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.DataService;
import com.ypyg.shopmanager.common.ImageCacheManager;
import com.ypyg.shopmanager.event.BusProvider;
import com.ypyg.shopmanager.event.TestDataEvent;
import com.ypyg.shopmanager.libcore.io.DiskLruCache;
import com.ypyg.shopmanager.util.ScreenUtils;
import com.ypyg.shopmanager.view.dialog.MyDialog;
import com.ypyg.shopmanager.view.loadingview.DialogUtil;

public class BaseActivity extends Activity {
	// 退出动画变量
	protected int activityCloseEnterAnimation;
	protected int activityCloseExitAnimation;

	private String mApkInstallPath = null;
	private AlertDialog mSelfUpdateDialog = null;
	protected Context mContext = null;
	public DataCener mDataCener = null;
	public DataService mDataService = null;
	public DiskLruCache mDiskLruCache = null;
	protected int lastPullUpOrDown = 0;
	protected int UP = 3211;
	protected int DOWN = 3223;
	/**
	 * 图片缓存管理类
	 */
	public ImageCacheManager mImageCacheManager = null;
	public int searchReqCode = 20004;

	// 加载中
	protected Dialog loadingView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// FrontiaApplication.initFrontiaApplication(getApplicationContext());//
		// 初始化百度分享模块
		initExitAnimation();// 初始化动画变量
		mContext = this;
		setLoadingViewText("加载中");

		mDataCener = DataCener.getInstance();
		mImageCacheManager = ImageCacheManager.getInstance(mContext);
		mDiskLruCache = ImageCacheManager.getInstance(mContext).getmDiskLruCache();
		if (null == DataCener.getInstance().getDataService()) {
			mDataCener.initDataCenter();
		}
		mDataService = mDataCener.getDataService();
		// 将当前activity推入栈
		ActivityStack.push(this);
		BusProvider.get().register(this);

		XDISTANCE_MIN=ScreenUtils.getScreenWidth(this)/2;
	}

	/**
	 * 自定义等待框提示
	 * 
	 * @param text
	 */
	protected void setLoadingViewText(String text) {
		loadingView = DialogUtil.createLoadingDialog(mContext, text);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// if (AppUtil.isNull(DataCener.getInstance())
		// || AppUtil.isNull(DataCener.getInstance().getmTBasicInfoBean())) {
		// Intent intent = new Intent(Intent.ACTION_MAIN);
		// ComponentName componentName = new ComponentName(
		// "com.hzganggangparents",
		// "com.hzganggangparents.activity.start.ActivitySplash");
		// intent.setComponent(componentName);
		// startActivity(intent);
		// }

		if (checkNetwork()) {
			Toast.makeText(this, "温馨提示 当前无网络", 300).show();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != mImageCacheManager)
			mImageCacheManager.fluchCache();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		View currentFocus = BaseActivity.this.getCurrentFocus();
		if (null != currentFocus) {
			imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityStack.pop(this);
		BusProvider.get().unregister(this);
		// if (null != mSelfUpdateDialog)
		// mSelfUpdateDialog.dismiss();
	}

	public void exit() {
		ActivityStack.finishAll();
	}

	protected void ShowUpdatedialog() {
		AlertDialog.Builder builder = new Builder(BaseActivity.this);
		builder.setTitle(R.string.update);
		builder.setMessage(R.string.update_content);

		builder.setPositiveButton(getString(R.string.update), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				AppUtil.installApk(mApkInstallPath, BaseActivity.this.getApplicationContext());
				// 选择更新 百度移动数据统计
				// StatService.onEvent(BaseActivity.this, "update",
				// "自更新",
				// 1);
			}
		});

		builder.setNegativeButton(getString(R.string.cancel), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		mSelfUpdateDialog = builder.create();
		mSelfUpdateDialog.show();
	}

	/**
	 * 关闭键盘
	 */
	public void KeyBoardCancle() {

		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	private boolean have_login = false;

	// protected void onEventMainThread(BaseEvent event) {
	// if (null == event) {
	// return;
	// }
	// if (!have_login && 1100 == event.getCode()) {
	// Intent intent = new Intent(this, ActivityLogin.class);
	// startActivity(intent);
	// Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
	// have_login = true;
	// }
	// }

	// protected void onEventMainThread(SelfUpdateEvent aEvent) {
	// if (aEvent.getCode() == IRespCode.SUCCESS) {
	// mApkInstallPath = aEvent.getApkpath();
	// ShowUpdatedialog();
	// }
	// }

	/**
	 * 返回按钮
	 * 
	 * @param v
	 */
	public void back(View v) {
		this.finish();
	}

	protected boolean checkNetwork() {

		ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		// 检查网络连接，如果无网络可用，就不需要进行连网操作等
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return true;
		}
		return false;

	}

	protected MyDialog dialog() {
		MyDialog dialog = new MyDialog(this, R.style.dialog_contact);
		dialog.setTitle("温馨提示");
		dialog.setText("很抱歉，请重试，或检查网络看看。");
		dialog.setSubmit(null);
		dialog.setSubmitText("好的");
		dialog.setCancelGone();
		dialog.show();
		return dialog;
	}

	protected MyDialog dialog(String text, String submittext) {
		MyDialog dialog = new MyDialog(this, R.style.dialog_contact);
		dialog.setTitle("温馨提示");
		dialog.setText(text);
		dialog.setSubmit(null);
		dialog.setSubmitText(submittext);
		dialog.setCancelGone();
		dialog.show();
		return dialog;
	}

	protected MyDialog dialog(String text, String submittext, String canceltext) {
		MyDialog dialog = new MyDialog(this, R.style.dialog_contact);
		dialog.setTitle("温馨提示");
		dialog.setText(text);
		dialog.setSubmit(null);
		dialog.setSubmitText(submittext);
		dialog.setCancelText(canceltext);
		dialog.show();
		return dialog;
	}

	protected MyDialog dialog(String text) {
		MyDialog dialog = new MyDialog(this, R.style.dialog_contact);
		dialog.setTitle("温馨提示");
		dialog.setText(text);
		dialog.setSubmit(null);
		dialog.setCancelGone();
		dialog.show();
		return dialog;
	}

	/**
	 * 初始化退出动画
	 */
	protected void initExitAnimation() {
		TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[] { android.R.attr.windowAnimationStyle });
		int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
		activityStyle.recycle();
		activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[] { android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation });
		activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
		activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
		activityStyle.recycle();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);// 执行退出页面时的动画
	}
	
	
	
	 //手指上下滑动时的最小速度
    private static final int YSPEED_MIN = 1000;

    //手指向右滑动时的最小距离
    private static int XDISTANCE_MIN = 500;

    //手指向上滑或下滑时的最小距离
    private static final int YDISTANCE_MIN = 100;

    //记录手指按下时的横坐标。
    private float xDown;

    //记录手指按下时的纵坐标。
    private float yDown;

    //记录手指移动时的横坐标。
    private float xMove;

    //记录手指移动时的纵坐标。
    private float yMove;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
         createVelocityTracker(event);
         switch (event.getAction()) {
         case MotionEvent.ACTION_DOWN:
               xDown = event.getRawX();
               yDown = event.getRawY();
               break;
         case MotionEvent.ACTION_MOVE:
               xMove = event.getRawX();
               yMove= event.getRawY();
               //滑动的距离
               int distanceX = (int) (xMove - xDown);
               int distanceY= (int) (yMove - yDown);
               //获取顺时速度
               int ySpeed = getScrollVelocity();
               //关闭Activity需满足以下条件：
               //1.x轴滑动的距离>XDISTANCE_MIN
               //2.y轴滑动的距离在YDISTANCE_MIN范围内
               //3.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
               if(distanceX > XDISTANCE_MIN &&(distanceY<YDISTANCE_MIN&&distanceY>-YDISTANCE_MIN)&& ySpeed < YSPEED_MIN) {
                     finish();
               }
               break;
         case MotionEvent.ACTION_UP:
               recycleVelocityTracker();
               break;
         default:
               break;
         }
         return super.dispatchTouchEvent(event);
   }

   /**
     * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     *
     */
   private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
               mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
   }

   /**
     * 回收VelocityTracker对象。
     */
   private void recycleVelocityTracker() {
         mVelocityTracker.recycle();
         mVelocityTracker = null;
   }

   /**
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
   private int getScrollVelocity() {
         mVelocityTracker.computeCurrentVelocity(1000);
         int velocity = (int) mVelocityTracker.getYVelocity();
         return Math.abs(velocity);
   }
//	/**
//	 * 原始数据响应
//	 * @param event
//	 */
//	protected void onEventMainThread(TestDataEvent event) {
//		Toast.makeText(mContext, event.getData(), 500).show();
//	}
}
