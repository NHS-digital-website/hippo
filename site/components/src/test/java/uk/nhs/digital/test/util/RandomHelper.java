package uk.nhs.digital.test.util;

import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toList;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public class RandomHelper {

    public static int newRandomInt() {
        return new Random().nextInt(1024);
    }

    public static String newRandomString() {
        return UUID.randomUUID().toString();
    }

    /**
     * @return An array of 256 bytes emulating binary content rather being a random text. The array contains
     *         all values possible for {@code byte} type in random order.
     */
    public static byte[] newRandomByteArray() {

        // @formatter:off
        final List<Byte> byteList = IntStream.rangeClosed(Byte.MIN_VALUE, Byte.MAX_VALUE)
            .boxed()
            .map(Integer::byteValue)
            .collect(toList());
        // @formatter:on

        shuffle(byteList);

        return ArrayUtils.toPrimitive(byteList.toArray(new Byte[0]));
    }
}
