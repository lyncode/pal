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

package com.lyncode.pal.support;

import com.lyncode.builder.Builder;
import com.lyncode.pal.model.Scenario;
import com.lyncode.pal.model.Status;
import com.lyncode.pal.syntax.given.GivensBuilder;
import com.lyncode.pal.syntax.given.GivensStore;
import org.apache.commons.lang3.StringUtils;
import org.mockito.Mockito;

import javax.annotation.Nullable;

public class MockedScenarioBuilder implements GivensBuilder, Builder<Scenario> {
    public static String scenarioName (String name) {
        if (StringUtils.isBlank(name))
            return "Scenario";
        return "Scenario "+ name;
    }

    public static MockedScenarioBuilder scenario () {
        return new MockedScenarioBuilder("");
    }
    public static MockedScenarioBuilder scenario (String name) {
        return new MockedScenarioBuilder(name);
    }
    
    private final Scenario scenario = Mockito.mock(Scenario.class);
    private final String name;


    public MockedScenarioBuilder(String name) {
        this.name = name;
    }
    
    


    @Nullable
    @Override
    public GivensStore apply(@Nullable GivensStore input) {
        input.add(scenarioName(name), scenario);
        return input;
    }

    @Override
    public Scenario build() {
        return scenario;
    }

    public MockedScenarioBuilder markedAs(Status status) {
        Mockito.when(scenario.status()).thenReturn(status);
        return this;
    }
}
