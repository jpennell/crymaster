package io.crymaster.serialization.protobuf;

import com.google.common.io.ByteSource;
import com.google.protobuf.GeneratedMessage;
import io.crymaster.serialization.Delimiter;
import io.crymaster.serialization.SerializationException;
import io.crymaster.serialization.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@Slf4j
public class ProtobufSerializer implements Serializer<GeneratedMessage> {

	final Delimiter<GeneratedMessage> delimiter;

	public ProtobufSerializer(final Delimiter<GeneratedMessage> delimiter) {
		this.delimiter = delimiter;
	}

	@Override
	public OutputStream serialize(final GeneratedMessage value) {

		final OutputStream out = new ByteArrayOutputStream();

		final ByteSource concat = ByteSource.concat(
			ByteSource.wrap(this.delimiter.get(value)),
			ByteSource.wrap(value.toByteArray()));

		try {

			concat.copyTo(out);
			return out;

		} catch (final Exception e) {
			log.error("Failed to serialize protobuf message", e);
			throw new SerializationException("Failed to serialize protobuf message", e);
		}

	}

}
