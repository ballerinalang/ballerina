/*
*  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.ballerinalang.model.tree.NodeKind;
import org.ballerinalang.model.tree.expressions.AwaitExpressionNode;
import org.ballerinalang.model.tree.expressions.ExpressionNode;
import org.wso2.ballerinalang.compiler.tree.BLangNodeVisitor;

/**
 * This represents the await expression implementation.
 * 
 * @since 0.965
 */
public class BLangAwaitExpr extends BLangExpression implements AwaitExpressionNode {

    private static final String WAIT_KEYWORD = "wait";
    
    public BLangExpression expr;

    @Override
    public NodeKind getKind() {
        return NodeKind.AWAIT_EXPR;
    }

    @Override
    public void accept(BLangNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return WAIT_KEYWORD + " " + String.valueOf(expr);
    }

    @Override
    public ExpressionNode getExpression() {
        return expr;
    }
}
