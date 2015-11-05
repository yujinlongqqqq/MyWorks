package com.ypyg.shopmanager.bean;

public class BasicUserInfoBean {
	private Integer id;// 自增标识 该参数代表uid 客户端登录后操作必带
	private Integer shop_id;// 店铺id 例如 222
	private Integer role_id;// 角色ID 例如 0
	private String username;// 头像
	private String realname;// 真实姓名
	private String mobile;// 手机
	private String email;// 邮箱
	private String ip_login;// 最后登录的IP
	private String dt_update;// 最后登录的时间

	public BasicUserInfoBean() {
	}

	public BasicUserInfoBean(Integer id, Integer shop_id, Integer role_id, String username, String realname, String mobile, String email, String ip_login, String dt_update) {
		this.id = id;
		this.shop_id = shop_id;
		this.role_id = role_id;
		this.username = username;
		this.realname = realname;
		this.mobile = mobile;
		this.email = email;
		this.ip_login = ip_login;
		this.dt_update = dt_update;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getShop_id() {
		return shop_id;
	}

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}

	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIp_login() {
		return ip_login;
	}

	public void setIp_login(String ip_login) {
		this.ip_login = ip_login;
	}

	public String getDt_update() {
		return dt_update;
	}

	public void setDt_update(String dt_update) {
		this.dt_update = dt_update;
	}

}
