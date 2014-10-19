package com.lyncode.pal.render.model;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.lyncode.pal.model.PalTestCase;
import com.lyncode.pal.model.PalTestGroup;
import com.lyncode.pal.model.Status;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class GroupModel {
    private final PalTestGroup palTestGroup;
    private final ArrayList<TestCaseModel> tests;

    public GroupModel(PalTestGroup palTestGroup) {
        this.palTestGroup = palTestGroup;
        this.tests = new ArrayList<TestCaseModel>(
                Collections2.transform(palTestGroup.testCases(), new Function<PalTestCase, TestCaseModel>() {
                    @Nullable
                    @Override
                    public TestCaseModel apply(@Nullable PalTestCase input) {
                        return new TestCaseModel(input);
                    }
                })
        );
    }

    public String name () {
        return palTestGroup.getPackage();
    }

    public int count (String status) {
        return palTestGroup.count(Status.valueOf(status));
    }

    public boolean hasErrors () {
        return palTestGroup.count(Status.Failed) > 0;
    }

    public ArrayList<TestCaseModel> getTests() {
        return tests;
    }
}
