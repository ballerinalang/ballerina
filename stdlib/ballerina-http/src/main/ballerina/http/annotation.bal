// Copyright (c) 2017 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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


///////////////////////////
/// Service Annotations ///
///////////////////////////

documentation {
    Contains the configurations for an HTTP service.

    F{{endpoints}} An array of endpoints the service would be attached to
    F{{basePath}} Service base path
    F{{compression}} The status of compression
    F{{chunking}} Configures the chunking behaviour for the service
    F{{cors}} The cross origin resource sharing configurations for the service
    F{{versioning}} The version of the service to be used
    F{{authConfig}} Authentication configurations for securing the service
    F{{overflowConfig}} Payload overflow configuration to control payload location(memory/temp file) based on a given
                        threshold
}
public type HttpServiceConfig {
    Listener[] endpoints,
    string basePath,
    Compression compression = "AUTO",
    Chunking chunking = CHUNKING_AUTO,
    CorsConfig cors,
    Versioning versioning,
    ListenerAuthConfig? authConfig,
    PayloadOverflowConfig overflowConfig,
};

documentation {
    Configuration to control payload location(memory/temp file) based on a given threshold value.

    F{{memoryThresholdInMB}} Maximum size of the payload to be kept in memory in MB
    F{{tempLocation}} Temporary directory to save large payloads
}
public type PayloadOverflowConfig {
    float memoryThresholdInMB = 2,
    string tempLocation,
};

documentation {
    Configurations for CORS support.

    F{{allowHeaders}} The array of allowed headers by the service
    F{{allowMethods}} The array of allowed methods by the service
    F{{allowOrigins}} The array of origins with which the response is shared by the service
    F{{exposeHeaders}} The whitelisted headers which clients are allowed to access
    F{{allowCredentials}} Specifies whether credentials are required to access the service
    F{{maxAge}} The maximum duration to cache the preflight from client side
}
public type CorsConfig {
    string[] allowHeaders,
    string[] allowMethods,
    string[] allowOrigins,
    string[] exposeHeaders,
    boolean allowCredentials,
    int maxAge= -1,
};


documentation {
    Configurations for service versioning.

    F{{pattern}} Expected version pattern in the request URL
    F{{allowNoVersion}} Allow requests with missing version path segment in the URL to be dispatched
    F{{matchMajorVersion}} Allow requests with only the major version specified in the URL to be dispatched
}
public type Versioning {
    string pattern = "v{major}.{minor}",
    boolean allowNoVersion = false,
    boolean matchMajorVersion = false,
};

documentation {
    Configurations for a WebSocket service.

    F{{endpoints}} An array of endpoints the service would be attached to
    F{{webSocketEndpoints}} An array of endpoints the service would be attached to
    F{{path}} Path of the WebSocket service
    F{{subProtocols}} Negotiable sub protocol by the service
    F{{idleTimeoutInSeconds}} Idle timeout for the client connection. This can be triggered by putting
                              an `onIdleTimeout` resource in the WebSocket service.
    F{{maxFrameSize}} The maximum payload size of a WebSocket frame in bytes
}
public type WSServiceConfig {
    Listener[] endpoints,
    WebSocketListener[] webSocketEndpoints,
    string path,
    string[] subProtocols,
    int idleTimeoutInSeconds,
    int maxFrameSize,
};

// TODO: Enable this when Ballerina supports service life time
//public type HttpServiceLifeTime "REQUEST"|"CONNECTION"|"SESSION"|"SINGLETON";

documentation {
    The annotation which is used to configure an HTTP service.
}
public annotation <service> ServiceConfig HttpServiceConfig;

documentation {
    The annotation which is used to configure a WebSocket service.
}
public annotation <service> WebSocketServiceConfig WSServiceConfig;

////////////////////////////
/// Resource Annotations ///
////////////////////////////
documentation {
    Configuration for an HTTP resource.

    F{{methods}} The array of allowed HTTP methods
    F{{path}} The path of resource
    F{{body}} Inbound request entity body name which declared in signature
    F{{consumes}} The media types which are accepted by resource
    F{{produces}} The media types which are produced by resource
    F{{cors}} The cross origin resource sharing configurations for the resource. If not set, the resource will inherit the CORS behaviour of the enclosing service.
    F{{transactionInfectable}} Allow to participate in the distributed transactions if value is true
    F{{webSocketUpgrade}} Annotation to define HTTP to WebSocket upgrade
    F{{authConfig}} Authentication Configs to secure the resource
}
public type HttpResourceConfig {
    string[] methods,
    string path,
    string body,
    string[] consumes,
    string[] produces,
    CorsConfig cors,
    boolean transactionInfectable = true,
    WebSocketUpgradeConfig? webSocketUpgrade,
    ListenerAuthConfig? authConfig,
};

documentation {
    Configures the HTTP to WebSocket upgrade.

    F{{upgradePath}} Path which is used to upgrade from HTTP to WebSocket
    F{{upgradeService}} WebSocket service which should be used after a successful upgrade
}
public type WebSocketUpgradeConfig {
    string upgradePath,
    typedesc upgradeService,
};

documentation {
    Configures the authentication scheme for a service or a resource.

    F{{authentication}} Enables/disables authentication
    F{{authProviders}} Array of authentication provider IDs
    F{{scopes}} Array of scopes
}
public type ListenerAuthConfig {
    Authentication? authentication,
    string[]? authProviders,
    string[]? scopes,
};

documentation {
    Can be used for enabling/disabling authentication in an HTTP service.

    F{{enabled}} Specifies whether authentication is enabled
}
public type Authentication {
    boolean enabled,
};

documentation {
    The annotation which is used to configure an HTTP resource.
}
public annotation <resource> ResourceConfig HttpResourceConfig;
