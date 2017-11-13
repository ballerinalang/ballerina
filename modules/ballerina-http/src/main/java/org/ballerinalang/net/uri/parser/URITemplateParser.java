/*
*  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.ballerinalang.net.uri.parser;

import org.ballerinalang.net.uri.URITemplateException;

/**
 * URITemplateParser parses the provided uri-template and build the tree.
 *
 * @param <NODE_ITEM> Specific node item for the parser.
 * @param <ITEM> Item stored in the node item.
 * @param <CHECKER> Additional checker for node item.
 */
public class URITemplateParser<NODE_ITEM extends NodeItem<ITEM, CHECKER>, ITEM, CHECKER> {

    private static final char[] operators = new char[] { '+', '.', '/', ';', '?', '&', '#' };

    private Node<NODE_ITEM> syntaxTree;
    private Node<NODE_ITEM> currentNode;
    private final NodeItemCreator<NODE_ITEM> nodeItemCreator;

    public URITemplateParser(Node<NODE_ITEM> rootNode, NodeItemCreator<NODE_ITEM> nodeItemCreator) {
        this.syntaxTree = rootNode;
        this.nodeItemCreator = nodeItemCreator;
    }

    public Node parse(String template, ITEM item) throws URITemplateException {
        if (!"/".equals(template) && template.endsWith("/")) {
            template = template.substring(0, template.length() - 1);
        }

        if ("/".equals(template)) {
            this.syntaxTree.getNodeItem().setItem(item);
            return syntaxTree;
        }
        String[] segments = template.split("/");
        for (int currentElement = 0; currentElement < segments.length; currentElement++) {
            String segment = segments[currentElement];
            boolean expression = false;
            int startIndex = 0;
            int maxIndex = segment.length() - 1;

            for (int pointerIndex = 0; pointerIndex < segment.length(); pointerIndex++) {
                char ch = segment.charAt(pointerIndex);
                switch (ch) {
                    case '{':
                        if (expression) {
                            throw new URITemplateException("Already in expression");
                        }
                        if (pointerIndex + 1 >= maxIndex) {
                            throw new URITemplateException("Illegal open brace character");
                        }
                        expression = true;
                        if (pointerIndex > startIndex) {
                            addNode(new Literal<>(createNodeItem(), segment.substring(startIndex, pointerIndex)));
                            startIndex = pointerIndex + 1;
                            // TODO: Check whether we really need this.
                        /*} else if (segment.charAt(pointerIndex - 1) != '}') {
                            throw new URITemplateException("Illegal empty literal");*/
                        } else {
                            startIndex++;
                        }
                        break;
                    case '}':
                        if (!expression) {
                            throw new URITemplateException("Illegal closing brace detected");
                        }
                        if (pointerIndex <= startIndex) {
                            throw new URITemplateException("Illegal empty expression");
                        }
                        expression = false;
                        String token = segment.substring(startIndex, pointerIndex);
                        createExpressionNode(token, maxIndex, pointerIndex);
                        startIndex = pointerIndex + 1;
                        break;
                    case '*':
                        if (pointerIndex == 0 && currentElement != segments.length - 1) {
                            throw new URITemplateException("/* is only allowed at the end of the Path");
                        }
                        // fallthru
                    default:
                        //TODO change below as well
                        if (pointerIndex == maxIndex) {
                            String tokenVal = segment.substring(startIndex);
                            if (expression) {
                                createExpressionNode(tokenVal, maxIndex, pointerIndex);
                            } else {
                                addNode(new Literal<>(createNodeItem(), tokenVal));
                            }
                        }
                }
            }
        }
        this.currentNode.getNodeItem().setItem(item);

        return syntaxTree;
    }

    private <NODE_EXTEND extends Node<NODE_ITEM>> void addNode(NODE_EXTEND node) {
        if (currentNode == null) {
            currentNode = syntaxTree;
        }
        if (node.getToken().equals("*")) {
            currentNode = currentNode.addChild("." + node.getToken(), node);
        } else {
            currentNode = currentNode.addChild(node.getToken(), node);
        }
    }

    private void createExpressionNode(String expression, int maxIndex, int pointerIndex) throws URITemplateException {
        Node<NODE_ITEM> node = null;
        if (isSimpleString(expression)) {
            if (maxIndex == pointerIndex) {
                node = new SimpleStringExpression<>(createNodeItem(), expression);
            } else {
                node = new SimpleSplitStringExpression<>(createNodeItem(), expression);
            }
        }

        if (expression.length() <= 1) {
            throw new URITemplateException("Invalid template expression: {" + expression + "}");
        }

        // TODO: Re-verify the usage of these nodes
        if (expression.startsWith("#")) {
            node = new FragmentExpression<>(createNodeItem(), expression.substring(1));
        } else if (expression.startsWith("+")) {
            node = new ReservedStringExpression<>(createNodeItem(), expression.substring(1));
        } else if (expression.startsWith(".")) {
            node = new LabelExpression<>(createNodeItem(), expression.substring(1));
        } else if (expression.startsWith("/")) {
            node = new PathSegmentExpression<>(createNodeItem(), expression.substring(1));
        }

        if (node != null) {
            addNode(node);
        } else {
            throw new URITemplateException("Unsupported template expression: {" + expression + "}");
        }
    }

    private boolean isSimpleString(String expression) {
        for (char op : operators) {
            if (expression.indexOf(op) == 0) {
                return false;
            }
        }
        return true;
    }

    private NODE_ITEM createNodeItem() {
        return nodeItemCreator.createItem();
    }
}
