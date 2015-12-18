package io.crymaster.serialization.protobuf;

import com.google.protobuf.GeneratedMessage;
import io.crymaster.serialization.Deserializer;
import io.crymaster.serialization.Serializer;
import io.crymaster.test.UnitTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.OutputStream;
import java.util.UUID;

@Slf4j
@Category(value = UnitTest.class)
public class ProtobufSerializationTest {

	private Serializer<GeneratedMessage> serializer;

	private Deserializer<GeneratedMessage> deserializer;

	@Before
	public void before() {
		final ProtobufDelimiter protobufDelimiter = new ProtobufDelimiter();
		this.serializer = new ProtobufSerializer(protobufDelimiter);
		this.deserializer = new ProtobufDeserializer(protobufDelimiter);
	}

	@Test
	public void test() {

		final String text = UUID.randomUUID().toString();

		final ExampleProtocol.Example example = ExampleProtocol.Example.newBuilder()
			.setText(text)
			.build();

		final OutputStream serialize = serializer.serialize(example);

		log.debug("{}", example);
		log.debug("{}", example.toByteArray());
		log.debug("{}", serialize);

	}

}
