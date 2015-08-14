package com.core.config;

public interface Config {

	/**
	 * 返回服务器ID
	 * 
	 * @return
	 */
	public int getId();

	/**
	 * 返回服务器名称
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 返回服务器Ip地址
	 * @return
	 */
	public String getIp();

	/**
	 * 返回服务器开放端口
	 * @return
	 */
	public int getPort();

}
