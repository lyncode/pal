package com.lyncode.pal.result.group.communication;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.pal.result.group.Group;
import com.lyncode.pal.syntax.flow.CommunicationStore;
import com.lyncode.pal.utils.JtwigUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;

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
            String url = String.format("%s.html", id);
            File outputFile = new File(FileUtils.getTempDirectory(), url);
            String templateLocation = "/pal/templates/pages/diagram.twig.html";
            JtwigUtils.renderTo(outputFile, templateLocation, new JtwigModelMap().withModelAttribute("store", store));

            return render("/pal/templates/groups/communication-diagram.twig.html",
                    new JtwigModelMap()
                            .withModelAttribute("url", url)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean shown() {
        if (store != null)
            return !store.isEmpty();
        return false;
    }
}
