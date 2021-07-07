/*
*   Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
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
package org.wso2.ballerinalang.compiler.tree.expressions;

import org.ballerinalang.model.tree.NodeKind;
import org.ballerinalang.model.tree.expressions.ExpressionNode;
import org.ballerinalang.model.tree.expressions.XMLAttributeNode;
import org.wso2.ballerinalang.compiler.nballerina.ModuleGen;
import org.wso2.ballerinalang.compiler.semantics.model.symbols.BSymbol;
import org.wso2.ballerinalang.compiler.tree.BLangNodeVisitor;

/**
 * Implementation of SimpleVariableReferenceNode.
 *
 * @since 0.94
 */
public class BLangXMLAttribute extends BLangExpression implements XMLAttributeNode {

    public BLangExpression name;
    public BLangXMLQuotedString value;
    public boolean isNamespaceDeclr;
    public BSymbol symbol;

    @Override
    public ExpressionNode getName() {
        return name;
    }

    @Override
    public void setName(ExpressionNode name) {
        this.name = (BLangExpression) name;
    }

    @Override
    public ExpressionNode getValue() {
        return value;
    }

    @Override
    public void setValue(ExpressionNode value) {
        this.value = (BLangXMLQuotedString) value;
    }

    @Override
    public NodeKind getKind() {
        return NodeKind.XML_ATTRIBUTE;
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
    public String toString() {
        return "BLangXMLAttribute: " + name + "=" + value;
    }
}
