package com.hellopublic.spring.aws.messaging.core.defaults;

import com.hellopublic.spring.aws.messaging.TestPayloadDto;
import com.hellopublic.spring.aws.messaging.core.domain.Header;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class DefaultHeaderExtractorTest {


    @Test
    public void given_input_headers_are_extracted() {

        DefaultHeaderExtractor headerExtractor = new DefaultHeaderExtractor();
        Map<String, Header> map = headerExtractor.extractHeaders(new TestPayloadDto());
        assertThat(map)
                .hasSize(2)
                .contains(entry(DefaultHeaderExtractor.CLASS_HEADER_NAME, new Header(Header.STRING_TYPE, TestPayloadDto.class.getName())))
                .contains(entry(DefaultHeaderExtractor.EVENT_HEADER_NAME, new Header(Header.STRING_TYPE, TestPayloadDto.class.getSimpleName())));

    }


}
