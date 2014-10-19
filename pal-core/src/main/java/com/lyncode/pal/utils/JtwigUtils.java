package com.lyncode.pal.utils;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.ClasspathJtwigResource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JtwigUtils {
    private static final JtwigConfiguration CONFIGURATION = new JtwigConfiguration();
    private static final Map<String, Renderable> cache = new ConcurrentHashMap<String, Renderable>();

    public static String render (String location, JtwigModelMap model) throws ParseException, CompileException, RenderException {
        Renderable renderable = template(location);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderable.render(RenderContext.create(CONFIGURATION.render(), model, outputStream));
        return outputStream.toString();
    }

    private static Renderable template(String location) throws ParseException, CompileException {
        if (!cache.containsKey(location))
            cache.put(location, new JtwigTemplate(new ClasspathJtwigResource("classpath:" + location), CONFIGURATION).compile());
        return cache.get(location);
    }

    public static void renderTo(File outputFile, String templateLocation, JtwigModelMap modelMap) throws FileNotFoundException, ParseException, CompileException, RenderException {
        template(templateLocation).render(RenderContext.create(CONFIGURATION.render(), modelMap, new FileOutputStream(outputFile)));
    }
}
