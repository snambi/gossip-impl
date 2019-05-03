package com.github.gossip.outgoing;

import com.github.gossip.MessageRouter;
import com.github.gossip.MessageWrapper;
import com.github.gossip.Starter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SecureClientHandler extends SimpleChannelInboundHandler<String> {

    private OutgoingConnectionManager outgoingConnectionManager;

    public SecureClientHandler(OutgoingConnectionManager outgoingConnectionManager) {
        this.outgoingConnectionManager = outgoingConnectionManager;
    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        MessageWrapper wrapper = MessageWrapper.fromJson(msg);

        System.out.println(">> "+ wrapper.getMessage().getContent() );

        // TODO: properly pass this object via constructor.
        MessageRouter messageRouter = Starter.getMessageRouter();
        messageRouter.process(ctx, wrapper);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
