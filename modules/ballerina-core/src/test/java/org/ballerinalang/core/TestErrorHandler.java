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
package org.ballerinalang.core;

import org.wso2.carbon.messaging.CarbonCallback;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.carbon.messaging.DefaultCarbonMessage;
import org.wso2.carbon.messaging.ServerConnectorErrorHandler;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Test error handler used with handling server connector error with http related test cases.
 */
public class TestErrorHandler implements ServerConnectorErrorHandler {
    @Override
    public void handleError(Exception exception, CarbonMessage carbonMessage, CarbonCallback carbonCallback)
            throws Exception {
        carbonCallback.done(createErrorMessage(exception.getMessage(), 500));
    }

    @Override
    public String getProtocol() {
        return "http";
    }

    private CarbonMessage createErrorMessage(String payload, int statusCode) {

        DefaultCarbonMessage response = new DefaultCarbonMessage();

        response.setStringMessageBody(payload);
        byte[] errorMessageBytes = payload.getBytes(Charset.defaultCharset());

        Map<String, String> transportHeaders = new HashMap<>();
        transportHeaders.put(org.wso2.carbon.transport.http.netty.common.Constants.HTTP_CONNECTION,
                org.wso2.carbon.transport.http.netty.common.Constants.CONNECTION_KEEP_ALIVE);
        transportHeaders.put(org.wso2.carbon.transport.http.netty.common.Constants.HTTP_CONTENT_TYPE,
                org.wso2.carbon.transport.http.netty.common.Constants.TEXT_PLAIN);
        transportHeaders.put(org.wso2.carbon.transport.http.netty.common.Constants.HTTP_CONTENT_LENGTH,
                (String.valueOf(errorMessageBytes.length)));

        response.setHeaders(transportHeaders);

        response.setProperty(org.wso2.carbon.transport.http.netty.common.Constants.HTTP_STATUS_CODE, statusCode);
        response.setProperty(org.wso2.carbon.messaging.Constants.DIRECTION,
                org.wso2.carbon.messaging.Constants.DIRECTION_RESPONSE);
        return response;

    }
}
