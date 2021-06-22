package com.qwertgold.spring.aws.messaging.sns;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

// don't do componentScan, to ensure that auto configuration works
@SpringBootConfiguration
@EnableAutoConfiguration
public class TestApplication {

    @Bean
    public SnsClient snsClient(LocalStackContainer localStackContainer) {
        return SnsClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.SNS))
                .build();
    }

    @Bean
    public SqsClient sqsClient(LocalStackContainer localStackContainer) {
        return SqsClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.SQS))
                .build();
    }

    @Bean
    public TestTopicArnResolver testTopicArnResolver() {
        return new TestTopicArnResolver();
    }


    /**
     * We need to know the URL for SQS, so we make localstack a managed bean instead of using ClassRule
     */
    @Bean(destroyMethod = "stop")
    public LocalStackContainer localStackContainer() {
        LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.11.1"))
                .withReuse(true)
                .withLabel("label", "sns")
                .withServices(LocalStackContainer.Service.SNS, LocalStackContainer.Service.SQS);
        // we start this now so we don't need to coordinate phases
        localStackContainer.start();
        return localStackContainer;

    }

}