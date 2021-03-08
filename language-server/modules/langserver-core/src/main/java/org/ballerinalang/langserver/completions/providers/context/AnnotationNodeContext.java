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
package org.ballerinalang.langserver.completions.providers.context;

import io.ballerina.compiler.api.ModuleID;
import io.ballerina.compiler.api.symbols.AnnotationAttachPoint;
import io.ballerina.compiler.api.symbols.AnnotationSymbol;
import io.ballerina.compiler.api.symbols.ModuleSymbol;
import io.ballerina.compiler.api.symbols.Symbol;
import io.ballerina.compiler.api.symbols.SymbolKind;
import io.ballerina.compiler.syntax.tree.AnnotationNode;
import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.ObjectConstructorExpressionNode;
import io.ballerina.compiler.syntax.tree.QualifiedNameReferenceNode;
import io.ballerina.compiler.syntax.tree.SyntaxKind;
import io.ballerina.projects.Module;
import org.ballerinalang.annotation.JavaSPIService;
import org.ballerinalang.langserver.common.utils.AnnotationUtil;
import org.ballerinalang.langserver.common.utils.CommonUtil;
import org.ballerinalang.langserver.common.utils.completion.QNameReferenceUtil;
import org.ballerinalang.langserver.commons.BallerinaCompletionContext;
import org.ballerinalang.langserver.commons.completion.LSCompletionItem;
import org.ballerinalang.langserver.completions.providers.AbstractCompletionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Completion provider for {@link AnnotationNode} context.
 *
 * @since 2.0.0
 */
@JavaSPIService("org.ballerinalang.langserver.commons.completion.spi.BallerinaCompletionProvider")
public class AnnotationNodeContext extends AbstractCompletionProvider<AnnotationNode> {

    public AnnotationNodeContext() {
        super(AnnotationNode.class);
    }

    @Override
    public List<LSCompletionItem> getCompletions(BallerinaCompletionContext context, AnnotationNode node) {
        List<LSCompletionItem> completionItems = new ArrayList<>();
        Node attachedNode = this.getAttached(node);

        if (QNameReferenceUtil.onQualifiedNameIdentifier(context, node.annotReference())) {
            QualifiedNameReferenceNode qNameRef = (QualifiedNameReferenceNode) node.annotReference();
            completionItems.addAll(this.getAnnotationsInModule(context, qNameRef.modulePrefix().text(), attachedNode));
        } else {
            // Fixme Temporarily disabled the caching usage
//        LSAnnotationCache.getInstance().getAnnotationMapForSyntaxKind(attachedNode, context)
//                .forEach((key, value) -> value.forEach(annotation -> {
//                    LSCompletionItem cItem;
//                    if (this.addAlias(context, node, key)) {
//                        cItem = AnnotationUtil.getModuleQualifiedAnnotationItem(key, annotation, context);
//                    } else {
//                        cItem = AnnotationUtil.getAnnotationItem(annotation, context);
//                    }
//                    completionItems.add(cItem);
//                }));
            completionItems.addAll(this.getModuleCompletionItems(context));
            completionItems.addAll(this.getCurrentModuleAnnotations(context, attachedNode));
        }
        this.sort(context, node, completionItems);

        return completionItems;
    }

    private List<LSCompletionItem> getCurrentModuleAnnotations(BallerinaCompletionContext ctx, Node attachedNode) {
        List<Symbol> visibleSymbols = ctx.visibleSymbols(ctx.getCursorPosition());
        return visibleSymbols.stream()
                .filter(symbol -> symbol.kind() == SymbolKind.ANNOTATION
                        && this.matchingAnnotation((AnnotationSymbol) symbol, attachedNode))
                .map(symbol -> AnnotationUtil.getAnnotationItem((AnnotationSymbol) symbol, ctx))
                .collect(Collectors.toList());
    }

