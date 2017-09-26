/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.ballerinalang.net.jms.actions;

import org.ballerinalang.bre.Context;
import org.ballerinalang.connector.api.ConnectorFuture;
import org.ballerinalang.model.types.TypeEnum;
import org.ballerinalang.model.values.BConnector;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.nativeimpl.actions.ClientConnectorFuture;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.Attribute;
import org.ballerinalang.natives.annotations.BallerinaAction;
import org.ballerinalang.natives.annotations.BallerinaAnnotation;
import org.ballerinalang.natives.connectors.AbstractNativeAction;
import org.ballerinalang.natives.connectors.BallerinaConnectorManager;
import org.ballerinalang.net.jms.actions.utils.Constants;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.messaging.CarbonMessageProcessor;
import org.wso2.carbon.messaging.ClientConnector;

import java.util.ServiceLoader;
import java.util.UUID;

/**
 * {@code Init} is the Init action implementation of the JMS Connector.
 *
 * @since 0.9
 */
@BallerinaAction(
        packageName = "ballerina.net.jms",
        actionName = "<init>",
        connectorName = Constants.CONNECTOR_NAME,
        args = {@Argument(name = "c", type = TypeEnum.CONNECTOR)
        },
        connectorArgs = {
                @Argument(name = "properties", type = TypeEnum.MAP)
        }
)
@BallerinaAnnotation(annotationName = "Description", attributes = { @Attribute(name = "value",
        value = "The init action implementation for JMS connector.") })
@Component(
        name = "action.net.jms.init",
        immediate = true,
        service = AbstractNativeAction.class)
public class Init extends AbstractJMSAction {

    @Override
    public ConnectorFuture execute(Context context) {
//        if (BallerinaConnectorManager.getInstance().
//                getClientConnector(Constants.PROTOCOL_JMS) == null) {
//            CarbonMessageProcessor carbonMessageProcessor = BallerinaConnectorManager.getInstance()
//                    .getMessageProcessor();
//            ServiceLoader<ClientConnector> clientConnectorLoader = ServiceLoader.load(ClientConnector.class);
//            clientConnectorLoader.forEach((clientConnector) -> {
//                clientConnector.setMessageProcessor(carbonMessageProcessor);
//                BallerinaConnectorManager.getInstance().registerClientConnector(clientConnector);
//            });
//        }
        BConnector bConnector = (BConnector) getRefArgument(context, 0);
        BMap sharedMap = (BMap) bConnector.getRefField(0);
        sharedMap.put(Constants.JMS_CONNECTOR_KEY, new BString(UUID.randomUUID().toString()));
        ClientConnectorFuture future = new ClientConnectorFuture();
        future.notifySuccess();
        return future;
    }

    @Override
    public boolean isNonBlockingAction() {
        return false;
    }
}
