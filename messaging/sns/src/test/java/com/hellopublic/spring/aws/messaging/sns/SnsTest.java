package com.hellopublic.spring.aws.messaging.sns;

import com.hellopublic.spring.aws.messaging.Helper;
import com.hellopublic.spring.aws.messaging.TestPayloadDto;
import com.hellopublic.spring.aws.messaging.core.EventPublisher;
import com.hellopublic.spring.aws.messaging.core.EventPublisherFactory;
import com.hellopublic.spring.aws.messaging.core.defaults.DefaultHeaderExtractor;
import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.spi.JsonConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestApplication.class)
public class SnsTest {

    @Autowired
    protected EventPublisherFactory eventPublisherFactory;

    @Autowired
    private SnsClient snsClient;

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private TestTopicArnResolver testTopicArnResolver;

    @Autowired
    private JsonConverter jsonConverter;
    private String queueUrl;

    @Before
    public void setupTopic() {

        String topicArn = snsClient.createTopic(CreateTopicRequest.builder().name("test-topic").build()).topicArn();
        queueUrl = sqsClient.createQueue(CreateQueueRequest.builder().queueName("test-queue").build()).queueUrl();
        snsClient.subscribe(builder -> builder.topicArn(topicArn).endpoint(queueUrl).protocol("sqs").attributes(Map.of("RawMessageDelivery", "true")));
        testTopicArnResolver.setTopicArn(topicArn);
    }

    @Test
    public void given_message_is_published_it_is_sent_to_sns() {

        EventPublisher publisher = eventPublisherFactory.createPublisher(new Destination(Helper.DESTINATION, SnsMessageRouterFactory.SNS_DESTINATION));

        TestPayloadDto payload = Helper.createPayload();
        publisher.send(payload);

        ReceiveMessageResponse response = sqsClient.receiveMessage(ReceiveMessageRequest.builder().queueUrl(queueUrl)
                .messageAttributeNames(DefaultHeaderExtractor.CLASS_HEADER_NAME, DefaultHeaderExtractor.EVENT_HEADER_NAME).build());

        assertThat(response.messages()).hasSize(1);
        software.amazon.awssdk.services.sqs.model.Message awsMessage = response.messages().get(0);

        String body = awsMessage.body();
        assertThat(body).isEqualTo(jsonConverter.toJson(payload));

        assertThat(awsMessage.messageAttributes()).hasSize(2);
        assertThat(awsMessage.messageAttributes().get(DefaultHeaderExtractor.CLASS_HEADER_NAME).stringValue()).isEqualTo(payload.getClass().getName());
        assertThat(awsMessage.messageAttributes().get(DefaultHeaderExtractor.EVENT_HEADER_NAME).stringValue()).isEqualTo(payload.getClass().getSimpleName());


        String receiptHandle = awsMessage.receiptHandle();
        sqsClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(receiptHandle).build());




    }
}
