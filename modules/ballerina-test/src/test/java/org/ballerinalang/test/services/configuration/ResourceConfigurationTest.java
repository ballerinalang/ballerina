/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.test.services.configuration;

import org.ballerinalang.launcher.util.BCompileUtil;
import org.ballerinalang.launcher.util.BServiceUtil;
import org.ballerinalang.launcher.util.CompileResult;
import org.ballerinalang.util.exceptions.BallerinaException;
import org.testng.annotations.Test;

/**
 * Test case for resources with multiple http:resourceConfig annotations.
 *
 * @since 0.95.4
 */
public class ResourceConfigurationTest {

    private CompileResult compileResult;

    @Test(expectedExceptions = BallerinaException.class,
          expectedExceptionsMessageRegExp = "multiple resource configuration annotations found in resource:.*")
    public void testDuplicateResourceConfigAnnotations() {
        compileResult = BCompileUtil.compile("test-src/services/configuration/resource-config-annotation.bal");
        BServiceUtil.runService(compileResult);
    }
}
