package com.ypyg.shopmanager.activity.good;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.ypyg.shopmanager.R;
import com.ypyg.shopmanager.activity.BaseActivity;
import com.ypyg.shopmanager.bean.GoodParentsSortBean;
import com.ypyg.shopmanager.bean.GoodSortBean;
import com.ypyg.shopmanager.bean.infobean.GoodinfoBean;
import com.ypyg.shopmanager.cache.ImageCache;
import com.ypyg.shopmanager.common.AppUtil;
import com.ypyg.shopmanager.common.Bimp;
import com.ypyg.shopmanager.common.Constants;
import com.ypyg.shopmanager.common.CropSaveImage;
import com.ypyg.shopmanager.common.DataCener;
import com.ypyg.shopmanager.common.ImageCacheManager;
import com.ypyg.shopmanager.common.util.FileUtils;
import com.ypyg.shopmanager.event.GoodAddEvent;
import com.ypyg.shopmanager.event.GoodDetailEvent;
import com.ypyg.shopmanager.event.ImageForSdkTenEvent;
import com.ypyg.shopmanager.event.ImageLoadFinishEvent;
import com.ypyg.shopmanager.event.ImageUploadInfoEvent;
import com.ypyg.shopmanager.libcore.io.DiskLruCache;
import com.ypyg.shopmanager.libcore.io.DiskLruCache.Snapshot;
import com.ypyg.shopmanager.net.IRespCode;
import com.ypyg.shopmanager.view.popupwindow.SelectPicPopupWindow;
import com.ypyg.shopmanager.view.popupwindow.SortSelectPopupWindow;
import com.ypyg.shopmanager.view.popupwindow.SortSelectPopupWindow.SortSelectSubmit;
import com.ypyg.shopmanager.view.uploadphoto.CropImageActivity;

/**
 * 商品新增或更新
 * 
 * @author Administrator
 * 
 */
public class ActivityGoodEdit extends BaseActivity {
	private String imageTag = "ActivityGoodEdit";
	private Long goodId = -1L;
	private int goodState = -1;
	/** 弹出控件 **/
	private SelectPicPopupWindow menuWindow;
	// 初始化弹出控件
	private Dialog emptyDialog;
	private CropSaveImage cropSaveImage = null;

	private ImageView upload_imageview;

	private EditText good_title, good_class, good_price, good_salesvolume, good_description, good_inventory, good_code, good_weight;
	private RadioGroup count_radiogroup;
	/** 商品小图 **/
	private String smallhead_url = "";
	/** 库存计数，1 付款减库存 2 拍下减库存 **/
	private int inventory_count;
	/** 要提交的数据 **/
	private GoodinfoBean infobean;

	private SortSelectPopupWindow mDoubleSelectPopupWindow = null;
	private List<GoodParentsSortBean> mGoodSorts;// 网络的商品分类
	private String[] firstMenu1;
	private String[][] secondMenu2;
	private int mPosition1, mPosition2;// 记录分类的位置
	private View top_condition = null;

