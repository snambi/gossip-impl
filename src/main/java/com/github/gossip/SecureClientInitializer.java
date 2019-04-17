package com.github.gossip;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

public class SecureClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslContext;

    public SecureClientInitializer(SslContext ctx ){
        this.sslContext = ctx;
    }

    protected void initChannel(SocketChannel channel) throws Exception {

        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast(sslContext.newHandler(channel.alloc(), GossipClient.LOCAL_HOST, GossipServer.PORT ));

        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());

        // Handler that processes that incoming events
        pipeline.addLast(new SecureClientHandler());
    }

}
