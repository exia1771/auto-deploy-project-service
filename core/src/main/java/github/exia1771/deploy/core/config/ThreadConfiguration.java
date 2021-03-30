package github.exia1771.deploy.core.config;

import github.exia1771.deploy.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Configuration
public class ThreadConfiguration {

	@Value("${thread-size:5}")
	private int threadSize;

	@Bean
	public ExecutorService fixedThreadPool() {
		Thread.UncaughtExceptionHandler exceptionHandler = (t, e) -> {
			throw new ServiceException(e);
		};

		ThreadFactory threadFactory = r -> {
			Thread thread = new Thread(r);
			thread.setUncaughtExceptionHandler(exceptionHandler);
			return thread;
		};

		return Executors.newFixedThreadPool(threadSize, threadFactory);
	}

}
