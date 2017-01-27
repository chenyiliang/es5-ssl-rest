package com.github.chenyiliang.es5sslrest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Es5SslRestTest {
	private static final AbstractApplicationContext CONTEXT = new ClassPathXmlApplicationContext("spring-rest-ssl.xml");
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			CONTEXT.close();
		}));
	}

	public static void main(String[] args) {
		RestTemplate restTemplate = CONTEXT.getBean(RestTemplate.class);

		String plainCreds = "admin:admin";
		byte[] plainCredsBytes = plainCreds.getBytes(StandardCharsets.UTF_8);
		String base64Creds = Base64.getEncoder().encodeToString(plainCredsBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		HttpEntity<String> request = new HttpEntity<String>(headers);

		ResponseEntity<String> response = restTemplate.exchange("https://121.40.108.158:9200", HttpMethod.GET, request, String.class);
		System.out.println(response.getBody());
	}

}
