/*
 *  Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package io.ballerina.converters.util;

import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.NodeList;
import io.ballerina.compiler.syntax.tree.RecordFieldNode;
import io.ballerina.compiler.syntax.tree.RecordTypeDescriptorNode;
import io.ballerina.compiler.syntax.tree.TypeDefinitionNode;
import io.ballerina.converters.exception.ConverterException;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Util methods for JSON to record converter.
 */
public class ConverterUtils {
    /**
     * Method for mapping openApi type to ballerina type.
     *
     * @param type  OpenApi parameter types
     * @return ballerina type
     */
    public static String convertOpenAPITypeToBallerina(String type) {
        String convertedType;
        switch (type) {
            case Constants.INTEGER:
                convertedType = "int";
                break;
            case Constants.STRING:
                convertedType = "string";
                break;
            case Constants.BOOLEAN:
                convertedType = "boolean";
                break;
            case Constants.ARRAY:
                convertedType = "[]";
                break;
            case Constants.OBJECT:
                convertedType = "record";
                break;
            case Constants.DECIMAL:
            case Constants.NUMBER:
            case Constants.DOUBLE:
                convertedType = "decimal";
                break;
            case Constants.FLOAT:
                convertedType = "float";
                break;
            default:
                convertedType = "anydata";
        }
        return convertedType;
    }

    /**
     * This method will escape special characters used in method names and identifiers.
     *
     * @param identifier - identifier or method name
     * @return - escaped string
     */
    public static String escapeIdentifier(String identifier) {

        if (identifier.matches("\\b[0-9]*\\b")) {
            return "'" + identifier;
        } else if (!identifier.matches("\\b[_a-zA-Z][_a-zA-Z0-9]*\\b")
                || Constants.BAL_KEYWORDS.stream().anyMatch(identifier::equals)) {

            // TODO: Remove this `if`. Refer - https://github.com/ballerina-platform/ballerina-lang/issues/23045
            if (identifier.equals("error")) {
                identifier = "_error";
            } else {
                identifier = identifier.replaceAll(Constants.ESCAPE_PATTERN, "\\\\$1");
                if (identifier.endsWith("?")) {
                    if (identifier.charAt(identifier.length() - 2) == '\\') {
                        StringBuilder stringBuilder = new StringBuilder(identifier);
                        stringBuilder.deleteCharAt(identifier.length() - 2);
                        identifier = stringBuilder.toString();
                    }
                    if (Constants.BAL_KEYWORDS.stream().anyMatch(Optional.ofNullable(identifier)
                            .filter(sStr -> sStr.length() != 0)
                            .map(sStr -> sStr.substring(0, sStr.length() - 1))
                            .orElse(identifier)::equals)) {
                        identifier = "'" + identifier;
                    } else {
                        return identifier;
                    }
                } else {
                    identifier = "'" + identifier;
                }
            }
        }
        return identifier;
    }

    /**
     * This method will extract reference type by splitting the reference string.
     *
     * @param referenceVariable - Reference String
     * @return Reference variable name
     * @throws ConverterException - Throws an exception if the reference string is incompatible.
     *                                     Note : Current implementation will not support external links a references.
     */
    public static String extractReferenceType(String referenceVariable) throws ConverterException {
        if (referenceVariable.startsWith("#") && referenceVariable.contains("/")) {
            String[] refArray = referenceVariable.split("/");
            return escapeIdentifier(refArray[refArray.length - 1]);
        } else {
            throw new ConverterException(ErrorMessages.invalidReference(referenceVariable));
        }
    }

    /**
     * This method takes a list of record nodes and returns it as a code block.
     *
     * @param typeNodeList list of type nodes
     * @return formatted code block as a string
     */
    public static String typeNodesToFormattedString(ArrayList<TypeDefinitionNode> typeNodeList) {
        StringBuilder sourceCodeString = new StringBuilder();
        for (TypeDefinitionNode typeNode : typeNodeList) {
            sourceCodeString.append(typeNode.typeKeyword().toSourceCode());
            sourceCodeString.append(" ");

            sourceCodeString.append(typeNode.typeName().toSourceCode());
            sourceCodeString.append(" ");

            if (!(typeNode.typeDescriptor() instanceof RecordTypeDescriptorNode)) {
                throw new IllegalArgumentException();
            }

            RecordTypeDescriptorNode recordNode = (RecordTypeDescriptorNode) typeNode.typeDescriptor();

            sourceCodeString.append(recordNode.recordKeyword().toSourceCode());
            sourceCodeString.append(" ");
            sourceCodeString.append(recordNode.bodyStartDelimiter().toSourceCode());
            sourceCodeString.append("\n");

            NodeList<Node> fieldNodes = recordNode.fields();

            for (Node field : fieldNodes) {
                if (!(field instanceof RecordFieldNode)) {
                    throw new IllegalArgumentException();
                }
                RecordFieldNode recordField = (RecordFieldNode) field;
                sourceCodeString.append("\t");
                sourceCodeString.append(recordField.typeName().toSourceCode());
                sourceCodeString.append(" ");
                sourceCodeString.append(recordField.fieldName().toSourceCode());
                if (recordField.questionMarkToken().isPresent()) {
                    sourceCodeString.append(recordField.questionMarkToken().get().toSourceCode());
                }
                sourceCodeString.append(recordField.semicolonToken().toSourceCode());
                sourceCodeString.append("\n");
            }

            sourceCodeString.append(recordNode.bodyEndDelimiter().toSourceCode());
            sourceCodeString.append(typeNode.semicolonToken().toSourceCode());
            sourceCodeString.append("\n\n");
        }
        return sourceCodeString.toString();
    }
}