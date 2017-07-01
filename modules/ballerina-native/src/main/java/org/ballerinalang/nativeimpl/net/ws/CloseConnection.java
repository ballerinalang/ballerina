/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.ballerinalang.nativeimpl.net.ws;

import org.ballerinalang.bre.Context;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Attribute;
import org.ballerinalang.natives.annotations.BallerinaAnnotation;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.services.dispatchers.ws.Constants;
import org.ballerinalang.services.dispatchers.ws.WebSocketConnectionManager;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.wso2.carbon.messaging.CarbonMessage;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * Close the current connection inside the resource.
 */
@BallerinaFunction(
        packageName = "ballerina.net.ws",
        functionName = "closeConnection",
        isPublic = true
)
@BallerinaAnnotation(annotationName = "Description",
                     attributes = { @Attribute(name = "value", value = "This close the current connection") })
public class CloseConnection extends AbstractNativeFunction {

    @Override
    public BValue[] execute(Context context) {

        if (context.getServiceInfo() == null) {
            throw new BallerinaException("This function is only working with services");
        }

        try {
            CarbonMessage carbonMessage = context.getCarbonMessage();
            Session session = (Session) carbonMessage.getProperty(Constants.WEBSOCKET_SESSION);
            session.close(new CloseReason(() -> 1000, "Normal closure"));
            WebSocketConnectionManager.getInstance().removeConnectionFromAll(session);
        } catch (Throwable e) {
            throw new BallerinaException("Error occurred in removing the connection");
        }
        return VOID_RETURN;
    }
}
