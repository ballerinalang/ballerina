/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ballerinalang.test.logging;

import org.ballerinalang.test.BaseTest;
import org.ballerinalang.test.context.BMainInstance;
import org.ballerinalang.test.context.BallerinaTestException;
import org.testng.annotations.Test;

import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Test case for the ballerina/log module.
 *
 * @since 1.0.0
 */
public class LogAPITestCase extends BaseTest {

    private static final String projectDirPath = Paths.get("src", "test", "resources", "logging", "log-project")
            .toAbsolutePath().toString();
    private static final String testFileLocation = Paths.get("src", "test", "resources", "logging")
            .toAbsolutePath().toString();
    private static final String logMessageTestFileLocation = Paths.get("src", "test", "resources",
            "logging", "log-messages").toAbsolutePath().toString();
    private static final String logLevelTestFileName = "log_level_test.bal";
    private static final String logMessageInfoTestFileName = "print_info_test.bal";
    private static final String logMessageDebugTestFileName = "print_debug_test.bal";
    private static final String logMessageTraceTestFileName = "print_trace_test.bal";
    private static final String logMessageErrorTestFileName = "print_error_test.bal";
    private static final String logMessageWarnTestFileName = "print_warn_test.bal";
    private static final String logMessageSprintInfoTestFileName = "sprint_info_test.bal";
    private static final String logMessageSprintErrorTestFileName = "sprint_error_test.bal";
    private static final String logMessageSprintDebugTestFileName = "sprint_debug_test.bal";
    private static final String logMessageSprintWarnTestFileName = "sprint_warn_test.bal";
    private static final String logMessageSprintTraceTestFileName = "sprint_trace_test.bal";
    private static final String logLevelProperty = "b7a.log.level";

    private static final String errLog = "ERROR level log";
    private static final String errLogWithErr = "ERROR level log with error : error B7aError foo=bar";
    private static final String warnLog = "WARN level log";
    private static final String infoLog = "INFO level log";
    private static final String debugLog = "DEBUG level log";
    private static final String traceLog = "TRACE level log";
    private static final String integerOutput = "123456";
    private static final String floatOutput = "123456.789";
    private static final String booleanOutput = "true";
    private static final String functionOutput = "Name of the fruit is is Apple";
    private static final String formattedOutput = "Employee John is 28 years old";

    private static final PrintStream console = System.out;

    @Test(description = "Tests basic log functionality")
    public void testBasicLogFunctionality() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String output = bMainInstance.runMainAndReadStdOut("run", new String[]{"mainmod"}, new HashMap<>(),
                                                           projectDirPath, true);
        String[] logLines = output.split("\n");
        assertEquals(logLines.length, 13);

