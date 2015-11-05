package com.ypyg.shopmanager.loader;

import com.ypyg.shopmanager.net.IReq;
import com.ypyg.shopmanager.net.IResp;

public interface IDataLoader {

	// loadFromCache �ӻ����л�ȡ����
	public Object loadFromCache(IReq aReq, IResp aResp) throws Exception;

	// ֱ�����������������
	public Object loadFromServer(IReq aReq, IResp aResp) throws Exception;

	// �ȶ�ȡ���� �������������
	public Object load(IReq aReq, IResp aResp) throws Exception;

	public Object loadWithGet(IReq aReq, IResp aResp) throws Exception;

}
