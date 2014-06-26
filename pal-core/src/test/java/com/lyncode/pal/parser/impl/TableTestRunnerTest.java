/**
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

package com.lyncode.pal.parser.impl;

import com.lyncode.pal.PalTest;
import com.lyncode.pal.syntax.flow.CommunicationBuilder;
import com.lyncode.pal.syntax.flow.CommunicationStore;
import com.lyncode.pal.syntax.given.GivensBuilder;
import com.lyncode.pal.syntax.given.GivensStore;
import org.junit.Test;

public class TableTestRunnerTest extends PalTest {
    private static final String SOME_VALUE = "Value";

    @Test
    public void passing() throws Exception {
        given(oneSmallThing().withSome(SOME_VALUE).withSome("Value").withSome("Value").withSome("Value").withSome("Value").withSome("Value").withSome("Value").withSome("Value"));
        when(somethingHappens());
    }


    private CommunicationBuilder somethingHappens() {
        return new CommunicationBuilder() {
            @Override
            public CommunicationStore apply(CommunicationStore communications) {
                return communications;
            }
        };
    }

    private A oneSmallThing() {
        return new A();
    }

    private static class A implements GivensBuilder {
        @Override
        public GivensStore apply(GivensStore givensStore) {
            return givensStore;
        }

        public A withSome (String a) {
            return this;
        }
    }
}
