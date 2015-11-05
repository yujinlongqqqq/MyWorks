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
	// �˳���������
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
	 * ͼƬ���������
	 */
	public ImageCacheManager mImageCacheManager = null;
	public int searchReqCode = 20004;

	// ������
	protected Dialog loadingView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// FrontiaApplication.initFrontiaApplication(getApplicationContext());//
		// ��ʼ���ٶȷ���ģ��
		initExitAnimation();// ��ʼ����������
		mContext = this;
		setLoadingViewText("������");

		mDataCener = DataCener.getInstance();
		mImageCacheManager = ImageCacheManager.getInstance(mContext);
		mDiskLruCache = ImageCacheManager.getInstance(mContext).getmDiskLruCache();
		if (null == DataCener.getInstance().getDataService()) {
			mDataCener.initDataCenter();
		}
		mDataService = mDataCener.getDataService();
		// ����ǰactivity����ջ
		ActivityStack.push(this);
		BusProvider.get().register(this);

		XDISTANCE_MIN=ScreenUtils.getScreenWidth(this)/2;
	}

	/**
	 * �Զ���ȴ�����ʾ
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
			Toast.makeText(this, "��ܰ��ʾ ��ǰ������", 300).show();
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
				// ѡ����� �ٶ��ƶ�����ͳ��
				// StatService.onEvent(BaseActivity.this, "update",
				// "�Ը���",
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
	 * �رռ���
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
	// Toast.makeText(this, "���ȵ�¼", Toast.LENGTH_SHORT).show();
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
	 * ���ذ�ť
	 * 
	 * @param v
	 */
	public void back(View v) {
		this.finish();
	}

	protected boolean checkNetwork() {

		ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		// ����������ӣ������������ã��Ͳ���Ҫ��������������
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return true;
		}
		return false;

	}

	protected MyDialog dialog() {
		MyDialog dialog = new MyDialog(this, R.style.dialog_contact);
		dialog.setTitle("��ܰ��ʾ");
		dialog.setText("�ܱ�Ǹ�������ԣ��������翴����");
		dialog.setSubmit(null);
		dialog.setSubmitText("�õ�");
		dialog.setCancelGone();
		dialog.show();
		return dialog;
	}

	protected MyDialog dialog(String text, String submittext) {
		MyDialog dialog = new MyDialog(this, R.style.dialog_contact);
		dialog.setTitle("��ܰ��ʾ");
		dialog.setText(text);
		dialog.setSubmit(null);
		dialog.setSubmitText(submittext);
		dialog.setCancelGone();
		dialog.show();
		return dialog;
	}

	protected MyDialog dialog(String text, String submittext, String canceltext) {
		MyDialog dialog = new MyDialog(this, R.style.dialog_contact);
		dialog.setTitle("��ܰ��ʾ");
		dialog.setText(text);
		dialog.setSubmit(null);
		dialog.setSubmitText(submittext);
		dialog.setCancelText(canceltext);
		dialog.show();
		return dialog;
	}

	protected MyDialog dialog(String text) {
		MyDialog dialog = new MyDialog(this, R.style.dialog_contact);
		dialog.setTitle("��ܰ��ʾ");
		dialog.setText(text);
		dialog.setSubmit(null);
		dialog.setCancelGone();
		dialog.show();
		return dialog;
	}

	/**
	 * ��ʼ���˳�����
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
		overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);// ִ���˳�ҳ��ʱ�Ķ���
	}
	
	
	
	 //��ָ���»���ʱ����С�ٶ�
    private static final int YSPEED_MIN = 1000;

    //��ָ���һ���ʱ����С����
    private static int XDISTANCE_MIN = 500;

    //��ָ���ϻ����»�ʱ����С����
    private static final int YDISTANCE_MIN = 100;

    //��¼��ָ����ʱ�ĺ����ꡣ
    private float xDown;

    //��¼��ָ����ʱ�������ꡣ
    private float yDown;

    //��¼��ָ�ƶ�ʱ�ĺ����ꡣ
    private float xMove;

    //��¼��ָ�ƶ�ʱ�������ꡣ
    private float yMove;

    //���ڼ�����ָ�������ٶȡ�
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
               //�����ľ���
               int distanceX = (int) (xMove - xDown);
               int distanceY= (int) (yMove - yDown);
               //��ȡ˳ʱ�ٶ�
               int ySpeed = getScrollVelocity();
               //�ر�Activity����������������
               //1.x�Ử���ľ���>XDISTANCE_MIN
               //2.y�Ử���ľ�����YDISTANCE_MIN��Χ��
               //3.y���ϣ������»������ٶȣ�<XSPEED_MIN��������ڣ�����Ϊ�û���ͼ�������»��������󻬽���Activity
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
     * ����VelocityTracker���󣬲�����������Ļ����¼����뵽VelocityTracker���С�
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
     * ����VelocityTracker����
     */
   private void recycleVelocityTracker() {
         mVelocityTracker.recycle();
         mVelocityTracker = null;
   }

   /**
     *
     * @return �����ٶȣ���ÿ�����ƶ��˶�������ֵΪ��λ��
     */
   private int getScrollVelocity() {
         mVelocityTracker.computeCurrentVelocity(1000);
         int velocity = (int) mVelocityTracker.getYVelocity();
         return Math.abs(velocity);
   }
//	/**
//	 * ԭʼ������Ӧ
//	 * @param event
//	 */
//	protected void onEventMainThread(TestDataEvent event) {
//		Toast.makeText(mContext, event.getData(), 500).show();
//	}
}
