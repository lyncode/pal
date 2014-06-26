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

package com.lyncode.pal.model;

import com.lyncode.pal.junit.annotations.Row;
import com.lyncode.pal.junit.annotations.Table;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class TestMethod {
    private final Method javaMethod;
    private final Map<String, Scenario> scenarios = new TreeMap<String, Scenario>();

    public TestMethod(Method method) {
        this.javaMethod = method;
    }

    public boolean hasTable () {
        return this.javaMethod.getAnnotation(Table.class) != null;
    }

    public List<Scenario> getScenarios() {
        List<Scenario> result = new ArrayList<Scenario>();
        if (hasTable()) {
            for (Row row : this.javaMethod.getAnnotation(Table.class).value())
                result.add(Scenario.from(javaMethod, row));

        } else {
            result.add(Scenario.from(javaMethod));
        }
        return result;
    }
}