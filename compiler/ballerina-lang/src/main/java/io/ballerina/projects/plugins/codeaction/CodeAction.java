/*
 * Copyright (c) 2021, WSO2 Inc. (http://wso2.com) All Rights Reserved.
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
package io.ballerina.projects.plugins.codeaction;

import io.ballerina.tools.diagnostics.Diagnostic;

import java.util.List;
import java.util.Optional;

/**
 * Code action interface.
 * 
 * @since 2.0.0
 */
public interface CodeAction {

    List<String> getSupportedDiagnosticCodes();

    Optional<CodeActionCommand> getCodeAction(ToolingCodeActionContext context, Diagnostic diagnostic);

    List<DocumentEdit> execute(ToolingCodeActionContext context, List<CodeActionArgument> arguments);

    String name();
}
