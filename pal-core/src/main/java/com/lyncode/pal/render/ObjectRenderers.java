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

package com.lyncode.pal.render;

import com.lyncode.pal.render.renderers.DefaultObjectRenderer;

import java.util.ArrayList;
import java.util.List;

public class ObjectRenderers {
    private static List<ObjectRenderer> renderers = new ArrayList<ObjectRenderer>();
    private static ObjectRenderer defaultRenderer = new DefaultObjectRenderer();

    public static void register(ObjectRenderer renderer) {
        if (!renderers.contains(renderer))
            renderers.add(0, renderer);
    }

    public static String render(Object object) {
        if (object == null) return "---";
        for (ObjectRenderer renderer : renderers) {
            if (renderer.supports(object.getClass()))
                return renderer.render(object);
        }
        return defaultRenderer.render(object);
    }
}
