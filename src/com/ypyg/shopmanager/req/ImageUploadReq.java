package com.ypyg.shopmanager.req;

import java.io.File;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

public class ImageUploadReq extends BaseQueryReq {

	private final String aImagePath;

	public ImageUploadReq(String aImagePath) {
		this.aImagePath = aImagePath;
	}

	@Override
	public String getUrl() {
		// DataCener aCener = DataCener.getInstance();
		// if (null != aCener) {
		// String sDomain = aCener.getImageServUrl();
		// String sMain = aCener.getMainServerDomain();
		// if (!sDomain.startsWith("http://")) {
		// sDomain = new String("http://" + sDomain);
		// }
		//
		// StringBuilder sBuilder = new StringBuilder(sDomain);
		// if (2 == sMain.split(":").length)
		// sBuilder.append(sMain.substring(sMain.lastIndexOf("/")));
		// else
		// sBuilder.append(sMain.substring(sMain.lastIndexOf(":")));
		// sBuilder.append(UPLOADIMAGE_URL);
		// final String sUri = sBuilder.toString();
		// }
		String mainserverurl = mdataCener.getMainServerDomain();
		StringBuilder url = new StringBuilder(mainserverurl);
		url.append(Image_URL);
		return url.toString();
	}

	@Override
	public HttpEntity getEntry() {
		HttpEntity aEntity = null;
		try {
			File ImageFile = new File(aImagePath);
			ContentBody contentBody = new FileBody(ImageFile);
			// FormBodyPart formBodyPart = new FormBodyPart(ImageFile.getName(),
			// contentBody);

			String time = String.valueOf(System.currentTimeMillis());
			String number = String.valueOf(new Random().nextInt());
			// String randomStr = SignUtil
			// .randomString((int) (Math.random() * 36));
			// String sign = SignUtil.getSignature(time, number);

			final MultipartEntity multipartEntity = new MultipartEntity();
			// multipartEntity.addPart(formBodyPart);
			multipartEntity.addPart("file", contentBody);
			multipartEntity.addPart("uid", new StringBody(mdataCener.mBasicUserInfoBean.getId() + ""));
			multipartEntity.addPart("token", new StringBody(token));
			// multipartEntity.addPart("signature", new StringBody(sign));
			// multipartEntity.addPart("timestamp", new StringBody(time));
			// multipartEntity.addPart("nonce", new StringBody(number));
			// multipartEntity.addPart("echostr", new StringBody(randomStr));
			aEntity = multipartEntity;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return aEntity;
	}
}
