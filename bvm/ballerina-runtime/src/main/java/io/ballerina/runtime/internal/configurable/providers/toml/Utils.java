/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ballerina.runtime.internal.configurable.providers.toml;

import io.ballerina.runtime.api.Module;
import io.ballerina.runtime.api.PredefinedTypes;
import io.ballerina.runtime.api.TypeTags;
import io.ballerina.runtime.api.creators.TypeCreator;
import io.ballerina.runtime.api.creators.ValueCreator;
import io.ballerina.runtime.api.flags.SymbolFlags;
import io.ballerina.runtime.api.types.Field;
import io.ballerina.runtime.api.types.IntersectableReferenceType;
import io.ballerina.runtime.api.types.IntersectionType;
import io.ballerina.runtime.api.types.RecordType;
import io.ballerina.runtime.api.types.TableType;
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BMapInitialValueEntry;
import io.ballerina.runtime.internal.configurable.exceptions.ConfigException;
import io.ballerina.runtime.internal.types.BIntersectionType;
import io.ballerina.runtime.internal.values.ArrayValue;
import io.ballerina.runtime.internal.values.ArrayValueImpl;
import io.ballerina.runtime.internal.values.ListInitialValueEntry;
import io.ballerina.runtime.internal.values.TableValueImpl;
import io.ballerina.toml.semantic.TomlType;
import io.ballerina.toml.semantic.ast.TomlArrayValueNode;
import io.ballerina.toml.semantic.ast.TomlBooleanValueNode;
import io.ballerina.toml.semantic.ast.TomlDoubleValueNodeNode;
import io.ballerina.toml.semantic.ast.TomlKeyValueNode;
import io.ballerina.toml.semantic.ast.TomlLongValueNode;
import io.ballerina.toml.semantic.ast.TomlNode;
import io.ballerina.toml.semantic.ast.TomlStringValueNode;
import io.ballerina.toml.semantic.ast.TomlTableArrayNode;
import io.ballerina.toml.semantic.ast.TomlTableNode;
import io.ballerina.toml.semantic.ast.TomlValueNode;
import io.ballerina.toml.semantic.ast.TopLevelNode;
import io.ballerina.tools.text.LinePosition;
import io.ballerina.tools.text.LineRange;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.ballerina.runtime.api.PredefinedTypes.TYPE_ANYDATA;
import static io.ballerina.runtime.api.PredefinedTypes.TYPE_READONLY_ANYDATA;
import static io.ballerina.runtime.internal.configurable.providers.toml.TomlConstants.CONFIG_FILE_NAME;
import static io.ballerina.runtime.internal.util.exceptions.RuntimeErrors.CONFIG_TOML_TYPE_NOT_SUPPORTED;
import static io.ballerina.runtime.internal.util.exceptions.RuntimeErrors.CONFIG_TYPE_NOT_SUPPORTED;

/**
 * Util methods required for configurable variables.
 *
 * @since 2.0.0
 */
public class Utils {

    private static final Type TYPE_READONLY_ANYDATA_INTERSECTION =
            new BIntersectionType(null, new Type[]{ TYPE_READONLY_ANYDATA},
                                  (IntersectableReferenceType) TYPE_READONLY_ANYDATA, 0, true);

    private Utils() {
    }

    static Object getTomlTypeString(TomlNode tomlNode) {
        switch (tomlNode.kind()) {
            case STRING:
                return "string";
            case INTEGER:
                return "int";
            case DOUBLE:
                return "float";
            case BOOLEAN:
                return "boolean";
            case ARRAY:
                return "array";
            case TABLE:
                return "record";
            case TABLE_ARRAY:
                return "table";
            case KEY_VALUE:
                return getTomlTypeString(((TomlKeyValueNode) tomlNode).value());
            default:
                return "unsupported type";
        }
    }

