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

package org.wso2.ballerinalang.compiler.tree.clauses;

import org.ballerinalang.model.clauses.WhereClauseNode;
import org.ballerinalang.model.tree.NodeKind;
import org.ballerinalang.model.tree.expressions.ExpressionNode;
import org.wso2.ballerinalang.compiler.nballerina.ModuleGen;
import org.wso2.ballerinalang.compiler.semantics.model.SymbolEnv;
import org.wso2.ballerinalang.compiler.tree.BLangNode;
import org.wso2.ballerinalang.compiler.tree.BLangNodeVisitor;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangExpression;

/**
 * Implementation of "where" clause statement.
 *
 * @since 1.2.0
 */
public class BLangWhereClause extends BLangNode implements WhereClauseNode {

    public SymbolEnv env;
    public BLangExpression expression;

    public BLangWhereClause() {
    }

    @Override
    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public void setExpression(ExpressionNode expression) {
        this.expression = (BLangExpression) expression;
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
        return NodeKind.WHERE;
    }

    @Override
    public String toString() {
        return "where " + expression;
    }
}
