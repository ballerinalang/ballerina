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
package org.ballerinalang.langserver.completions.util;

import io.ballerina.compiler.api.ModuleID;
import io.ballerina.compiler.api.symbols.ArrayTypeSymbol;
import io.ballerina.compiler.api.symbols.FunctionSymbol;
import io.ballerina.compiler.api.symbols.FunctionTypeSymbol;
import io.ballerina.compiler.api.symbols.MapTypeSymbol;
import io.ballerina.compiler.api.symbols.MethodSymbol;
import io.ballerina.compiler.api.symbols.ModuleSymbol;
import io.ballerina.compiler.api.symbols.ObjectTypeSymbol;
import io.ballerina.compiler.api.symbols.Qualifier;
import io.ballerina.compiler.api.symbols.RecordFieldSymbol;
import io.ballerina.compiler.api.symbols.RecordTypeSymbol;
import io.ballerina.compiler.api.symbols.Symbol;
import io.ballerina.compiler.api.symbols.SymbolKind;
import io.ballerina.compiler.api.symbols.TypeDescKind;
import io.ballerina.compiler.api.symbols.TypeSymbol;
import io.ballerina.compiler.syntax.tree.AnnotAccessExpressionNode;
import io.ballerina.compiler.syntax.tree.FieldAccessExpressionNode;
import io.ballerina.compiler.syntax.tree.FunctionCallExpressionNode;
import io.ballerina.compiler.syntax.tree.IndexedExpressionNode;
import io.ballerina.compiler.syntax.tree.MethodCallExpressionNode;
import io.ballerina.compiler.syntax.tree.NameReferenceNode;
import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.NodeTransformer;
import io.ballerina.compiler.syntax.tree.QualifiedNameReferenceNode;
import io.ballerina.compiler.syntax.tree.SimpleNameReferenceNode;
import io.ballerina.compiler.syntax.tree.SyntaxKind;
import io.ballerina.projects.Module;
import io.ballerina.projects.ModuleId;
import io.ballerina.projects.Package;
import io.ballerina.projects.Project;
import org.ballerinalang.langserver.common.utils.CommonUtil;
import org.ballerinalang.langserver.common.utils.SymbolUtil;
import org.ballerinalang.langserver.common.utils.completion.QNameReferenceUtil;
import org.ballerinalang.langserver.commons.PositionedOperationContext;
import org.wso2.ballerinalang.compiler.util.Names;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

/**
 * Symbol resolver for the field access expressions.
 * This visitor will go through the nodes and capture the respective types of the particular node.
 *
 * @since 2.0.0
 */
public class FieldAccessCompletionResolver extends NodeTransformer<Optional<TypeSymbol>> {
    private final PositionedOperationContext context;
    private final boolean optionalFieldAccess;

    public FieldAccessCompletionResolver(PositionedOperationContext context, boolean optionalFieldAccess) {
        this.context = context;
        this.optionalFieldAccess = optionalFieldAccess;
    }

    @Override
    public Optional<TypeSymbol> transform(SimpleNameReferenceNode node) {
        Optional<Symbol> symbol = this.getSymbolByName(context.visibleSymbols(context.getCursorPosition()),
                node.name().text());

        if (symbol.isEmpty()) {
            return Optional.empty();
        }

        return SymbolUtil.getTypeDescriptor(symbol.get());
    }

    @Override
    public Optional<TypeSymbol> transform(FieldAccessExpressionNode node) {
        // First capture the expression and the respective symbols
        Optional<TypeSymbol> typeSymbol = node.expression().apply(this);
//        this.getVisibleEntries(typeSymbol.orElseThrow());

        NameReferenceNode fieldName = node.fieldName();
        if (fieldName.kind() != SyntaxKind.SIMPLE_NAME_REFERENCE) {
            return Optional.empty();
        }
        String name = ((SimpleNameReferenceNode) fieldName).name().text();
        List<Symbol> visibleEntries = this.getVisibleEntries(typeSymbol.orElseThrow(), node.expression());
        Optional<Symbol> filteredSymbol = this.getSymbolByName(visibleEntries, name);

        if (filteredSymbol.isEmpty()) {
            return Optional.empty();
        }

        return SymbolUtil.getTypeDescriptor(filteredSymbol.get());
    }

