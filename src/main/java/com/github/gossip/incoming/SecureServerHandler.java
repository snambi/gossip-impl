package com.github.gossip.incoming;

import com.github.gossip.Message;
import com.github.gossip.MessageRouter;
import com.github.gossip.MessageWrapper;
import com.github.gossip.Starter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

public class SecureServerHandler extends SimpleChannelInboundHandler<String> {

    //static final ChannelGroup incomingChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private IncomingConnectionManager incomingConnectionManager;


    public SecureServerHandler(IncomingConnectionManager connectionManager) {
        this.incomingConnectionManager = connectionManager;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {

        // when the connection happens,
        // right after the execution of SSL handler,
        // a welcome message is sent to the client

        ctx.pipeline()
                .get(SslHandler.class)
                .handshakeFuture()
                .addListener(new GenericFutureListener<Future<? super Channel>>() {

                    public void operationComplete(Future<? super Channel> future) throws Exception {

                        //System.out.println("Received connection from : "+ ctx.channel().remoteAddress());

                        String msg = String.format("Welcome to %s secure stream. The connection is protected by %s.",
                                incomingConnectionManager.getNodeName(),
                                ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite());

                        Message m = Message.create(incomingConnectionManager.getNodeName(), msg );
                        MessageWrapper wrapper = new MessageWrapper(m.toJson());

                        //System.out.println( "Sending to : "+ ctx.channel().remoteAddress());

                        ctx.writeAndFlush( wrapper.toJson() + "\n");

                        incomingConnectionManager.getIncomingChannels().add(ctx.channel());
                    }
                });
    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        // Close the connection if the client has sent 'bye'.
        if ("bye".equals(msg.toLowerCase())) {
            ctx.channel().writeAndFlush("[you] "+ msg + '\n');
            ctx.close();
            return;
        }

        MessageWrapper wrapper = MessageWrapper.fromJson(msg);

        // TODO: properly pass this object via constructor.
        MessageRouter messageRouter = Starter.getMessageRouter();
        messageRouter.process(ctx, wrapper);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
