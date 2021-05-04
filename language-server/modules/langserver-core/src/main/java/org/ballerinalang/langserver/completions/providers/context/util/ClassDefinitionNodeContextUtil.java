package org.ballerinalang.langserver.completions.providers.context.util;

import io.ballerina.compiler.syntax.tree.ClassDefinitionNode;
import io.ballerina.compiler.syntax.tree.FunctionDefinitionNode;
import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.SyntaxKind;

/**
 * Utilities for the class definition context.
 * 
 * @since 2.0.0
 */
public class ClassDefinitionNodeContextUtil {
    private ClassDefinitionNodeContextUtil() {
    }
    
    public static boolean onSuggestResourceSnippet(Node node) {
        if (node.kind() == SyntaxKind.SERVICE_DECLARATION) {
            return true;
        }
        if (node.kind() == SyntaxKind.CLASS_DEFINITION) {
            return ((ClassDefinitionNode) node).classTypeQualifiers().stream()
                    .anyMatch(token -> token.kind() == SyntaxKind.SERVICE_KEYWORD);
        }
        
        return false;
    }

    public static boolean onSuggestInitFunction(Node node) {
        if (node.kind() == SyntaxKind.CLASS_DEFINITION) {
            ClassDefinitionNode classDefinitionNode = (ClassDefinitionNode) node;
            return classDefinitionNode.members().stream()
                    .filter(member -> member.kind() == SyntaxKind.OBJECT_METHOD_DEFINITION)
                    .map(member -> (FunctionDefinitionNode) member)
                    .noneMatch(funcDef -> "init".equals(funcDef.functionName().text()));
        }

        return false;
    }
}
