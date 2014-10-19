package com.lyncode.pal.render.model;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.lyncode.pal.model.PalTestGroup;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class IndexModel {
    private final Collection<GroupModel> groups;

    public IndexModel(Collection<PalTestGroup> groups) {
        this.groups = new ArrayList<GroupModel>(Collections2.transform(sort(new ArrayList<PalTestGroup>(groups)), new Function<PalTestGroup, GroupModel>() {
            @Nullable
            @Override
            public GroupModel apply(@Nullable PalTestGroup input) {
                return new GroupModel(input);
            }
        }));
    }

    private Collection<PalTestGroup> sort(ArrayList<PalTestGroup> palTestGroups) {
        Collections.sort(palTestGroups);
        return palTestGroups;
    }

    public int count (String status) {
        int sum = 0;
        for (GroupModel palTestGroup : groups) {
            sum += palTestGroup.count(status);
        }
        return sum;
    }

    public Collection<GroupModel> groups() {
        return groups;
    }

    public boolean hasErrors () {
        return count("Failed") > 0;
    }
}
