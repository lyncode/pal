package org.paltest.pal.render.model;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.paltest.pal.model.PalTestCase;
import org.paltest.pal.model.PalTestScenario;
import org.paltest.pal.model.Status;
import org.paltest.pal.utils.SubtleWordUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class TestCaseModel {
    private final PalTestCase input;

    public TestCaseModel(PalTestCase input) {
        this.input = input;
    }

    public int count (String status) {
        return input.count(Status.valueOf(status));
    }

    public String name () {
        String simpleName = input.type().getSimpleName();
        if (simpleName.endsWith("Test"))
            simpleName = simpleName.substring(0, simpleName.length() - 4);
        return SubtleWordUtils.sentencify(simpleName, true);
    }

    public boolean hasErrors () {
        return input.count(Status.Failed) > 0;
    }

    public String url () {
        return input.type().getName() + ".html";
    }

    public Collection<ScenarioModel> scenarios () {
        ArrayList<ScenarioModel> models = new ArrayList<ScenarioModel>(Collections2.transform(input.scenarios(), new Function<PalTestScenario, ScenarioModel>() {
            @Nullable
            @Override
            public ScenarioModel apply(@Nullable PalTestScenario input) {
                return new ScenarioModel(input);
            }
        }));
        Collections.sort(models);
        return models;
    }
}
