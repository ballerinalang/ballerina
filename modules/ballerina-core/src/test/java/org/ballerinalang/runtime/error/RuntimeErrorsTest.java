/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.runtime.error;

import org.ballerinalang.BLangProgramLoader;
import org.ballerinalang.BLangProgramRunner;
import org.ballerinalang.bre.Context;
import org.ballerinalang.core.EnvironmentInitializer;
import org.ballerinalang.core.utils.BTestUtils;
import org.ballerinalang.core.utils.MessageUtils;
import org.ballerinalang.model.BLangProgram;
import org.ballerinalang.model.util.Services;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.ballerinalang.util.program.BLangFunctions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.messaging.CarbonMessage;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Runtime Errors test class for ballerina filers.
 * This class test error handling for runtime errors in ballerina files.
 */
public class RuntimeErrorsTest {

    private BLangProgram bLangProgram;

    @BeforeClass
    public void setup() {
        bLangProgram = BTestUtils.parseBalFile("lang/errors/runtime");
    }

    @Test
    public void testStackTraceOnError() {
        Exception ex = null;
        Context bContext = new Context();
        String expectedStackTrace = "\t at lang.errors.runtime:getApple(runtime-errors.bal:27)\n" +
                "\t at lang.errors.runtime:getFruit2(runtime-errors.bal:23)\n" +
                "\t at lang.errors.runtime:getFruit1(runtime-errors.bal:19)\n" +
                "\t at lang.errors.runtime:testStackTrace(runtime-errors.bal:15)";
        try {
            BLangFunctions.invoke(bLangProgram, "testStackTrace", new BString[0], bContext);
        } catch (Exception e) {
            ex = e;
        } finally {
            Assert.assertTrue(ex instanceof BallerinaException, "Expected a " + BallerinaException.class.getName() +
                    ", but found: " + ex + ".");
            Assert.assertEquals(getRootCause(ex).getMessage(), "arrays index out of range: Index: 24, Size: 0",
                    "Incorrect error message printed.");
            // Check the stack trace
            Assert.assertNotNull(((BallerinaException) ex).getBException());
            String stackTrace = ((BallerinaException) ex).getBException().value().getStackTrace();
            Assert.assertEquals(stackTrace, expectedStackTrace);
        }
    }

    @Test(description = "Test error of a service in default package")
    public void testDefaultPackageServiceError() {
        String sourceFilePath = "lang/errors/undeclared-package-errors.bal";
        Path path;
        try {
            path = Paths.get(BTestUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("error while running test: " + e.getMessage());
        }
        BLangProgram undeclaredPackageProgram = new BLangProgramLoader().loadMain(path, Paths.get(sourceFilePath));
        Exception ex = null;
        String expectedStackTrace = "error in ballerina program: arrays index out of range: Index: 24, Size: 0\n" +
                "\t at getApple(undeclared-package-errors.bal:20)\n" +
                "\t at getFruit2(undeclared-package-errors.bal:16)\n" +
                "\t at getFruit1(undeclared-package-errors.bal:12)\n" +
                "\t at testStackTrace(undeclared-package-errors.bal:8)\n" +
                "\t at main(undeclared-package-errors.bal:2)";
        try {
            new BLangProgramRunner().runMain(undeclaredPackageProgram, new String[0]);
        } catch (Exception e) {
            ex = e;
        } finally {
            // Check the stack trace
            Assert.assertEquals(ex.getMessage(), expectedStackTrace);
        }
    }

    @Test(description = "Test if a cast exception is thrown in an invalid type cast")
    public void testTypeCastError() {
        Throwable ex = null;
        Context bContext = new Context();

        try {
            BLangFunctions.invoke(bLangProgram, "testTypeCastException", new BString[0], bContext);
        } catch (Exception e) {
            ex = e;
        } finally {
            Assert.assertTrue(ex != null);
            Assert.assertTrue(ex instanceof BallerinaException, "Expected a " + BallerinaException.class.getName() +
                    ", but found: " + ex.getClass() + ".");
            Assert.assertEquals(getRootCause(ex).getMessage(), "input value value cannot be cast to integer");

        }
    }

    @Test
    public void testStackTraceOnCrossPkgError() {
        BLangProgram muplitplePackageAccessProg = EnvironmentInitializer.setup("lang/errors/services");
        Throwable ex = null;
        String expectedStackTrace = "\t at lang.errors.runtime:getFruit2(runtime-errors.bal:23)\n" +
                "\t at lang.errors.runtime:getFruit1(runtime-errors.bal:19)\n" +
                "\t at lang.errors.runtime:testStackTrace(runtime-errors.bal:15)\n" +
                "\t at lang.errors.services:echoResource(multiple-package-errors.bal:10)\n" +
                "\t at lang.errors.services:echoService(multiple-package-errors.bal:9)\n";
        try {
            CarbonMessage cMsg = MessageUtils.generateHTTPMessage("/test/crossPkg", "GET");
            Services.invoke(cMsg);
        } catch (Exception e) {
            ex = e;
        } finally {
            // Check exception type
            Assert.assertTrue(ex instanceof BallerinaException, "Expected a " + BallerinaException.class.getName() +
                    ", but found: " + ex + ".");

            // Check error message
            String errorMsg = getRootCause(ex).getMessage();
            Assert.assertEquals(errorMsg, "error in ballerina program: arrays index out of range: Index: 24, Size: 0",
                    "Incorrect error message printed.");

            // Check the stack trace
            Assert.assertNotNull(((BallerinaException) ex).getBException());
            String stackTrace = ((BallerinaException) ex).getBException().value().getStackTrace();
            Assert.assertEquals(stackTrace, expectedStackTrace);

            // cleanup
            EnvironmentInitializer.cleanup(muplitplePackageAccessProg);
        }
    }

    @Test
    public void testStackTraceOnConnectorError() {
        BLangProgram connectorErrorProg = EnvironmentInitializer.setup("lang/errors/connectors");
        Throwable ex = null;
        String expectedStackTrace = "\t at lang.errors.runtime:getFruit2(runtime-errors.bal:23)\n" +
                "\t at lang.errors.runtime:getFruit1(runtime-errors.bal:19)\n" +
                "\t at lang.errors.runtime:testStackTrace(runtime-errors.bal:15)\n" +
                "\t at lang.errors.connectors:action1(connector-runtime-errors.bal:18)\n" +
                "\t at lang.errors.connectors:echoResource(connector-runtime-errors.bal:11)\n" +
                "\t at lang.errors.connectors:echoService(connector-runtime-errors.bal:9)\n";
        try {
            CarbonMessage cMsg = MessageUtils.generateHTTPMessage("/test/connector", "GET");
            Services.invoke(cMsg);
        } catch (Exception e) {
            ex = e;
        } finally {
            // Check exception type
            Assert.assertTrue(ex instanceof BallerinaException, "Expected a " + BallerinaException.class.getName() +
                    ", but found: " + ex + ".");

            // Check error message
            String errorMsg = getRootCause(ex).getMessage();
            Assert.assertEquals(errorMsg, "error in ballerina program: arrays index out of range: Index: 24, Size: 0",
                    "Incorrect error message printed.");

            // Check the stack trace
            Assert.assertNotNull(((BallerinaException) ex).getBException());
            String stackTrace = ((BallerinaException) ex).getBException().value().getStackTrace();
            Assert.assertEquals(stackTrace, expectedStackTrace);

            // cleanup
            EnvironmentInitializer.cleanup(connectorErrorProg);
        }
    }

    private Throwable getRootCause(Throwable t) {
        if (t.getCause() != null) {
            return getRootCause(t.getCause());
        }
        return t;
    }
}
