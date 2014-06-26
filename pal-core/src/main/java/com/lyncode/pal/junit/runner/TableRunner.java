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

package com.lyncode.pal.junit.runner;

import com.lyncode.pal.junit.annotations.Row;
import com.lyncode.pal.junit.annotations.Table;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TableRunner extends BlockJUnit4ClassRunner {
    private org.junit.runner.manipulation.Filter filter;

    public TableRunner(Class<?> typeClass) throws InitializationError {
        super(typeClass);
    }

    @Override
    protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
        // skip
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        List<FrameworkMethod> result = new ArrayList<FrameworkMethod>();
        List<FrameworkMethod> methods = computeTestMethods();
        for (FrameworkMethod method : methods) {
            final Table annotation = method.getAnnotation(Table.class);
            if (annotation == null) {
                result.add(new DecoratingFrameworkMethod(method, null));
            } else {
                for (Row row : annotation.value()) {
                    result.add(new DecoratingFrameworkMethod(method, row));
                }
            }
        }
        return result;
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if (filter == null)
            return super.computeTestMethods();

        List<FrameworkMethod> list = super.computeTestMethods();
        Iterator<FrameworkMethod> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (!filter.shouldRun(describeChild(iterator.next())))
                iterator.remove();
        }

        return list;
    }

    @Override
    public void filter(org.junit.runner.manipulation.Filter filter) throws NoTestsRemainException {
        this.filter = filter;
        if (computeTestMethods().isEmpty()) {
            throw new NoTestsRemainException();
        }
    }

    public static class DecoratingFrameworkMethod extends FrameworkMethod {
        private final Row row;

        public DecoratingFrameworkMethod(FrameworkMethod method, Row row) {
            super(method.getMethod());
            this.row = row;
        }

        @Override
        public Object invokeExplosively(Object target, Object... params) throws Throwable {
            if (row != null)
                return super.invokeExplosively(target, (Object[]) row.value());
            else
                return super.invokeExplosively(target, (Object[]) null);
        }

        @Override
        public String getName() {
            if (row == null) return super.getName();
            else {
                List<String> values = new ArrayList<String>();
                Collections.addAll(values, row.value());
                return "(" + StringUtils.join(values, ", ") + ")";
            }
        }

        public Row getRow() {
            return row;
        }
    }
}
