/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.ballerinalang.logging.test;

/**
 * Tests for log formatter implementations
 */
public class LogFormatterTest {

//    private PrintStream original;
//
//    @BeforeClass
//    public void setup() {
//        original = System.err;
//    }
//
//    @Test
//    public void testConsoleLogFormatter() throws IOException {
//        String expectedLogMsg = "INFO [.:main] [test.bal:10] [default] - \"Test log message\" ";
//        String logFormat = "{{timestamp}}[yyyy-MM-dd HH:mm:ss,SSS] {{level}} [{{package}}:{{unit}}] " +
//                "[{{file}}:{{line}}] [{{worker}}] - \"{{msg}}\" {{err}}";
//
//        ConsoleLogFormatter logFormatter = new ConsoleLogFormatter();
//
//        BLogRecord logRecord = new BLogRecord(BLogLevel.INFO, "Test log message");
//        logRecord.setLoggerName("ballerina.logger.info");
//        logRecord.setPackageName(".");
//        logRecord.setCallableUnitName("main");
//        logRecord.setFileName("test.bal");
//        logRecord.setLineNumber(10);
//        logRecord.setWorkerName("default");
//
//        String logRecordStr = logFormatter.format(logRecord).substring("yyyy-MM-dd HH:mm:ss,SSS ".length());
//
//        Assert.assertEquals(logRecordStr, expectedLogMsg);
//    }


}
