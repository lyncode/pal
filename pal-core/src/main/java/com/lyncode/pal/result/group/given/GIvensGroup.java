package com.lyncode.pal.result.group.given;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.pal.result.group.Group;
import com.lyncode.pal.syntax.given.GivensStore;

import static com.lyncode.pal.utils.JtwigUtils.render;

public class GivensGroup implements Group {
    private final GivensStore store;

    public GivensGroup(GivensStore store) {
        this.store = store;
    }

    @Override
    public String title() {
        return "Interesting Givens";
    }

    @Override
    public String icon() {
        return "fa-file-text";
    }

    @Override
    public String content() {
        try {
            return render("/pal/templates/groups/givens.twig.html",
                    new JtwigModelMap()
                            .withModelAttribute("store", store)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean shown() {
        if (store != null)
            return !store.isEmpty();
        return false;
    }
}