    static Object getBalValueFromToml(TomlNode tomlNode, Set<TomlNode> visitedNodes) {
        visitedNodes.add(tomlNode);
        switch (tomlNode.kind()) {
            case STRING:
                return StringUtils.fromString(((TomlStringValueNode) tomlNode).getValue());
            case INTEGER:
                return ((TomlLongValueNode) tomlNode).getValue();
            case DOUBLE:
                return ((TomlDoubleValueNodeNode) tomlNode).getValue();
            case BOOLEAN:
                return ((TomlBooleanValueNode) tomlNode).getValue();
            case KEY_VALUE:
                return getBalValueFromToml(((TomlKeyValueNode) tomlNode).value(), visitedNodes);
            case ARRAY:
                return getAnydataArray((TomlArrayValueNode) tomlNode, visitedNodes);
            case TABLE:
                return getAnydataMap((TomlTableNode) tomlNode, visitedNodes);
            case TABLE_ARRAY:
                return getAnydataTable((TomlTableArrayNode) tomlNode, visitedNodes);
            default:
                // should not come here
                return null;
        }
    }

    private static Object getAnydataTable(TomlTableArrayNode tomlNode, Set<TomlNode> visitedNodes) {
        List<TomlTableNode> tableNodeList = tomlNode.children();
        int tableSize = tableNodeList.size();
        ListInitialValueEntry.ExpressionEntry[] tableEntries =
                new ListInitialValueEntry.ExpressionEntry[tableSize];
        int count = 0;
        for (TomlTableNode tomlTableNode : tableNodeList) {
            Object value = getBalValueFromToml(tomlTableNode, visitedNodes);
            tableEntries[count++] = new ListInitialValueEntry.ExpressionEntry(value);
        }
        ArrayValue tableData = new ArrayValueImpl(TypeCreator.createArrayType(TYPE_READONLY_ANYDATA_INTERSECTION,
                                                                              true), tableSize, tableEntries);
        ArrayValue keyNames = (ArrayValue) StringUtils.fromStringArray(new String[0]);
        TableType tableType = TypeCreator.createTableType(TypeCreator.createMapType(TYPE_READONLY_ANYDATA_INTERSECTION,
                                                                                    true), true);
        return new TableValueImpl<>(tableType, tableData, keyNames);
    }

    private static Object getAnydataMap(TomlTableNode tomlNode, Set<TomlNode> visitedNodes) {
        BMapInitialValueEntry[] initialValues = new BMapInitialValueEntry[tomlNode.entries().size()];
        int count = 0;
        for (Map.Entry<String, TopLevelNode> entry : tomlNode.entries().entrySet()) {
            initialValues[count++] = ValueCreator.createKeyFieldEntry(StringUtils.fromString(entry.getKey()),
                                                                      getBalValueFromToml(entry.getValue(),
                                                                                          visitedNodes));
        }
        return ValueCreator.createMapValue(TypeCreator.createMapType(TYPE_READONLY_ANYDATA_INTERSECTION,
                                                                     true), initialValues);
    }

    private static Object getAnydataArray(TomlArrayValueNode tomlNode, Set<TomlNode> visitedNodes) {
        ListInitialValueEntry[] arrayValues = new ListInitialValueEntry[tomlNode.elements().size()];
        List<TomlValueNode> elements = tomlNode.elements();
        int count = 0;
        for (TomlValueNode tomlValueNode : elements) {
            arrayValues[count++] = new ListInitialValueEntry.ExpressionEntry(
                    getBalValueFromToml(tomlValueNode, visitedNodes));
        }
        return new ArrayValueImpl(TypeCreator.createArrayType(TYPE_READONLY_ANYDATA_INTERSECTION, true),
                                  arrayValues.length, arrayValues);
    }

