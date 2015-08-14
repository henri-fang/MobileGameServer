package com.game.utils;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionUtil {
private static Logger closelog = LoggerFactory.getLogger("GATESESSIONCLOSE");
	
	public static void closeSession(IoSession session, String reason){
		closelog.error(session + "-->close [because] " + reason);
		session.close(true);
	}
	
	public static void closeSession(IoSession session, String reason, boolean force){
		closelog.error(session + "-->close [because] " + reason);
		session.close(force);
	}
}
