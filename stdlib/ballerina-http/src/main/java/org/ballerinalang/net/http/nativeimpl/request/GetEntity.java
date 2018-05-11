/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.ballerinalang.net.http.nativeimpl.request;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.bre.bvm.persistency.ConnectionException;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.net.http.HttpUtil;

import static org.ballerinalang.net.http.HttpConstants.HTTP_MESSAGE_INDEX;
import static org.ballerinalang.net.http.HttpConstants.TRANSPORT_MESSAGE;

/**
 * Get the 'Entity' of the inbound request with the body.
 *
 * @since 0.96.0
 */
@BallerinaFunction(orgName = "ballerina", packageName = "http",
                   functionName = "getEntity",
                   receiver = @Receiver(type = TypeKind.STRUCT,
                                        structType = "Request",
                                        structPackage = "ballerina.http"),
                   returnType = { @ReturnType(type = TypeKind.STRUCT) },
                   isPublic = true)
public class GetEntity extends BlockingNativeCallableUnit {
    @Override
    public void execute(Context context) {
        BStruct httpMessageStruct = (BStruct) context.getRefArgument(HTTP_MESSAGE_INDEX);
        if (httpMessageStruct == null || httpMessageStruct.getNativeData(TRANSPORT_MESSAGE) == null) {
            throw new ConnectionException("Incoming message is not set.");
        }
        context.setReturnValues(HttpUtil.getEntity(context, true, true));
    }
}
