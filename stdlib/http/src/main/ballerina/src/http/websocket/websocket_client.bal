// Copyright (c) 2018 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

# Represents a WebSocket client endpoint.
public type WebSocketClient client object {

    private string id = "";
    private string? negotiatedSubProtocol = ();
    private boolean secure = false;
    private boolean open = false;
    private Response? response = ();
    private map<any> attributes = {};

    private WebSocketConnector conn = new;
    private string url = "";
    private WebSocketClientConfiguration config = {};

    # Initializes the client when called.
    #
    # + c - The `WebSocketClientConfiguration` of the endpoint
    public function __init(string url, public WebSocketClientConfiguration? config = ()) {
        self.url = url;
        self.config = config ?: {};
        self.initEndpoint();
    }

    # Initializes the endpoint.
    function initEndpoint() = external;

    # Pushes text to the connection.
    #
    # + data - Data to be sent. If it is a byte[], it is converted to a UTF-8 string for sending.
    # + finalFrame - Set to `true` if this is a final frame of a (long) message
    # + return  - Returns a`WebSocketError` if an error occurs while sending the text message to the server.
    #   The message will be lost if an error occurs.
    public remote function pushText(string|json|xml|boolean|int|float|byte|byte[] data,
    public boolean finalFrame = true) returns WebSocketError? {
        return self.conn.pushText(data, finalFrame);
    }

    # Push binary data to the connection.
    #
    # + data - Binary data to be sent
    # + finalFrame - Set to `true` if this is a final frame of a (long) message
    # + return - Returns a `WebSocketError` if an error occurs while sending the binary message to the server.
    #   The message will be lost if an error occurs.
    public remote function pushBinary(byte[] data, public boolean finalFrame = true) returns error? {
        return self.conn.pushBinary(data, finalFrame);
    }

    # Pings the connection.
    #
    # + data - Binary data to be sent.
    # + return - Returns a`WebSocketError` if an error occurs while sending the ping frame to the server.
    #   The frame will be lost if an error occurs.
    public remote function ping(byte[] data) returns WebSocketError? {
        return self.conn.ping(data);
    }

    # Sends a pong message to the connection.
    #
    # + data - Binary data to be sent
    # + return - Returns a `WebSocketError` if an error occurs while sending the pong frame to the server.
    #   The frame will be lost if an error occurs.
    public remote function pong(byte[] data) returns WebSocketError? {
        return self.conn.pong(data);
    }

    # Closes the connection.
    #
    # + statusCode - Status code for closing the connection.
    # + reason - Reason for closing the connection
    # + timeoutInSeconds - Time to wait for the close frame to be received from the remote endpoint before closing the
    #                   connection. If the timeout exceeds, then the connection is terminated even though a close frame
    #                   is not received from the remote endpoint. If the value is < 0 (e.g., -1), then the connection
    #                   waits until a close frame is received. If the WebSocket frame is received from the remote endpoint
    #                   within the waiting period, the connection is terminated immediately.
    # + return - Returns a `WebSocketError` if an error occurs while closing the webSocket connection.
    public remote function close(public int? statusCode = 1000, public string? reason = (),
        public int timeoutInSeconds = 60) returns WebSocketError? {
        return self.conn.close(statusCode, reason, timeoutInSeconds);
    }

    # Called when the endpoint is ready to receive messages. Can be called only once per endpoint. For the
    # WebSocketListener, it can be called only in the `upgrade` or `onOpen` resources.
    #
    # + return - Returns a`WebSocketError` if an error occurs while checking the connection state.
    public remote function ready() returns WebSocketError? {
        return self.conn.ready();
    }

    # Sets a connection related attribute.
    #
    # + key - key that identifies the attribute
    # + value - value of the attribute
    public function setAttribute(string key, any value) {
        self.attributes[key] = value;
    }

    # Gets connection related attribute if any.
    #
    # + key - the key to identify the attribute.
    # + return - the attribute related to the given key or `nil`
    public function getAttribute(string key) returns any {
        return self.attributes[key];
    }

    # Removes connection related attribute if any.
    #
    # + key - the key to identify the attribute.
    # + return - the attribute related to the given key or `nil`
    public function removeAttribute(string key) returns any {
        return self.attributes.remove(key);
    }

    # Gives the connection id associated with this connection.
    #
    # + return - the unique id associated with the connection
    public function getConnectionId() returns string {
        return self.id;
    }

    # Gives the subprotocol if any that is negotiated with the client.
    #
    # + return - The subprotocol if any negotiated with the client or `nil`
    public function getNegotiatedSubProtocol() returns string? {
        return self.negotiatedSubProtocol;
    }

    # Gives the secured status of the connection.
    #
    # + return - `true` if the connection is secure.
    public function isSecure() returns boolean {
        return self.secure;
    }

    # Gives the open or closed status of the connection.
    #
    # + return - `true` if the connection is open
    public function isOpen() returns boolean {
        return self.open;
    }

    # Gives the HTTP response if any received for the client handshake request.
    #
    # + return - the HTTP response received for the client handshake request
    public function getHttpResponse() returns Response? {
        return self.response;
    }
    
};

# Configurations for the WebSocket client endpoint.
#
# + callbackService - The callback service for the client. Resources in this service gets called on the
#                     receipt of messages from the server.
# + subProtocols - Negotiable sub protocols for the client.
# + customHeaders - Custom headers, which should be sent to the server.
# + idleTimeoutInSeconds - Idle timeout of the client. Upon timeout, the `onIdleTimeout` resource (if defined)
#                          of the client service will be triggered.
# + readyOnConnect - Set to `true` if the client is ready to receive messages as soon as the connection is established.
#                    This is set to `true` by default. If changed to `false`, the `ready()` function of the
#                    `WebSocketClient` needs to be called once to start receiving messages.
# + secureSocket - SSL/TLS-related options.
# + maxFrameSize - The maximum payload size of a WebSocket frame in bytes.
#                  If this is not set, is negative, or is zero, the default frame size of 65536 will be used.
# + webSocketCompressionEnabled - Enable support for compression in WebSocket.
# + retryConfig - Configurations related to retrying.
public type WebSocketClientConfiguration record {|
    service? callbackService = ();
    string[] subProtocols = [];
    map<string> customHeaders = {};
    int idleTimeoutInSeconds = -1;
    boolean readyOnConnect = true;
    ClientSecureSocket? secureSocket = ();
    int maxFrameSize = 0;
    boolean webSocketCompressionEnabled = true;
    WebSocketRetryConfig retryConfig?;
|};

# Configurations for reconnecting to the WebSocket.
#
# + maxCount - The maximum number of retry attempts. If the count is zero, the client will retry indefinitely.
# + intervalInMillis - The number of milliseconds to delay before attempting to reconnect.
# + backOffFactor - The rate of increase of the reconnect delay. Allows reconnect attempts to back off when problems
#                persist.
# + maxWaitIntervalInMillis - Maximum time of the retry interval in milliseconds.
public type WebSocketRetryConfig record {|
    int maxCount = 0;
    int intervalInMillis = 1000;
    float backOffFactor = 1.0;
    int maxWaitIntervalInMillis = 30000;
|};
