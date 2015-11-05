package com.ypyg.shopmanager.bean;

public class BaseReqBean {

	private String brand;// 品牌

	private int cell_id; // 小区识别码,蜂窝ID,
							// 三种主要的基于位置服务(LBS)技术之一。小区识别码通过识别网络中哪一个小区传输用户呼叫并将该信息翻译成纬度和经度来确定用户位置。

	private float density; // 屏幕的密度

	private int height; // 屏幕高

	private String imei;// imei号

	private String imsi; // imsi号

	/*
	 * location area code 位置区码
	 * （移动通信系统中）,是为寻呼而设置的一个区域,覆盖一片地理区域,初期一般按行政区域划分(一个县或一个区
	 * ),现在很灵活了,按寻呼量划分.当一个LAC下的寻呼量达到一个预警门限,就必须拆分.
	 * 为了确定移动台的位置，每个GSMPLMN的覆盖区都被划分成许多位置区，位置区码(LAC)则用于标识不同的位置区。
	 * 位置区码(LAC)包含于LAI中，
	 * 由两个字节组成，采用16进制编码。可用范围为0x0000－0xFFFF，码组0x0000和0xFFFE不可以使用(
	 * 参见GSM规范03.03、04.08和11.11)。一个位置区可以包含一个或多个小区。
	 */
	private int lac;

	/*
	 * Mobile Network Code 的缩写，译为移动网络代码。它由二到三位数字组成。它和 MCC 合在一起唯一标识一个移动网络提供者。比如
	 * 中国移动的 MNC 是 00，中国联通的 MNC 是 01，中国联通 CDMA 的 MNC 是 03，中国卫星全球星网的 MNC 是 04。*
	 */
	private String mnc;

	/*
	 * Mobile Country Code 的缩写，译为移动国家代码。它由三位数字组成。用于标识一个国家，但一个国家可以被分配多个 MCC 中国的
	 * MCC 只有 460*
	 */
	private String mcc;

	/**
	 * phone type
	 */
	private String model;// 手机型号

	private String phoneid; // Android 设备的唯一标识码

	private int ram; // 内存大小。手机上的RAM是指系统运行及软件运行可需要的临时空间，跟电脑上的内存是相同的意思。

	private int rom; // ROM大小。手机上的ROM是指手机系统及可安装程序的空间，ROM越大，能直接在系统里安装的程序就越多，相当于我们电脑的C盘。

	private int sdkver; // sdk版本号

	private int width;// 屏幕宽度

	private String nettype;// 网络类型,wifi为0，gprs为1,3g为2

	private String mac; // mac地址

	/**
	 * 网络制式
	 */
	private int netstandard;// 手机的网络制式。网络类型非wifi时，该值有效

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

	public static void cpoyObjAttr(Object sourceObj, Object targetObj,
			Class<?> clazz) throws Exception {
		if (sourceObj == null || targetObj == null) {
			throw new Exception("源对象和目标对象不能为null");
		}
		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			Object sourceValue = fields[i].get(sourceObj);
			fields[i].set(targetObj, sourceValue);
		}
		if (clazz.getSuperclass() == Object.class) {
			return;
		}
		cpoyObjAttr(sourceObj, targetObj, clazz.getSuperclass());
	}
}