    @Override
    public Optional<TypeSymbol> transform(MethodCallExpressionNode node) {
        Optional<TypeSymbol> exprTypeSymbol = node.expression().apply(this);
        NameReferenceNode nameRef = node.methodName();
        if (nameRef.kind() != SyntaxKind.SIMPLE_NAME_REFERENCE) {
            return Optional.empty();
        }
        Predicate<Symbol> predicate = symbol -> symbol.kind() == SymbolKind.METHOD
                || symbol.kind() == SymbolKind.FUNCTION;
        String methodName = ((SimpleNameReferenceNode) nameRef).name().text();
        List<Symbol> visibleEntries = this.getVisibleEntries(exprTypeSymbol.orElseThrow(), node.expression());

        FunctionSymbol symbol = (FunctionSymbol) this.getSymbolByName(visibleEntries, methodName, predicate);
        FunctionTypeSymbol functionTypeSymbol = (FunctionTypeSymbol) SymbolUtil.getTypeDescriptor(symbol).orElseThrow();

        return functionTypeSymbol.returnTypeDescriptor();
    }

    @Override
    public Optional<TypeSymbol> transform(FunctionCallExpressionNode node) {
        NameReferenceNode nameRef = node.functionName();

        Predicate<Symbol> predicate = symbol -> symbol.kind() == SymbolKind.FUNCTION;
        List<Symbol> visibleEntries;
        String functionName;
        if (nameRef.kind() == SyntaxKind.QUALIFIED_NAME_REFERENCE) {
            QualifiedNameReferenceNode qNameRef = (QualifiedNameReferenceNode) nameRef;
            visibleEntries = QNameReferenceUtil.getModuleContent(this.context, qNameRef, predicate);
            functionName = qNameRef.identifier().text();
        } else if (nameRef.kind() == SyntaxKind.SIMPLE_NAME_REFERENCE) {
            functionName = ((SimpleNameReferenceNode) nameRef).name().text();
            visibleEntries = context.visibleSymbols(context.getCursorPosition()).stream()
                    .filter(predicate)
                    .collect(Collectors.toList());
        } else {
            return Optional.empty();
        }

        Optional<Symbol> functionSymbol = this.getSymbolByName(visibleEntries, functionName);
        if (functionSymbol.isEmpty() || functionSymbol.get().kind() != SymbolKind.FUNCTION) {
            return Optional.empty();
        }

        return ((FunctionSymbol) functionSymbol.get()).typeDescriptor().returnTypeDescriptor();
    }

    @Override
    public Optional<TypeSymbol> transform(IndexedExpressionNode node) {
        Optional<TypeSymbol> containerType = node.containerExpression().apply(this);
        TypeSymbol rawType = CommonUtil.getRawType(containerType.orElseThrow());
        if (rawType.typeKind() == TypeDescKind.ARRAY) {
            return Optional.of(((ArrayTypeSymbol) rawType).memberTypeDescriptor());
        }
        if (rawType.typeKind() == TypeDescKind.MAP) {
            return ((MapTypeSymbol) rawType).typeParameter();
        }

        return Optional.empty();
    }

    @Override
    public Optional<TypeSymbol> transform(AnnotAccessExpressionNode node) {
        return this.context.currentSemanticModel().orElseThrow().type(node);
    }

    @Override
    protected Optional<TypeSymbol> transformSyntaxNode(Node node) {
        return Optional.empty();
    }

    /**
     * Get the visible symbol entries on the type of the node.
     *
     * @param node to be evaluated
     * @return {@link List}
     */
    public List<Symbol> getVisibleEntries(Node node) {
        Optional<TypeSymbol> typeSymbol = node.apply(this);
        return typeSymbol.map(tSymbol -> this.getVisibleEntries(tSymbol, node)).orElse(Collections.emptyList());
    }

