package com.lyncode.pal.result.group.communication;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.pal.result.group.Group;
import com.lyncode.pal.syntax.flow.Communication;
import com.lyncode.pal.syntax.flow.CommunicationStore;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.lyncode.pal.utils.JtwigUtils.render;

public class CommunicationDiagramGroup implements Group {
    private final CommunicationStore store;
    private final String id;

    public CommunicationDiagramGroup(CommunicationStore store, String id) {
        this.store = store;
        this.id = id;
    }

    @Override
    public String title() {
        return "Communication Diagram";
    }

    @Override
    public String icon() {
        return "fa-desktop";
    }

    @Override
    public String content() {
        try {
            return render("/pal/templates/groups/communication-diagram.twig.html",
                    new JtwigModelMap()
                            .withModelAttribute("store", store)
                            .withModelAttribute("id", id)
                            .withModelAttribute("specification", generateSpecification(store))
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String generateSpecification(CommunicationStore store) {
        int spaces = 0;
        List<String> entities = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < store.size(); i++) {
            Communication message = store.get(i);
            if (!entities.contains(message.getOrigin())) {
                spaces = 0;
                appendSpaces(builder, spaces++);
                builder.append(String.format("@found \"%s\"", message.getOrigin()));
                entities.add(message.getOrigin());
            }
            builder.append(String.format(", ->\n"));
            appendSpaces(builder, spaces++);
            builder.append(String.format("@message \"%s\", \"%s\"", message.getMessageTitle(), message.getDestination()));
            if (!entities.contains(message.getDestination()))
                entities.add(message.getDestination());
        }
        return builder.toString();
    }

    private void appendSpaces(StringBuilder builder, int size) {
        for (int i = 0; i < (size * 2); i++) {
            builder.append(" ");
        }
    }

    @Override
    public boolean shown() {
        if (store != null)
            return !store.isEmpty();
        return false;
    }
}
