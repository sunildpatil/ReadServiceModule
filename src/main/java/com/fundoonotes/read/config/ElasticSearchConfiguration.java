package com.fundoonotes.read.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfiguration extends AbstractFactoryBean<RestHighLevelClient> {

	private static final Logger LOGGER = Logger.getLogger(ElasticSearchConfiguration.class);

	@Value("${spring.data.elasticsearch.cluster-name}")
	private String clusterName;

	@Value("${spring.data.elasticsearch.cluster-nodes}")
	private String clusterNodes;

	private RestHighLevelClient restHighLevelClient;

	@Override
	public void destroy() {
		if (restHighLevelClient != null) {
			try {
				restHighLevelClient.close();
			} catch (final Exception e) {
				LOGGER.error("Error closing elastic search client : " + e);
			}
		}
	}

	@Override
	public Class<RestHighLevelClient> getObjectType() {
		return RestHighLevelClient.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	protected RestHighLevelClient createInstance() throws Exception {
		return buildClient();
	}

	private RestHighLevelClient buildClient() {
		try {
			restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http"),
					new HttpHost("localhost", 9201, "http")));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return restHighLevelClient;
	}
}
