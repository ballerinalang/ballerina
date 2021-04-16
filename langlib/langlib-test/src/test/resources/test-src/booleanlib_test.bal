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

import ballerina/lang.'boolean;

function testFromString(string s, boolean|error expected) {
    assert(expected, 'boolean:fromString(s));
}

// Util functions

function assert(boolean|error expected, boolean|error actual) {
    if (isEqual(actual, expected)) {
        return;
    }

    typedesc<anydata|error> expT = typeof expected;
    typedesc<anydata|error> actT = typeof actual;

    string expectedValAsString = expected is error ? expected.toString() : expected.toString();
    string actualValAsString = actual is error ? actual.toString() : actual.toString();
    string reason = "expected [" + expectedValAsString + "] of type [" + expT.toString()
                        + "], but found [" + actualValAsString + "] of type [" + actT.toString() + "]";
    error e = error(reason);
    panic e;
}

isolated function isEqual(anydata|error actual, anydata|error expected) returns boolean {
    if (actual is anydata && expected is anydata) {
        return (actual == expected);
    } if (actual is error && expected is error) {
        var actualMessage = actual.detail()["message"];
        var expectedMessage = expected.detail()["message"];
        if (actualMessage is anydata && expectedMessage is anydata) {
            return actual.message() == expected.message() && isEqual(actualMessage, expectedMessage);
        }
    }
    return (actual === expected);
}
