/*
 * Copyright (c) 2017, WSO2 Inc. (http://wso2.com) All Rights Reserved.
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
 *
 */

package org.wso2.ballerina.tooling.service.dockerizer.handler;

import io.fabric8.docker.api.model.ImageDelete;
import io.fabric8.docker.client.Config;
import io.fabric8.docker.client.ConfigBuilder;
import io.fabric8.docker.client.DefaultDockerClient;
import io.fabric8.docker.client.DockerClient;
import io.fabric8.docker.client.DockerClientException;
import io.fabric8.docker.dsl.EventListener;
import io.fabric8.docker.dsl.OutputHandle;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.ballerina.tooling.service.dockerizer.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Manage Docker related functionality.
 */
public class DockerHandler {
    private static final Logger logger = LoggerFactory.getLogger(DockerHandler.class);
    private static Map<String, DockerClient> clients = new HashMap<>();

    public static boolean createServiceImage(String dockerEnv, String serviceName, String ballerinaConfig)
            throws IOException, InterruptedException {
        return createImage(dockerEnv, serviceName, ballerinaConfig, true);
    }

    public static boolean createFunctionImage(String dockerEnv, String serviceName, String ballerinaConfig)
            throws IOException, InterruptedException {
        return createImage(dockerEnv, serviceName, ballerinaConfig, false);
    }

    public static boolean createImage(String dockerEnv, String serviceName, String ballerinaConfig,
                                      boolean service) throws IOException, InterruptedException {

        String imageName = serviceName.toLowerCase();

        // 1. Create a tmp docker context
        String tempDirName = "ballerina-docker-" + String.valueOf(Instant.now().getEpochSecond());
        Path tmpDir = Files.createTempDirectory(tempDirName);
        Files.createDirectory(Paths.get(tmpDir.toString() + "/files"));
        Files.copy(Paths.get(Utils.getResourceFile("docker/image/Dockerfile").getAbsolutePath()),
                Paths.get(tmpDir.toString() + "/Dockerfile"), StandardCopyOption.REPLACE_EXISTING);

        // 2. Create a .bal file inside context/files
        Path ballerinaFile = Files.createFile(Paths.get(tmpDir + "/files/" + serviceName + ".bal"));
        Files.write(ballerinaFile, ballerinaConfig.getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // 3. Create a docker image from the temp context
        DockerClient client = getDockerClient(dockerEnv);

        final CountDownLatch buildDone = new CountDownLatch(1);
        final boolean[] err = {false};
        OutputHandle buildHandle = client.image()
                .build()
                .withRepositoryName(imageName)
                .withNoCache()
                .alwaysRemovingIntermediate()
                .withBuildArgs("{\"SVC_MODE\":\"" + String.valueOf(service) + "\"}")
                .usingListener(new EventListener() {
                    @Override
                    public void onSuccess(String successEvent) {
                        logger.info("Docker image " + imageName + " for Service " + serviceName + " build complete.");
                        buildDone.countDown();
                    }

                    @Override
                    public void onError(String errorEvent) {
                        logger.error("Error while building Docker image " + imageName + " for Service " + serviceName + ": " + errorEvent);
                        err[0] = true;
                        buildDone.countDown();
                    }

                    @Override
                    public void onEvent(String event) {
                        logger.debug(event);
                    }
                })
                .writingOutput(System.out)
                .fromFolder(tmpDir.toString());

        buildDone.await();
        buildHandle.close();
        client.close();

        // 4. ??
        // 5. Profit

        FileUtils.deleteDirectory(new File(tmpDir.toString()));
        return (!err[0]);
    }

    public static boolean deleteImage(String dockerEnv, String serviceName) {
        List<ImageDelete> imageDeleteList = null;
        try {
            imageDeleteList = getDockerClient(dockerEnv).image().withName(serviceName).delete().force().andPrune();
        } catch (DockerClientException e) {
            if (e.getMessage().contains("No such image")) {
                return false;
            }
        }

        for (ImageDelete imageDelete : imageDeleteList) {
            if (StringUtils.isNotEmpty(imageDelete.getDeleted())) {
                System.out.println("Deleted:" + imageDelete.getDeleted());
            }
            if (StringUtils.isNotEmpty(imageDelete.getUntagged())) {
                System.out.println("Untagged:" + imageDelete.getUntagged());
            }
        }

        return true;
    }

    private static DockerClient getDockerClient(String env) {
        if (!clients.containsKey(env)){
            DockerClient client;
            if (env == null) {
                client = new DefaultDockerClient();
            } else {
                Config dockerClientConfig = new ConfigBuilder()
                        .withDockerUrl(env)
                        .build();
                client = new DefaultDockerClient(dockerClientConfig);
            }

            clients.put(env, client);
        }

        return clients.get(env);
    }
}