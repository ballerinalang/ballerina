/*
 * Copyright (c) 2020, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ballerinalang.formatter.core;

import io.ballerina.tools.text.LineRange;
import io.ballerina.tools.text.TextDocument;
import io.ballerina.tools.text.TextDocuments;
import io.ballerinalang.compiler.syntax.tree.ModulePartNode;
import io.ballerinalang.compiler.syntax.tree.SyntaxTree;

/**
 * Class that exposes the formatting APIs.
 */
public class Formatter {

    /**
     * Formats the provided source string and returns back the formatted source string.
     *
     * @param source A Ballerina source in string form
     * @return A modified source string after formatting changes
     * @throws FormatterException Exception caught while formatting
     */
    public static String format(String source) throws FormatterException {
        return format(source, new FormattingOptions());
    }

    /**
     * Formats a line range of the provided SyntaxTree. All the nodes falling within the line range
     * specified will be formatted.
     *
     * @param syntaxTree The complete SyntaxTree, of which a part is to be formatted
     * @param range LineRange which specifies the range to be formatted
     * @return The modified SyntaxTree after formatting changes
     * @throws FormatterException Exception caught while formatting
     */
    public static SyntaxTree format(SyntaxTree syntaxTree, LineRange range) throws FormatterException {
        return format(syntaxTree, range, new FormattingOptions());
    }

    /**
     * Formats the provided SyntaxTree and returns back a formatted SyntaxTree.
     *
     * @param syntaxTree The SyntaxTree which is to be formatted
     * @return The modified SyntaxTree after formatting changes
     * @throws FormatterException Exception caught while formatting
     */
    public static SyntaxTree format(SyntaxTree syntaxTree) throws FormatterException {
        return format(syntaxTree, new FormattingOptions());
    }

    /**
     * Formats the provided source string while using the formatting options provided.
     *
     * @param source A Ballerina source in string form
     * @param options Formatting options that are to be used when formatting
     * @return A modified source string after formatting changes
     * @throws FormatterException Exception caught while formatting
     */
    public static String format(String source, FormattingOptions options) throws FormatterException {
        TextDocument textDocument = TextDocuments.from(source);
        SyntaxTree syntaxTree = SyntaxTree.from(textDocument);
        return modifyTree(syntaxTree, options, null).toSourceCode();
    }

    /**
     * Formats a line range of the provided SyntaxTree while using the formatting options provided. All the
     * nodes falling within the line range provided will be formatted.
     *
     * @param syntaxTree The complete SyntaxTree, of which a part is to be formatted
     * @param range LineRange which needs to be formatted
     * @param options Formatting options that are to be used when formatting
     * @return The modified SyntaxTree after formatting changes
     * @throws FormatterException Exception caught while formatting
     */
    public static SyntaxTree format(SyntaxTree syntaxTree, LineRange range, FormattingOptions options)
            throws FormatterException {
        return modifyTree(syntaxTree, options, range);
    }

    /**
     * Formats the provided SyntaxTree while using the formatting options provided.
     *
     * @param syntaxTree The SyntaxTree which is to be formatted
     * @param options Formatting options that are to be used when formatting
     * @return The modified SyntaxTree after formatting changes
     * @throws FormatterException Exception caught while formatting
     */
    public static SyntaxTree format(SyntaxTree syntaxTree, FormattingOptions options) throws FormatterException {
        return modifyTree(syntaxTree, options, null);
    }

    private static SyntaxTree modifyTree(SyntaxTree syntaxTree, FormattingOptions options, LineRange range)
            throws FormatterException {
        FormattingTreeModifier treeModifier = new FormattingTreeModifier(options, range);
        ModulePartNode modulePartNode = syntaxTree.rootNode();
        try {
            SyntaxTree newSyntaxTree = syntaxTree.modifyWith(treeModifier.transform(modulePartNode));
            return newSyntaxTree.modifyWith(treeModifier.transform((ModulePartNode) newSyntaxTree.rootNode()));
        } catch (Exception e) {
            throw new FormatterException("Error while formatting the source: " + e.getMessage(), e.getCause());
        }
    }
}
