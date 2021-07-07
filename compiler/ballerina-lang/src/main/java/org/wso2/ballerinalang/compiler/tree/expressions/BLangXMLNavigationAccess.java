/*
 *  Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.wso2.ballerinalang.compiler.tree.expressions;

import io.ballerina.tools.diagnostics.Location;
import org.ballerinalang.model.Whitespace;
import org.ballerinalang.model.tree.NodeKind;
import org.ballerinalang.model.tree.expressions.XMLNavigationAccess;
import org.wso2.ballerinalang.compiler.nballerina.ModuleGen;
import org.wso2.ballerinalang.compiler.tree.BLangNodeVisitor;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @since 1.2.0
 */
public class BLangXMLNavigationAccess extends BLangExpression implements XMLNavigationAccess {

    public BLangExpression expr;
    public final NavAccessType navAccessType;
    public final List<BLangXMLElementFilter> filters;
    public BLangExpression childIndex;
    // Hack to avoid duplicate error messages in CodeAnalyzer, the reason for this flag is since we are adding
    // the 'receiver' of a method invocation as the first parameter to langlib invocations, XMLNavigationAccess
    // could be checked multiple times when used with langlib functions producing multiple error messages.
    public boolean methodInvocationAnalyzed;

    public BLangXMLNavigationAccess(Location pos, Set<Whitespace> ws, BLangExpression expr,
                                    List<BLangXMLElementFilter> filters,
                                    NavAccessType navAccessType,
                                    BLangExpression childIndex) {
        this.pos = pos;
        this.addWS(ws);
        this.expr = expr;
        this.filters = filters;
        this.navAccessType = navAccessType;
        this.childIndex = childIndex;
    }

    @Override
    public void accept(BLangNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Object accept(ModuleGen visitor) {
        return visitor.visit(this);
    }

    @Override
    public NodeKind getKind() {
        return NodeKind.XML_NAVIGATION;
    }

    @Override
    public NavAccessType getNavAccessType() {
        return navAccessType;
    }

    @Override
    public List<BLangXMLElementFilter> getFilters() {
        return this.filters;
    }

    @Override
    public BLangExpression getExpression() {
        return this.expr;
    }

    @Override
    public BLangExpression getChildIndex() {
        return this.childIndex;
    }

    @Override
    public String toString() {
        switch (navAccessType) {
            case CHILDREN:
                return String.valueOf(expr) + "/*";
            case CHILD_ELEMS:
                StringJoiner filters = new StringJoiner(" |");
                this.filters.forEach(f -> filters.toString());
                return String.valueOf(expr) + "/<" + filters.toString() + ">" +
                        (childIndex != null ? "[" + String.valueOf(childIndex) + "]" : "");
        }
        return null;
    }
}
