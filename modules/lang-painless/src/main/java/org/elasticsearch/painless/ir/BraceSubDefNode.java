/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.painless.ir;

import org.elasticsearch.painless.ClassWriter;
import org.elasticsearch.painless.DefBootstrap;
import org.elasticsearch.painless.MethodWriter;
import org.elasticsearch.painless.phase.IRTreeVisitor;
import org.elasticsearch.painless.symbol.WriteScope;
import org.objectweb.asm.Type;

public class BraceSubDefNode extends IndexNode {

    /* ---- begin visitor ---- */

    @Override
    public <Input, Output> Output visit(IRTreeVisitor<Input, Output> irTreeVisitor, Input input) {
        return irTreeVisitor.visitBraceSubDef(this, input);
    }

    /* ---- end visitor ---- */

    @Override
    protected void write(ClassWriter classWriter, MethodWriter methodWriter, WriteScope writeScope) {
        setup(classWriter, methodWriter, writeScope);
        load(classWriter, methodWriter, writeScope);
    }

    @Override
    protected int accessElementCount() {
        return 2;
    }

    @Override
    protected void setup(ClassWriter classWriter, MethodWriter methodWriter, WriteScope writeScope) {
        getIndexNode().write(classWriter, methodWriter, writeScope);
    }

    @Override
    protected void load(ClassWriter classWriter, MethodWriter methodWriter, WriteScope writeScope) {
        methodWriter.writeDebugInfo(location);

        Type methodType = Type.getMethodType(MethodWriter.getType(
                getExpressionType()), Type.getType(Object.class), MethodWriter.getType(getIndexNode().getExpressionType()));
        methodWriter.invokeDefCall("arrayLoad", methodType, DefBootstrap.ARRAY_LOAD);
    }

    @Override
    protected void store(ClassWriter classWriter, MethodWriter methodWriter, WriteScope writeScope) {
        methodWriter.writeDebugInfo(location);

        Type methodType = Type.getMethodType(Type.getType(void.class), Type.getType(Object.class),
                MethodWriter.getType(getIndexNode().getExpressionType()), MethodWriter.getType(getExpressionType()));
        methodWriter.invokeDefCall("arrayStore", methodType, DefBootstrap.ARRAY_STORE);
    }
}
