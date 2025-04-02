package com.gn.todo.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
	
	//application-secret.properties 의 key 값을 value에 넣어준다
	
	
	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;
	
	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;
	
	@Value("${cloud.aws.region.static}")
	private String region;
	
	//S3를 하는중
	@Bean
	AmazonS3Client amazonS3Client() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
		return (AmazonS3Client)AmazonS3ClientBuilder.standard()
								.withRegion(region)
								.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
								.build();
	}
	
	
	
	
	
	
	
}
