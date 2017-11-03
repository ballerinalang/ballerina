/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.ballerinalang.logging.util;

import org.ballerinalang.logging.BLogManager;

import java.util.logging.Level;

/**
 * A mapper class to map log levels from JDK logging to Ballerina log levels.
 *
 * @since 0.89
 */
public class BLogLevelMapper {

    public static BLogLevel getBallerinaLogLevel(String level) {
        switch (level) {
            case "SEVERE":
                return BLogLevel.ERROR;
            case "WARNING":
                return BLogLevel.WARN;
            case "INFO":
                return BLogLevel.INFO;
            case "CONFIG":
                return BLogLevel.INFO;
            case "FINE":
                return BLogLevel.DEBUG;
            case "FINER":
                return BLogLevel.DEBUG;
            case "FINEST":
                return BLogLevel.TRACE;
            case "ALL":
                return BLogLevel.ALL;
            case "OFF":
                return BLogLevel.OFF;
            default:
                BLogManager.stdErr.println("Invalid log level: " + level + ", defaulting to 'INFO'.");
                return BLogLevel.INFO;
        }
    }

    public static BLogLevel getBallerinaLogLevel(Level level) {
        return getBallerinaLogLevel(level.getName());
    }

    public static String getJDKLogLevel(String level) {
        switch (level) {
            case "ERROR":
                return "SEVERE";
            case "WARN":
                return "WARNING";
            case "INFO":
                return "INFO";
            case "DEBUG":
                return "FINE";
            case "TRACE":
                return "FINEST";
            case "ALL":
                return "ALL";
            case "OFF":
                return "OFF";
            default:
                BLogManager.stdErr.println("Invalid log level: " + level + ", defaulting to 'INFO'.");
                return "INFO";
        }
    }
}
