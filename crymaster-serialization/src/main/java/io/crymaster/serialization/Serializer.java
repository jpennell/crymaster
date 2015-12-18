package io.crymaster.serialization;

import java.io.OutputStream;

public interface Serializer<T> {

	OutputStream serialize(final T value);

}