	private String imagePath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_editl);
		first();
		initCommenView();

		switch (goodState) {
		case Constants.GOODEDIT_ADD:// 新增商品状态
			initView1();

			break;
		case Constants.GOODEDIT_UPDATE:// 修改商品状态
			req2();

			break;
		}

	}

	private SortSelectSubmit mDoubleSelectSubmit = new SortSelectSubmit() {

		@Override
		public void submit(String value1, String value2, Integer position1, Integer position2) {
			mPosition1 =mGoodSorts.get(position1).getId() ;
			mPosition2 = mGoodSorts.get(position1).getData().get(position2).getId();
			good_class.setText(value1 + "；" + value2);
		}

	};

	/**
	 * 初始化共用控件
	 */
	private void initCommenView() {

		top_condition = findViewById(R.id.top_condition);
		good_description = (EditText) findViewById(R.id.good_description);
		good_class = (EditText) findViewById(R.id.good_class);
		good_title = (EditText) findViewById(R.id.good_title);
		good_price = (EditText) findViewById(R.id.good_price);
		good_salesvolume = (EditText) findViewById(R.id.good_salesvolume);
		good_inventory = (EditText) findViewById(R.id.good_inventory);
		good_code = (EditText) findViewById(R.id.good_code);
		count_radiogroup = (RadioGroup) findViewById(R.id.count_radiogroup);
		witchChecked(count_radiogroup);
		count_radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				witchChecked(group);
			}
		});

		initPopWin();

		upload_imageview = (ImageView) findViewById(R.id.upload_imageview);
		cropSaveImage = new CropSaveImage(mContext);
		emptyDialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
		final View emptyDialogView = LayoutInflater.from(mContext).inflate(R.layout.empty, null);
		WindowManager.LayoutParams lp = emptyDialog.getWindow().getAttributes();
		lp.dimAmount = 0.5f;
		emptyDialog.getWindow().setAttributes(lp);
		emptyDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		emptyDialog.setContentView(emptyDialogView);
		emptyDialog.setCanceledOnTouchOutside(true);
		// 实例化SelectPicPopupWindow
		menuWindow = new SelectPicPopupWindow(mContext, itemsOnClick);
		emptyDialog.setOnShowListener(new OnShowListener() {

			public void onShow(DialogInterface arg0) {
				menuWindow.showAtLocation(emptyDialogView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

			}
		});
		menuWindow.setOnDismissListener(new OnDismissListener() {

			public void onDismiss() {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				emptyDialog.dismiss();
			}
		});

	}

	/**
	 * 判断哪个选中
	 */
	private void witchChecked(RadioGroup radio) {
		switch (radio.getCheckedRadioButtonId()) {
		case R.id.counttrue:
			inventory_count = 1;
			break;
		case R.id.countfalse:
			inventory_count = 2;
			break;
		}
	}

	/**
	 * 初始化筛选弹出窗口
	 */
	private void initPopWin() {
		mGoodSorts=mDataCener.mGoodSorts;
		// 判断获取的商品分类
		if (mGoodSorts != null && mGoodSorts.size() > 0) {
			firstMenu1 = new String[mGoodSorts.size()];
			secondMenu2 = new String[mGoodSorts.size()][];
			for (int i = 0; i < mGoodSorts.size(); i++) {
				firstMenu1[i] = mGoodSorts.get(i).getName();
				List<GoodSortBean> data = mGoodSorts.get(i).getData();
				secondMenu2[i] = new String[data.size()];
				for (int j = 0; j < data.size(); j++) {
					secondMenu2[i][j] = data.get(j).getName();
				}
			}

			mDoubleSelectPopupWindow = new SortSelectPopupWindow(mContext, firstMenu1, secondMenu2);
			mDoubleSelectPopupWindow.setListener(mDoubleSelectSubmit);
			good_class.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 选择分类
					KeyBoardCancle();
					mDoubleSelectPopupWindow.show(top_condition);
				}
			});
		} else {
			good_class.setVisibility(View.GONE);
		}
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.go_to_image:// 去相册
				String sdcardState = Environment.getExternalStorageState();
				String sdcardPathDir = Constants.FILE_image;
				if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
					// 有sd卡，是否有myImage文件夹
					File fileDir = new File(sdcardPathDir);
					if (!fileDir.exists()) {
						fileDir.mkdirs();
					}
				}

				Intent i = new Intent(
				// 相册
						Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
				menuWindow.dismiss();
				break;
			case R.id.go_to_photo:// 去拍照
				photo();
				menuWindow.dismiss();
				break;
			case R.id.cancel_button:
				menuWindow.dismiss();
				break;
			}
		}

	};

	private static final int TAKE_PICTURE = 0;
	private static final int RESULT_LOAD_IMAGE = 1;
	private static final int CUT_PHOTO_REQUEST_CODE = 2;
	private String path = "";
	private Uri photoUri;

	public void photo() {
		try {
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = Constants.FILE_image;
			File file = null;
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				// 有sd卡，是否有myImage文件夹
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				// 是否有headImg文件
				file = new File(sdcardPathDir + System.currentTimeMillis() + ".JPEG");
			}
			if (file != null) {
				path = file.getPath();
				photoUri = Uri.fromFile(file);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:// 拍照返回
			if (resultCode == -1) {// 拍照
				startPhotoZoom(photoUri);
			}

			break;
		case RESULT_LOAD_IMAGE:// 相册返回
			if (resultCode == RESULT_OK && null != data) {// 相册返回
				Uri uri = data.getData();
				if (uri != null) {
					startPhotoZoom(uri);
				}
			}
			break;

		case CUT_PHOTO_REQUEST_CODE:
			if (resultCode == RESULT_OK && null != data) {// 裁剪返回
				String path = data.getStringExtra("path");// 得到裁剪后的图片路径
				if (!"".equals(path) && !"".equals(OriginalPath)) {
					Bitmap bitmap = Bimp.getLoacalBitmap(path);
					upload_imageview.setImageBitmap(bitmap);

					// File file = new File(OriginalPath);
					// //压缩原始图片
					// String temp =
					// cropSaveImage.createThumbSourceImage(OriginalPath,
					// file.getName());
					// TODO 上传图片到服务器
					loadingView.show();
					mDataService.upLoadImage(path, imageTag);
				}
			}

			break;
		}
	}

	protected void onEventMainThread(ImageUploadInfoEvent event) {
		loadingView.dismiss();
		if (event.getCode() == IRespCode.SUCCESS) {
			smallhead_url =event.getUrl();
			mDataCener.showToast(mContext, "图片上传成功!");
			return;
		}
		mDataCener.showToast(mContext, "图片上传失败！");

	}

	private String OriginalPath = "";

	private void startPhotoZoom(Uri uri) {
		// 去裁剪Activity
		if (!TextUtils.isEmpty(uri.getAuthority())) {
			Cursor cursor = mContext.getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
			if (null == cursor) {
				Toast.makeText(mContext, "图片没找到", 0).show();
				return;
			}
			cursor.moveToFirst();
			String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			cursor.close();
			OriginalPath = path;
			Intent intent = new Intent(mContext, CropImageActivity.class);
			intent.putExtra("path", path);
			startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);
		} else {
			OriginalPath = uri.getPath();
			Intent intent = new Intent(mContext, CropImageActivity.class);
			intent.putExtra("path", uri.getPath());
			startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);
		}
	}

	private void first() {
		// TODO 初始化状态参数
		goodId = getIntent().getLongExtra("id", -1L);
		goodState = getIntent().getIntExtra("state", -1);

		mDataCener = DataCener.getInstance();
		if (!AppUtil.isNull(mDataCener))
			mDataService = mDataCener.getDataService();
	}

	private void initView1() {
		// TODO 初始化控件1
		imagePath = getIntent().getStringExtra("imagepath");
		if (imagePath != null && !"".equals(imagePath)) {
			upload_imageview.setImageBitmap(Bimp.getLoacalBitmap(imagePath));
		}

	}

	private void initView2() {
		// TODO 初始化控件2

	}

	/**
	 * 提交商品
	 */
	private void req() {
		if (infobean != null)
			mDataService.GoodAdd(mDataCener.mBasicUserInfoBean.getId(), infobean);
	}

	private void req2() {
		// TODO 请求商品详情
		mDataService.GoodDetail(goodId + "");
		initView2();
	}

	// -------------------------------------------------用户点击事件-------------------------------------------------
	/**
	 * 图片上传
	 * 
	 * @param v
	 */
	public void upLoadImage(View v) {
		emptyDialog.show();
	}

	protected void onEventMainThread(GoodDetailEvent event) {
		if (null == event || IRespCode.SUCCESS != event.getCode()) {
			dialog("无数据！");
			return;
		}

		mDataCener.showToast(mContext, event.getInfobeans().toString());

	}

	protected void onDestroy() {
		FileUtils.deleteDir(FileUtils.SDPATH);
		FileUtils.deleteDir(FileUtils.SDPATH1);
		super.onDestroy();
	}

	/**
	 * 图片1
	 * 
	 * @param event
	 */
	protected void onEventMainThread(ImageLoadFinishEvent event) {
		if (null == event) {
			return;
		}
		if (null == event.getTag())
			return;
		if (!imageTag.equals(event.getTag()))
			return;
		String imageUrl = (String) event.getImageUrl();
		String key = event.getImageKey();
		ImageView imageView = (ImageView) upload_imageview.findViewWithTag(imageUrl);
		DiskLruCache mDiskLruCache = ImageCacheManager.getInstance(mContext).getmDiskLruCache();
		// 缓存被写入后，再次查找key对应的缓存
		Snapshot snapShot;
		try {
			snapShot = mDiskLruCache.get(key);

			FileInputStream fileInputStream = null;
			FileDescriptor fileDescriptor = null;
			if (snapShot != null) {
				fileInputStream = (FileInputStream) snapShot.getInputStream(0);
				fileDescriptor = fileInputStream.getFD();
			}
			// 将缓存数据解析成Bitmap对象
			Bitmap bitmap = null;
			if (fileDescriptor != null) {
				bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
			}

			if (imageView != null && bitmap != null) {
				imageView.setImageBitmap(bitmap);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图片2
	 * 
	 * @param event
	 */
	protected void onEventMainThread(ImageForSdkTenEvent event) {
		if (null == event) {
			return;
		}
		if (null == event.getmTag())
			return;
		if (!imageTag.equals(event.getmTag()))
			return;
		String imageUrl = (String) event.getImageUrl();
		ImageCache imageCache = ImageCache.getInstance(mContext);
		ImageView imageView = (ImageView) upload_imageview.findViewWithTag(imageUrl);
		Bitmap bitmap = null;
		if (!AppUtil.isNull(imageUrl)) {
			String key = AppUtil.getStringMD5(imageUrl);
			bitmap = imageCache.get(key);
			if (null != imageView)
				imageView.setImageBitmap(bitmap);
		}
	}

	/**
	 * 保存按钮
	 * 
	 * @param v
	 */
	public void toSubmit(View v) {
		// 非空验证
		String title = good_title.getText().toString().trim();
		String description = good_description.getText().toString().trim();
		String price = good_price.getText().toString().trim();
		String salesvolume = good_salesvolume.getText().toString().trim();
		String inventory = good_inventory.getText().toString().trim();
		String goodcode = good_code.getText().toString().trim();
		String goodclass = good_class.getText().toString().trim();

		// 组装数据，去请求接口
		infobean = new GoodinfoBean(title, description, price, salesvolume, smallhead_url, inventory, goodcode, mPosition1 + ";" + mPosition2, inventory_count);
		req();

	}

	/**
	 * 商品提交响应
	 * 
	 * @param event
	 */
	protected void onEventMainThread(GoodAddEvent event) {
		if (null == event || IRespCode.SUCCESS != event.getCode()) {
			dialog("商品提交失败！");
			return;
		}

		mDataCener.showToast(mContext, "商品提交成功！");
		finish();
	}

}
