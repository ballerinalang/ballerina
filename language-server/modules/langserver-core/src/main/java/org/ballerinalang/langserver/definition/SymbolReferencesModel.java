/*
 * Copyright (c) 2019, WSO2 Inc. (http://wso2.com) All Rights Reserved.
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
package org.ballerinalang.langserver.definition;

import org.wso2.ballerinalang.compiler.semantics.model.symbols.BSymbol;
import org.wso2.ballerinalang.compiler.util.diagnotic.DiagnosticPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a BLang Symbol reference.
 *
 * @since 0.990.4
 */
public class SymbolReferencesModel {
    private List<Reference> references = new ArrayList<>();
    private List<Reference> definitions = new ArrayList<>();
    private Reference symbolAtCursor = null;

    public List<Reference> getReferences() {
        return references;
    }

    public List<Reference> getDefinitions() {
        return definitions;
    }

    public void addReference(Reference reference) {
        this.references.add(reference);
    }

    public void addDefinition(Reference definition) {
        this.definitions.add(definition);
    }

    public Optional<Reference> getSymbolAtCursor() {
        return Optional.ofNullable(symbolAtCursor);
    }

    public void setSymbolAtCursor(Reference symbol) {
        this.symbolAtCursor = symbol;
    }

    public static class Reference {
        private DiagnosticPos position;
        private BSymbol symbol;
        private String compilationUnit;
        private String pkgName;

        public Reference(DiagnosticPos position, BSymbol symbol, String compilationUnit, String pkgName) {
            this.position = position;
            this.symbol = symbol;
            this.compilationUnit = compilationUnit;
            this.pkgName = pkgName;
        }

        public DiagnosticPos getPosition() {
            return position;
        }

        public String getCompilationUnit() {
            return compilationUnit;
        }

        public String getPkgName() {
            return pkgName;
        }

        public BSymbol getSymbol() {
            return symbol;
        }
    }
}

