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
package io.samjs.jarlibrary.diagnosticutils;

import io.ballerina.tools.diagnostics.Diagnostic;
import io.ballerina.tools.diagnostics.DiagnosticInfo;
import io.ballerina.tools.diagnostics.Location;
import io.ballerina.tools.diagnostics.properties.DiagnosticProperty;
import io.ballerina.tools.text.LinePosition;
import io.ballerina.tools.text.LineRange;
import io.ballerina.tools.text.TextRange;
import io.samjs.jarlibrary.stringutils.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * A dummy diagnostic related util class.
 */
public class DiagnosticUtils {

    public static Diagnostic createDiagnostic() {
        return new Diagnostic() {
            @Override
            public Location location() {
                return new Location() {
                    @Override
                    public LineRange lineRange() {
                        return LineRange.from("foo.bal",
                                LinePosition.from(1, 2), LinePosition.from(2, 4));
                    }

                    @Override
                    public TextRange textRange() {
                        return TextRange.from(10, 5);
                    }
                };
            }

            @Override
            public DiagnosticInfo diagnosticInfo() {
                return null;
            }

            @Override
            public String message() {
                return "Dummy diagnostic";
            }

            @Override
            public List<DiagnosticProperty<?>> properties() {
                return Collections.emptyList();
            }
        };
    }

    public static boolean isStringEmpty(String value) {
        return StringUtils.isEmpty(value);
    }
}