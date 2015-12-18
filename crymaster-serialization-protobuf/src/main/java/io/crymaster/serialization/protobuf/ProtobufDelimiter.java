package io.crymaster.serialization.protobuf;

import com.google.protobuf.GeneratedMessage;
import io.crymaster.serialization.Delimiter;

public class ProtobufDelimiter implements Delimiter<GeneratedMessage> {

	@Override
	public byte[] get(final GeneratedMessage value) {

		final int length = value.getSerializedSize();
		return new byte[]{(byte)(length >>> 24), (byte)(length >>> 16), (byte)(length >>> 8), (byte)length};

	}

}
