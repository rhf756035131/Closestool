package com.example.rahul.closestool;
/**
 * JavaBean
 *
 *
 */
public class CityBean {
	public static final String ID = "_id";
	public static final String IP = "ip";
	public static final String NAME = "name";
	public static final String MAC = "mac";
	
	private String id;
	private String ip;
	private String name;
	private String mac;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}

}
