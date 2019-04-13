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

import static com.github.gossip.GossipServer.PORT;

/**
 * Gossip Client is responsible for
 *
 * <ul>
 *     <li>Connecting to other nodes that are passed via CLI parameters. These are outgoing connections</li>
 *     <li>Also Connects to the default listener running on this process</li>
 *     <li>Constantly reads the STDIN and sends the message to the local listener</li>
 * </ul>
 */
public class GossipClient {

    public static final String HOST = System.getProperty("host", "127.0.0.1");


    private int remotePort = 9002;
    private String remoteHost = null;
    private Channel localChannel = null;
    private EventLoopGroup localLoopGroup = null;

    public GossipClient(String remoteHost, int remotePort) {
        this.remotePort = remotePort;
        this.remoteHost = remoteHost;
    }

    public static void main(String[] args ){

        GossipClient client = new GossipClient( HOST, PORT);
        client.start();
    }


    public void start(){
        startLocalChannel();
        startCli();

        // start remote channels
    }

    public void startCli(){

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        for (;;) {

            String line = null;

            try {
                line = in.readLine();

                sendLocal(line);

                if (line == null) {
                    break;
                }

//                if( line.toLowerCase().equals("bye")){
//                    in.close();
//                    break;
//                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Connects the local listening socket to send messages.
     */
    public void startLocalChannel(){

        final SslContext sslContext;

        localLoopGroup = new NioEventLoopGroup(1);

        try {

            sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(localLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler( new SecureClientInitializer( sslContext ));

            System.out.println("Connecting to "+ HOST+ ":" + PORT );
            localChannel = bootstrap.connect( HOST, PORT ).sync().channel();

        } catch (SSLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendLocal( String msg ) throws InterruptedException {

        ChannelFuture lastWriteFuture = localChannel.writeAndFlush(msg + "\r\n");

        if( lastWriteFuture != null ){
            lastWriteFuture.sync();
        }

        if( msg.toLowerCase().equals("bye")){
            localChannel.closeFuture().sync();
        }
    }

    public void shutdownLocal(){
        localLoopGroup.shutdownGracefully();
    }


    public int getRemotePort() {
        return remotePort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }
}