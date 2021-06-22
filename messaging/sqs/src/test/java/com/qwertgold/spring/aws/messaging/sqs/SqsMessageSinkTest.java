package com.qwertgold.spring.aws.messaging.sqs;

import com.qwertgold.spring.aws.messaging.Helper;
import com.qwertgold.spring.aws.messaging.TestPayloadDto;
import com.qwertgold.spring.aws.messaging.core.EventPublisher;
import com.qwertgold.spring.aws.messaging.core.EventPublisherFactory;
import com.qwertgold.spring.aws.messaging.core.domain.Destination;
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

import static com.qwertgold.spring.aws.messaging.sqs.SqsMessageSinkFactory.SQS_DESTINATION;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TestApplication.class)
public class SqsMessageSinkTest {

    @Autowired
    protected EventPublisherFactory eventPublisherFactory;

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private SqsUrlResolver urlResolver;

    @Autowired
    private DefaultSqsRequestBuilder sqsRequestBuilder;

    @Before
    public void createQueue() {
        sqsClient.createQueue(CreateQueueRequest.builder().queueName(Helper.DESTINATION).build());
    }

    @Test
    public void given_message_is_received_it_is_readable_from_localstack() throws Exception {

        EventPublisher publisher = eventPublisherFactory.createPublisher(new Destination(Helper.DESTINATION, SQS_DESTINATION));

        TestPayloadDto payload = Helper.createPayload();
        publisher.send(payload);


        String queueUrl = urlResolver.getQueueUrl(Helper.DESTINATION);
        ReceiveMessageResponse response = sqsClient.receiveMessage(ReceiveMessageRequest.builder().queueUrl(queueUrl).build());
        assertThat(response.messages()).hasSize(1);
        software.amazon.awssdk.services.sqs.model.Message awsMessage = response.messages().get(0);

        String body = awsMessage.body();
        assertThat(body).isEqualTo(sqsRequestBuilder.json(payload));

        String receiptHandle = awsMessage.receiptHandle();
        sqsClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(receiptHandle).build());
    }
}
