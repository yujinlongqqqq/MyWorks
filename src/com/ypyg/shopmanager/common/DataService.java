package com.ypyg.shopmanager.common;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import com.ypyg.shopmanager.cache.ImageCache;
import com.ypyg.shopmanager.event.BaseStatusEvent;
import com.ypyg.shopmanager.event.BusProvider;
import com.ypyg.shopmanager.event.ConsumptionRecordEvent;
import com.ypyg.shopmanager.event.DataOrderCountEvent;
import com.ypyg.shopmanager.event.DataOrderEvent;
import com.ypyg.shopmanager.event.DataRevenueEvent;
import com.ypyg.shopmanager.event.DataVisitorEvent;
import com.ypyg.shopmanager.event.DeleteOrderEvent;
import com.ypyg.shopmanager.event.ExitLoginEvent;
import com.ypyg.shopmanager.event.GetValidateEvent;
import com.ypyg.shopmanager.event.GoodChooseEvent;
import com.ypyg.shopmanager.event.GoodDetailEvent;
import com.ypyg.shopmanager.event.GoodOfflineListEvent;
import com.ypyg.shopmanager.event.GoodOnlineListEvent;
import com.ypyg.shopmanager.event.GoodSChooseEvent;
import com.ypyg.shopmanager.event.GoodSStatusEvent;
import com.ypyg.shopmanager.event.GoodSortsEvent;
import com.ypyg.shopmanager.event.GoodStatusEvent;
import com.ypyg.shopmanager.event.GoodUpdateEvent;
import com.ypyg.shopmanager.event.IEvent;
import com.ypyg.shopmanager.event.ImageForSdkTenEvent;
import com.ypyg.shopmanager.event.ImageLoadFinishEvent;
import com.ypyg.shopmanager.event.ImageUploadInfoEvent;
import com.ypyg.shopmanager.event.LoginEvent;
import com.ypyg.shopmanager.event.MarketGoodsListEvent;
import com.ypyg.shopmanager.event.MemberAddEvent;
import com.ypyg.shopmanager.event.MemberDetailEvent;
import com.ypyg.shopmanager.event.MemberListEvent;
import com.ypyg.shopmanager.event.MemberUpdateEvent;
import com.ypyg.shopmanager.event.ResetPasswordEvent;
import com.ypyg.shopmanager.event.ServerConfigEvent;
import com.ypyg.shopmanager.libcore.io.DiskLruCache;
import com.ypyg.shopmanager.libcore.io.DiskLruCache.Snapshot;
import com.ypyg.shopmanager.loader.DataLoader;
import com.ypyg.shopmanager.loader.IDataLoader;
import com.ypyg.shopmanager.net.BaseHttpService;
import com.ypyg.shopmanager.net.HttpBase64Connection;
import com.ypyg.shopmanager.net.HttpConnection;
import com.ypyg.shopmanager.net.IHttpService;
import com.ypyg.shopmanager.net.INetState;
import com.ypyg.shopmanager.net.IReq;
import com.ypyg.shopmanager.net.IResp;
import com.ypyg.shopmanager.net.IRespBean;
import com.ypyg.shopmanager.net.IRespCode;
import com.ypyg.shopmanager.net.NetState;
import com.ypyg.shopmanager.req.BaseStatusReq;
import com.ypyg.shopmanager.req.ConsumptionRecordReq;
import com.ypyg.shopmanager.req.DataOrderCountReq;
import com.ypyg.shopmanager.req.DataOrderReq;
import com.ypyg.shopmanager.req.DataRevenueReq;
import com.ypyg.shopmanager.req.DataVisitorReq;
import com.ypyg.shopmanager.req.DeleteOrderReq;
import com.ypyg.shopmanager.req.ExitLoginReq;
import com.ypyg.shopmanager.req.GetValidateReq;
import com.ypyg.shopmanager.req.GoodChooseReq;
import com.ypyg.shopmanager.req.GoodDetailReq;
import com.ypyg.shopmanager.req.GoodOfflineListReq;
import com.ypyg.shopmanager.req.GoodOnlineListReq;
import com.ypyg.shopmanager.req.GoodSChooseReq;
import com.ypyg.shopmanager.req.GoodSStatusReq;
import com.ypyg.shopmanager.req.GoodSortsReq;
import com.ypyg.shopmanager.req.GoodStatusReq;
import com.ypyg.shopmanager.req.GoodUpdateReq;
import com.ypyg.shopmanager.req.ImageDownloadReq;
import com.ypyg.shopmanager.req.ImageUploadReq;
import com.ypyg.shopmanager.req.LoginReq;
import com.ypyg.shopmanager.req.MarketGoodsListReq;
import com.ypyg.shopmanager.req.MemberAddReq;
import com.ypyg.shopmanager.req.MemberDetailReq;
import com.ypyg.shopmanager.req.MemberListReq;
import com.ypyg.shopmanager.req.MemberUpdateReq;
import com.ypyg.shopmanager.req.ResetPasswordReq;
import com.ypyg.shopmanager.req.ServerConfigReq;
import com.ypyg.shopmanager.resp.BaseStatusResp;
import com.ypyg.shopmanager.resp.ConsumptionRecordResp;
import com.ypyg.shopmanager.resp.DataOrderCountResp;
import com.ypyg.shopmanager.resp.DataOrderResp;
import com.ypyg.shopmanager.resp.DataRevenueResp;
import com.ypyg.shopmanager.resp.DataVisitorResp;
import com.ypyg.shopmanager.resp.DeleteOrderResp;
import com.ypyg.shopmanager.resp.ExitLoginResp;
import com.ypyg.shopmanager.resp.GetValidateResp;
import com.ypyg.shopmanager.resp.GoodChooseResp;
import com.ypyg.shopmanager.resp.GoodDetailResp;
import com.ypyg.shopmanager.resp.GoodOfflineListResp;
import com.ypyg.shopmanager.resp.GoodOnlineListResp;
import com.ypyg.shopmanager.resp.GoodSChooseResp;
import com.ypyg.shopmanager.resp.GoodSStatusResp;
import com.ypyg.shopmanager.resp.GoodSortsResp;
import com.ypyg.shopmanager.resp.GoodStatusResp;
import com.ypyg.shopmanager.resp.GoodUpdateResp;
import com.ypyg.shopmanager.resp.ImageDownloadResp;
import com.ypyg.shopmanager.resp.ImageDownloadResp2;
import com.ypyg.shopmanager.resp.ImageUploadResp;
import com.ypyg.shopmanager.resp.LoginResp;
import com.ypyg.shopmanager.resp.MarketGoodsListResp;
import com.ypyg.shopmanager.resp.MemberAddResp;
import com.ypyg.shopmanager.resp.MemberDetailResp;
import com.ypyg.shopmanager.resp.MemberListResp;
import com.ypyg.shopmanager.resp.MemberUpdateResp;
import com.ypyg.shopmanager.resp.ResetPasswordResp;
import com.ypyg.shopmanager.resp.ServerConfigResp;
import com.ypyg.shopmanager.thread.ThreadPool;

