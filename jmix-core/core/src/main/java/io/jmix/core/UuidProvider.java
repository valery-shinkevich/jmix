/*
 * Copyright 2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jmix.core;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Provides static method to create UUIDs.
 */
public final class UuidProvider {

    public static final long LSB_MASK = 0x3FFFFFFFFFFFFFFFL;
    public static final long TS_MASK = 0x7FFFFFFFFFFFL;
    public static final int UUID_VERSION = 0b0111;
    public static final int VARIANT = 0b10;

    private UuidProvider() {
    }

    private static UUID createClassicUuid() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong());
    }

    private static UUID createUuid7() {
        Random random;
        try {
            random = SecureRandom.getInstance("NativePRNGNonBlocking");
        } catch (Exception e) {
            random = ThreadLocalRandom.current();
        }
        long ts = System.currentTimeMillis();
        long rand1 = random.nextLong(4096);
        long rand2 = random.nextLong();

        long msb = (((ts & TS_MASK) << 4 | UUID_VERSION) << 12) | rand1;
        long lsb = (VARIANT & LSB_MASK) << 62 | rand2 & LSB_MASK;

        return new UUID(msb, lsb);
    }

    /**
     * @return new UUID
     */
    public static UUID createUuid() {
        return createUuid7();
    }

    /**
     * Fast implementation of creating a {@code UUID} from the standard string representation.
     * <br>
     * Borrowed from Apache Jackrabbit project which is licensed under the Apache License, Version 2.0.
     * See http://www.apache.org/licenses/LICENSE-2.0.
     *
     * @param str   a string according to {@link UUID#toString()} rules
     * @return      UUID instance
     */
    public static UUID fromString(String str) {
        if (str == null)
            return null;

        if (str.length() != 36) {
            if (!str.contains("-") && str.length() == 32) {
                str = str.substring(0, 8) + "-"
                        + str.substring(8, 12) + "-"
                        + str.substring(12, 16) + "-"
                        + str.substring(16, 20) + "-"
                        + str.substring(20);
            } else {
                throw new IllegalArgumentException(str);
            }
        }
        long m = 0, x = 0;
        for (int i = 0; i < 36; i++) {
            int c = str.charAt(i);
            switch (i) {
                case 18:
                    m = x;
                    x = 0;
                    // fall through
                case 8:
                case 13:
                case 23:
                    if (c != '-') {
                        throw new IllegalArgumentException(str);
                    }
                    break;
                default:
                    if (c >= '0' && c <= '9') {
                        x = (x << 4) | (c - '0');
                    } else if (c >= 'a' && c <= 'f') {
                        x = (x << 4) | (c - 'a' + 0xa);
                    } else if (c >= 'A' && c <= 'F') {
                        x = (x << 4) | (c - 'A' + 0xa);
                    } else {
                        throw new IllegalArgumentException(str);
                    }
            }
        }
        return new UUID(m, x);
    }
}