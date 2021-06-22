package com.qwertgold.spring.aws.messaging.sqs;

import com.qwertgold.spring.aws.messaging.Helper;
import com.qwertgold.spring.aws.messaging.TestPayloadDto;
import com.qwertgold.spring.aws.messaging.core.EventPublisher;
import com.qwertgold.spring.aws.messaging.core.EventPublisherFactory;
import com.qwertgold.spring.aws.messaging.core.defaults.DefaultHeaderExtractor;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
import com.qwertgold.spring.aws.messaging.core.domain.Header;
import com.qwertgold.spring.aws.messaging.core.spi.JsonConverter;
import com.qwertgold.spring.aws.messaging.sqs.defaults.DefaultSqsRequestBuilder;
import com.qwertgold.spring.aws.messaging.sqs.spi.SqsUrlResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import static com.qwertgold.spring.aws.messaging.sqs.SqsMessageRouterFactory.SQS_DESTINATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestApplication.class)
public class SqsMessageRouterTest {

    @Autowired
    protected EventPublisherFactory eventPublisherFactory;

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private SqsUrlResolver urlResolver;


    @Autowired
    private JsonConverter jsonConverter;

    @Before
    public void createQueue() {
        sqsClient.createQueue(CreateQueueRequest.builder().queueName(Helper.DESTINATION).build());
    }

    @Test
    public void given_message_is_received_it_is_readable_from_localstack() {

        EventPublisher publisher = eventPublisherFactory.createPublisher(new Destination(Helper.DESTINATION, SQS_DESTINATION));

        TestPayloadDto payload = Helper.createPayload();
        publisher.send(payload);


        String queueUrl = urlResolver.getQueueUrl(Helper.DESTINATION);
        ReceiveMessageResponse response = sqsClient.receiveMessage(ReceiveMessageRequest.builder().queueUrl(queueUrl)
                        .messageAttributeNames(DefaultHeaderExtractor.CLASS_HEADER_NAME, DefaultHeaderExtractor.EVENT_HEADER_NAME).build());

        assertThat(response.messages()).hasSize(1);
        software.amazon.awssdk.services.sqs.model.Message awsMessage = response.messages().get(0);

        assertThat(awsMessage.messageAttributes()).hasSize(2);
        assertThat(awsMessage.messageAttributes().get(DefaultHeaderExtractor.CLASS_HEADER_NAME).stringValue()).isEqualTo(payload.getClass().getName());
        assertThat(awsMessage.messageAttributes().get(DefaultHeaderExtractor.EVENT_HEADER_NAME).stringValue()).isEqualTo(payload.getClass().getSimpleName());

        String body = awsMessage.body();
        assertThat(body).isEqualTo(jsonConverter.toJson(payload));

        String receiptHandle = awsMessage.receiptHandle();
        sqsClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(receiptHandle).build());
    }
}
