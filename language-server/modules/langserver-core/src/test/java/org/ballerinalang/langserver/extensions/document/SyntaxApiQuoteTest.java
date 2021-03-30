/*
 * Copyright (c) 2021, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ballerinalang.langserver.extensions.document;

import org.ballerinalang.langserver.extensions.LSExtensionTestUtil;
import org.ballerinalang.langserver.extensions.ballerina.document.BallerinaSyntaxApiQuoteResponse;
import org.ballerinalang.langserver.util.FileUtils;
import org.ballerinalang.langserver.util.TestUtil;
import org.eclipse.lsp4j.jsonrpc.Endpoint;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Test SyntaxApiQuote methods.
 *
 * @since 2.0.0
 */
public class SyntaxApiQuoteTest {
    private final Path emptyFile = FileUtils.RES_DIR.resolve("extensions")
            .resolve("document")
            .resolve("syntaxApiQuote")
            .resolve("empty.bal");
    private final Path mainFile = FileUtils.RES_DIR.resolve("extensions")
            .resolve("document")
            .resolve("syntaxApiQuote")
            .resolve("main.bal");
    private final Path mainFileWoMinutiaeResult = FileUtils.RES_DIR.resolve("extensions")
            .resolve("document")
            .resolve("syntaxApiQuote")
            .resolve("main.no.minutiae.txt");
    private final Path mainFileResult = FileUtils.RES_DIR.resolve("extensions")
            .resolve("document")
            .resolve("syntaxApiQuote")
            .resolve("main.result.txt");
    private Endpoint serviceEndpoint;

    @BeforeClass
    public void startLanguageServer() throws Exception {
        this.serviceEndpoint = TestUtil.initializeLanguageSever();
    }

    @Test(description = "Request for an empty Ballerina file")
    public void testEmptySourceFile() throws Exception {
        TestUtil.openDocument(serviceEndpoint, emptyFile);
        BallerinaSyntaxApiQuoteResponse response = LSExtensionTestUtil
                .getBallerinaSyntaxApiQuote(emptyFile.toString(), false, this.serviceEndpoint);
        Assert.assertTrue(response.isParseSuccess());
        Assert.assertEquals(response.getSource(), "");
        Assert.assertEquals(response.getCode(), "" +
                "NodeFactory.createModulePartNode(\n" +
                "    NodeFactory.createNodeList(),\n" +
                "    NodeFactory.createNodeList(),\n" +
                "    NodeFactory.createToken(SyntaxKind.EOF_TOKEN)\n" +
                ")");
        TestUtil.closeDocument(this.serviceEndpoint, emptyFile);
    }

    @Test(description = "Request for a sample Ballerina file")
    public void testSampleBallerinaFile() throws Exception {
        TestUtil.openDocument(serviceEndpoint, mainFile);
        BallerinaSyntaxApiQuoteResponse response = LSExtensionTestUtil
                .getBallerinaSyntaxApiQuote(mainFile.toString(), false, this.serviceEndpoint);
        String mainFileContent = new String(Files.readAllBytes(mainFile));
        String mainFileResultContent = new String(Files.readAllBytes(mainFileResult));
        Assert.assertTrue(response.isParseSuccess());
        Assert.assertEquals(response.getSource(), mainFileContent);
        Assert.assertEquals(response.getCode().trim(), mainFileResultContent.trim());
        TestUtil.closeDocument(this.serviceEndpoint, mainFile);
    }

    @Test(description = "Request for a sample Ballerina file without minutiae")
    public void testSampleBallerinaFileWithoutMinutiae() throws Exception {
        TestUtil.openDocument(serviceEndpoint, mainFile);
        BallerinaSyntaxApiQuoteResponse response = LSExtensionTestUtil
                .getBallerinaSyntaxApiQuote(mainFile.toString(), true, this.serviceEndpoint);
        String mainFileContent = new String(Files.readAllBytes(mainFile));
        String mainFileResultContent = new String(Files.readAllBytes(mainFileWoMinutiaeResult));
        Assert.assertTrue(response.isParseSuccess());
        Assert.assertEquals(response.getSource(), mainFileContent);
        Assert.assertEquals(response.getCode().trim(), mainFileResultContent.trim());
        TestUtil.closeDocument(this.serviceEndpoint, mainFile);
    }
}