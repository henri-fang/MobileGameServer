package com.game.config;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.core.config.Config;
import com.game.server.MainServer;

public class ServerConfig implements Config {

	private int id;

	private String name;

	private String ip;

	private int port;

	public ServerConfig(String config) {
		readXml(config);
	}
	
	private void readXml(String config) {
		SAXReader reader = new SAXReader();
		try {
			String dir = System.getProperty("user.dir");
			Document doc = reader.read(new File(dir, config));
			Element root = doc.getRootElement();
			this.id = Integer.parseInt(root.element("id").getStringValue());
			this.name = root.element("name").getStringValue();
			this.ip = root.element("serverip").getStringValue();
			this.port = Integer.parseInt(root.element("serverport").getStringValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void main(String[] args){
		MainServer server = new MainServer();
	}
}
