package com.core.codec;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.core.message.GameMessage;

public class ServerProtocolDecoder extends ProtocolDecoderAdapter {

	public static final String BUFFER = "PTBUFFER";

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		IoBuffer buffer = (IoBuffer) session.getAttribute(BUFFER);

		boolean hasUseBuffer = false;
		if (buffer == null) {
			buffer = in;
			buffer.order(ByteOrder.LITTLE_ENDIAN);
		} else {
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			in.order(ByteOrder.LITTLE_ENDIAN);
			buffer.put(in);
			buffer.flip();
			hasUseBuffer = true;
		}
		for (;;) {
			if (buffer.remaining() > 6) {
				System.err.println(Arrays.toString(buffer.array()));
				int position = buffer.position();
				if (buffer.get() == GameMessage.HEAD[0] && buffer.get() == GameMessage.HEAD[1]) {
					int startPos = in.position();
					int length = buffer.getInt();
					if (length > 6 && buffer.remaining() >= length - 6) {
						byte[] bytes = new byte[length - 6];
						buffer.get(bytes);
						out.write(bytes);
					} else {
						buffer.position(position);
						break;
					}
				} else {
					session.setAttribute(BUFFER, null);
					session.close(true);
					throw new IOException("packet head error.");
				}
			} else {
				break;
			}
		}
		if (buffer.hasRemaining()) {
			storeRemaining(buffer, session);
		} else {
			if (hasUseBuffer) {
				session.setAttribute(BUFFER, null);
			}
		}
	}

	private void storeRemaining(IoBuffer buf, IoSession session) {
		IoBuffer remainingBuf = IoBuffer.allocate(buf.capacity());
		remainingBuf.setAutoExpand(true);
		remainingBuf.order(buf.order());
		remainingBuf.put(buf);
		session.setAttribute(BUFFER, remainingBuf);
	}

}
