/*
 * Copyright 2024 Haulmont.
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

package uuid

import io.jmix.core.UuidProvider
import spock.lang.Specification

class UuidProviderTest extends Specification {

    def "default uuids is unordered"() {
        List<UUID> uuids = new ArrayList<UUID>();
        for (int i = 0; i < 1_000; i++) {
            UUID uuid = UuidProvider.createUuid();
            uuids.add(uuid)
        }
        List<UUID> sorted = uuids.sort(false)
        expect:
        uuids != sorted
        uuids.sort(false) == sorted
    }

    def "test uuid7 type 1"() {
        UUID uuid = UuidProvider.createUuid7t1()
        expect:
        UuidProvider.fromString(uuid.toString()) == uuid
        uuid.version() == 7
        uuid.variant() == 0b10
    }

    def "test uuid7 type 2"() {
        UUID uuid = UuidProvider.createUuid7t2()
        expect:
        UuidProvider.fromString(uuid.toString()) == uuid
        uuid.version() == 7
        uuid.variant() == 0b10
    }

    def "test uuid7 type 3"() {
        UUID uuid = UuidProvider.createUuid7t3()
        expect:
        UuidProvider.fromString(uuid.toString()) == uuid
        uuid.version() == 7
        uuid.variant() == 0b10
    }

    def "uuid7 type 1 order test"() {
        List<UUID> uuids = new ArrayList<UUID>();
        for (int i = 0; i < 1_000; i++) {
            UUID uuid = UuidProvider.createUuid7t1();
            uuids.add(uuid)
        }
        List<UUID> sorted = uuids.sort(false)
        expect:
        uuids == sorted
    }

    def "uuid7 type 2 order test"() {
        List<UUID> uuids = new ArrayList<UUID>();
        for (int i = 0; i < 1_000; i++) {
            UUID uuid = UuidProvider.createUuid7t2();
            uuids.add(uuid)
        }
        List<UUID> sorted = uuids.sort(false)
        expect:
        uuids == sorted
    }

    def "uuid7 type 3 order test"() {
        List<UUID> uuids = new ArrayList<UUID>();
        for (int i = 0; i < 1_000; i++) {
            UUID uuid = UuidProvider.createUuid7t3();
            uuids.add(uuid)
        }
        List<UUID> sorted = uuids.sort(false)
        expect:
        uuids == sorted
    }


}