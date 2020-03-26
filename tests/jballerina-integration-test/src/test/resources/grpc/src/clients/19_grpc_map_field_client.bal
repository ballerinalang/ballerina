// Copyright (c) 2020 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import ballerina/grpc;
import ballerina/io;

NegotiatorBlockingClient negotiatorEp = new ("http://localhost:9109");

public function main(string... args) {
    io:println("starting negotiator client");
    io:println(testMapFields());
    io:println(testOptionalFields());
}

public function testMapFields() returns string {
    MetricsPublishRequest request = {
        id: "xxxxx",
        metrics: [{
            timestamp: 1580966325916,
            name: "ballerina/http/Caller_3XX_requests_total_count",
            value: 0.0,
            tags: [{key: "action", value: "respond"}]
        }]
    };
    grpc:Headers | error publishMetrics = negotiatorEp->publishMetrics(request);
    if (publishMetrics is error) {
        return io:sprintf("Metrics publish failed: %s - %s",
                            publishMetrics.reason(), <string>publishMetrics.detail()["message"]);
    } else {
        return "Metrics published successfully";
    }
}

public function testOptionalFields() returns string {
    HandshakeRequest request = {};
    [HandshakeResponse, grpc:Headers] | grpc:Error result = negotiatorEp->handshake(request);
    if (result is error) {
        return io:sprintf("Handshake failed: %s - %s", result.reason(), <string>result.detail()["message"]);
    } else {
        HandshakeResponse handshakeResponse;
        [handshakeResponse, _] = result;
        return io:sprintf("Handshake succeeded: %s", handshakeResponse.id);
    }
}

public type NegotiatorBlockingClient client object {

    *grpc:AbstractClientEndpoint;

    private grpc:Client grpcClient;

    public function __init(string url, grpc:ClientConfiguration? config = ()) {
        // initialize client endpoint.
        self.grpcClient = new (url, config);
        checkpanic self.grpcClient.initStub(self, "blocking", ROOT_DESCRIPTOR, getDescriptorMap());
    }

    public remote function handshake(HandshakeRequest req, grpc:ClientContext? context = ())
                                                returns ([HandshakeResponse, grpc:Headers] | grpc:Error) {
        grpc:Headers? headers = ();
        if context is grpc:ClientContext {
            headers = context.getContextHeaders();
        }
        var payload = check self.grpcClient->blockingExecute("Negotiator/handshake", req, headers);
        grpc:Headers resHeaders = new;
        anydata result = ();
        [result, resHeaders] = payload;
        return [<HandshakeResponse>result, resHeaders];
    }

    public remote function publishMetrics(MetricsPublishRequest req, grpc:ClientContext? context = ())
                                                returns (grpc:Headers | grpc:Error) {
        grpc:Headers? headers = ();
        if context is grpc:ClientContext {
            headers = context.getContextHeaders();
        }
        var payload = check self.grpcClient->blockingExecute("Negotiator/publishMetrics", req, headers);
        grpc:Headers resHeaders = new;
        [_, resHeaders] = payload;
        return resHeaders;
    }

    public remote function publishTraces(TracesPublishRequest req, grpc:ClientContext? context = ())
                                                returns (grpc:Headers | grpc:Error) {
        grpc:Headers? headers = ();
        if context is grpc:ClientContext {
            headers = context.getContextHeaders();
        }
        var payload = check self.grpcClient->blockingExecute("Negotiator/publishTraces", req, headers);
        grpc:Headers resHeaders = new;
        [_, resHeaders] = payload;
        return resHeaders;
    }
};

public type NegotiatorClient client object {

    *grpc:AbstractClientEndpoint;

    private grpc:Client grpcClient;

    public function __init(string url, grpc:ClientConfiguration? config = ()) {
        // initialize client endpoint.
        self.grpcClient = new (url, config);
        checkpanic self.grpcClient.initStub(self, "non-blocking", ROOT_DESCRIPTOR, getDescriptorMap());
    }

    public remote function handshake(HandshakeRequest req, service msgListener, grpc:ClientContext? context = ())
                                                                                        returns (grpc:Error?) {
        grpc:Headers? headers = ();
        if context is grpc:ClientContext {
            headers = context.getContextHeaders();
        }
        return self.grpcClient->nonBlockingExecute("Negotiator/handshake", req, msgListener, headers);
    }

    public remote function publishMetrics(MetricsPublishRequest req, service msgListener, grpc:ClientContext? context = ())
                                                                                        returns (grpc:Error?) {
        grpc:Headers? headers = ();
        if context is grpc:ClientContext {
            headers = context.getContextHeaders();
        }
        return self.grpcClient->nonBlockingExecute("Negotiator/publishMetrics", req, msgListener, headers);
    }

    public remote function publishTraces(TracesPublishRequest req, service msgListener, grpc:ClientContext? context = ())
                                                                                        returns (grpc:Error?) {
        grpc:Headers? headers = ();
        if context is grpc:ClientContext {
            headers = context.getContextHeaders();
        }
        return self.grpcClient->nonBlockingExecute("Negotiator/publishTraces", req, msgListener, headers);
    }
};

