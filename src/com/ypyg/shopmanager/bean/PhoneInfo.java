package com.ypyg.shopmanager.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;

/**
 * �ն��ֻ�������Ϣ
 */
public class PhoneInfo {
	private String brand;// Ʒ��

	private int cell_id; // С��ʶ����,����ID,
							// ������Ҫ�Ļ���λ�÷���(LBS)����֮һ��С��ʶ����ͨ��ʶ����������һ��С�������û����в�������Ϣ�����γ�Ⱥ;�����ȷ���û�λ�á�

	private float density; // ��Ļ���ܶ�

	private int height; // ��Ļ��

	private String imei;// imei��

	private String imsi; // imsi��

	/*
	 * location area code λ������
	 * ���ƶ�ͨ��ϵͳ�У�,��ΪѰ�������õ�һ������,����һƬ��������,����һ�㰴�������򻮷�(һ���ػ�һ����
	 * ),���ں������,��Ѱ��������.��һ��LAC�µ�Ѱ�����ﵽһ��Ԥ������,�ͱ�����.
	 * Ϊ��ȷ���ƶ�̨��λ�ã�ÿ��GSMPLMN�ĸ������������ֳ����λ������λ������(LAC)�����ڱ�ʶ��ͬ��λ������
	 * λ������(LAC)������LAI�У�
	 * �������ֽ���ɣ�����16���Ʊ��롣���÷�ΧΪ0x0000��0xFFFF������0x0000��0xFFFE������ʹ��(
	 * �μ�GSM�淶03.03��04.08��11.11)��һ��λ�������԰���һ������С����
	 */
	private int lac;

	/*
	 * Mobile Network Code ����д����Ϊ�ƶ�������롣���ɶ�����λ������ɡ����� MCC ����һ��Ψһ��ʶһ���ƶ������ṩ�ߡ�����
	 * �й��ƶ��� MNC �� 00���й���ͨ�� MNC �� 01���й���ͨ CDMA �� MNC �� 03���й�����ȫ�������� MNC �� 04��*
	 */
	private String mnc;

	/*
	 * Mobile Country Code ����д����Ϊ�ƶ����Ҵ��롣������λ������ɡ����ڱ�ʶһ�����ң���һ�����ҿ��Ա������� MCC �й���
	 * MCC ֻ�� 460*
	 */
	private String mcc;

	/**
	 * phone type
	 */
	private String model;// �ֻ��ͺ�

	private String phoneid; // Android �豸��Ψһ��ʶ��

	private int ram; // �ڴ��С���ֻ��ϵ�RAM��ָϵͳ���м�������п���Ҫ����ʱ�ռ䣬�������ϵ��ڴ�����ͬ����˼��

	private int rom; // ROM��С���ֻ��ϵ�ROM��ָ�ֻ�ϵͳ���ɰ�װ����Ŀռ䣬ROMԽ����ֱ����ϵͳ�ﰲװ�ĳ����Խ�࣬�൱�����ǵ��Ե�C�̡�

	private int sdkver; // sdk�汾��

	private int width;// ��Ļ���

	private String nettype;// ��������,wifiΪ0��gprsΪ1,3gΪ2

	private String mac; // mac��ַ

	/**
	 * ������ʽ
	 */
	private int netstandard;// �ֻ���������ʽ���������ͷ�wifiʱ����ֵ��Ч

	public String getBrand() {
		return brand;
	}

	public void setBrand(String aBrand) {
		brand = aBrand;
	}

	public int getCellId() {
		return cell_id;
	}

	public void setCellId(int aCell) {
		cell_id = aCell;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float aDensity) {
		density = aDensity;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int aHeight) {
		height = aHeight;
	}

	public String getIMEI() {
		return imei;
	}

	public void setIMEI(String aImei) {
		imei = aImei;
	}

	public String getIMSI() {
		return imsi;
	}

	public void setIMSI(String aImsi) {
		imsi = aImsi;
	}

	public String getMnc() {
		return mnc;
	}

	public void setMnc(String aMnc) {
		mnc = aMnc;
	}

	public int getLac() {
		return lac;
	}

	public void setLac(int aLac) {
		lac = aLac;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String aMcc) {
		mcc = aMcc;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String aModel) {
		model = aModel;
	}

	public String getPhoneId() {
		return phoneid;
	}

	public void setPhoneId(String aPhoneId) {
		phoneid = aPhoneId;
	}

	public int getRam() {
		return ram;
	}

	public void setRam(int aRam) {
		ram = aRam;
	}

	public int getRom() {
		return rom;
	}

	public void setRom(int aRom) {
		rom = aRom;
	}

	public int getSdkVer() {
		return sdkver;
	}

	public void setSdkVer(int aVer) {
		sdkver = aVer;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int aWidth) {
		width = aWidth;
	}

	public String getNetType() {
		return nettype;
	}

	public void setNetType(String aType) {
		nettype = aType;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String aMac) {
		mac = aMac;
	}

	public void setNetStandard(int aStandard) {
		netstandard = aStandard;
	}

