package com.github.gossip;

import com.github.gossip.incoming.IncomingConnectionManager;
import com.github.gossip.outgoing.OutgoingConnectionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class MessageRouter {

    private IncomingConnectionManager incomingConnectionManager;
    private OutgoingConnectionManager outgoingConnectionManager;

    private MessageStorage storage;


    public MessageRouter(IncomingConnectionManager incomingConnectionManager,
                         OutgoingConnectionManager outgoingConnectionManager) {
        this.incomingConnectionManager = incomingConnectionManager;
        this.outgoingConnectionManager = outgoingConnectionManager;

        this.storage = new MessageStorage();
    }

    /**
     * Handles all incoming messages, from both incoming and outgoing channels.
     *
     * <ol>
     *     <li>check whether the message is already received</li>
     *     <li>broadcast on incoming and outgoing channels</li>
     *     <li>add the message to received messages list</li>
     * </ol>
    */
    public void process(ChannelHandlerContext handlerContext, MessageWrapper messageWrapper){

        if( messageWrapper == null ){
            return;
        }

        if( storage.containsMessage(messageWrapper.getMessage())){
            return;
        }

        System.out.println(">"+ messageWrapper.getMessage().getContent());
        broadcast(handlerContext, messageWrapper);

        storage.add(messageWrapper.getMessage());
    }

    /**
     * Sends the message to all incoming and outgoing channels
     * @param messageWrapper
     */
    public void broadcast( ChannelHandlerContext handlerContext, MessageWrapper messageWrapper ){

        incomingConnectionManager.broadcast(handlerContext, messageWrapper);
        outgoingConnectionManager.broadcast(messageWrapper);
    }
}
