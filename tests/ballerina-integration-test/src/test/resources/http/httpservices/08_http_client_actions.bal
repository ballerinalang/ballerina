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

import ballerina/io;
import ballerina/http;
import ballerina/mime;

http:Client clientEP2 = new ("http://localhost:9097", config = { cache: { enabled: false }});

@http:ServiceConfig {
    basePath: "/test1"
}
service backEndService on new http:Listener(9097) {

    @http:ResourceConfig {
        methods: ["GET"],
        path: "/greeting"
    }
    resource function replyText(http:Caller caller, http:Request req) {
        _ = caller->respond("Hello");
    }

    @http:ResourceConfig {
        methods: ["POST"],
        path: "/byteChannel"
    }
    resource function sendByteChannel(http:Caller caller, http:Request req) {
        var byteChannel = req.getByteChannel();
        if (byteChannel is io:ReadableByteChannel) {
            _ = caller->respond(crypto:unsafeMarkUntainted(byteChannel))));
        } else if (byteChannel is error) {
            _ = caller->respond(crypto:unsafeMarkUntainted(byteChannel.reason()))));
        }
    }

    @http:ResourceConfig {
        methods: ["POST"],
        path: "/directPayload"
    }
    resource function postReply(http:Caller caller, http:Request req) {
        if (req.hasHeader("content-type")) {
            var mediaType = mime:getMediaType(req.getContentType());
            if (mediaType is mime:MediaType) {
                string baseType = mediaType.getBaseType();
                if (mime:TEXT_PLAIN == baseType) {
                    var textValue = req.getTextPayload();
                    if (textValue is string) {
                        _ = caller->respond(crypto:unsafeMarkUntainted(textValue))));
                    } else if (textValue is error) {
                        _ = caller->respond(crypto:unsafeMarkUntainted(textValue.reason()))));
                    }
                } else if (mime:APPLICATION_XML == baseType) {
                    var xmlValue = req.getXmlPayload();
                    if (xmlValue is xml) {
                        _ = caller->respond(crypto:unsafeMarkUntainted(xmlValue))));
                    } else if (xmlValue is error) {
                        _ = caller->respond(crypto:unsafeMarkUntainted(xmlValue.reason()))));
                    }
                } else if (mime:APPLICATION_JSON == baseType) {
                    var jsonValue = req.getJsonPayload();
                    if (jsonValue is json) {
                        _ = caller->respond(crypto:unsafeMarkUntainted(jsonValue))));
                    } else if (jsonValue is error) {
                        _ = caller->respond(crypto:unsafeMarkUntainted(jsonValue.reason()))));
                    }
                } else if (mime:APPLICATION_OCTET_STREAM == baseType) {
                    var blobValue = req.getBinaryPayload();
                    if (blobValue is byte[]) {
                        _ = caller->respond(crypto:unsafeMarkUntainted(blobValue))));
                    } else if (blobValue is error) {
                        _ = caller->respond(crypto:unsafeMarkUntainted(blobValue.reason()))));
                    }
                } else if (mime:MULTIPART_FORM_DATA == baseType) {
                    var bodyParts = req.getBodyParts();
                    if (bodyParts is mime:Entity[]) {
                    _ = caller->respond(crypto:unsafeMarkUntainted(bodyParts));
                    } else if (bodyParts is error) {
                    _ = caller->respond(crypto:unsafeMarkUntainted(bodyParts.reason()));
                    }
                }
            } else if (mediaType is error) {
                _ = caller->respond("Error in parsing media type");
            }
        } else {
            _ = caller->respond(());
        }
    }
}

