/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.ballerinalang.nativeimpl.util;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.ReturnType;

/**
 * Native function ballerina.util:decimalToHex.
 *
 * @since 0.962.0
 */
@BallerinaFunction(
        packageName = "ballerina.util",
        functionName = "decimalToHex",
        args = {@Argument(name = "decimalValue", type = TypeKind.INT)},
        returnType = {@ReturnType(type = TypeKind.STRING)},
        isPublic = true
)
public class DecimalToHex extends AbstractNativeFunction {

    @Override
    public BValue[] execute(Context context) {
        long decimalValue = getIntArgument(context, 0);
        return getBValues(new BString("0x".concat(Long.toHexString(decimalValue))));
    }
}