    static TomlType getEffectiveTomlType(Type expectedType, String variableName) {
        switch (expectedType.getTag()) {
            case TypeTags.INT_TAG:
            case TypeTags.BYTE_TAG:
                return TomlType.INTEGER;
            case TypeTags.BOOLEAN_TAG:
                return TomlType.BOOLEAN;
            case TypeTags.FLOAT_TAG:
            case TypeTags.DECIMAL_TAG:
                return TomlType.DOUBLE;
            case TypeTags.STRING_TAG:
            case TypeTags.UNION_TAG:
                return TomlType.STRING;
            case TypeTags.ARRAY_TAG:
                return TomlType.ARRAY;
            case TypeTags.MAP_TAG:
            case TypeTags.RECORD_TYPE_TAG:
                return TomlType.TABLE;
            case TypeTags.TABLE_TAG:
                return TomlType.TABLE_ARRAY;
            case TypeTags.XML_ATTRIBUTES_TAG:
            case TypeTags.XML_COMMENT_TAG:
            case TypeTags.XML_ELEMENT_TAG:
            case TypeTags.XML_PI_TAG:
            case TypeTags.XML_TAG:
            case TypeTags.XML_TEXT_TAG:
                throw new ConfigException(CONFIG_TOML_TYPE_NOT_SUPPORTED, variableName, expectedType.toString());
            case TypeTags.INTERSECTION_TAG:
                Type effectiveType = ((IntersectionType) expectedType).getEffectiveType();
                return getEffectiveTomlType(effectiveType, variableName);
            default:
                throw new ConfigException(CONFIG_TYPE_NOT_SUPPORTED, variableName, expectedType.toString());
        }
    }

    static boolean isPrimitiveType(int typeTag) {
        return typeTag <= TypeTags.BOOLEAN_TAG;
    }

    static String getModuleKey(Module module) {
        return module.getOrg() + "." + module.getName();
    }

    static String getLineRange(TomlNode node) {
        if (node.location() == null) {
            return CONFIG_FILE_NAME;
        }
        LineRange oneBasedLineRange = getOneBasedLineRange(node.location().lineRange());
        return oneBasedLineRange.filePath() + ":" + oneBasedLineRange;
    }

    static LineRange getOneBasedLineRange(LineRange lineRange) {
        return LineRange.from(
                lineRange.filePath(),
                LinePosition.from(lineRange.startLine().line() + 1, lineRange.startLine().offset() + 1),
                LinePosition.from(lineRange.endLine().line() + 1, lineRange.endLine().offset() + 1));
    }

    public static Type getTypeFromTomlValue(TomlNode tomlNode) {
        switch (tomlNode.kind()) {
            case STRING:
                return PredefinedTypes.TYPE_STRING;
            case INTEGER:
                return PredefinedTypes.TYPE_INT;
            case DOUBLE:
                return PredefinedTypes.TYPE_FLOAT;
            case BOOLEAN:
                return PredefinedTypes.TYPE_BOOLEAN;
            case KEY_VALUE:
                return getTypeFromTomlValue(((TomlKeyValueNode) tomlNode).value());
            case ARRAY:
                return TypeCreator.createArrayType(TYPE_ANYDATA, true);
            case TABLE:
                return TypeCreator.createMapType(TYPE_ANYDATA, true);
            case TABLE_ARRAY:
                return TypeCreator.createArrayType(TypeCreator.createMapType(TYPE_ANYDATA), true);
            default:
                // should not come here
                return null;
        }
    }

    public static Field createAdditionalField(RecordType recordType, String fieldName, TomlNode value) {
        Type restFieldType = recordType.getRestFieldType();
        if (!isAnyDataType(restFieldType)) {
            return TypeCreator.createField(restFieldType, fieldName, SymbolFlags.READONLY);
        } else {
            return TypeCreator.createField(Utils.getTypeFromTomlValue(value), fieldName, SymbolFlags.READONLY);
        }
    }

    private static boolean isAnyDataType(Type restFieldType) {
        return restFieldType.getTag() == TypeTags.ANYDATA_TAG || (restFieldType.getTag() == TypeTags.INTERSECTION_TAG &&
                ((IntersectionType) restFieldType).getEffectiveType().getTag() == TypeTags.ANYDATA_TAG);
    }
}
