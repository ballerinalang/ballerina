/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.ballerina.core.model.statements;

import org.wso2.ballerina.core.model.LinkedNodeExecutor;
import org.wso2.ballerina.core.model.NodeExecutor;
import org.wso2.ballerina.core.model.NodeLocation;
import org.wso2.ballerina.core.model.NodeVisitor;
import org.wso2.ballerina.core.model.expressions.Expression;

/**
 * {@code ReturnStmt} represents a return statement.
 *
 * @since 0.8.0
 */
public class ReturnStmt extends AbstractStatement {
    private Expression[] exprs;

    public ReturnStmt(NodeLocation location, Expression[] exprs) {
        super(location);
        this.exprs = exprs;
    }

    public Expression[] getExprs() {
        return exprs;
    }

    public void setExprs(Expression[] exprs) {
        this.exprs = exprs;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void execute(NodeExecutor executor) {
        executor.visit(this);
    }

    @Override
    public void executeLNode(LinkedNodeExecutor executor) {
        executor.visit(this);
    }
}
