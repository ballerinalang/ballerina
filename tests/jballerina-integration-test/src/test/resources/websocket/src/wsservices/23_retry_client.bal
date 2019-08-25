// Copyright (c) 2019 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/http;
import ballerina/log;

@http:WebSocketServiceConfig {
}
service on new http:WebSocketListener(30003) {

    resource function onOpen(http:WebSocketCaller wsEp) {
        http:WebSocketClient wsClientEp = new("ws://localhost:15300/websocket", { callbackService:
            retryClientCallbackService, readyOnConnect: false, retryConfig: {intervalInMillis : 3000}});
        wsEp.setAttribute(ASSOCIATED_CONNECTION, wsClientEp);
        wsClientEp.setAttribute(ASSOCIATED_CONNECTION, wsEp);
        var returnVal = wsClientEp->ready();
        if (returnVal is http:WebSocketError) {
            panic <error> returnVal;
        }
    }

    resource function onText(http:WebSocketCaller wsEp, string text) {
        http:WebSocketClient clientEp = getAssociatedClientEndpoint(wsEp);
        var returnVal = clientEp->pushText(text);
        if (returnVal is http:WebSocketError) {
            panic <error> returnVal;
        }
    }

    resource function onBinary(http:WebSocketCaller wsEp, byte[] data) {
        http:WebSocketClient clientEp = getAssociatedClientEndpoint(wsEp);
        var returnVal = clientEp->pushBinary(data);
        if (returnVal is http:WebSocketError) {
            panic <error> returnVal;
        }
    }

    resource function onClose(http:WebSocketCaller wsEp, int statusCode, string reason) {
        http:WebSocketClient clientEp = getAssociatedClientEndpoint(wsEp);
        var returnVal = clientEp->close(statusCode, reason);
        if (returnVal is http:WebSocketError) {
            panic <error> returnVal;
        }
    }
}

service retryClientCallbackService = @http:WebSocketServiceConfig {} service {
    resource function onText(http:WebSocketClient wsEp, string text) {
        http:WebSocketCaller serviceEp = getAssociatedListener(wsEp);
        var returnVal = serviceEp->pushText(text);
        if (returnVal is http:WebSocketError) {
            panic <error> returnVal;
        }
    }

    resource function onBinary(http:WebSocketClient wsEp, byte[] data) {
        http:WebSocketCaller serviceEp = getAssociatedListener(wsEp);
        var returnVal = serviceEp->pushBinary(data);
        if (returnVal is http:WebSocketError) {
            panic <error> returnVal;
        }
    }

    resource function onClose(http:WebSocketClient wsEp, int statusCode, string reason) {
        http:WebSocketCaller serviceEp = getAssociatedListener(wsEp);
        var returnVal = serviceEp->close(statusCode = statusCode, reason = reason);
        if (returnVal is http:WebSocketError) {
            panic <error> returnVal;
        }
    }
};
