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

package org.ballerinalang.logging.formatters.jul;

import org.ballerinalang.logging.BLogManager;
import org.ballerinalang.logging.util.BLogLevelMapper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A custom log formatter for formatting the BRE log
 *
 * @since 0.94
 */
public class BRELogFormatter extends Formatter {

    private final String format;

    private SimpleDateFormat dateFormat;

    public BRELogFormatter() {
        format = BLogManager.getLogManager().getProperty(BRELogFormatter.class.getCanonicalName() + ".format");
    }

    @Override
    public String format(LogRecord record) {
        String ex = "";

        if (record.getThrown() != null) {
            StringWriter stringWriter = new StringWriter();
            stringWriter.append('\n');
            record.getThrown().printStackTrace(new PrintWriter(stringWriter));
            ex = stringWriter.toString();
        }

        // TODO: Make BRE log more configurable by adding optional fields such as class name, method, thread ID
        return String.format(format,
                             record.getMillis(),
                             BLogLevelMapper.getBallerinaLogLevel(record.getLevel()),
                             record.getLoggerName(),
                             record.getMessage(),
                             ex);
    }
}
