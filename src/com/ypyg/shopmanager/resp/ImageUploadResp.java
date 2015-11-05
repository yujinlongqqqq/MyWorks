package com.ypyg.shopmanager.resp;

import com.ypyg.shopmanager.bean.ImageUploadRespBean;
import com.ypyg.shopmanager.net.BaseResp;

public class ImageUploadResp extends BaseResp {

	@Override
	public Class<?> getRespClass() {
		return ImageUploadRespBean.class;
	}

}
