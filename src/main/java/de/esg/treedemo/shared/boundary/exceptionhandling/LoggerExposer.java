package de.esg.treedemo.shared.boundary.exceptionhandling;

import java.util.function.Consumer;

import javax.enterprise.inject.Produces;

public class LoggerExposer
{
	@Produces
	public Consumer<Throwable> fatalErrorConsumer()
	{
		return LoggerExposer::printThrowable;
	}

	public static void printThrowable(Throwable t)
	{
		System.err.println("Fataler Fehler: " + t.getMessage());
	}
}
