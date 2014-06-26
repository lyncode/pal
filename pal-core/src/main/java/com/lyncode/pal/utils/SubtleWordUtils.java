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

package com.lyncode.pal.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class SubtleWordUtils {
    public static String sentencify(String singleWord, boolean firstCapital) {
        String[] words = singleWord.split("(?=\\p{Upper})");
        if (words.length > 0) {
            if (firstCapital)
                words[0] = WordUtils.capitalize(words[0]);
            else
                words[0] = WordUtils.uncapitalize(words[0]);
            for (int i = 1;i<words.length;i++)
                words[i] = WordUtils.uncapitalize(words[i]);
        }

        return StringUtils.join(words, " ");
    }
}
