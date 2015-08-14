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
			// ����Э��ͷ ħ��
			buffer.put(GameMessage.HEAD);
			// ����Э��ͷ����
			buffer.putShort((short) 0);// len = cmd + body
			buffer.putShort((short) message.getCmdId());
			// ����Э��������
			message.encodeBody(buffer);
			int totalLength = buffer.position();
			// ������Ϣ���С,��ȥ��Ϣͷ��,�Լ���¼���ȵ��ֶγ���
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
