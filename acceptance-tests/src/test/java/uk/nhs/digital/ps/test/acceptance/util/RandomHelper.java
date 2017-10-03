package uk.nhs.digital.ps.test.acceptance.util;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toList;

public class RandomHelper {

    /**
     * Number of unique values held by type {@code byte}.
     */
    private static final int BYTE_VALUES_COUNT = 256;


    /**
     * @return A random string of fixed length (delegates to {@linkplain UUID#randomUUID()}).
     */
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
