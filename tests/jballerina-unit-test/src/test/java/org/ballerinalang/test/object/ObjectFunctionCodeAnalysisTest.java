/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.ballerinalang.test.object;

import org.ballerinalang.test.util.BAssertUtil;
import org.ballerinalang.test.util.BCompileUtil;
import org.ballerinalang.test.util.CompileResult;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.wso2.ballerinalang.compiler.tree.BLangFunction;

/**
 * Test cases for code analysis in Object types.
 */
public class ObjectFunctionCodeAnalysisTest {

    @Test
    public void testObjectFunctionReturnValidation() {
        CompileResult compileResult = BCompileUtil.compile(
                "test-src/structs/object-function-code-analysis-negative.bal");
        BAssertUtil.validateError(compileResult, 0, "this function must return a result", 5, 3);
    }

    @Test
    public void testObjectFunctionParamParentValidation() {
        CompileResult compileResult = BCompileUtil.compile(
                "test-src/structs/object-function-code-analysis.bal");
        BLangFunction function = (BLangFunction) compileResult.getAST().getFunctions().stream().findFirst().get();
        function.requiredParams.removeIf(parameter -> parameter.parent != null);
        Assert.assertEquals(function.requiredParams.size(), 0);
    }

    @Test
    public void testObjectFunctionRestParamParentValidation() {
        CompileResult compileResult = BCompileUtil.compile(
                "test-src/structs/object-function-code-analysis.bal");
        BLangFunction function = (BLangFunction) compileResult.getAST().getFunctions().stream().findFirst().get();
        Assert.assertTrue(function.restParam.parent != null);
    }

}
