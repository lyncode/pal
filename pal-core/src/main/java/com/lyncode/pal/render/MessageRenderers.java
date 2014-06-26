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

import com.lyncode.pal.render.renderers.DefaultMessageRenderer;

import java.util.ArrayList;
import java.util.List;

public class MessageRenderers {
    private static List<MessageRenderer> renderers = new ArrayList<MessageRenderer>();
    private static MessageRenderer defaultRenderer = new DefaultMessageRenderer();

    public static void register(MessageRenderer renderer) {
        renderers.add(0, renderer);
    }

    public static MessageRenderer.Message render(Object object) {
        if (object == null) return new MessageRenderer.Message("Empty Message", "<b>Nothing</b>");
        for (MessageRenderer renderer : renderers) {
            if (renderer.supports(object.getClass()))
                return renderer.render(object);
        }
        return defaultRenderer.render(object);
    }
}