	public int getNetStandard() {
		return netstandard;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("brand=").append(brand);
		sb.append("\nmodel=").append(model);
		sb.append("\nphoneid=").append(phoneid);
		sb.append("\ndensity=").append(density);
		sb.append("\nheight=").append(height);
		sb.append("\nwidth=").append(width);
		sb.append("\nimsi=").append(imsi);
		sb.append("\nimei=").append(imei);
		sb.append("\nram=").append(ram);
		sb.append("\nrom=").append(rom);
		sb.append("\nsdkver=").append(sdkver);
		return sb.toString();
	}

	/**
	 * ��ȡ�ڴ��С
	 */
	private static void getMemInfo(Context aContext, PhoneInfo aPhoneInfo) {
		ActivityManager mActivityManager = (ActivityManager) aContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		mActivityManager.getMemoryInfo(mi);
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader("/proc/meminfo");
			br = new BufferedReader(fr);
			String line = br.readLine();
			String[] arrayOfString = line.split("\\s+");
			int ramSize = Integer.valueOf(arrayOfString[1]).intValue() / 1024;
			aPhoneInfo.setRam(ramSize);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		File root = Environment.getDataDirectory();
		StatFs sf = new StatFs(root.getPath());
		@SuppressWarnings("deprecation")
		double iBlockCount = sf.getBlockCount();
		@SuppressWarnings("deprecation")
		double iBlockSize = sf.getBlockSize();
		double total = iBlockCount * iBlockSize;
		aPhoneInfo.setRom((int) (total / 1024 / 1024));
	}

	public static void setNetType(Context aContext, PhoneInfo aPhoneInfo) {
		ConnectivityManager connectivityManager = (ConnectivityManager) aContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != connectivityManager) {
			// ����androidԭ��api
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
				try {
					String nettypename = networkInfo.getTypeName().toLowerCase(
							Locale.getDefault());
					if (nettypename.equals("wifi")) {
						// ��ʼ��ȫ�ֵ���������
						aPhoneInfo.setNetType(nettypename);
						aPhoneInfo.setNetStandard(-1);
					} else {
						aPhoneInfo.setNetType(nettypename);
						TelephonyManager mTelephonyMgr = (TelephonyManager) aContext
								.getSystemService(Context.TELEPHONY_SERVICE);
						if (mTelephonyMgr != null) {
							aPhoneInfo.setNetStandard(mTelephonyMgr
									.getNetworkType()); // �������������ͣ�ȡ��ǰʹ�õ�
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void initPhoneInfo(Context aContext, PhoneInfo aPhoneInfo) {
		int iLen = 32;
		String sInfo = (Build.BRAND.length() > iLen ? Build.BRAND.substring(0,
				iLen) : Build.BRAND).toUpperCase(Locale.getDefault());
		aPhoneInfo.setBrand(sInfo);
		sInfo = (Build.MODEL.length() > iLen ? Build.MODEL.substring(0, iLen)
				: Build.MODEL).toUpperCase(Locale.getDefault());
		aPhoneInfo.setModel(sInfo);
		aPhoneInfo.setSdkVer(Build.VERSION.SDK_INT);
		TelephonyManager mTelephonyMgr = (TelephonyManager) aContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = mTelephonyMgr.getSubscriberId();
		String defaultVal = "111111111111111";
		sInfo = imsi == null ? defaultVal : imsi;
		sInfo = imsi;
		aPhoneInfo.setIMSI(sInfo);
		String imei = mTelephonyMgr.getDeviceId();
		if (imei != null && imei.length() > 15) {
			imei = imei.substring(imei.length() - 15);
		}
		sInfo = imei == null ? defaultVal : imei;
		sInfo = imei;
		aPhoneInfo.setIMEI(sInfo);
		try {
			CellLocation cell = CellLocation.getEmpty();
			GsmCellLocation gsm = (GsmCellLocation) cell;
			aPhoneInfo.setLac(gsm.getLac());
			aPhoneInfo.setCellId(gsm.getCid());
		} catch (Exception e) {
			aPhoneInfo.setLac('0');
			aPhoneInfo.setCellId(1234);
		}
		WifiManager wifiManager = (WifiManager) aContext
				.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager != null) {
			WifiInfo info = wifiManager.getConnectionInfo();
			if (info != null) {
				aPhoneInfo.setMac(info.getMacAddress());
			}
		}
		Configuration tConfiguration = aContext.getResources()
				.getConfiguration();
		int mcc = tConfiguration.mcc;
		int mnc = tConfiguration.mnc;
		aPhoneInfo.setMcc(Integer.toString(mcc));
		/*
		 * 1 ��ͨ 2 �ƶ� 3 ����
		 */
		aPhoneInfo.setMnc(Integer.toString(mnc));
		DisplayMetrics dm = aContext.getResources().getDisplayMetrics();
		aPhoneInfo.setWidth(dm.widthPixels);
		aPhoneInfo.setHeight(dm.heightPixels);
		aPhoneInfo.setPhoneId(Secure.getString(aContext.getContentResolver(),
				Secure.ANDROID_ID));
		getMemInfo(aContext, aPhoneInfo);
		aPhoneInfo.setDensity(dm.density);
		setNetType(aContext, aPhoneInfo);
	}
}
