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
package org.wso2.ballerina.test;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.wso2.ballerina.test.context.Constant;
import org.wso2.ballerina.test.context.ServerInstance;
import org.wso2.ballerina.test.util.TestConstant;
import org.wso2.ballerina.test.util.http2.HTTP2Client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Parent test class of all http2 test and this will provide basic functionality for integration test.
 */
public abstract class HTTP2IntegrationTestCase {
    private ServerInstance serverInstance;
    private static final Logger log = LoggerFactory.getLogger(HTTP2IntegrationTestCase.class);
    public HTTP2Client http2Client = null;
    private static final String HTTP2_ENABLED_NETTY_CONF = "src" + File.separator + "test" + File.separator +
            "resources" + File.separator + "http2" + File.separator + "conf" + File.separator + "netty-transports.yml";
    private static final String SERVER_NETTY_CONF_PATH = File.separator + "bre" + File.separator + "conf" + File
            .separator + "netty-transports.yml";

    @BeforeClass(alwaysRun = true)
    public void init() throws Exception {
        //path of the zip file distribution
        String serverZipPath = System.getProperty(Constant.SYSTEM_PROP_SERVER_ZIP);
        serverInstance = new ServerInstance(serverZipPath, TestConstant.HTTP2_TEST_PORT) {
            //config the service files need to be deployed
            @Override
            protected void configServer() throws IOException {
                //path of the sample bal file directory
                String serviceSampleDir = this.getServerHome() + File.separator + Constant.SERVICE_SAMPLE_DIR;
                //list of sample bal files to be deploy
                String[] serviceFilesArr = listFiles(serviceSampleDir, new ArrayList<>());
                setArguments(serviceFilesArr);
                // copy http2 enabled netty-transports.yml file
                copyFile(new File(HTTP2_ENABLED_NETTY_CONF), new File(this
                        .getServerHome() + SERVER_NETTY_CONF_PATH));
            }
        };
        try {
            serverInstance.start();
            http2Client = new HTTP2Client(false, "localhost", TestConstant.HTTP2_TEST_PORT);
        } catch (Exception e) {
            log.error("Server failed to start. " + e.getMessage(), e);
            throw new RuntimeException("Server failed to start. " + e.getMessage(), e);
        }
    }

    @AfterClass(alwaysRun = true)
    public void destroy() {
        if (serverInstance != null && serverInstance.isRunning()) {
            try {
                serverInstance.stop();
            } catch (Exception e) {
                log.error("Server failed to stop. " + e.getMessage(), e);
                throw new RuntimeException("Server failed to stop. " + e.getMessage(), e);
            } finally {
                if (http2Client != null) {
                    http2Client.close();
                }
            }
        }
    }

    /**
     * Used to copy conf file
     *
     * @param source source file
     * @param dest   destination file
     * @throws IOException throws when file copying failed
     */
    private static void copyFile(File source, File dest)
            throws IOException {
        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public String getServiceURLHttp(String servicePath) {
        return serverInstance.getServiceURLHttp(servicePath);
    }

    /**
     * List the file in a given directory.
     *
     * @param path of the directory
     * @param list collection of files found
     * @return String array of file absolute paths
     */
    private static String[] listFiles(String path, List<String> list) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (list.size() > 100) {
            //returning the search when it comes to 100 files
            log.warn("Sample file deployment restricted to 100 files");
            return list.toArray(new String[]{});
        }
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isDirectory()) {
                    log.info("Searching service ballerina files in " + file.getPath());
                    listFiles(file.getAbsolutePath(), list);
                } else {
                    if (file.getPath().endsWith(Constant.SERVICE_FILE_EXTENSION)) {
                        log.info("Adding file " + file.getPath());
                        list.add(file.getAbsolutePath());
                    }
                }
            }
        }
        return list.toArray(new String[]{});
    }

    /**
     * Get content from response\
     *
     * @param msg HTTP Response
     * @return content
     */
    protected String getResponse(FullHttpResponse msg) {
        ByteBuf content = msg.content();
        if (content.isReadable()) {
            int contentLength = content.readableBytes();
            byte[] arr = new byte[contentLength];
            content.readBytes(arr);
            return new String(arr, 0, contentLength, CharsetUtil.UTF_8);
        }
        return null;
    }
}
