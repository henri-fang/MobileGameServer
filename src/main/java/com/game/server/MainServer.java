package com.game.server;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.core.config.Config;
import com.core.server.MinaServer;
import com.game.config.ServerConfig;
import com.game.utils.SessionUtil;

public final class MainServer extends MinaServer {

	
	private static Logger logger = LoggerFactory.getLogger(MainServer.class);
	
	private static Logger closelog = LoggerFactory.getLogger("GATESESSIONCLOSE");
	
	private static Logger flowlog = LoggerFactory.getLogger("flow");
	
	private static Logger innerflowlog = LoggerFactory.getLogger("GATEINNERFLOW");
	
	private static Logger messagelog = LoggerFactory.getLogger("GATEMESSAGE");
	
	private static Logger sessionlog = LoggerFactory.getLogger("session");
	
	private static Logger innercloselog = LoggerFactory.getLogger("INNERSESSIONCLOSE");
	
	private static Logger closenumlog = LoggerFactory.getLogger("GATESESSIONCLOSENUM");
	/**
	 * 最大连接数
	 */
	private static final int MAX_SESSION = 10000;
	
	// 上一次心跳时间
	private static final String LAST_HEART = "last_heart";
	// 上一次命令时间
	private static final String LAST_CMD = "last_cmd";
	
	private static final String config = "config/server-config.xml";
	
	private static Config SERVER_CONFIG;

	private volatile int max_connect = 0;
	
	private volatile int max_message = 0;
	
	
	static {
		SERVER_CONFIG = new ServerConfig(config);
	}

	public static Config getSERVER_CONFIG() {
		return SERVER_CONFIG;
	}

	public MainServer() {
		super(SERVER_CONFIG);
	}

	public MainServer(Config config) {
		super(config);
	}

	
	
	@Override
	public void run() {
		super.run();
		//流量监控
		flowMonitor();
		//消息数量监控
		messageMonitor();
		//空闲链接监控
		freeSessionMonitor();
	}

	@Override
	protected void stop() {

	}

	public void reload() {

	}

	@Override
	public void doCommand(IoSession session, IoBuffer buf) {
		
	}

	@Override
	public void sessionCreate(IoSession session) {
		
	}

	@Override
	public void sessionOpened(IoSession session) {
		if (acceptor.getManagedSessionCount() > MAX_SESSION) {
			SessionUtil.closeSession(session, "连接数过多(" + acceptor.getManagedSessionCount() + ")");
		}
		if (max_connect < acceptor.getManagedSessionCount()) {
			max_connect = acceptor.getManagedSessionCount();
		}
		if (!session.containsAttribute(LAST_HEART)) {
			session.setAttribute(LAST_HEART, System.currentTimeMillis());
		}
		sessionlog.error("session max create:" + max_connect);

	}

	@Override
	public void sessionClosed(IoSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus idlestatus) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void flowMonitor() {
		// 流量测试使用
		new Timer("Quantity-Timer").schedule(new TimerTask() {

			@Override
			public void run() {
				if (acceptor == null || acceptor.getStatistics() == null)
					return;
				acceptor.getStatistics().updateThroughput(System.currentTimeMillis());

				StringBuffer buf = new StringBuffer();
				buf.append("WR:" + acceptor.getScheduledWriteBytes()).append(",");
				buf.append("MWR:" + acceptor.getScheduledWriteMessages()).append(",");
				buf.append("WT:" + acceptor.getStatistics().getWrittenBytes()).append(",");
				buf.append("RT:" + acceptor.getStatistics().getReadBytes()).append(",");
				buf.append("MWT:" + acceptor.getStatistics().getWrittenMessages()).append(",");
				buf.append("MRT:" + acceptor.getStatistics().getReadMessages()).append(",");
				buf.append("WS:" + acceptor.getStatistics().getWrittenBytesThroughput()).append(",");
				buf.append("RS:" + acceptor.getStatistics().getReadBytesThroughput());

				flowlog.error(buf.toString());
			}
		}, 5 * 1000, 5 * 1000);
	}

	private void messageMonitor() {
		// 消息数量用
		new Timer("AllMessage-Timer").schedule(new TimerTask() {
			@Override
			public void run() {
				messagelog.error("发送消息个数：" + max_message);
				max_message = 0;
			}
		}, 1 * 1000, 1 * 1000);
	}
	
	
	private void freeSessionMonitor() {
		//关闭session中空闲连接
		new Timer("Close-Session-Timer").schedule(new TimerTask(){
			
			@Override
			public void run() {
				if(acceptor==null || acceptor.getManagedSessions()==null || acceptor.getManagedSessions().size()==0) return;
				long now = System.currentTimeMillis();
				
				IoSession[] sessionArray = acceptor.getManagedSessions().values().toArray(new IoSession[0]);
				for (IoSession ioSession : sessionArray) {
					if(ioSession!=null && ioSession.isConnected()){
						if(now - ioSession.getCreationTime() > 10 * 1000 /*&& !ioSession.containsAttribute(PlayerManager.USER_ID)*/){
							SessionUtil.closeSession(ioSession, "10秒内没有发送登陆信息");
						}else if(acceptor.getManagedSessionCount() > 5000 && ioSession.containsAttribute(LAST_HEART)){
							long pre = (Long)ioSession.getAttribute(LAST_HEART);
							if(now - pre > 5 * 60 * 1000){
								SessionUtil.closeSession(ioSession, "5分钟内没有发送心跳信息");
							}
						}
					}
				}
			}
		}, 5 * 1000, 5 * 1000);
	}


}
