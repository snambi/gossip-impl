package com.github.gossip;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.Inet4Address;

public class SecureServerHandler extends SimpleChannelInboundHandler<String> {

    static final ChannelGroup incomingChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    String serverName = null;

    public SecureServerHandler(String serverName) {
        this.serverName = serverName;
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

                        System.out.println("Connection made from: "+ ctx.channel().remoteAddress());

                        ctx.writeAndFlush("Welcome to "+ Inet4Address.getLocalHost().getHostName() + ":"+ serverName + " secure stream\n");
                        ctx.writeAndFlush("Your session is protected by "+ ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() + "\n");

                        incomingChannels.add(ctx.channel());
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

        for( Channel channel : incomingChannels){
            if( ctx.channel() != channel ){
                // send the message to other incomingChannels
                channel.writeAndFlush("["+ ctx.channel().remoteAddress() + "] =>" + msg + '\n' );
            }else{
                // This is the channel, on which the message came.
                // So, don't write anyhing back
                // TODO: we may need a way to acknowledge the receipt of the message
                //channel.writeAndFlush("[you] "+ msg + '\n');
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