    private List<LSCompletionItem> getAnnotationsInModule(BallerinaCompletionContext context, String alias,
                                                          Node attachedNode) {
        Optional<ModuleSymbol> moduleEntry = CommonUtil.searchModuleForAlias(context, alias);
        // TODO: Enable after annotation cache is supported
//        if (moduleEntry.isEmpty()) {
//            List<LSCompletionItem> completionItems = new ArrayList<>();
//            // Import statement has not been added. Hence try resolving from the annotation cache
//            LSAnnotationCache.getInstance(context.languageServercontext())
//                    .getAnnotationsInModule(context, alias, attachedNode)
//                    .forEach((key, value) -> value.forEach(annotation -> {
//                        completionItems.add(AnnotationUtil.getAnnotationItem(annotation, context));
//                    }));
//
//            return completionItems;
//        }

        return moduleEntry.orElseThrow().allSymbols().stream()
                .filter(symbol -> symbol.kind() == SymbolKind.ANNOTATION
                        && this.matchingAnnotation((AnnotationSymbol) symbol, attachedNode))
                .map(symbol -> AnnotationUtil.getAnnotationItem((AnnotationSymbol) symbol, context))
                .collect(Collectors.toList());
    }

    private Node getAttached(AnnotationNode node) {
        if (node.parent().kind() == SyntaxKind.METADATA) {
            return node.parent().parent();
        }

        return node.parent();
    }

    private boolean matchingAnnotation(AnnotationSymbol symbol, Node attachedNode) {
        if (symbol.attachPoints().isEmpty()) {
            return true;
        }
        switch (attachedNode.kind()) {
            case SERVICE_DECLARATION:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.SERVICE);
            case EXPLICIT_ANONYMOUS_FUNCTION_EXPRESSION:
            case IMPLICIT_ANONYMOUS_FUNCTION_EXPRESSION:
            case FUNCTION_DEFINITION:
            case RESOURCE_ACCESSOR_DEFINITION:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.FUNCTION);
            case METHOD_DECLARATION:
            case OBJECT_METHOD_DEFINITION:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.FUNCTION)
                        || AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.OBJECT_METHOD);
            case LISTENER_DECLARATION:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.LISTENER);
            case NAMED_WORKER_DECLARATION:
            case START_ACTION:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.WORKER);
            case CONST_DECLARATION:
            case ENUM_MEMBER:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.CONST);
            case ENUM_DECLARATION:
            case TYPE_CAST_PARAM:
            case TYPE_DEFINITION:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.TYPE);
            case CLASS_DEFINITION:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.CLASS);
            case RETURN_TYPE_DESCRIPTOR:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.RETURN);
            case OBJECT_FIELD:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.FIELD)
                        || AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.OBJECT_FIELD);
            case RECORD_FIELD:
            case RECORD_FIELD_WITH_DEFAULT_VALUE:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.FIELD)
                        || AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.RECORD_FIELD);
            case MODULE_VAR_DECL:
            case LOCAL_VAR_DECL:
            case LET_VAR_DECL:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.VAR);
            case EXTERNAL_FUNCTION_BODY:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.EXTERNAL);
            case ANNOTATION_DECLARATION:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.ANNOTATION);
            case REQUIRED_PARAM:
            case DEFAULTABLE_PARAM:
            case REST_PARAM:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.PARAMETER);
            case OBJECT_CONSTRUCTOR:
                return AnnotationUtil.hasAttachment(symbol, AnnotationAttachPoint.SERVICE)
                        && ((ObjectConstructorExpressionNode) attachedNode).objectTypeQualifiers()
                        .stream()
                        .anyMatch(token -> token.kind() == SyntaxKind.SERVICE_KEYWORD);
            default:
                return false;
        }
    }

    @Override
    public boolean onPreValidation(BallerinaCompletionContext context, AnnotationNode node) {
        return !node.atToken().isMissing();
    }

    private boolean addAlias(BallerinaCompletionContext context, AnnotationNode node, ModuleID annotationOwner) {
        Optional<Module> currentModule = context.workspace().module(context.filePath());
        if (currentModule.isEmpty()) {
            return false;
        }
        String orgName = annotationOwner.orgName();
        String value = annotationOwner.moduleName();
        return node.annotReference().kind() != SyntaxKind.QUALIFIED_NAME_REFERENCE
                && !currentModule.get().moduleName().moduleNamePart().equals(annotationOwner.moduleName())
                && !("ballerina".equals(orgName)
                && "lang.annotations".equals(value));
    }
}
