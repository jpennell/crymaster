package io.crymaster.io.tcp;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.japi.pf.ReceiveBuilder;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class Server extends AbstractActor {

	public static Props props(final Config config, final ActorRef resourceActor) {
		return Props.create(Server.class, config, resourceActor);
	}

	private final Config config;

	private final ActorRef resourceActor;

	public Server(final Config config, final ActorRef resourceActor) {

		this.config = config;
		this.resourceActor = resourceActor;

		this.receive(ReceiveBuilder
			.match(Tcp.Bound.class, this::handleTcpBound)
			.match(Tcp.CommandFailed.class, this::handleTcpCommandFailed)
			.match(Tcp.Connected.class, this::handleTcpConnected)
			.matchAny(this::unhandled)
			.build());

	}

	private void handleTcpBound(final Tcp.Bound tcpBound) {

		log.debug("{}", tcpBound);

	}

	private void handleTcpCommandFailed(final Tcp.CommandFailed tcpCommandFailed) {

		log.error("{}", tcpCommandFailed);

		this.context().stop(this.self());

	}

	private void handleTcpConnected(final Tcp.Connected tcpConnected) {

		log.debug("{}", tcpConnected);

		final ActorRef handler = this.context().actorOf(ServerHandler.props(this.resourceActor));
		this.sender().tell(TcpMessage.register(handler), this.self());

	}

	@Override
	public void preStart() throws Exception {

		final ActorRef tcp = Tcp.get(this.context().system()).manager();

		final String host = this.config.getString("server.host");
		final int port = this.config.getInt("server.port");

		tcp.tell(TcpMessage.bind(this.self(), new InetSocketAddress(host, port), 100), this.self());

	}

}
