/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * you may obtain a copy of the License at
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
package org.ballerinalang.persistence.serializable.responses.impl;

import org.ballerinalang.bre.bvm.ForkJoinWorkerResponseContext;
import org.ballerinalang.bre.bvm.WorkerExecutionContext;
import org.ballerinalang.bre.bvm.WorkerResponseContext;
import org.ballerinalang.persistence.Deserializer;
import org.ballerinalang.persistence.serializable.SerializableContext;
import org.ballerinalang.persistence.serializable.SerializableState;
import org.ballerinalang.persistence.serializable.responses.SerializableResponseContext;
import org.ballerinalang.util.codegen.CallableUnitInfo;
import org.ballerinalang.util.codegen.ProgramFile;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class implements @{@link SerializableResponseContext} to serialize @{@link ForkJoinWorkerResponseContext}.
 *
 * @since 0.981.2
 */
public class SerializableForkJoinResponse extends SerializableResponseContext {

    public int[] retRegIndexes;

    public int workerCount;

    private int reqJoinCount;

    private int joinTargetIp;

    private int joinVarReg;

    private int timeoutTargetIp;

    private int timeoutVarReg;

    private Set<String> joinWorkerNames;

    private Map<String, String> channelNames;

    public SerializableForkJoinResponse(String respCtxKey, ForkJoinWorkerResponseContext respCtx) {
        this.respCtxKey = respCtxKey;
        retRegIndexes = respCtx.getRetRegIndexes();
        workerCount = respCtx.getWorkerCount();
        reqJoinCount = respCtx.getReqJoinCount();
        joinTargetIp = respCtx.getJoinTargetIp();
        joinVarReg = respCtx.getJoinVarReg();
        timeoutTargetIp = respCtx.getTimeoutTargetIp();
        timeoutVarReg = respCtx.getTimeoutVarReg();
        joinWorkerNames = new HashSet<>(respCtx.getJoinWorkerNames());
        channelNames = respCtx.getChannelNames();
    }

    @Override
    public WorkerResponseContext getResponseContext(ProgramFile programFile, CallableUnitInfo callableUnitInfo,
                                                    SerializableState state, Deserializer deserializer) {
        return new ForkJoinWorkerResponseContext(null, joinTargetIp, joinVarReg, timeoutTargetIp, timeoutVarReg,
                                                 workerCount, reqJoinCount, joinWorkerNames, channelNames);
    }

    @Override
    public void addTargetContexts(WorkerResponseContext respCtx, SerializableState state) {
        ForkJoinWorkerResponseContext forkJoinCtx = (ForkJoinWorkerResponseContext) respCtx;
        SerializableContext sTargetCtx = state
                .getSerializableContext(String.valueOf(forkJoinCtx.getTargetContext().hashCode()));
        targetCtxKey = sTargetCtx.ctxKey;
    }

    @Override
    public void joinTargetContextInfo(WorkerResponseContext respCtx, ProgramFile programFile,
                                      SerializableState state, Deserializer deserializer) {
        WorkerExecutionContext ctx = state.getExecutionContext(targetCtxKey, programFile, deserializer);
        respCtx.joinTargetContextInfo(ctx, retRegIndexes);
    }
}
