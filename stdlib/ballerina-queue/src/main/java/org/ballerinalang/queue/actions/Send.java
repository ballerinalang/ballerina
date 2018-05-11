/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.ballerinalang.queue.actions;

import io.ballerina.messaging.broker.core.BrokerException;
import io.ballerina.messaging.broker.core.Message;
import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.CallableUnitCallback;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.net.jms.Constants;
import org.ballerinalang.queue.BrokerUtils;
import org.ballerinalang.queue.QueueConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code Send} is the send action implementation of the JMS Connector.
 */
@BallerinaFunction(orgName = "ballerina", packageName = "queue",
                   functionName = "send",
                   receiver = @Receiver(type = TypeKind.STRUCT,
                                        structType = "ClientConnector",
                                        structPackage =
                                                "ballerina.queue"),
                   args = {
                           @Argument(name = "message",
                                     type = TypeKind.STRUCT)
                   }
)
public class Send extends AbstractJMSAction {
    private static final Logger log = LoggerFactory.getLogger(Send.class);

    public void execute(Context context, CallableUnitCallback callableUnitCallback) {


        // Extract argument values
        BStruct messageStruct = ((BStruct) context.getRefArgument(1));
        try {
            BrokerUtils.publish((Message) messageStruct.getNativeData(QueueConstants.JMS_API_MESSAGE));
        } catch (BrokerException e) {
            log.error("Message sending failed", e);
        }
        callableUnitCallback.notifySuccess();
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
