package org.paltest.pal.result.group.specification;

import com.lyncode.jtwig.JtwigModelMap;
import org.paltest.pal.result.group.Group;

import static org.paltest.pal.utils.JtwigUtils.render;

public class SpecificationGroup implements Group {
    private final String text;

    public SpecificationGroup(String specification) {
        this.text = specification;
    }

    @Override
    public String title() {
        return "Specification";
    }

    @Override
    public String icon() {
        return "fa-file-text-o";
    }

    @Override
    public String content() {
        try {
            return render("/pal/templates/groups/specification.twig.html",
                    new JtwigModelMap()
                            .withModelAttribute("specification", text)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean shown() {
        return !text.isEmpty();
    }
}
