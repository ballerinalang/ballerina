/*
*   Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.ballerinalang.model.statements;

import org.ballerinalang.core.utils.BTestUtils;
import org.ballerinalang.model.values.BBoolean;
import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.util.codegen.ProgramFile;
import org.ballerinalang.util.program.BLangFunctions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This contains methods to test different behaviours of the transform statement.
 *
 * @since 0.8.7
 */
public class TransformStmtTest {
    private ProgramFile programFile;

    @BeforeClass
    public void setup() {
        programFile = BTestUtils.getProgramFile("lang/statements/transformStmt/transform-stmt.bal");
    }

    @Test(description = "Test one to one simple transformation")
    public void testOneToOneTransform() {
        BValue[] returns = BLangFunctions.invokeNew(programFile, "oneToOneTransform");

        Assert.assertEquals(returns.length, 3);

        Assert.assertTrue(returns[0] instanceof BString);
        Assert.assertEquals(returns[0].stringValue(), "John");

        Assert.assertTrue(returns[1] instanceof BInteger);
        Assert.assertEquals(((BInteger) returns[1]).intValue(), 30);

        Assert.assertTrue(returns[2] instanceof BString);
        Assert.assertEquals(returns[2].stringValue(), "London");
    }

    @Test(description = "Test one to one simple transformation")
    public void testFunctionsInTransform() {
        BValue[] returns = BLangFunctions.invokeNew(programFile, "functionsInTransform");

        Assert.assertEquals(returns.length, 3);

        Assert.assertTrue(returns[0] instanceof BString);
        Assert.assertEquals(returns[0].stringValue(), "Mr.John");

        Assert.assertTrue(returns[1] instanceof BInteger);
        Assert.assertEquals(((BInteger) returns[1]).intValue(), 30);

        Assert.assertTrue(returns[2] instanceof BString);
        Assert.assertEquals(returns[2].stringValue(), "London");
    }

    @Test(description = "Test one to one simple transformation with var for temporary variables")
    public void testVarInTransform() {
        BValue[] returns = BLangFunctions.invokeNew(programFile, "varInTransform");

        Assert.assertEquals(returns.length, 3);

        Assert.assertTrue(returns[0] instanceof BString);
        Assert.assertEquals(returns[0].stringValue(), "Mr.John");

        Assert.assertTrue(returns[1] instanceof BInteger);
        Assert.assertEquals(((BInteger) returns[1]).intValue(), 30);

        Assert.assertTrue(returns[2] instanceof BString);
        Assert.assertEquals(returns[2].stringValue(), "London");
    }

    @Test(description = "Test one to one simple transformation with new variable definitions")
    public void testVarDefInTransform() {
        BValue[] returns = BLangFunctions.invokeNew(programFile, "varDefInTransform");

        Assert.assertEquals(returns.length, 3);

        Assert.assertTrue(returns[0] instanceof BString);
        Assert.assertEquals(returns[0].stringValue(), "Ms.John");

        Assert.assertTrue(returns[1] instanceof BInteger);
        Assert.assertEquals(((BInteger) returns[1]).intValue(), 30);

        Assert.assertTrue(returns[2] instanceof BString);
        Assert.assertEquals(returns[2].stringValue(), "London");
    }

    @Test(description = "Test one to one simple transformation with type cast and conversion")
    public void testCastAndConversionInTransform() {
        BValue[] returns = BLangFunctions.invokeNew(programFile, "castAndConversionInTransform");

        Assert.assertEquals(returns.length, 4);

        Assert.assertTrue(returns[0] instanceof BString);
        Assert.assertEquals(returns[0].stringValue(), "Mr.John");

        Assert.assertTrue(returns[1] instanceof BInteger);
        Assert.assertEquals(((BInteger) returns[1]).intValue(), 20);

        Assert.assertTrue(returns[2] instanceof BString);
        Assert.assertEquals(returns[2].stringValue(), "New York");

        Assert.assertTrue(returns[3] instanceof BInteger);
        Assert.assertEquals(returns[3].stringValue(), "30");
    }

    @Test(description = "Test one to one transformation with temporary variables")
    public void testTempVariablesInTransform() {
        BValue[] returns = BLangFunctions.invokeNew(programFile, "temporaryVariablesInTransform");

        Assert.assertEquals(returns.length, 2);

        Assert.assertTrue(returns[0] instanceof BString);
        Assert.assertEquals(returns[0].stringValue(), "Mr.John");

        Assert.assertTrue(returns[1] instanceof BString);
        Assert.assertEquals(returns[1].stringValue(), "Mr.John");
    }

    @Test(description = "Test one to one transformation with constants")
    public void testConstantsInTransform() {
        BValue[] returns = BLangFunctions.invokeNew(programFile, "constantsInTransform");

        Assert.assertEquals(returns.length, 1);

        Assert.assertTrue(returns[0] instanceof BString);
        Assert.assertEquals(returns[0].stringValue(), "Mrs.John");
    }

    @Test(description = "Test one to one transformation with binary expressions")
    public void testBinaryExpressionsInTransform() {
        BValue[] returns = BLangFunctions.invokeNew(programFile, "binaryExpressionsInTransform");

        Assert.assertEquals(returns.length, 1);

        Assert.assertTrue(returns[0] instanceof BString);
        Assert.assertEquals(returns[0].stringValue(), "Ms.John");
    }

    @Test(description = "Test one to one transformation with unary expressions")
    public void testUnaryExpressionsInTransform() {
        BValue[] returns = BLangFunctions.invokeNew(programFile, "unaryExpressionsInTransform");

        Assert.assertEquals(returns.length, 1);

        Assert.assertTrue(returns[0] instanceof BBoolean);
        Assert.assertEquals(((BBoolean) returns[0]).booleanValue(), true);
    }
}
