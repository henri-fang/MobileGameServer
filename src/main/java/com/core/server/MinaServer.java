package com.core.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.core.codec.ServerProtocolCodecFactory;
import com.core.config.Config;
import com.core.handler.ServerProtocolHandler;

public abstract class MinaServer implements IServer, Runnable {

	protected static Logger logger = LoggerFactory.getLogger(MinaServer.class);

	protected NioSocketAcceptor acceptor;

	private int id;

	private String name;

	private String ip;

	private int port;

	public MinaServer() {

	}

	public MinaServer(Config config) {
		this.id = config.getId();
		this.name = config.getName();
		this.ip = config.getIp();
		this.port = config.getPort();
	}

	@Override
	public void run() {
		// Mina连接线程启动
		new Thread(new ConnectServer(this)).start();
		Runtime.getRuntime().addShutdownHook(new Thread(new CloseByExit(name)));
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIp() {
		return ip;
	}

	/**
	 * 服务器关闭事件
	 */
	protected abstract void stop();

	private class ConnectServer implements Runnable {

		// 日志
		private Logger log = LoggerFactory.getLogger(ConnectServer.class);

		// Mina服务器
		private MinaServer server;

		public ConnectServer(MinaServer server) {
			this.server = server;
		}

		@Override
		public void run() {
			// 设置缓存为可复用
			// IoBuffer.setAllocator(new CachedBufferAllocator());
			// 注册Mina Nio端口接收
			acceptor = new NioSocketAcceptor();

			DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
			// 注册数据编解码器
			chain.addLast("codec", new ProtocolCodecFilter(new ServerProtocolCodecFactory()));
			// 注册日志管理
			// chain.addLast("logger", new LoggingFilter());

			OrderedThreadPoolExecutor threadpool = new OrderedThreadPoolExecutor(500);
			chain.addLast("threadPool", new ExecutorFilter(threadpool));

			int recsize = 5120;
			int sendsize = 20480;
			int timeout = 30;
			acceptor.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用
			SocketSessionConfig sc = acceptor.getSessionConfig();
			sc.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用
			sc.setReceiveBufferSize(recsize);// 设置输入缓冲区的大小
			sc.setSendBufferSize(sendsize);// 设置输出缓冲区的大小
			sc.setTcpNoDelay(true);// flush函数的调用
									// 设置为非延迟发送，为true则不组装成大包发送，收到东西马上发出
			sc.setSoLinger(0);
			sc.setIdleTime(IdleStatus.READER_IDLE, timeout);

			// 绑定Mina服务器管理模块
			acceptor.setHandler(new ServerProtocolHandler(server));
			try {
				// 绑定服务器数据监听端口，启动服务器
				acceptor.bind(new InetSocketAddress(port));
				log.info("Mina Server " + server.getName() + " Start At Port " + port);
			} catch (IOException e) {
				log.error("Mina Server " + server.getName() + " Port " + port + "Already Use:" + e.getMessage());
				System.exit(1);
			}
		}

	}

	// 服务器关闭线程
	private class CloseByExit implements Runnable {

		private Logger log = LoggerFactory.getLogger(CloseByExit.class);

		// 服务器名字
		private String name;

		public CloseByExit(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			// 执行关闭事件
			stop();
			log.info(this.name + " Stop!");
		}

	}

}
