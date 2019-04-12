package com.github.gossip;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GossipClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8992"));

    private int remotePort = 9002;
    private String remoteHost = null;

    public GossipClient(String remoteHost, int remotePort) {
        this.remotePort = remotePort;
        this.remoteHost = remoteHost;
    }

    public static void main(String[] args ){

        GossipClient client = new GossipClient( HOST, PORT);
        client.start();
    }

    public void start(){

        final SslContext sslContext;
        EventLoopGroup loopGroup = new NioEventLoopGroup();

        try {

            sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();


            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .handler( new SecureClientInitializer( sslContext ));


            // try connecting to the server
            System.out.println("Connecting to "+ getRemoteHost() + ":" + getRemotePort() );
            Channel ch = bootstrap.connect( getRemoteHost(), getRemotePort()).sync().channel();

            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            for (;;) {

                String line = in.readLine();

                if (line == null) {
                    break;
                }

                lastWriteFuture = ch.writeAndFlush( line + "\r\n");

                if( line.toLowerCase().equals("bye")){
                    ch.closeFuture().sync();
                    break;
                }
            }

            if( lastWriteFuture != null ){
                lastWriteFuture.sync();
            }

        } catch (SSLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            loopGroup.shutdownGracefully();
        }
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }
}