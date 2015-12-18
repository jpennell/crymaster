package io.crymaster.serialization.protobuf;

import com.google.protobuf.GeneratedMessage;
import io.crymaster.serialization.Delimiter;
import io.crymaster.serialization.Deserializer;
import io.crymaster.serialization.SerializationException;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public class ProtobufDeserializer<T extends GeneratedMessage> implements Deserializer<T> {

	final Delimiter<T> delimiter;

	public ProtobufDeserializer(final Delimiter<T> delimiter) {
		this.delimiter = delimiter;
	}

	@Override
	public T deserialize(final InputStream value) {

		try {

			//TODO:
			return null;

		} catch (final Exception e) {
			log.error("Failed to deserialize protobuf message", e);
			throw new SerializationException("Failed to deserialize protobuf message", e);
		}

	}

}
