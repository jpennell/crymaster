package io.crymaster.core;

import io.crymaster.annotations.In;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExampleResource {

	@In
	public void process(final ExampleMessage message) {

		log.debug("{}", message);

	}

}