    private Optional<Symbol> getSymbolByName(List<Symbol> visibleSymbols, String name) {
        return visibleSymbols.stream()
                .filter((symbol -> symbol.getName().orElse("").equals(name)))
                .findFirst();
    }

    private Symbol getSymbolByName(List<Symbol> visibleSymbols, String name, @Nonnull Predicate<Symbol> predicate) {
        Predicate<Symbol> namePredicate = symbol -> symbol.getName().orElse("").equals(name);
        return visibleSymbols.stream()
                .filter(namePredicate.and(predicate))
                .findFirst().orElseThrow();
    }

    private List<Symbol> getVisibleEntries(TypeSymbol typeSymbol, Node node) {
        List<Symbol> visibleEntries = new ArrayList<>();
        TypeSymbol rawType = CommonUtil.getRawType(typeSymbol);
        switch (rawType.typeKind()) {
            case RECORD:
                // If the invoked for field access expression, then avoid suggesting the optional fields
                List<RecordFieldSymbol> filteredEntries =
                        ((RecordTypeSymbol) rawType).fieldDescriptors().values().stream()
                                .filter(recordFieldSymbol -> this.optionalFieldAccess
                                        || !recordFieldSymbol.isOptional())
                                .collect(Collectors.toList());
                visibleEntries.addAll(filteredEntries);
                break;
            case OBJECT:
                // add class field access test case as well
                Optional<Package> currentPkg = context.workspace()
                        .project(context.filePath())
                        .map(Project::currentPackage);
                Optional<Module> currentModule = context.currentModule();
                if (currentModule.isEmpty() || currentPkg.isEmpty()) {
                    break;
                }
                ObjectTypeSymbol objTypeDesc = (ObjectTypeSymbol) rawType;
                visibleEntries.addAll(objTypeDesc.fieldDescriptors().values());
                boolean isClient = SymbolUtil.isClient(objTypeDesc);
                // If the object type desc is a client, then we avoid all the remote methods
                List<MethodSymbol> methodSymbols = objTypeDesc.methods().values().stream()
                        .filter(methodSymbol -> (!isClient || !methodSymbol.qualifiers().contains(Qualifier.REMOTE))
                                && !methodSymbol.qualifiers().contains(Qualifier.RESOURCE)
                                && withValidAccessModifiers(node, methodSymbol, currentPkg.get(),
                                currentModule.get().moduleId()))
                        .collect(Collectors.toList());
                visibleEntries.addAll(methodSymbols);
                break;
            default:
                break;
        }
        visibleEntries.addAll(typeSymbol.langLibMethods());

        return visibleEntries;
    }

    private boolean withValidAccessModifiers(Node exprNode, MethodSymbol methodSymbol, Package currentPackage,
                                             ModuleId currentModule) {
        Optional<Project> project = context.workspace().project(context.filePath());
        Optional<ModuleSymbol> methodSymbolModule = methodSymbol.getModule();
        if (project.isEmpty() || methodSymbolModule.isEmpty()) {
            return false;
        }
        boolean isResource = methodSymbol.qualifiers().contains(Qualifier.RESOURCE);
        boolean isPrivate = methodSymbol.qualifiers().contains(Qualifier.PRIVATE);
        boolean isPublic = methodSymbol.qualifiers().contains(Qualifier.PUBLIC);

        if (exprNode.kind() == SyntaxKind.SIMPLE_NAME_REFERENCE
                && ((SimpleNameReferenceNode) exprNode).name().text().equals(Names.SELF.getValue())
                && !isResource) {
            return true;
        }
        ModuleID objModuleId = methodSymbolModule.get().id();

        return isPublic || (!isPrivate && objModuleId.moduleName().equals(currentModule.moduleName())
                && objModuleId.orgName().equals(currentPackage.packageOrg().value()));
    }
}
