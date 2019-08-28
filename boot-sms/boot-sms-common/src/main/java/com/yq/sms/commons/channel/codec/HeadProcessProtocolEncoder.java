package com.yq.sms.commons.channel.codec;

import com.yq.sms.commons.sms.packet.SendMessageContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 数据编码，写入通道</p>
 * @author youq  2019/8/23 16:44
 */
@Slf4j
public class HeadProcessProtocolEncoder implements ProtocolEncoder {

	@SuppressWarnings("unchecked")
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		try {
			if (message instanceof byte[]) {
				byte[] SendMessage = (byte[]) message;
				IoBuffer buf = IoBuffer.allocate(SendMessage.length);
				buf.put(SendMessage);
				buf.flip();
				out.write(buf);
				out.flush();
			} else {
                ArrayList<SendMessageContext> sendContent = (ArrayList<SendMessageContext>) message;
				List<ArrayList<SendMessageContext>> result = new ArrayList<>();
				int sendCount = 0;
				int split = 16;
				if (sendContent.size() > split) {
					for (int i = 0; i < sendContent.size(); i += split) {
						ArrayList<SendMessageContext> tempData = new ArrayList<>();
						int leavings = sendContent.size() - sendCount;
						if (leavings > split) {
							sendCount = split + i;
						} else {
							sendCount = sendContent.size();
						}
						for (int j = i; j < sendCount; j++) {
							tempData.add(sendContent.get(j));
						}
						result.add(tempData);
					}
				} else {
					result.add(sendContent);
				}
				for (ArrayList<SendMessageContext> arrayList : result) {
					int i = 0;
					for (SendMessageContext context : arrayList) {
						i += context.getSendContent().length;
					}
					IoBuffer buf = IoBuffer.allocate(i);
					for (SendMessageContext sendMessageInfo : arrayList) {
						buf.put(sendMessageInfo.getSendContent());
					}
					buf.flip();
					out.write(buf);
					out.flush();
				}
			}
		} catch (Exception e) {
			log.error("write channel exception:", e);
		}
		log.info("信息提交通道完成");
	}

	public void dispose(IoSession session) throws Exception {
		session.close(true);
	}

}
