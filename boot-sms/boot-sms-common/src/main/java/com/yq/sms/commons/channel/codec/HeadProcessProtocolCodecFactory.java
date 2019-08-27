package com.yq.sms.commons.channel.codec;

import com.yq.kernel.enu.OperatorTypeEnum;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class HeadProcessProtocolCodecFactory implements ProtocolCodecFactory {

	private final HeadProcessProtocolEncoder encoder;

	private final HeadProcessProtocolDecoder decoder;

	public HeadProcessProtocolCodecFactory(OperatorTypeEnum operatorType) {
		encoder = new HeadProcessProtocolEncoder();
		decoder = new HeadProcessProtocolDecoder(operatorType);
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

}
