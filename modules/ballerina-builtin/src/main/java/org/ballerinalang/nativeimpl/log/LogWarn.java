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

package org.ballerinalang.nativeimpl.log;

import org.ballerinalang.bre.Context;
import org.ballerinalang.logging.BLogRecord;
import org.ballerinalang.logging.BLogger;
import org.ballerinalang.logging.util.BLogLevel;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;

/**
 * Native function ballerina.log:warn
 *
 * @since 0.89
 */
@BallerinaFunction(
        packageName = "ballerina.log",
        functionName = "warn",
        args = {@Argument(name = "value", type = TypeKind.STRING)},
        isPublic = true
)
public class LogWarn extends AbstractLogFunction {

    private static final BLogger logger = new BLogger("ballerina.logger.warn", BLogLevel.WARN);

    public BValue[] execute(Context ctx) {
        BLogRecord logRecord = createLogRecord(ctx, BLogLevel.WARN);
        logRecord.setLoggerName("ballerina.logger.warn");
        logger.log(logRecord);
        return VOID_RETURN;
    }
}
