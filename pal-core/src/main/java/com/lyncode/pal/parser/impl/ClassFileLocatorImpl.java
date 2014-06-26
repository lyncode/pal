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

package com.lyncode.pal.parser.impl;

import com.google.common.base.Function;
import com.lyncode.pal.parser.api.ClassFileLocator;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.*;

import static com.google.common.collect.Collections2.transform;
import static java.util.Arrays.asList;

public class ClassFileLocatorImpl implements ClassFileLocator {
    private Map<String, List<File>> roots = new TreeMap<String, List<File>>();

    @Override
    public File locationOf(Class<?> typeClass) throws FileNotFoundException {
        String child = pathFor(typeClass);
        List<File> roots = roots(typeClass);
        for (File file : roots) {
            File fileTry = new File(file, child);
            if (fileTry.exists())
                return fileTry;
        }
        throw new FileNotFoundException();
    }

    private String pathFor (Class<?> typeClass) {
        return StringUtils.join(typeClass.getName().split("\\."), File.separator) + ".java";
    }

    private List<File> roots(Class<?> typeClass) {
        String[] split = typeClass.getName().split("\\.");
        if (split.length > 0) return roots(split[0]);
        else throw new RuntimeException("Buddy only supports test classes stored in some package (empty package not allowed)");
    }

    private List<File> roots(String root) {
        if (!roots.containsKey(root))
            roots.put(root, locate(new File("."), root));
        return roots.get(root);
    }

    private List<File> locate(File start, final String root) {
        Collection<List<File>> transform = transform(asList(start.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        })), new Function<File, List<File>>() {
            @Nullable
            @Override
            public List<File> apply(@Nullable File input) {
                return input.getName().equals(root) ?
                        asList(input.getParentFile()) :
                        locate(input, root);
            }
        });

        List<File> result = new ArrayList<File>();
        for (List<File> files : transform) {
            for (File file : files) {
                result.add(file);
            }
        }
        return result;
    }
}
