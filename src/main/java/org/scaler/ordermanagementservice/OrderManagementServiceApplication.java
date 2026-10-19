package org.scaler.ordermanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class OrderManagementServiceApplication {
	private static final Logger logger = LoggerFactory.getLogger(OrderManagementServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementServiceApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void logSwaggerUrl(ApplicationReadyEvent event) {
		Environment environment = event.getApplicationContext().getEnvironment();
		String scheme = environment.getProperty("server.ssl.enabled", Boolean.class, false) ? "https" : "http";
		int port = environment.getProperty("local.server.port", Integer.class,
				environment.getProperty("server.port", Integer.class, 8080));
		String contextPath = environment.getProperty("server.servlet.context-path", "");

		logger.info("Swagger UI available at: {}://localhost:{}{}/swagger-ui/index.html",
				scheme, port, contextPath);
	}

}
