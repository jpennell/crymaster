package io.crymaster.io.tcp;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import akka.japi.pf.ReceiveBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerHandler extends AbstractActor {

	public static Props props(final ActorRef resourceActor) {
		return Props.create(ServerHandler.class, resourceActor);
	}

	public ServerHandler(final ActorRef resourceActor) {

		this.receive(ReceiveBuilder
			.match(Tcp.ConnectionClosed.class, this::handleTcpConnectionClosed)
			.match(Tcp.Received.class, this::handleTcpReceived)
			.matchAny(this::unhandled)
			.build());

	}

	private void handleTcpConnectionClosed(final Tcp.ConnectionClosed tcpConnectionClosed) {

		log.debug("{}", tcpConnectionClosed);

	}

	private void handleTcpReceived(final Tcp.Received handleTcpReceived) {

		log.debug("{}", handleTcpReceived);
		final byte[] bytes = handleTcpReceived.data().toArray();

		//TODO: Deserialization
		//TODO: Tell resourceActor

	}

}
