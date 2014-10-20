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

package org.paltest.pal.syntax;

import org.hamcrest.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;

public class SyntacticSugar {
    public static <T> T and (T value) {
        return value;
    }
    public static <T> T the (T value) {
        return value;
    }
    public static <T> T a (T value) {
        return value;
    }
    public static <T> T to (T value) {
        return value;
    }
    public static <T> T an (T value) {
        return value;
    }
    public static <T> T which (T value) {
        return value;
    }
    public static <T> T that (T value) {
        return value;
    }

    public static <T> T then (T value, Matcher<? super T> matcher) {
        assertThat(value, matcher);
        return value;
    }

    public static <T> T and (T value, Matcher<? super T> matcher) {
        assertThat(value, matcher);
        return value;
    }
}
