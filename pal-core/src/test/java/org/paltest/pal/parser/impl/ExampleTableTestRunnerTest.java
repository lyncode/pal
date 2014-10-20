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

package org.paltest.pal.parser.impl;

import org.paltest.pal.PalTest;
import org.paltest.pal.junit.annotations.Row;
import org.paltest.pal.junit.annotations.Table;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ExampleTableTestRunnerTest extends PalTest {
    @Test
    @Table({
            @Row({ "hello" })
    })
    public void testName(String input) throws Exception {
        assertThat(input, equalTo("hello"));
    }
}
