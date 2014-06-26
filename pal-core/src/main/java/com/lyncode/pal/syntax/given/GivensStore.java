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

package com.lyncode.pal.syntax.given;

import com.lyncode.pal.render.ObjectRenderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GivensStore extends HashMap<String, Object> {
    public List<Given> getList () {
        ArrayList<Given> result = new ArrayList<Given>();
        for (Map.Entry<String, Object> stringObjectEntry : this.entrySet()) {
            result.add(new Given(stringObjectEntry.getKey(), ObjectRenderers.render(stringObjectEntry.getValue())));
        }
        return result;
    }

    public GivensStore add (String name, Object value) {
        this.put(name, value);
        return this;
    }

    public <T> T get (String name, Class<T> type) {
        Object o = this.get(name);
        if (o == null) return null;
        return type.cast(o);
    }

    public static class Given {
        private final String name;
        private final String value;

        public Given(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
