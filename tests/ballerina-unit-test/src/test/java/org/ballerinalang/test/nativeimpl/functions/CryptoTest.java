/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.ballerinalang.test.nativeimpl.functions;

import org.ballerinalang.launcher.util.BCompileUtil;
import org.ballerinalang.launcher.util.BRunUtil;
import org.ballerinalang.launcher.util.CompileResult;
import org.ballerinalang.model.util.JsonParser;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.model.values.BValueArray;
import org.ballerinalang.model.values.BXMLItem;
import org.ballerinalang.stdlib.crypto.util.HashUtils;
import org.ballerinalang.util.exceptions.BLangRuntimeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

/**
 * Test cases for ballerina.crypto native functions.
 */
public class CryptoTest {

    private CompileResult compileResult;

    @BeforeClass
    public void setup() {
        compileResult = BCompileUtil.compile("test-src/nativeimpl/functions/crypto-test.bal");
    }

    @Test
    public void testHmac() {
        String messageString = "Ballerina HMAC test";
        BString message = new BString(messageString);
        String keyString = "abcdefghijk";
        BString key = new BString(keyString);
        BString hexKey = new BString(HashUtils.toHexString(keyString.getBytes(StandardCharsets.UTF_8)));
        BString base64Key = new BString(
                new String(java.util.Base64.getEncoder().encode(keyString.getBytes(StandardCharsets.UTF_8)),
                        StandardCharsets.UTF_8));

        String expectedMD5Hash = "3D5AC29160F2905A5C8153597798A4C1";
        String expectedSHA1Hash = "13DD8D54D0EB702EDC6E8EDCAF616837D3A51499";
        String expectedSHA256Hash = "2651203E18BF0088D3EF1215022D147E2534FD4BAD5689C9E5F12436E9758B15";

        BValue[] args = {message, key};
        BValue[] returnValues = BRunUtil.invoke(compileResult, "testHmacWithMD5", args);
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedMD5Hash);

        args = new BValue[]{message, key};
        returnValues = BRunUtil.invoke(compileResult, "testHmacWithSHA1", args);
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedSHA1Hash);

        args = new BValue[]{message, key};
        returnValues = BRunUtil.invoke(compileResult, "testHmacWithSHA256", args);
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedSHA256Hash);

        args = new BValue[]{message, hexKey};
        returnValues = BRunUtil.invoke(compileResult, "testHmacHexKeyMD5", args);
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedMD5Hash);

        args = new BValue[]{message, base64Key};
        returnValues = BRunUtil.invoke(compileResult, "testHmacBase64KeyMD5", args);
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedMD5Hash);

        args = new BValue[]{message, hexKey};
        returnValues = BRunUtil.invoke(compileResult, "testHmacHexKeySHA1", args);
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedSHA1Hash);

        args = new BValue[]{message, base64Key};
        returnValues = BRunUtil.invoke(compileResult, "testHmacBase64KeySHA1", args);
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedSHA1Hash);

        args = new BValue[]{message, hexKey};
        returnValues = BRunUtil.invoke(compileResult, "testHmacHexKeySHA256", args);
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedSHA256Hash);

        args = new BValue[]{message, base64Key};
        returnValues = BRunUtil.invoke(compileResult, "testHmacBase64KeySHA256", args);
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedSHA256Hash);
    }

    @Test(expectedExceptions = BLangRuntimeException.class)
    public void testHmacNegativeInvalidKey() {
        BValue[] args = {new BString("Ballerina HMAC test"), new BString("")};
        BValue[] returnValues = BRunUtil.invoke(compileResult, "testHmacWithSHA1", args);
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null,
                           "Invalid return value");
    }

    @Test
    public void testHashing() {
        BValue[] args = {new BString("Ballerina Hash test")};

        String expectedMD5Hash = "A65F8F7B0A397271B93BEE395E72BA14";
        String expectedSHA1Hash = "189AF447E0D338DDD5EDB01E8CC0C899402DA4AF";
        String expectedSHA256Hash = "5AF499F2E770D43DB7F769F8742BA77DA713949EDCD233477DEF4509AEBBB44C";

        BValue[] returnValues = BRunUtil.invoke(compileResult, "testHashWithMD5", args);
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedMD5Hash);

        returnValues = BRunUtil.invoke(compileResult, "testHashWithSHA1", args);
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedSHA1Hash);

        returnValues = BRunUtil.invoke(compileResult, "testHashWithSHA256", args);
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedSHA256Hash);
    }

    @Test(description = "Testing CRC32 generation for strings")
    public void testCRC32ForText() {
        String payload = "Ballerina CRC32 Hash Test";
        String expectedCRC32Hash = "e1ad4853";

        BValue[] returnValues = BRunUtil.invoke(compileResult, "testHashWithCRC32ForText",
                                                new BValue[]{new BString(payload)});
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedCRC32Hash);
    }

    @Test(description = "Testing CRC32 generation for blobs")
    public void testCRC32ForBinary() {
        String payload = "Ballerina CRC32 Hash Test for Blob";
        String expectedCRC32Hash = "f3638b7f";

        BValue[] returnValues = BRunUtil.invoke(compileResult, "testHashWithCRC32ForBinary",
                                                new BValue[]{new BValueArray(payload.
                                                        getBytes(StandardCharsets.UTF_8))});
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedCRC32Hash);
    }

    @Test(description = "Testing CRC32 generation for JSON")
    public void testCRC32ForJSON() {
        String payload = "{\"name\":{\"fname\":\"Jack\", \"lname\":\"Taylor\"}, \"state\":\"CA\", \"age\":20}";

        CRC32 crc = new CRC32();
        crc.update(payload.getBytes());
        String expectedCRC32Hash = Long.toHexString(crc.getValue());

        BValue[] returnValues = BRunUtil.invoke(compileResult, "testHashWithCRC32ForJSON",
                                                new BValue[]{ JsonParser.parse(payload) });
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedCRC32Hash);
    }

    @Test(description = "Testing CRC32 generation for XML")
    public void testCRC32ForXML() {
        String payload = "<foo>hello</foo>";
        String expectedCRC32Hash = "748efc2";

        BValue[] returnValues = BRunUtil.invoke(compileResult, "testHashWithCRC32ForXML",
                                                new BValue[]{new BXMLItem(payload)});
        Assert.assertFalse(returnValues == null || returnValues.length == 0 || returnValues[0] == null);
        Assert.assertEquals(returnValues[0].stringValue(), expectedCRC32Hash);
    }
}
