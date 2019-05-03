/*
 *  Copyright (c) 2019 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.ballerinalang.stdlib.task.scheduler;

import org.ballerinalang.launcher.util.BCompileUtil;
import org.ballerinalang.launcher.util.BRunUtil;
import org.ballerinalang.launcher.util.BServiceUtil;
import org.ballerinalang.launcher.util.CompileResult;
import org.ballerinalang.model.values.BBoolean;
import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.util.exceptions.BLangRuntimeException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

/**
 * Tests for Ballerina Task Timer Scheduler.
 */
@Test
public class TimerSchedulerTest {

    @Test(description = "Test service parameter passing")
    public void testSimpleTimerScheduler() {
        CompileResult compileResult = BCompileUtil.compileAndSetup("scheduler/timer/simple_timer.bal");
        BRunUtil.invokeStateful(compileResult, "main");
        await().atMost(10000, TimeUnit.MILLISECONDS).until(() -> {
            BValue[] count = BRunUtil.invokeStateful(compileResult, "getCount");
            Assert.assertEquals(count.length, 1);
            Assert.assertTrue(count[0] instanceof BInteger);
            return (((BInteger) count[0]).intValue() > 3);
        });
    }

    @Test(description = "Test service parameter passing", enabled = false)
    public void testTimerAttachment() {
        CompileResult compileResult = BCompileUtil.compile(
                "scheduler/timer/service_parameter.bal");
        BRunUtil.invoke(compileResult, "attachTimer");
        String expectedResult = "Sam is 10 years old";
        await().atMost(5000, TimeUnit.MILLISECONDS).until(() -> {
            BValue[] result = BRunUtil.invokeStateful(compileResult, "getResult");
            Assert.assertEquals(result.length, 1);
            Assert.assertTrue(result[0] instanceof BString);
            return (expectedResult.equals(result[0].stringValue()));
        });
    }

    @Test(description = "Tests for pause and resume functions of the timer")
    public void testPauseResume() {
        CompileResult compileResult = BCompileUtil.compile("scheduler/timer/pause_resume.bal");
        BServiceUtil.runService(compileResult);
        BRunUtil.invokeStateful(compileResult, "testAttach");
        await().atMost(10000, TimeUnit.MILLISECONDS).until(() -> {
            BValue[] isPaused = BRunUtil.invokeStateful(compileResult, "getIsPaused");
            BValue[] isResumed = BRunUtil.invokeStateful(compileResult, "getIsResumed");
            Assert.assertEquals(isPaused.length, 1);
            Assert.assertTrue(isPaused[0] instanceof BBoolean);
            Assert.assertEquals(isResumed.length, 1);
            Assert.assertTrue(isResumed[0] instanceof BBoolean);
            return (((BBoolean) isPaused[0]).booleanValue() && ((BBoolean) isResumed[0]).booleanValue());
        });
    }

    @Test(description = "Tests a timer scheduler cancel functionality")
    public void testListenerTimerStop() {
        CompileResult compileResult = BCompileUtil.compileAndSetup("scheduler/timer/timer_stop.bal");
        await().atLeast(4000, TimeUnit.MILLISECONDS).atMost(10000, TimeUnit.MILLISECONDS).until(() -> {
            BRunUtil.invokeStateful(compileResult, "triggerTimer");
            BValue[] count = BRunUtil.invokeStateful(compileResult, "getCount");
            Assert.assertEquals(count.length, 1);
            Assert.assertTrue(count[0] instanceof BInteger);
            return (((BInteger) count[0]).intValue() == -2000);
        });
    }

    @Test(description = "Tests a timer scheduler which runs for a limited number of times")
    public void testLimitedNumberOfRuns() {
        CompileResult compileResult = BCompileUtil.compileAndSetup("scheduler/timer/limited_number_of_runs.bal");

        BRunUtil.invokeStateful(compileResult, "triggerTimer");
        BValue[] count = BRunUtil.invokeStateful(compileResult, "getCount");
        Assert.assertEquals(count.length, 1);
        Assert.assertTrue(count[0] instanceof BInteger);
        Assert.assertEquals(((BInteger) count[0]).intValue(), 3);
    }

    @Test(description = "Tests a timer scheduler with zero delay")
    public void testZeroDelay() {
        CompileResult compileResult = BCompileUtil.compileAndSetup("scheduler/timer/zero_delay.bal");
        BRunUtil.invokeStateful(compileResult, "triggerTimer");
        await().atMost(10000, TimeUnit.MILLISECONDS).until(() -> {
            BValue[] count = BRunUtil.invokeStateful(compileResult, "getCount");
            Assert.assertEquals(count.length, 1);
            Assert.assertTrue(count[0] instanceof BInteger);
            return (((BInteger) count[0]).intValue() > 3);
        });
    }

    @Test(
            description = "Tests a timer scheduler with zero interval",
            expectedExceptions = BLangRuntimeException.class,
            expectedExceptionsMessageRegExp = ".*Timer scheduling interval should be a positive integer.*"
    )
    public void testZeroInterval() {
        CompileResult compileResult = BCompileUtil.compileAndSetup("scheduler/timer/zero_interval.bal");
        BRunUtil.invokeStateful(compileResult, "triggerTimer");
        await().atMost(10000, TimeUnit.MILLISECONDS).until(() -> {
            BValue[] count = BRunUtil.invokeStateful(compileResult, "getCount");
            Assert.assertEquals(count.length, 1);
            Assert.assertTrue(count[0] instanceof BInteger);
            return (((BInteger) count[0]).intValue() > 3);
        });
    }

    @Test(description = "Tests a timer scheduler with multiple services attached")
    public void testMultipleServices() {
        CompileResult compileResult = BCompileUtil.compileAndSetup("scheduler/timer/multiple_services.bal");
        BRunUtil.invokeStateful(compileResult, "triggerTimer");
        await().atMost(10000, TimeUnit.MILLISECONDS).until(() -> {
            BValue[] count = BRunUtil.invokeStateful(compileResult, "getResult");
            Assert.assertEquals(count.length, 1);
            Assert.assertTrue(count[0] instanceof BBoolean);
            return ((BBoolean) count[0]).booleanValue();
        });
    }
}
