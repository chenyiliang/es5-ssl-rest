package com.github.chenyiliang.es5sslrest;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

public class Es5SslRestTest3 {

	public static void main(String[] args) throws Exception {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(new ClassPathResource("truststore.jks").getInputStream(), "t123456".toCharArray());
		SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
		sslContextBuilder.loadTrustMaterial(keyStore, new TrustSelfSignedStrategy());
		SSLContext sslContext = sslContextBuilder.build();
		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslSocketFactory).build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.setConnectionManager(connectionManager).build();
		HttpGet get = new HttpGet("https://121.40.108.158:9200");
		HttpResponse response = httpClient.execute(get);
		String string = StreamUtils.copyToString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		System.out.println(string);
	}

}
