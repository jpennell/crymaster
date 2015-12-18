package io.crymaster.serialization;

import java.io.InputStream;

public interface Deserializer<T> {

	T deserialize(final InputStream value);

}
