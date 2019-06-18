/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.ballerinalang.messaging.rabbitmq.nativeimpl.message;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.BlockingNativeCallableUnit;
import org.ballerinalang.messaging.rabbitmq.RabbitMQConstants;
import org.ballerinalang.messaging.rabbitmq.RabbitMQTransactionContext;
import org.ballerinalang.messaging.rabbitmq.RabbitMQUtils;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BFloat;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Retrieves the float content of the RabbitMQ message.
 *
 * @since 0.995.0
 */
@BallerinaFunction(
        orgName = RabbitMQConstants.ORG_NAME,
        packageName = RabbitMQConstants.RABBITMQ,
        functionName = "getFloatContent",
        receiver = @Receiver(type = TypeKind.OBJECT,
                structType = RabbitMQConstants.MESSAGE_OBJECT,
                structPackage = RabbitMQConstants.PACKAGE_RABBITMQ),
        isPublic = true
)
public class GetFloatContent extends BlockingNativeCallableUnit {

    @Override
    public void execute(Context context) {
        boolean isInTransaction = context.isInTransaction();
        @SuppressWarnings(RabbitMQConstants.UNCHECKED)
        BMap<String, BValue> messageObject = (BMap<String, BValue>) context.getRefArgument(0);
        byte[] messageContent = (byte[]) messageObject.getNativeData(RabbitMQConstants.MESSAGE_CONTENT);
        RabbitMQTransactionContext transactionContext = (RabbitMQTransactionContext) messageObject.
                getNativeData(RabbitMQConstants.RABBITMQ_TRANSACTION_CONTEXT);
        if (isInTransaction && !Objects.isNull(transactionContext)) {
            transactionContext.handleTransactionBlock(context);
        }
        try {
            context.setReturnValues(new BFloat(Float.parseFloat(new String(messageContent,
                    StandardCharsets.UTF_8.name()))));
        } catch (UnsupportedEncodingException exception) {
            RabbitMQUtils.returnError("Error while retrieving the float content of the message: "
                    + exception.getMessage(), context, exception);
        }
    }
}
