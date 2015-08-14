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
		// Mina�����߳�����
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
	 * �������ر��¼�
	 */
	protected abstract void stop();

	private class ConnectServer implements Runnable {

		// ��־
		private Logger log = LoggerFactory.getLogger(ConnectServer.class);

		// Mina������
		private MinaServer server;

		public ConnectServer(MinaServer server) {
			this.server = server;
		}

		@Override
		public void run() {
			// ���û���Ϊ�ɸ���
			// IoBuffer.setAllocator(new CachedBufferAllocator());
			// ע��Mina Nio�˿ڽ���
			acceptor = new NioSocketAcceptor();

			DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
			// ע�����ݱ������
			chain.addLast("codec", new ProtocolCodecFilter(new ServerProtocolCodecFactory()));
			// ע����־����
			// chain.addLast("logger", new LoggingFilter());

			OrderedThreadPoolExecutor threadpool = new OrderedThreadPoolExecutor(500);
			chain.addLast("threadPool", new ExecutorFilter(threadpool));

			int recsize = 5120;
			int sendsize = 20480;
			int timeout = 30;
			acceptor.setReuseAddress(true);// ����ÿһ�������������ӵĶ˿ڿ�������
			SocketSessionConfig sc = acceptor.getSessionConfig();
			sc.setReuseAddress(true);// ����ÿһ�������������ӵĶ˿ڿ�������
			sc.setReceiveBufferSize(recsize);// �������뻺�����Ĵ�С
			sc.setSendBufferSize(sendsize);// ��������������Ĵ�С
			sc.setTcpNoDelay(true);// flush�����ĵ���
									// ����Ϊ���ӳٷ��ͣ�Ϊtrue����װ�ɴ�����ͣ��յ��������Ϸ���
			sc.setSoLinger(0);
			sc.setIdleTime(IdleStatus.READER_IDLE, timeout);

			// ��Mina����������ģ��
			acceptor.setHandler(new ServerProtocolHandler(server));
			try {
				// �󶨷��������ݼ����˿ڣ�����������
				acceptor.bind(new InetSocketAddress(port));
				log.info("Mina Server " + server.getName() + " Start At Port " + port);
			} catch (IOException e) {
				log.error("Mina Server " + server.getName() + " Port " + port + "Already Use:" + e.getMessage());
				System.exit(1);
			}
		}

	}

	// �������ر��߳�
	private class CloseByExit implements Runnable {

		private Logger log = LoggerFactory.getLogger(CloseByExit.class);

		// ����������
		private String name;

		public CloseByExit(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			// ִ�йر��¼�
			stop();
			log.info(this.name + " Stop!");
		}

	}

}
