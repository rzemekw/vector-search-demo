package com.ittouch.vectorsearchdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;

@SpringBootApplication(exclude = {ElasticsearchDataAutoConfiguration.class})
public class VectorSearchDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(VectorSearchDemoApplication.class, args);
	}

}
