package com.github.chenyiliang.es5sslrest;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.core.io.ClassPathResource;

import com.floragunn.searchguard.ssl.SearchGuardSSLPlugin;
import com.floragunn.searchguard.ssl.util.SSLConfigConstants;

public class Es5SslTransportTest2 {
	// https://github.com/floragunncom/search-guard/issues/234
	// https://github.com/floragunncom/search-guard/blob/5.0.0/src/test/java/com/floragunn/searchguard/SGTests.java#L2156
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		ClassPathResource keystoreResource = new ClassPathResource("node-0-keystore.jks");
		ClassPathResource truststoreResource = new ClassPathResource("truststore.jks");
		Settings settings = Settings.builder().put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENFORCE_HOSTNAME_VERIFICATION, false)
				.put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENFORCE_HOSTNAME_VERIFICATION_RESOLVE_HOST_NAME, false)
				.put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_FILEPATH, keystoreResource.getFile().getAbsolutePath())
				.put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_FILEPATH, truststoreResource.getFile().getAbsolutePath())
				.put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_PASSWORD, "k123456").put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_PASSWORD, "t123456")
				.put("cluster.name", "elasticsearch").put("path.home", ".").build();
		TransportClient client = new PreBuiltTransportClient(settings, Arrays.asList(SearchGuardSSLPlugin.class))
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("121.40.108.158"), 9300));
		ClusterStatsResponse response = client.admin().cluster().prepareClusterStats().get();
		System.out.println(response.getClusterName().value());
		System.out.println(response.getIndicesStats().getIndexCount());
		client.close();
	}

}
