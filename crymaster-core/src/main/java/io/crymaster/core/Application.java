package io.crymaster.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.crymaster.io.tcp.Server;

public abstract class Application {

	private ActorSystem system;

	public final void run() {

		this.system = ActorSystem.create(this.getName());

		final ActorRef resourceActor = this.system.actorOf(ResourceActor.props(this.getClass().getPackage().getName()));
		this.system.actorOf(Server.props(this.getConfig(), resourceActor));

	}

	public Config getConfig() {
		return ConfigFactory.load();
	}

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public ActorSystem system() {
		return this.system;
	}

}
