/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballerinalang.net.http.actions.httpclient;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.CallableUnitCallback;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.net.http.DataContext;
import org.ballerinalang.net.http.HttpConstants;
import org.ballerinalang.net.http.HttpUtil;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.wso2.transport.http.netty.contract.ClientConnectorException;
import org.wso2.transport.http.netty.message.HTTPCarbonMessage;


/**
 * {@code Get} is the GET action implementation of the HTTP Connector.
 */
@BallerinaFunction(
        orgName = "ballerina", packageName = "http",
        functionName = "get",
        receiver = @Receiver(type = TypeKind.STRUCT, structType = HttpConstants.CALLER_ACTIONS,
                structPackage = "ballerina.http"),
        args = {
                @Argument(name = "client", type = TypeKind.STRUCT),
                @Argument(name = "path", type = TypeKind.STRING),
                @Argument(name = "req", type = TypeKind.STRUCT, structType = "Request",
                        structPackage = "ballerina.http")
        },
        returnType = {
                @ReturnType(type = TypeKind.STRUCT, structType = "Response", structPackage = "ballerina.http"),
                @ReturnType(type = TypeKind.STRUCT, structType = "HttpConnectorError",
                        structPackage = "ballerina.http"),
        }
)
public class Get extends AbstractHTTPAction {

    @Override
    public void execute(Context context, CallableUnitCallback callback) {
        DataContext dataContext = new DataContext(context, callback, createOutboundRequestMsg(context));
        try {
            executeNonBlockingAction(dataContext);
        } catch (ClientConnectorException clientConnectorException) {
            // This is should be a JavaError. Need to handle this properly.
            BallerinaException exception = new BallerinaException("Failed to invoke 'get' action in " +
                    HttpConstants.CALLER_ACTIONS + ". " + clientConnectorException.getMessage(), context);
            dataContext.notifyReply(null, HttpUtil.getHttpConnectorError(context, exception));
        }
    }

    @Override
    public boolean persistAfterOperation() {
        return true;
    }

    protected HTTPCarbonMessage createOutboundRequestMsg(Context context) {
        HTTPCarbonMessage outboundReqMsg = super.createOutboundRequestMsg(context);
        outboundReqMsg.setProperty(HttpConstants.HTTP_METHOD, HttpConstants.HTTP_METHOD_GET);
        return outboundReqMsg;
    }
}
