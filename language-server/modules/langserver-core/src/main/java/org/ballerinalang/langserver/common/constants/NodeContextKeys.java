/*
 * Copyright (c) 2018, WSO2 Inc. (http://wso2.com) All Rights Reserved.
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
package org.ballerinalang.langserver.common.constants;

import org.ballerinalang.langserver.compiler.LSContext;
import org.ballerinalang.langserver.util.references.SymbolReferencesModel;
import org.ballerinalang.langserver.util.references.TokenReferenceModel;
import org.ballerinalang.model.elements.PackageID;
import org.eclipse.lsp4j.Location;
import org.wso2.ballerinalang.compiler.tree.BLangNode;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Keys for the hover context.
 */
public class NodeContextKeys {
    public static final LSContext.Key<BLangNode> NODE_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<PackageID> PACKAGE_OF_NODE_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<String> NAME_OF_NODE_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<String> SYMBOL_KIND_OF_NODE_PARENT_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<Object> PREVIOUSLY_VISITED_NODE_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<String> NODE_OWNER_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<PackageID> NODE_OWNER_PACKAGE_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<Stack<BLangNode>> NODE_STACK_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<String> SYMBOL_KIND_OF_NODE_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<String> VAR_NAME_OF_NODE_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<List<Location>> REFERENCE_RESULTS_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<List<PackageID>> REFERENCE_PKG_IDS_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<String> NODE_NAME_KEY
            = new LSContext.Key<>();
    // Maps the package name to the token references and references map is key to cUnit and to list of token models
    public static final LSContext.Key<Map<String, Map<String, List<TokenReferenceModel>>>> TOKEN_REFERENCES_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<List<TokenReferenceModel>> CURRENT_CUNIT_TOKENS_KEY
            = new LSContext.Key<>();
    public static final LSContext.Key<SymbolReferencesModel> REFERENCES_KEY
            = new LSContext.Key<>();
}