        validateLog(logLines[9], "INFO", "[logorg/foo]", "Logging from inside `foo` module");
        validateLog(logLines[10], "INFO", "[logorg/bar]", "Logging from inside `bar` module");
        validateLog(logLines[11], "ERROR", "[logorg/baz]", "Logging at ERROR level inside `baz`");
        validateLog(logLines[12], "INFO", "[logorg/mainmod]", "Logging from inside `mainmod` module");
    }

    @Test(description = "Tests log functionality when all log are turned off")
    public void testLogsOff() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[]{logLevelTestFileName, "--" + logLevelProperty + "=OFF"};
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), testFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 3);
    }

    @Test(description = "Tests error level log functionality")
    public void testErrorLevel() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[] {logLevelTestFileName, "--" + logLevelProperty + "=ERROR" };
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), testFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 6);

        console.println(logLines[4]);
        validateLog(logLines[4], "ERROR", "[]", errLog);

        console.println(logLines[5]);
        validateLog(logLines[5], "ERROR", "[]", errLogWithErr);
    }

    @Test(description = "Tests warn level log functionality")
    public void testWarnLevel() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[] {logLevelTestFileName, "--" + logLevelProperty + "=WARN", };
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), testFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 7);

        console.println(logLines[4]);
        validateLog(logLines[4], "ERROR", "[]", errLog);

        console.println(logLines[5]);
        validateLog(logLines[5], "ERROR", "[]", errLogWithErr);

        console.println(logLines[6]);
        validateLog(logLines[6], "WARN", "[]", warnLog);
    }

    @Test(description = "Tests info level log functionality")
    public void testInfoLevel() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[]{logLevelTestFileName, "--" + logLevelProperty + "=INFO"};
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), testFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 8);

        console.println(logLines[4]);
        validateLog(logLines[4], "ERROR", "[]", errLog);

        console.println(logLines[5]);
        validateLog(logLines[5], "ERROR", "[]", errLogWithErr);

        console.println(logLines[6]);
        validateLog(logLines[6], "WARN", "[]", warnLog);

        console.println(logLines[7]);
        validateLog(logLines[7], "INFO", "[]", infoLog);
    }

    @Test(description = "Tests debug level log functionality")
    public void testDebugLevel() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[] {logLevelTestFileName, "--" + logLevelProperty + "=DEBUG" };
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), testFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 9);

        console.println(logLines[4]);
        validateLog(logLines[4], "ERROR", "[]", errLog);

        console.println(logLines[5]);
        validateLog(logLines[5], "ERROR", "[]", errLogWithErr);

        console.println(logLines[6]);
        validateLog(logLines[6], "WARN", "[]", warnLog);

        console.println(logLines[7]);
        validateLog(logLines[7], "INFO", "[]", infoLog);

        console.println(logLines[8]);
        validateLog(logLines[8], "DEBUG", "[]", debugLog);
    }

    @Test(description = "Tests trace level log functionality")
    public void testTraceLevel() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[] {logLevelTestFileName, "--" + logLevelProperty + "=TRACE" };
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), testFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 10);

        console.println(logLines[4]);
        validateLog(logLines[4], "ERROR", "[]", errLog);

        console.println(logLines[5]);
        validateLog(logLines[5], "ERROR", "[]", errLogWithErr);

        console.println(logLines[6]);
        validateLog(logLines[6], "WARN", "[]", warnLog);

        console.println(logLines[7]);
        validateLog(logLines[7], "INFO", "[]", infoLog);

        console.println(logLines[8]);
        validateLog(logLines[8], "DEBUG", "[]", debugLog);

        console.println(logLines[9]);
        validateLog(logLines[9], "TRACE", "[]", traceLog);
    }

    @Test(description = "Tests log functionality when all log levels are turned on")
    public void testAllOn() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[] {logLevelTestFileName, "--" + logLevelProperty + "=ALL" };
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), testFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 10);

        console.println(logLines[4]);
        validateLog(logLines[4], "ERROR", "[]", errLog);

        console.println(logLines[5]);
        validateLog(logLines[5], "ERROR", "[]", errLogWithErr);

        console.println(logLines[6]);
        validateLog(logLines[6], "WARN", "[]", warnLog);

        console.println(logLines[7]);
        validateLog(logLines[7], "INFO", "[]", infoLog);

        console.println(logLines[8]);
        validateLog(logLines[8], "DEBUG", "[]", debugLog);

        console.println(logLines[9]);
        validateLog(logLines[9], "TRACE", "[]", traceLog);
    }

    @Test(description = "Tests log functionality when log level is set tp package")
    public void testSettingLogLevelToPackage() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[] { "mainmod", "--logorg/foo.loglevel=DEBUG", "--logorg/baz.loglevel=ERROR",
                "--logorg/mainmod.loglevel=OFF" };
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), projectDirPath, true);
        String[] logLines = output.split("\n");
        assertEquals(logLines.length, 13, printLogLines(logLines));

        console.println(logLines[9]);
        validateLog(logLines[9], "INFO", "[logorg/foo]", "Logging from inside `foo` module");

        console.println(logLines[10]);
        validateLog(logLines[10], "DEBUG", "[logorg/foo]", "Logging at DEBUG level inside `foo`");

        console.println(logLines[11]);
        validateLog(logLines[11], "INFO", "[logorg/bar]", "Logging from inside `bar` module");

        console.println(logLines[12]);
        validateLog(logLines[12], "ERROR", "[logorg/baz]", "Logging at ERROR level inside `baz`");
    }

    @Test(description = "Tests printError functionality")
    public void testErrorMessage() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[]{logMessageErrorTestFileName, "--" + logLevelProperty + "=ERROR"};
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), logMessageTestFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 9);

        console.println(logLines[4]);
        validateLog(logLines[4], "ERROR", "[]", errLog);

        console.println(logLines[5]);
        validateLog(logLines[5], "ERROR", "[]", integerOutput);

        console.println(logLines[6]);
        validateLog(logLines[6], "ERROR", "[]", floatOutput);

        console.println(logLines[7]);
        validateLog(logLines[7], "ERROR", "[]", booleanOutput);

        console.println(logLines[8]);
        validateLog(logLines[8], "ERROR", "[]", functionOutput);
    }

    @Test(description = "Tests printWarn functionality")
    public void testWarnMessage() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[]{logMessageWarnTestFileName, "--" + logLevelProperty + "=WARN"};
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), logMessageTestFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 9);

        console.println(logLines[4]);
        validateLog(logLines[4], "WARN", "[]", warnLog);

        console.println(logLines[5]);
        validateLog(logLines[5], "WARN", "[]", integerOutput);

        console.println(logLines[6]);
        validateLog(logLines[6], "WARN", "[]", floatOutput);

        console.println(logLines[7]);
        validateLog(logLines[7], "WARN", "[]", booleanOutput);

        console.println(logLines[8]);
        validateLog(logLines[8], "WARN", "[]", functionOutput);
    }

    @Test(description = "Tests printInfo functionality")
    public void testInfoMessage() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[]{logMessageInfoTestFileName, "--" + logLevelProperty + "=INFO"};
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), logMessageTestFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 9);

        console.println(logLines[4]);
        validateLog(logLines[4], "INFO", "[]", infoLog);

        console.println(logLines[5]);
        validateLog(logLines[5], "INFO", "[]", integerOutput);

        console.println(logLines[6]);
        validateLog(logLines[6], "INFO", "[]", floatOutput);

        console.println(logLines[7]);
        validateLog(logLines[7], "INFO", "[]", booleanOutput);

        console.println(logLines[8]);
        validateLog(logLines[8], "INFO", "[]", functionOutput);
    }

    @Test(description = "Tests printDebug functionality")
    public void testDebugMessage() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[]{logMessageDebugTestFileName, "--" + logLevelProperty + "=DEBUG"};
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), logMessageTestFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 9);

        console.println(logLines[4]);
        validateLog(logLines[4], "DEBUG", "[]", debugLog);

        console.println(logLines[5]);
        validateLog(logLines[5], "DEBUG", "[]", integerOutput);

        console.println(logLines[6]);
        validateLog(logLines[6], "DEBUG", "[]", floatOutput);

        console.println(logLines[7]);
        validateLog(logLines[7], "DEBUG", "[]", booleanOutput);

        console.println(logLines[8]);
        validateLog(logLines[8], "DEBUG", "[]", functionOutput);
    }

    @Test(description = "Tests printTrace functionality")
    public void testTraceMessage() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[]{logMessageTraceTestFileName, "--" + logLevelProperty + "=TRACE"};
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), logMessageTestFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 9);

        console.println(logLines[4]);
        validateLog(logLines[4], "TRACE", "[]", traceLog);

        console.println(logLines[5]);
        validateLog(logLines[5], "TRACE", "[]", integerOutput);

        console.println(logLines[6]);
        validateLog(logLines[6], "TRACE", "[]", floatOutput);

        console.println(logLines[7]);
        validateLog(logLines[7], "TRACE", "[]", booleanOutput);

        console.println(logLines[8]);
        validateLog(logLines[8], "TRACE", "[]", functionOutput);
    }

    @Test(description = "Tests sprintError functionality")
    public void testSprintErrorMessage() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[]{logMessageSprintErrorTestFileName, "--" + logLevelProperty + "=ERROR"};
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), logMessageTestFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 5);

        console.println(logLines[4]);
        validateLog(logLines[4], "ERROR", "[ballerina/log]", formattedOutput);
    }

    @Test(description = "Tests sprintWarn functionality")
    public void testSprintWarnMessage() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[]{logMessageSprintWarnTestFileName, "--" + logLevelProperty + "=WARN"};
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), logMessageTestFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 5);

        console.println(logLines[4]);
        validateLog(logLines[4], "WARN", "[ballerina/log]", formattedOutput);
    }

    @Test(description = "Tests sprintInfo functionality")
    public void testSprintInfoMessage() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[]{logMessageSprintInfoTestFileName, "--" + logLevelProperty + "=INFO"};
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), logMessageTestFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 5);

        console.println(logLines[4]);
        validateLog(logLines[4], "INFO", "[ballerina/log]", formattedOutput);
    }

    @Test(description = "Tests sprintDebug functionality")
    public void testSprintDebugMessage() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[]{logMessageSprintDebugTestFileName, "--" + logLevelProperty + "=DEBUG"};
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), logMessageTestFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 5);

        console.println(logLines[4]);
        validateLog(logLines[4], "DEBUG", "[ballerina/log]", formattedOutput);
    }

    @Test(description = "Tests sprintTrace functionality")
    public void testSprintTraceMessage() throws BallerinaTestException {
        BMainInstance bMainInstance = new BMainInstance(balServer);
        String[] args = new String[]{logMessageSprintTraceTestFileName, "--" + logLevelProperty + "=TRACE"};
        String output = bMainInstance.runMainAndReadStdOut("run", args, new HashMap<>(), logMessageTestFileLocation, true);
        String[] logLines = output.split("\n");

        assertEquals(logLines.length, 5);

        console.println(logLines[4]);
        validateLog(logLines[4], "TRACE", "[ballerina/log]", formattedOutput);
    }

    private void validateLog(String log, String logLevel, String logLocation, String logMsg) {
        assertTrue(log.contains(logLevel));
        assertTrue(log.contains(logLocation));
        assertTrue(log.contains(logMsg));
    }

    private String printLogLines(String[] logs) {
        StringBuilder sBuilder = new StringBuilder("\n");
        for (String log : logs) {
            sBuilder.append(log).append('\n');
        }
        return sBuilder.toString();
    }
}