public class DataService implements IRespCode {
	private static Context mContext = null;

	private IHttpService httpService = null;
	private HttpConnection httpConnection = null;
	private HttpBase64Connection httpBaseConnection = null;

	private INetState mNetState = null;

	public DataService(Context aContext, NetState aNetState) {
		mContext = aContext;
		mNetState = aNetState;
		httpService = new BaseHttpService(mContext, mNetState, BaseHttpService.DEFAULT_READ_TIMEOUT);
		httpConnection = new HttpConnection(mContext, mNetState, BaseHttpService.DEFAULT_READ_TIMEOUT);
		httpBaseConnection = new HttpBase64Connection(mContext, mNetState, BaseHttpService.DEFAULT_READ_TIMEOUT);
	}

	private void AsyncPostCommand(final IReq aReq, final IResp aResp, final IEvent aEvent) {
		Runnable aConfigQuery = new Runnable() {
			@Override
			public void run() {
				IDataLoader dataLoader = new DataLoader(httpService);
				aEvent.setCode(FAILED);
				aEvent.setEventEntity(null);
				try {
					IRespBean aIBean = (IRespBean) dataLoader.load(aReq, aResp);
					if (null != aIBean) {
						aEvent.setCode(aIBean.getCode());
						aEvent.setEventEntity(aIBean.getBeanEntity());
						aEvent.setCount(aIBean.getCount());
						aEvent.setOffset(aIBean.getOffset());
						aEvent.setMsg(aIBean.getMsg());
						aEvent.setStatus(aIBean.getStatus());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				BusProvider.get().post(aEvent);
			}
		};
		ThreadPool.submit(aConfigQuery);

	}

	// private void AsyncPostCommon(final IReq aReq, final IResp aResp,
	// final IEvent aEvent) {
	// Runnable aConfigQuery = new Runnable() {
	// @Override
	// public void run() {
	// IDataLoader dataLoader = new NewDataLoader(httpConnection);
	// aEvent.setCode(FAILED);
	// aEvent.setEventEntity(null);
	// try {
	// IRespBean aIBean = (IRespBean) dataLoader.load(aReq, aResp);
	// if (null != aIBean) {
	// // 用户未登录
	// if (aIBean.getCode() == ERROR1100) {
	// Intent intent = new Intent(
	// "com.schoolnews.pushservice.action.RELOGIN");
	// mContext.sendBroadcast(intent);
	// DataCener.getInstance().setToken(null);
	// }
	// aEvent.setCode(aIBean.getCode());
	// aEvent.setErrorcontext(aIBean.getErrorcontext());
	// aEvent.setEventEntity(aIBean.getBeanEntity());
	// aEvent.setCount(aIBean.getCount());
	// if (null != aIBean.getOffset())
	// aEvent.setOffset(aIBean.getOffset());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// BusProvider.get().post(aEvent);
	// }
	// };
	// ThreadPool.submit(aConfigQuery);
	// }

	// datamode 为0的BASE64加密 需重写onNewResponse；
	// private void AsyncPostBase64(final IReq aReq, final IResp aResp,
	// final IEvent aEvent) {
	// Runnable aConfigQuery = new Runnable() {
	// @Override
	// public void run() {
	// IDataLoader dataLoader = new NewDataLoader(httpBaseConnection);
	// aEvent.setCode(FAILED);
	// aEvent.setEventEntity(null);
	// try {
	// IRespBean aIBean = (IRespBean) dataLoader.load(aReq, aResp);
	// if (null != aIBean) {
	// aEvent.setCode(aIBean.getCode());
	// aEvent.setEventEntity(aIBean.getBeanEntity());
	// aEvent.setCount(aIBean.getCount());
	// aEvent.setErrorcontext(aIBean.getErrorcontext());
	// aEvent.setOffset(aIBean.getOffset());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// BusProvider.get().post(aEvent);
	// }
	// };
	// ThreadPool.submit(aConfigQuery);
	// }

	public void getServerConfig() {
		final ServerConfigReq aReq = new ServerConfigReq();
		final ServerConfigResp aResp = new ServerConfigResp();
		final ServerConfigEvent aEvent = new ServerConfigEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 登录后返回的基础数据
	public void BaseStatus() {
		final BaseStatusReq aReq = new BaseStatusReq();
		final BaseStatusResp aResp = new BaseStatusResp();
		final BaseStatusEvent aEvent = new BaseStatusEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	/**
	 * 登录
	 * 
	 * @param username
	 * @param password
	 * @param auto
	 *            自动登录
	 */
	public void Login(String username, String password, String auto) {
		final LoginReq aReq = new LoginReq(username, password, auto);
		final LoginResp aResp = new LoginResp();
		final LoginEvent aEvent = new LoginEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	/** 用户退出登录接口 **/
	public void mainExitLogin(Integer uid) {
		final ExitLoginReq aReq = new ExitLoginReq(uid);
		final ExitLoginResp aResp = new ExitLoginResp();
		final ExitLoginEvent aEvent = new ExitLoginEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}
	
	/**
	 * 获取商品分类
	 */
	public void getGoodSorts(Integer uid){
		final GoodSortsReq aReq = new GoodSortsReq(uid);
		final GoodSortsResp aResp = new GoodSortsResp();
		final GoodSortsEvent aEvent = new GoodSortsEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 获取验证码
	public void GetValidate(String username, String password, String auto) {
		final GetValidateReq aReq = new GetValidateReq(username);
		final GetValidateResp aResp = new GetValidateResp();
		final GetValidateEvent aEvent = new GetValidateEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 重置密码
	public void ResetPassword(String username, String password, String validate) {
		final ResetPasswordReq aReq = new ResetPasswordReq(username, password, validate);
		final ResetPasswordResp aResp = new ResetPasswordResp();
		final ResetPasswordEvent aEvent = new ResetPasswordEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	/**
	 * 上架商品列表
	 * @param catid
	 * @param offset
	 * @param count
	 */
	public void GoodOnlineList(String catid, Long offset, Long count) {
		final GoodOnlineListReq aReq = new GoodOnlineListReq(catid, offset, count);
		final GoodOnlineListResp aResp = new GoodOnlineListResp();
		final GoodOnlineListEvent aEvent = new GoodOnlineListEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	/**
	 * 下架商品列表
	 * @param catid
	 * @param offset
	 * @param count
	 */
	public void GoodOfflineList(String catid, Long offset, Long count) {
		final GoodOfflineListReq aReq = new GoodOfflineListReq(catid, offset, count);
		final GoodOfflineListResp aResp = new GoodOfflineListResp();
		final GoodOfflineListEvent aEvent = new GoodOfflineListEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	/**
	 * 商品明细
	 * @param id
	 */
	public void GoodDetail(String id) {
		final GoodDetailReq aReq = new GoodDetailReq(id);
		final GoodDetailResp aResp = new GoodDetailResp();
		final GoodDetailEvent aEvent = new GoodDetailEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 商品更新
	public void GoodUpdate(String id) {
		final GoodUpdateReq aReq = new GoodUpdateReq(id);
		final GoodUpdateResp aResp = new GoodUpdateResp();
		final GoodUpdateEvent aEvent = new GoodUpdateEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 商品增加
	// public void GoodAdd(String id) {
	// final GoodUpdateReq aReq = new GoodUpdateReq(id);
	// final GoodUpdateResp aResp = new GoodUpdateResp();
	// final GoodUpdateEvent aEvent = new GoodUpdateEvent();
	// AsyncPostCommand(aReq, aResp, aEvent);
	// }

	// 单个商品上下架
	public void GoodStatus(Long id, String isonline, int position, String tag) {
		final GoodStatusReq aReq = new GoodStatusReq(id, isonline);
		final GoodStatusResp aResp = new GoodStatusResp();
		final GoodStatusEvent aEvent = new GoodStatusEvent();
		aEvent.setPosition(position);
		aEvent.setId(id);
		aEvent.setTag(tag);
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 批量商品上下架
	public void GoodSStatus(String goodsid, String isonline, ArrayList<Integer> positions, String tag) {
		final GoodSStatusReq aReq = new GoodSStatusReq(goodsid, isonline);
		final GoodSStatusResp aResp = new GoodSStatusResp();
		final GoodSStatusEvent aEvent = new GoodSStatusEvent();
		aEvent.setPosition(positions);
		aEvent.setId(goodsid);
		aEvent.setTag(tag);
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 选货 单个商品
	public void GoodChoose(String goodsid) {
		final GoodChooseReq aReq = new GoodChooseReq(goodsid);
		final GoodChooseResp aResp = new GoodChooseResp();
		final GoodChooseEvent aEvent = new GoodChooseEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 批量选货
	public void GoodSChoose(String ids) {
		final GoodSChooseReq aReq = new GoodSChooseReq(ids);
		final GoodSChooseResp aResp = new GoodSChooseResp();
		final GoodSChooseEvent aEvent = new GoodSChooseEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 市场选货列表
	public void MarketGoodsList(String id, Long offset, Long count) {
		final MarketGoodsListReq aReq = new MarketGoodsListReq(id, offset, count);
		final MarketGoodsListResp aResp = new MarketGoodsListResp();
		final MarketGoodsListEvent aEvent = new MarketGoodsListEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 会员列表
	public void MemberList(String sortcat, Long offset, Long count) {
		final MemberListReq aReq = new MemberListReq(sortcat, offset, count);
		final MemberListResp aResp = new MemberListResp();
		final MemberListEvent aEvent = new MemberListEvent();
		aEvent.setCatid(sortcat);
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 新增会员
	public void MemberAdd(String smallhead, String nickname, String phone, String score, String level, String brand, String customtag) {
		final MemberAddReq aReq = new MemberAddReq(smallhead, nickname, phone, score, level, brand, customtag);
		final MemberAddResp aResp = new MemberAddResp();
		final MemberAddEvent aEvent = new MemberAddEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 会员明细
	public void MemberDetail(String id) {
		final MemberDetailReq aReq = new MemberDetailReq(id);
		final MemberDetailResp aResp = new MemberDetailResp();
		final MemberDetailEvent aEvent = new MemberDetailEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 会员编辑
	public void MemberUpdate(String smallhead, String nickname, String phone, String score, String level, String brand, String customtag) {
		final MemberUpdateReq aReq = new MemberUpdateReq(smallhead, nickname, phone, score, level, brand, customtag);
		final MemberUpdateResp aResp = new MemberUpdateResp();
		final MemberUpdateEvent aEvent = new MemberUpdateEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 会员消费记录
	public void ConsumptionRecord(String id, String sort, String cat) {
		final ConsumptionRecordReq aReq = new ConsumptionRecordReq(id, sort, cat);
		final ConsumptionRecordResp aResp = new ConsumptionRecordResp();
		final ConsumptionRecordEvent aEvent = new ConsumptionRecordEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 销售数据
	public void DataRevenue() {
		final DataRevenueReq aReq = new DataRevenueReq();
		final DataRevenueResp aResp = new DataRevenueResp();
		final DataRevenueEvent aEvent = new DataRevenueEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	// 访客数据
	public void DataVisitor() {
		final DataVisitorReq aReq = new DataVisitorReq();
		final DataVisitorResp aResp = new DataVisitorResp();
		final DataVisitorEvent aEvent = new DataVisitorEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}
	
	// 订单数据
	public void DataOrderCount() {
		final DataOrderCountReq aReq = new DataOrderCountReq();
		final DataOrderCountResp aResp = new DataOrderCountResp();
		final DataOrderCountEvent aEvent = new DataOrderCountEvent();
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	/**
	 * 查询订单列表
	 * @param catid
	 * @param offset
	 * @param count
	 */
	public void DataOrder(String catid, Long offset, Long count) {
		final DataOrderReq aReq = new DataOrderReq(catid, offset, count);
		final DataOrderResp aResp = new DataOrderResp();
		final DataOrderEvent aEvent = new DataOrderEvent();
		aEvent.setCatid(catid);
		AsyncPostCommand(aReq, aResp, aEvent);
	}
	
	/**
	 * 删除订单（单个、多个）
	 * @param catid
	 * @param orderid
	 */
	public void DeleteOrder(String catid, String orderid,Object item) {
		final DeleteOrderReq aReq = new DeleteOrderReq(catid, orderid);
		final DeleteOrderResp aResp = new DeleteOrderResp();
		final DeleteOrderEvent aEvent = new DeleteOrderEvent();
		aEvent.setItem(item);
		aEvent.setCatid(catid);
		AsyncPostCommand(aReq, aResp, aEvent);
	}

	/**
	 * 上传图片
	 */
	public void upLoadImage(final String aImagePath, final String aTag) {
		// Runnable aQuery = new Runnable() {
		// final ImageUploadReq aReq = new ImageUploadReq(aImagePath);
		// final ImageUploadResp aResp = new ImageUploadResp();
		// final ImageUploadInfoEvent aEvent = new ImageUploadInfoEvent();
		//
		// @Override
		// public void run() {
		// IDataLoader dataLoader = new DataLoader(httpService);
		// try {
		// Object aObj = dataLoader.loadFromServer(aReq, aResp);
		// if (aObj instanceof ImageUploadInfoBean) {
		// ImageUploadInfoBean aBean = (ImageUploadInfoBean) aObj;
		// aEvent.setUrl(aBean.getResult());
		// aEvent.setCode(aBean.getCode());
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// aEvent.setmTag(aTag);
		// BusProvider.get().post(aEvent);
		// }
		// };
		// ThreadPool.submit(aQuery);
		final ImageUploadReq aReq = new ImageUploadReq(aImagePath);
		final ImageUploadResp aResp = new ImageUploadResp();
		final ImageUploadInfoEvent aEvent = new ImageUploadInfoEvent();
		aEvent.setmTag(aTag);
		Runnable aConfigQuery = new Runnable() {
			@Override
			public void run() {
				IDataLoader dataLoader = new DataLoader(httpService);
				aEvent.setCode(FAILED);
				aEvent.setEventEntity(null);
				try {
					IRespBean aIBean = (IRespBean) dataLoader.loadFromServer(aReq, aResp);
					if (null != aIBean) {
						aEvent.setCode(aIBean.getCode());
						aEvent.setEventEntity(aIBean.getBeanEntity());
						aEvent.setMsg(aIBean.getMsg());
						aEvent.setStatus(aIBean.getStatus());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				BusProvider.get().post(aEvent);
			}
		};
		ThreadPool.submit(aConfigQuery);

	}

	/**
	 * 图片下载
	 * @param mMemoryCache
	 * @param imageUrl
	 * @param mDiskLruCache
	 * @param aTag
	 * @param px
	 */
	public void getLruCacheImage(final LruCache<String, Bitmap> mMemoryCache, final String imageUrl, final DiskLruCache mDiskLruCache, final Object aTag, final String px) {

		Runnable aQuery = new Runnable() {

			@Override
			public void run() {
				FileDescriptor fileDescriptor = null;
				FileInputStream fileInputStream = null;
				Snapshot snapShot = null;

				try {
					// 生成图片URL对应的key
					final String key = AppUtil.getStringMD5(imageUrl);
					if (snapShot == null) {
						IDataLoader dataLoader = new DataLoader(httpService);
						ImageDownloadReq aReq = new ImageDownloadReq(imageUrl, px);
						ImageDownloadResp2 aResp = new ImageDownloadResp2(imageUrl, mDiskLruCache, aTag);
						dataLoader.loadWithGet(aReq, aResp);
						// 缓存被写入后，再次查找key对应的缓存
						snapShot = mDiskLruCache.get(key);
					}
					if (snapShot != null) {
						fileInputStream = (FileInputStream) snapShot.getInputStream(0);
						fileDescriptor = fileInputStream.getFD();
					}
					// 将缓存数据解析成Bitmap对象
					Bitmap bitmap = null;
					if (fileDescriptor != null) {
						bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
					}
					if (bitmap != null) {
						// 将Bitmap对象添加到内存缓存当中
						if (mMemoryCache.get(imageUrl) == null) {
							mMemoryCache.put(imageUrl, bitmap);
						}
						ImageLoadFinishEvent aEvent = new ImageLoadFinishEvent();
						aEvent.setImageKey(key);
						aEvent.setTag(aTag);
						aEvent.setImageUrl(imageUrl);

						BusProvider.get().post(aEvent);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fileDescriptor == null && fileInputStream != null) {
						try {
							fileInputStream.close();
						} catch (IOException e) {
						}
					}
				}

			}
		};
		ThreadPool.submit(aQuery);
	}

	// 获取图片
	public void getImageForSdk10(final String imageUrl, final ImageCache aCache, final Object aTag, final String px) {
		Runnable aQuery = new Runnable() {
			@Override
			public void run() {
				IDataLoader dataLoader = new DataLoader(httpService);
				try {
					ImageDownloadReq aReq = new ImageDownloadReq(imageUrl, px);
					ImageDownloadResp aResp = new ImageDownloadResp(imageUrl, aCache);
					dataLoader.loadWithGet(aReq, aResp);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				ImageForSdkTenEvent aEvent = new ImageForSdkTenEvent();
				aEvent.setImageUrl(imageUrl);
				aEvent.setmTag(aTag);
				BusProvider.get().post(aEvent);
			}
		};
		ThreadPool.submitImage(aQuery);
	}

	// 获取图片
	public void getImage(final List<String> aImageUrls, final ImageCache aCache, final Object aTag) {
		Runnable aQuery = new Runnable() {
			@Override
			public void run() {
				List<String> aUrls = new ArrayList<String>();
				ImageCache aCache = ImageCache.getInstance(mContext);
				if (null != aCache) {
					for (String sUrl : aImageUrls) {
						String sKey = AppUtil.getStringMD5(sUrl);
						Bitmap bm = aCache.get(sKey);
						if (null == bm) {
							aUrls.add(sUrl);
						}
					}
				} else {
					aUrls.addAll(aImageUrls);
				}
				IDataLoader dataLoader = new DataLoader(httpService);
				try {
					for (String sUrl : aImageUrls) {
						ImageDownloadReq aReq = new ImageDownloadReq(sUrl, null);
						ImageDownloadResp aResp = new ImageDownloadResp(sUrl, aCache);
						dataLoader.load(aReq, aResp);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				ImageLoadFinishEvent aEvent = new ImageLoadFinishEvent();
				if (aTag instanceof String) {
					if (((String) aTag).endsWith(Constants.thumbnailImage)) {
						String tag = ((String) aTag).substring(0, ((String) aTag).length() - Constants.thumbnailImage.length());
						aEvent.setTag(tag);
					} else {
						aEvent.setTag(aTag);
					}
				} else {
					aEvent.setTag(aTag);
				}
				aEvent.setUrls(aImageUrls);
				BusProvider.get().post(aEvent);
			}
		};
		ThreadPool.submitImage(aQuery);
	}

	// // 登录
	// public void Login(final String tag) {
	// final LoginReq aReq = new LoginReq();
	// final LoginResp aResp = new LoginResp();
	// final LoginEvent aEvent = new LoginEvent();
	// Runnable aQuery = new Runnable() {
	// @Override
	// public void run() {
	// IDataLoader dataLoader = new DataLoader(httpService);
	// try {
	// Object aObj = dataLoader.load(aReq, aResp);
	// if (aObj instanceof LoginRespBean) {
	// LoginRespBean aBean = (LoginRespBean) aObj;
	// aEvent.setCode(aBean.getCode());
	// aEvent.setmToken(aBean.getToken());
	// aEvent.setInfobean(aBean.getInfobean());
	// aEvent.setTag(tag);
	// DataCener.getInstance().setmTBasicInfoBean(aBean.getInfobean());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// BusProvider.get().post(aEvent);
	// }
	// };
	// ThreadPool.submit(aQuery);
	// }

	// // 获取图片
	// public void getLruCacheImage(final LruCache<String, Bitmap> mMemoryCache,
	// final String imageUrl, final DiskLruCache mDiskLruCache, final Object
	// aTag, final String px) {
	//
	// Runnable aQuery = new Runnable() {
	//
	// @Override
	// public void run() {
	// FileDescriptor fileDescriptor = null;
	// FileInputStream fileInputStream = null;
	// Snapshot snapShot = null;
	//
	// try {
	// // 生成图片URL对应的key
	// final String key = AppUtil.getStringMD5(imageUrl);
	// if (snapShot == null) {
	// IDataLoader dataLoader = new DataLoader(httpService);
	// ImageDownloadReq aReq = new ImageDownloadReq(imageUrl, px);
	// ImageDownloadResp2 aResp = new ImageDownloadResp2(imageUrl,
	// mDiskLruCache, aTag);
	// dataLoader.load(aReq, aResp);
	// // 缓存被写入后，再次查找key对应的缓存
	// snapShot = mDiskLruCache.get(key);
	// }
	// if (snapShot != null) {
	// fileInputStream = (FileInputStream) snapShot.getInputStream(0);
	// fileDescriptor = fileInputStream.getFD();
	// }
	// // 将缓存数据解析成Bitmap对象
	// Bitmap bitmap = null;
	// if (fileDescriptor != null) {
	// bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
	// }
	// if (bitmap != null) {
	// // 将Bitmap对象添加到内存缓存当中
	// if (mMemoryCache.get(imageUrl) == null) {
	// mMemoryCache.put(imageUrl, bitmap);
	// }
	// ImageLoadFinishEvent aEvent = new ImageLoadFinishEvent();
	// aEvent.setImageKey(key);
	// aEvent.setTag(aTag);
	// aEvent.setImageUrl(imageUrl);
	//
	// BusProvider.get().post(aEvent);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (fileDescriptor == null && fileInputStream != null) {
	// try {
	// fileInputStream.close();
	// } catch (IOException e) {
	// }
	// }
	// }
	//
	// }
	// };
	// ThreadPool.submit(aQuery);
	// }

	// // 获取图片
	// public void getImageForSdk10(final String imageUrl,
	// final ImageCache aCache, final Object aTag, final String px) {
	// Runnable aQuery = new Runnable() {
	// @Override
	// public void run() {
	// IDataLoader dataLoader = new DataLoader(httpService);
	// try {
	// ImageDownloadReq aReq = new ImageDownloadReq(imageUrl, px);
	// ImageDownloadResp aResp = new ImageDownloadResp(imageUrl,
	// aCache);
	// dataLoader.load(aReq, aResp);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return;
	// }
	// ImageForSdkTenEvent aEvent = new ImageForSdkTenEvent();
	// aEvent.setImageUrl(imageUrl);
	// aEvent.setmTag(aTag);
	// BusProvider.get().post(aEvent);
	// }
	// };
	// ThreadPool.submitImage(aQuery);
	// }

	// // 获取图片
	// public void getImage(final List<String> aImageUrls,
	// final ImageCache aCache, final Object aTag) {
	// Runnable aQuery = new Runnable() {
	// @Override
	// public void run() {
	// List<String> aUrls = new ArrayList<String>();
	// ImageCache aCache = ImageCache.getInstance(mContext);
	// if (null != aCache) {
	// for (String sUrl : aImageUrls) {
	// String sKey = AppUtil.getStringMD5(sUrl);
	// Bitmap bm = aCache.get(sKey);
	// if (null == bm) {
	// aUrls.add(sUrl);
	// }
	// }
	// } else {
	// aUrls.addAll(aImageUrls);
	// }
	// IDataLoader dataLoader = new DataLoader(httpService);
	// try {
	// for (String sUrl : aImageUrls) {
	// ImageDownloadReq aReq = new ImageDownloadReq(sUrl, null);
	// ImageDownloadResp aResp = new ImageDownloadResp(sUrl,
	// aCache);
	// dataLoader.load(aReq, aResp);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// return;
	// }
	// ImageLoadFinishEvent aEvent = new ImageLoadFinishEvent();
	// if (aTag instanceof String) {
	// if (((String) aTag).endsWith(Constants.thumbnailImage)) {
	// String tag = ((String) aTag).substring(0,
	// ((String) aTag).length()
	// - Constants.thumbnailImage.length());
	// aEvent.setTag(tag);
	// } else {
	// aEvent.setTag(aTag);
	// }
	// } else {
	// aEvent.setTag(aTag);
	// }
	// aEvent.setUrls(aImageUrls);
	// BusProvider.get().post(aEvent);
	// }
	// };
	// ThreadPool.submitImage(aQuery);
	// }

	// /** 用户获取验证码接口 **/
	// public void mainGetValidateCode(String mobile) {
	// final MainGetValidateCodeReq aReq = new MainGetValidateCodeReq(mobile);
	// final MainGetValidateCodeResp aResp = new MainGetValidateCodeResp();
	// final MainGetValidateCodeEvent aEvent = new MainGetValidateCodeEvent();
	// AsyncPostCommon(aReq, aResp, aEvent);
	// }

	// /** 主服务器登录接口 **/
	// public void mainLogin(final String tag, String password, String
	// pushUserId) {
	// final MainLoginReq aReq = new MainLoginReq(password, pushUserId);
	// final MainLoginResp aResp = new MainLoginResp();
	// final LoginResultEvent aEvent = new LoginResultEvent();
	// Runnable aQuery = new Runnable() {
	// @Override
	// public void run() {
	// IDataLoader dataLoader = new NewDataLoader(httpConnection);
	// aEvent.setCode(FAILED);
	// aEvent.setEventEntity(null);
	// try {
	// Object aObj = dataLoader.load(aReq, aResp);
	// if (aObj instanceof MainLoginRespBean) {
	// MainLoginRespBean aBean = (MainLoginRespBean) aObj;
	// aEvent.setCode(aBean.getCode());
	// aEvent.setToken(aBean.getToken());
	// aEvent.setInfobean(aBean.getInfobean());
	// aEvent.setErrorcontext(aBean.getErrorcontext());
	// aEvent.setTag(tag);
	// aEvent.setUniqueuserid(aBean.getUniqueuserid());
	// // DataCener.getInstance().seTMBASICINFOBEAN(
	// // ABEAN.GETINFOBEAN());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// BusProvider.get().post(aEvent);
	// }
	// };
	// ThreadPool.submit(aQuery);
	// }

	/** 主服务器注册接口 **/
	// public void mainRegister(String mAccount, String password,
	// String validatecode) {
	// final MainRegisterReq aReq = new MainRegisterReq(mAccount, password,
	// validatecode);
	// final MainRegisterResp aResp = new MainRegisterResp();
	// final MainRegisterEvent aEvent = new MainRegisterEvent();
	// AsyncPostCommon(aReq, aResp, aEvent);
	// }

	// /** 用户忘记密码接口4.0 **/
	// public void mainForgetPassWord(String account, String password,
	// String validatecode) {
	// final MainForgetPassWordReq aReq = new MainForgetPassWordReq(account,
	// password, validatecode);
	// final MainForgetPassWordResp aResp = new MainForgetPassWordResp();
	// final MainForgetPassWordEvent aEvent = new MainForgetPassWordEvent();
	// AsyncPostCommon(aReq, aResp, aEvent);
	// }

	// // 用户获取上传七牛服务器密钥接口
	// public void getUpToken(String uploadtype, final Object tag,
	// final String voiceLength) {
	// final UpTokenReq aReq = new UpTokenReq(uploadtype);
	// final UpTokenResp aResp = new UpTokenResp();
	// final UpTokenEvent aEvent = new UpTokenEvent();
	// Runnable aConfigQuery = new Runnable() {
	// @Override
	// public void run() {
	// IDataLoader dataLoader = new NewDataLoader(httpConnection);
	// aEvent.setCode(FAILED);
	// aEvent.setEventEntity(null);
	//
	// try {
	// IRespBean aIBean = (IRespBean) dataLoader.load(aReq, aResp);
	// if (null != aIBean) {
	// aEvent.setCode(aIBean.getCode());
	// aEvent.setErrorcontext(aIBean.getErrorcontext());
	// aEvent.setEventEntity(aIBean.getBeanEntity());
	// aEvent.setCount(aIBean.getCount());
	// aEvent.setOffset(aIBean.getOffset());
	// aEvent.setTag(tag);
	// aEvent.setVoiceLength(voiceLength);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// BusProvider.get().post(aEvent);
	// }
	// };
	// ThreadPool.submit(aConfigQuery);
	// // }

	// /** 获取天气 **/
	// public void GetWeatherCityCode(String cityCode) {
	// final WeatherReq aReq = new WeatherReq(cityCode);
	// final WeatherCityCodeResp aResp = new WeatherCityCodeResp();
	// final WeatherEvent aEvent = new WeatherEvent();
	//
	// Runnable aConfigQuery = new Runnable() {
	// @Override
	// public void run() {
	// IDataLoader dataLoader = new DataLoader(httpService);
	// aEvent.setCode(FAILED);
	// aEvent.setEventEntity(null);
	// try {
	// WeatherRespBean aIBean = (WeatherRespBean) dataLoader
	// .loadWithGet(aReq, aResp);
	// if (null != aIBean) {
	// aEvent.setCode(aIBean.getCode());
	// aEvent.setEventEntity(aIBean.getBeanEntity());
	// aEvent.setCount(aIBean.getCount());
	// aEvent.setOffset(aIBean.getOffset());
	// aEvent.setErrMsg(aIBean.getErrMsg());
	// aEvent.setStatus(aIBean.getStatus());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// BusProvider.get().post(aEvent);
	// }
	// };
	// ThreadPool.submit(aConfigQuery);
	// }

}