@http:ServiceConfig {
    basePath: "/test2"
}
service testService on new http:Listener(9098) {

    @http:ResourceConfig {
        methods: ["GET"],
        path: "/clientGet"
    }
    resource function testGet(http:Caller caller, http:Request req) {
        string value = "";
        //No Payload
        var response1 = clientEP2->get("/test1/greeting");
        if (response1 is http:Response) {
            var result = response1.getTextPayload();
            if (result is string) {
                value = result;
            } else if (result is error) {
                value = result.reason();
            }
        }

        //No Payload
        var response2 = clientEP2->get("/test1/greeting", message = ());
        if (response2 is http:Response) {
            var result = response2.getTextPayload();
            if (result is string) {
                value = value + result;
            } else if (result is error) {
                value = value + result.reason();
            }
        }

        http:Request httpReq = new;
        //Request as message
        var response3 = clientEP2->get("/test1/greeting", message = httpReq);
        if (response3 is http:Response) {
            var result = response3.getTextPayload();
            if (result is string) {
                value = value + result;
            } else if (result is error) {
                value = value + result.reason();
            }
        }
        _ = caller->respond(crypto:unsafeMarkUntainted(value));
    }

    @http:ResourceConfig {
        methods: ["GET"],
        path: "/clientPostWithoutBody"
    }
    resource function testPost(http:Caller caller, http:Request req) {
        string value = "";
        //No Payload
        var clientResponse = clientEP2->post("/test1/directPayload", ());
        if (clientResponse is http:Response) {
            var returnValue = clientResponse.getTextPayload();
            if (returnValue is string) {
                value = returnValue;
            } else if (returnValue is error) {
                value = <string> returnValue.detail().message;
            }
        } else if (clientResponse is error) {
            value = clientResponse.reason();
        }

        _ = caller->respond(crypto:unsafeMarkUntainted(value));
    }

    @http:ResourceConfig {
        methods: ["GET"],
        path: "/clientPostWithBody"
    }
    resource function testPostWithBody(http:Caller caller, http:Request req) {
        string value = "";
        var textResponse = clientEP2->post("/test1/directPayload", "Sample Text");
        if (textResponse is http:Response) {
            var result = textResponse.getTextPayload();
            if (result is string) {
                value = result;
            } else if (result is error) {
                value = result.reason();
            }
        }

        var xmlResponse = clientEP2->post("/test1/directPayload", xml `<yy>Sample Xml</yy>`);
        if (xmlResponse is http:Response) {
            var result = xmlResponse.getXmlPayload();
            if (result is xml) {
                value = value + result.getTextValue();
            } else if (result is error) {
                value = value + result.reason();
            }
        }

        var jsonResponse = clientEP2->post("/test1/directPayload", { name: "apple", color: "red" });
        if (jsonResponse is http:Response) {
            var result = jsonResponse.getJsonPayload();
            if (result is json) {
                value = value + result.toString();
            } else if (result is error) {
                value = value + result.reason();
            }
        }
        _ = caller->respond(crypto:unsafeMarkUntainted(value));
    }

    @http:ResourceConfig {
        methods: ["GET"],
        path: "/handleBinary"
    }
    resource function testPostWithBinaryData(http:Caller caller, http:Request req) {
        string value = "";
        string textVal = "Sample Text";
        byte[] binaryValue = textVal.toByteArray("UTF-8");
        var textResponse = clientEP2->post("/test1/directPayload", binaryValue);
        if (textResponse is http:Response) {
            var result = textResponse.getPayloadAsString();
            if (result is string) {
                value = result;
            } else if (result is error) {
                value = result.reason();
            }
        }
        _ = caller->respond(crypto:unsafeMarkUntainted(value));
    }

    @http:ResourceConfig {
        methods: ["POST"],
        path: "/handleByteChannel"
    }
    resource function testPostWithByteChannel(http:Caller caller, http:Request req) {
        string value = "";
        var byteChannel = req.getByteChannel();
        if (byteChannel is io:ReadableByteChannel) {
            var res = clientEP2->post("/test1/byteChannel", crypto:unsafeMarkUntainted(byteChannel));
            if (res is http:Response) {
                var result = res.getPayloadAsString();
                if (result is string) {
                    value = result;
                } else if (result is error) {
                    value = result.reason();
                }
            } else if (res is error) {
                value = res.reason();
            }
        } else if (byteChannel is error) {
            value = byteChannel.reason();
        }
        _ = caller->respond(crypto:unsafeMarkUntainted(value));
    }

    @http:ResourceConfig {
        methods: ["GET"],
        path: "/handleMultiparts"
    }
    resource function testPostWithBodyParts(http:Caller caller, http:Request req) {
        string value = "";
        mime:Entity part1 = new;
        part1.setJson({ "name": "wso2" });
        mime:Entity part2 = new;
        part2.setText("Hello");
        mime:Entity[] bodyParts = [part1, part2];

        var res = clientEP2->post("/test1/directPayload", bodyParts);
        if (res is http:Response) {
            var returnParts = res.getBodyParts();
            if (returnParts is mime:Entity[]) {
                foreach var bodyPart in returnParts {
                    var mediaType = mime:getMediaType(bodyPart.getContentType());
                    if (mediaType is mime:MediaType) {
                        string baseType = mediaType.getBaseType();
                        if (mime:APPLICATION_JSON == baseType) {
                            var payload = bodyPart.getJson();
                            if (payload is json) {
                                value = payload.toString();
                            } else if (payload is error) {
                                value = payload.reason();
                            }
                        }
                        if (mime:TEXT_PLAIN == baseType) {
                            var textVal = bodyPart.getText();
                            if (textVal is string) {
                                value = value + textVal;
                            } else if (textVal is error) {
                                value = value + textVal.reason();
                            }
                        }
                    } else if (mediaType is error) {
                        value = value + mediaType.reason();
                    }
                }
            } else if (returnParts is error) {
                value = returnParts.reason();
            }
        } else if (res is error) {
            value = res.reason();
        }
        _ = caller->respond(crypto:unsafeMarkUntainted(value));
    }
}
