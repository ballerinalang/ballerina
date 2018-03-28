/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.ballerinalang.nativeimpl.io.events.characters;

import org.ballerinalang.nativeimpl.io.CloseCharacterChannel;
import org.ballerinalang.nativeimpl.io.channels.base.CharacterChannel;
import org.ballerinalang.nativeimpl.io.events.Event;
import org.ballerinalang.nativeimpl.io.events.EventContext;
import org.ballerinalang.nativeimpl.io.events.EventResult;
import org.ballerinalang.nativeimpl.io.events.result.BooleanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Event which will close the character channel.
 */
public class CloseCharacterChannelEvent implements Event {
    /**
     * Character channel which will be closed.
     */
    private CharacterChannel channel;
    /**
     * Holds context to the event.
     */
    private EventContext context;

    private static final Logger log = LoggerFactory.getLogger(CloseCharacterChannel.class);

    public CloseCharacterChannelEvent(CharacterChannel channel) {
        this.channel = channel;
    }

    public CloseCharacterChannelEvent(CharacterChannel channel, EventContext context) {
        this.channel = channel;
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventResult get() {
        BooleanResult result;
        try {
            channel.close();
            result = new BooleanResult(true, context);
        } catch (IOException e) {
            log.error("Error occurred while closing character channel", e);
            context.setError(e);
            result = new BooleanResult(context);
        }
        return result;
    }
}
