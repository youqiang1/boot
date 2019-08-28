package com.yq.sms.commons.channel.codec;

import com.yq.kernel.enu.OperatorTypeEnum;
import com.yq.sms.commons.constants.SmsConstants;
import com.yq.sms.commons.sms.packet.HeadBodyBaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.util.ArrayList;

/**
 * <p> 数据解码</p>
 * @author youq  2019/8/23 16:44
 */
@Slf4j
public class HeadProcessProtocolDecoder implements ProtocolDecoder {

	private OperatorTypeEnum operatorType;

	HeadProcessProtocolDecoder(OperatorTypeEnum operatorType) {
		this.operatorType = operatorType;
	}

	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		try {
			IoBuffer buf;
			if (session.containsAttribute(SmsConstants.READ_CONTENT)) {
				buf = (IoBuffer) session.getAttribute(SmsConstants.READ_CONTENT);
			} else {
				buf = IoBuffer.allocate(40960).setAutoExpand(true);
				session.setAttribute(SmsConstants.READ_CONTENT, buf);
			}
            // 将读取到的信息放入缓存
			buf.put(in);
            // 指针重置
			buf.flip();
			int headLength = OperatorTypeEnum.CUCC.equals(operatorType) ? 20 : 12;

            // 有效数据小于头长度
			if (buf.remaining() < headLength) {
				log.info("重新回到写入的位置");
				return;
			}
			ArrayList<HeadBodyBaseMessage> list = new ArrayList<>();
			// 头信息可读
			while (buf.remaining() >= headLength) {
                // 记录指针
				buf.mark();
				HeadBodyBaseMessage headBodyBaseMessage = new HeadBodyBaseMessage();
                // 读取头信息
				byte[] head = new byte[headLength];
				buf.get(head, 0, headLength);
				headBodyBaseMessage.setHead(head);
                // 获取包体总长度
				headBodyBaseMessage.setTotalLength();
				if (headBodyBaseMessage.getTotalLength() < headLength) {
					buf.reset();
					break;
				}
				// 获取体信息
				if (buf.remaining() >= (headBodyBaseMessage.getTotalLength() - headLength)) {
					byte[] content = new byte[headBodyBaseMessage.getTotalLength() - headLength];
					buf.get(content, 0, content.length);
                    headBodyBaseMessage.setContent(content);
                    // out.write(headBodyBaseMessage);
					list.add(headBodyBaseMessage);
				} else {
					buf.reset();
					break;
				}
			}
			if (list.size() > 0) {
				out.write(list);
			}
			if (buf.hasRemaining()) {
				// 将数据移到buffer的最前面
				IoBuffer temp = IoBuffer.allocate(buf.limit() - buf.position());
				temp.put(buf);
				temp.flip();
				buf.clear();
				buf.put(temp);
			} else {// 如果数据已经处理完毕，进行清空
				buf.clear();
			}
		} catch (Exception e) {
			log.error("Exception is happened!", e);
			throw new IllegalArgumentException(e);
		}
	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {

	}

	public void dispose(IoSession session) throws Exception {
		if (session.containsAttribute(SmsConstants.READ_CONTENT)) {
			session.removeAttribute(SmsConstants.READ_CONTENT);
		}
	}

}