public type Empty record {|
|};

public type HandshakeResponse record {|
    string id = "";
    string[] protocols = [];
|};

public type MetricsPublishRequest record {|
    string id = "";
    Metric[] metrics = [];
|};

public type Metric record {|
    int timestamp = 0;
    string name = "";
    float value = 0.0;
    record {|string key; string value;|}[] tags = [];
|};

public type TagsEntry record {|
    string key = "";
    string value = "";
|};

public type TracesPublishRequest record {|
    string id = "";
    TraceSpan[] spans = [];
|};

public type TraceSpan record {|
    string traceId = "";
    string spanId = "";
    string parentSpanId = "";
    string serviceName = "";
    string operationName = "";
    int timestamp = 0;
    int duration = 0;
    record {|string key; string value;|}[] tags = [];
|};

public type HandshakeRequest record {|
    string jsonStr = "";
    string programHash = "";
    string userId = "";
    string instanceId = "";
    string applicationId = "";
|};

const string ROOT_DESCRIPTOR = "0A106E65676F746961746F722E70726F746F1A1B676F6F676C652F70726F746F6275662F656D7074792E70726F746F1A1E676F6F676C652F70726F746F6275662F77726170706572732E70726F746F22410A1148616E647368616B65526573706F6E7365120E0A02696418012001280952026964121C0A0970726F746F636F6C73180220032809520970726F746F636F6C73224A0A154D6574726963735075626C69736852657175657374120E0A0269641801200128095202696412210A076D65747269637318022003280B32072E4D657472696352076D65747269637322B0010A064D6574726963121C0A0974696D657374616D70180120012803520974696D657374616D7012120A046E616D6518022001280952046E616D6512140A0576616C7565180320012801520576616C756512250A047461677318042003280B32112E4D65747269632E54616773456E7472795204746167731A370A0954616773456E74727912100A036B657918012001280952036B657912140A0576616C7565180220012809520576616C75653A02380122480A145472616365735075626C69736852657175657374120E0A0269641801200128095202696412200A057370616E7318022003280B320A2E54726163655370616E52057370616E7322C6020A0954726163655370616E12180A077472616365496418012001280952077472616365496412160A067370616E496418022001280952067370616E496412220A0C706172656E745370616E4964180320012809520C706172656E745370616E496412200A0B736572766963654E616D65180420012809520B736572766963654E616D6512240A0D6F7065726174696F6E4E616D65180520012809520D6F7065726174696F6E4E616D65121C0A0974696D657374616D70180620012803520974696D657374616D70121A0A086475726174696F6E18072001280352086475726174696F6E12280A047461677318082003280B32142E54726163655370616E2E54616773456E7472795204746167731A370A0954616773456E74727912100A036B657918012001280952036B657912140A0576616C7565180220012809520576616C75653A02380122AC010A1048616E647368616B655265717565737412180A076A736F6E53747218012001280952076A736F6E53747212200A0B70726F6772616D48617368180220012809520B70726F6772616D4861736812160A067573657249641803200128095206757365724964121E0A0A696E7374616E63654964180420012809520A696E7374616E6365496412240A0D6170706C69636174696F6E4964180520012809520D6170706C69636174696F6E496432C2010A0A4E65676F746961746F7212320A0968616E647368616B6512112E48616E647368616B65526571756573741A122E48616E647368616B65526573706F6E736512400A0E7075626C6973684D65747269637312162E4D6574726963735075626C697368526571756573741A162E676F6F676C652E70726F746F6275662E456D707479123E0A0D7075626C69736854726163657312152E5472616365735075626C697368526571756573741A162E676F6F676C652E70726F746F6275662E456D70747942360A346F72672E62616C6C6572696E616C616E672E6F6273657276652E74726163652E657874656E73696F6E2E63686F72656F2E67656E620670726F746F33";
function getDescriptorMap() returns map<string> {
    return {
        "negotiator.proto": "0A106E65676F746961746F722E70726F746F1A1B676F6F676C652F70726F746F6275662F656D7074792E70726F746F1A1E676F6F676C652F70726F746F6275662F77726170706572732E70726F746F22410A1148616E647368616B65526573706F6E7365120E0A02696418012001280952026964121C0A0970726F746F636F6C73180220032809520970726F746F636F6C73224A0A154D6574726963735075626C69736852657175657374120E0A0269641801200128095202696412210A076D65747269637318022003280B32072E4D657472696352076D65747269637322B0010A064D6574726963121C0A0974696D657374616D70180120012803520974696D657374616D7012120A046E616D6518022001280952046E616D6512140A0576616C7565180320012801520576616C756512250A047461677318042003280B32112E4D65747269632E54616773456E7472795204746167731A370A0954616773456E74727912100A036B657918012001280952036B657912140A0576616C7565180220012809520576616C75653A02380122480A145472616365735075626C69736852657175657374120E0A0269641801200128095202696412200A057370616E7318022003280B320A2E54726163655370616E52057370616E7322C6020A0954726163655370616E12180A077472616365496418012001280952077472616365496412160A067370616E496418022001280952067370616E496412220A0C706172656E745370616E4964180320012809520C706172656E745370616E496412200A0B736572766963654E616D65180420012809520B736572766963654E616D6512240A0D6F7065726174696F6E4E616D65180520012809520D6F7065726174696F6E4E616D65121C0A0974696D657374616D70180620012803520974696D657374616D70121A0A086475726174696F6E18072001280352086475726174696F6E12280A047461677318082003280B32142E54726163655370616E2E54616773456E7472795204746167731A370A0954616773456E74727912100A036B657918012001280952036B657912140A0576616C7565180220012809520576616C75653A02380122AC010A1048616E647368616B655265717565737412180A076A736F6E53747218012001280952076A736F6E53747212200A0B70726F6772616D48617368180220012809520B70726F6772616D4861736812160A067573657249641803200128095206757365724964121E0A0A696E7374616E63654964180420012809520A696E7374616E6365496412240A0D6170706C69636174696F6E4964180520012809520D6170706C69636174696F6E496432C2010A0A4E65676F746961746F7212320A0968616E647368616B6512112E48616E647368616B65526571756573741A122E48616E647368616B65526573706F6E736512400A0E7075626C6973684D65747269637312162E4D6574726963735075626C697368526571756573741A162E676F6F676C652E70726F746F6275662E456D707479123E0A0D7075626C69736854726163657312152E5472616365735075626C697368526571756573741A162E676F6F676C652E70726F746F6275662E456D70747942360A346F72672E62616C6C6572696E616C616E672E6F6273657276652E74726163652E657874656E73696F6E2E63686F72656F2E67656E620670726F746F33",
        "google/protobuf/wrappers.proto": "0A1E676F6F676C652F70726F746F6275662F77726170706572732E70726F746F120F676F6F676C652E70726F746F62756622230A0B446F75626C6556616C756512140A0576616C7565180120012801520576616C756522220A0A466C6F617456616C756512140A0576616C7565180120012802520576616C756522220A0A496E74363456616C756512140A0576616C7565180120012803520576616C756522230A0B55496E74363456616C756512140A0576616C7565180120012804520576616C756522220A0A496E74333256616C756512140A0576616C7565180120012805520576616C756522230A0B55496E74333256616C756512140A0576616C756518012001280D520576616C756522210A09426F6F6C56616C756512140A0576616C7565180120012808520576616C756522230A0B537472696E6756616C756512140A0576616C7565180120012809520576616C756522220A0A427974657356616C756512140A0576616C756518012001280C520576616C756542570A13636F6D2E676F6F676C652E70726F746F627566420D577261707065727350726F746F50015A057479706573F80101A20203475042AA021E476F6F676C652E50726F746F6275662E57656C6C4B6E6F776E5479706573620670726F746F33",
        "google/protobuf/empty.proto": "0A1B676F6F676C652F70726F746F6275662F656D7074792E70726F746F120F676F6F676C652E70726F746F62756622070A05456D70747942540A13636F6D2E676F6F676C652E70726F746F627566420A456D70747950726F746F50015A057479706573F80101A20203475042AA021E476F6F676C652E50726F746F6275662E57656C6C4B6E6F776E5479706573620670726F746F33"

    };
}
