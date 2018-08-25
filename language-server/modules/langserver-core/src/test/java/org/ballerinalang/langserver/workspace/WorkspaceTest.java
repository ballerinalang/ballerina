/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.ballerinalang.langserver.workspace;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ballerinalang.langserver.completion.util.FileUtils;
import org.ballerinalang.langserver.util.TestUtil;
import org.eclipse.lsp4j.jsonrpc.Endpoint;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Ballerina Language Server Workspace Test.
 * 
 * @since 0.982.0
 */
public class WorkspaceTest {
    
    private Endpoint serviceEndpoint;

    private JsonParser parser = new JsonParser();

    @BeforeClass
    public void init() throws Exception {
        this.serviceEndpoint = TestUtil.initializeLanguageSever();
    }
    
    @Test(description = "Test the Workspace symbol", dataProvider = "workspace-data-provider")
    public void testWorkspaceSymbol(String config, String query) throws IOException {
        String configJsonPath = "workspace" + File.separator + config;
        JsonObject configJsonObject = FileUtils.fileContentAsObject(configJsonPath);
        JsonObject expected = configJsonObject.get("expected").getAsJsonObject();
        this.openDocuments();
        String response = TestUtil.getWorkspaceSymbolResponse(this.serviceEndpoint, query);
        this.closeDocuments();
        JsonObject jsonResponse = parser.parse(response).getAsJsonObject();
        JsonArray result = jsonResponse.getAsJsonArray("result");
        for (JsonElement element : result) {
            element.getAsJsonObject().get("location").getAsJsonObject().remove("uri");
        }

        Assert.assertTrue(this.isArgumentsSubArray(expected.getAsJsonArray("result"), result));
    }

    @DataProvider(name = "workspace-data-provider")
    public Object[][] workspaceSymbolDataProvider() {
        return new Object[][] {
                {"workspaceSymbol.json", ""},
                {"workspaceSymbolWithQuery.json", "test"},
        };
    }
    
    private void openDocuments() throws IOException {
        Path projectPath = FileUtils.RES_DIR.resolve("workspace").resolve("source").resolve("workspaceSymbolProject");
        TestUtil.openDocument(this.serviceEndpoint, projectPath.resolve("pkg1").resolve("pkg1Source.bal"));
        TestUtil.openDocument(this.serviceEndpoint, projectPath.resolve("pkg2").resolve("pkg2Source.bal"));
        TestUtil.openDocument(this.serviceEndpoint, projectPath.resolve("pkg3").resolve("pkg3Source.bal"));
    }
    
    private void closeDocuments() throws IOException {
        Path projectPath = FileUtils.RES_DIR.resolve("workspace").resolve("source").resolve("workspaceSymbolProject");
        TestUtil.closeDocument(this.serviceEndpoint, projectPath.resolve("pkg1").resolve("pkg1Source.bal"));
        TestUtil.closeDocument(this.serviceEndpoint, projectPath.resolve("pkg2").resolve("pkg2Source.bal"));
        TestUtil.closeDocument(this.serviceEndpoint, projectPath.resolve("pkg3").resolve("pkg3Source.bal"));
    }

    private boolean isArgumentsSubArray(JsonArray checkAgainst, JsonArray evalArray) {
        for (JsonElement jsonElement : evalArray) {
            if (!checkAgainst.contains(jsonElement)) {
                return false;
            }
        }

        return true;
    }
    
    @AfterClass
    public void shutdownLanguageServer() {
        TestUtil.shutdownLanguageServer(this.serviceEndpoint);
    }
}
