/*
 *
 *   Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 * /
 */
package org.ballerinalang.test.metrics;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.ballerinalang.launcher.util.BCompileUtil;
import org.ballerinalang.launcher.util.BRunUtil;
import org.ballerinalang.launcher.util.CompileResult;
import org.ballerinalang.model.values.BFloat;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.util.exceptions.BLangRuntimeException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests for Counter metric.
 */
public class CounterTest {
    private CompileResult compileResult;

    @BeforeTest
    public void setup() {
        compileResult = BCompileUtil.compile("test-src/metrics/counter-test.bal");
        Metrics.globalRegistry.add(new SimpleMeterRegistry());
    }

    @Test
    public void testCounterIncrementByOne() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testCounterIncrementByOne");
        Assert.assertEquals(returns[0], new BFloat(1));
    }

    @Test
    public void testCounterIncrement() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testCounterIncrement");
        Assert.assertEquals(returns[0], new BFloat(5));
    }

    @Test
    public void testCounterWithoutTags() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testCounterWithoutTags");
        Assert.assertEquals(returns[0], new BFloat(3));
    }

    @Test
    public void testCounterWithoutDescription() {
        BValue[] returns = BRunUtil.invoke(compileResult, "testCounterWithoutDescription");
        Assert.assertEquals(returns[0], new BFloat(4));
    }

    @Test (expectedExceptions = BLangRuntimeException.class)
    public void testCounterWithEmptyStruct() {
        BRunUtil.invoke(compileResult, "testCounterWithEmptyStruct");
    }
}
