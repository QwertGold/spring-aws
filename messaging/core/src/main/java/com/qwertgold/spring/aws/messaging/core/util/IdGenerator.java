package com.qwertgold.spring.aws.messaging.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

import java.security.SecureRandom;

/**
 * UUIDs have dashes, which can be annoying to copy
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
            .withinRange(new char[]{'0', 'z'})
            .usingRandom(random::nextInt)
            .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
            .build();

    public static String generateId() {
        return randomStringGenerator.generate(32);
    }

}
