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
 *
 */

package org.wso2.carbon.transport.http.netty.contractImpl.websocket.message;

import org.wso2.carbon.transport.http.netty.contract.websocket.WebSocketTextMessage;
import org.wso2.carbon.transport.http.netty.contractImpl.websocket.WebSocketChannelContextImpl;


/**
 * Implementation of {@link WebSocketTextMessage}.
 */
public class WebSocketTextMessageImpl extends WebSocketChannelContextImpl implements WebSocketTextMessage {

    private final String text;
    private final boolean isFinalFragment;

    public WebSocketTextMessageImpl(String text, WebSocketChannelContextImpl channelContext) {
        super(channelContext.getChannelSession(), channelContext.getChannelContext());
        this.text = text;
        this.isFinalFragment = true;
    }

    public WebSocketTextMessageImpl(String text, boolean isFinalFragment, WebSocketChannelContextImpl channelContext) {
        super(channelContext.getChannelSession(), channelContext.getChannelContext());
        this.text = text;
        this.isFinalFragment = isFinalFragment;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean isFinalFragment() {
        return isFinalFragment;
    }
}
