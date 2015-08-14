package com.core.codec;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.core.message.GameMessage;
import com.core.message.ResponseMessage;

public class ServerProtocolEncoder extends ProtocolEncoderAdapter {

	private static Logger logger = LoggerFactory.getLogger(ServerProtocolEncoder.class);

	@Override
	public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) throws Exception {
		IoBuffer buffer = encodeIoBuffer((ResponseMessage) obj);
		out.write(buffer);
	}

	private IoBuffer encodeIoBuffer(ResponseMessage message) {
		try {
			IoBuffer buffer = IoBuffer.allocate(100);
			buffer.setAutoExpand(true);
			buffer.setAutoShrink(true);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			// 设置协议头 魔数
			buffer.put(GameMessage.HEAD);
			// 设置协议头数据
			buffer.putShort((short) 0);// len = cmd + body
			buffer.putShort((short) message.getCmdId());
			// 设置协议体数据
			message.encodeBody(buffer);
			int totalLength = buffer.position();
			// 设置消息体大小,减去消息头长,以及记录长度的字段长度
			totalLength -= GameMessage.HEAD.length + 2;
			buffer.putShort(GameMessage.HEAD.length, (short) totalLength);
			buffer.flip();
			return buffer;
		} catch (Exception e) {
			logger.error("encode message error!CommandId:" + message.getCmdId() + " ; throwable:" + e);
			throw new RuntimeException(e);
		}
	}

}
