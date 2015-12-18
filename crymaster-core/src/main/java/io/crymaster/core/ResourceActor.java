package io.crymaster.core;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.crymaster.annotations.In;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ResourceActor extends AbstractActor {

	public static Props props(final String path) {
		return Props.create(ResourceActor.class, path);
	}

	private Injector injector;

	public ResourceActor(final String path) {

		final Reflections reflections = new Reflections(new ConfigurationBuilder()
			.forPackages(path)
			.setScanners(new SubTypesScanner(), new TypeAnnotationsScanner(), new MethodAnnotationsScanner())
		);

		final Set<Method> methods = reflections.getMethodsAnnotatedWith(In.class);
		final List<Class<?>> classes = methods.stream().map(Method::getDeclaringClass).distinct().collect(Collectors.toList());

		final String newline = System.lineSeparator();
		final char tab = (char)9;
		final StringBuilder builder = new StringBuilder()
			.append("Scanning ").append(path).append(" for resources");

		if(methods.isEmpty()) {
			builder.append(newline).append(tab).append("- ").append("NONE");
		} else {
			methods.forEach(m -> {
				final Class<?> declaringClass = m.getDeclaringClass();
				final Class<?> parameterTypeClass = Arrays.asList(m.getParameterTypes()).get(0);
				builder.append(newline).append(tab).append("- ").append(declaringClass).append(" -> ").append(parameterTypeClass);
			});
		}

		log.debug("{}", builder);

		this.injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				classes.forEach(this::bind);
			}

		});

		this.receive(ReceiveBuilder
			.matchAny(
				any -> methods.stream()
					.filter(m -> m.getParameterTypes()[0].equals(any.getClass()))
					.forEach(m -> invoke(m, any))
			).build());

	}

	@SneakyThrows
	private void invoke(final Method method, final Object any) {
		method.invoke(this.injector.getInstance(method.getDeclaringClass()), any);
	}

}

