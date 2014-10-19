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

package com.lyncode.pal;

import com.lyncode.pal.junit.runner.PalRunner;
import com.lyncode.pal.syntax.flow.CommunicationBuilder;
import com.lyncode.pal.syntax.flow.CommunicationStore;
import com.lyncode.pal.syntax.given.GivensBuilder;
import com.lyncode.pal.syntax.given.GivensStore;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(PalRunner.class)
public abstract class PalTest {
    private GivensStore givensStore;
    private CommunicationStore communicationStore;

    @Before
    public void setUpBuddy() {
        givensStore = new GivensStore();
        communicationStore = new CommunicationStore();
    }

    protected GivensStore given(GivensBuilder givens) {
        GivensStore apply = givens.apply(givensStore);
        assert apply != null;
        return apply;
    }

    protected <T> T given(T givens) {
        return givens;
    }

    protected GivensStore and(GivensBuilder givens) {
        GivensStore apply = givens.apply(givensStore);
        assert apply != null;
        return apply;
    }

    protected <T> T and(T value) {
        return value;
    }

    protected CommunicationStore when (CommunicationBuilder builder) {
        CommunicationStore apply = builder.apply(communicationStore);
        assert apply != null;
        return apply;
    }

    protected CommunicationStore and(CommunicationBuilder builder) {
        CommunicationStore apply = builder.apply(communicationStore);
        assert apply != null;
        return apply;
    }

    protected <T> T when(T value) {
        return value;
    }

    public GivensStore givens () {
        return givensStore;
    }

    public CommunicationStore communications () {
        return communicationStore;
    }
}
