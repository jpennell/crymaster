package io.crymaster.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.CallingThreadDispatcher;
import io.crymaster.test.UnitTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.UUID;

@Slf4j
@Category(value = UnitTest.class)
public class ExampleUnitTest {

	private ActorSystem system;

	@Before
	public void before() {
		this.system = ActorSystem.create();
	}

	@After
	public void after() throws Exception {
		Await.result(this.system.terminate(), Duration.Inf());
	}

	@Test
	public void test() {

		final ActorRef processor = this.system.actorOf(
			ResourceActor.props("io.crymaster.core").withDispatcher(CallingThreadDispatcher.Id())
		);

		processor.tell(new ExampleMessage(UUID.randomUUID().toString()), ActorRef.noSender());

	}

}
