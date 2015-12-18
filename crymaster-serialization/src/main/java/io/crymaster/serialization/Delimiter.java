package io.crymaster.serialization;

public interface Delimiter<T> {

	byte[] get(final T value);

}
