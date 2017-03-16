/*
*   Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.ballerinalang;

import org.ballerinalang.bre.nonblocking.ModeResolver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

/**
 * Util class to configure Test with external configuration.
 */
public class TestConfigurationUtil {
    
    private boolean isNonBlockingEnabled;

    @Parameters("enableNonBlocking")
    @BeforeTest
    public void configureTestExecutor(String enableNonBlocking) {
        isNonBlockingEnabled = ModeResolver.getInstance().isNonblockingEnabled();
        ModeResolver.getInstance().setNonblockingEnabled(Boolean.valueOf(enableNonBlocking));
    }
    
    @AfterTest
    public void cleanupTestExecutor() {
        ModeResolver.getInstance().setNonblockingEnabled(isNonBlockingEnabled);
    }
}
