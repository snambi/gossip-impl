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
import java.util.ArrayList;
import java.util.List;

import static com.github.gossip.Starter.PORT;


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

    public static final String LOCAL_HOST = System.getProperty("host", "127.0.0.1");

    private int localPort;
    List<RemoteNode> nodes = null;

    private Channel localChannel = null;
    private EventLoopGroup localLoopGroup = null;
    private NioEventLoopGroup remoteLoopGroup = null;

    private List<Channel> outgoingChannels = new ArrayList<Channel>();
    private String name;

    /**
     * Connects only to local host, on a given port.
     * If the port is passed as 0, it connects on the default port.
     * @param localPort
     */
    public GossipClient( int localPort){
        this( RandomString.generateString(5), localPort, null);
    }

    public GossipClient( String nodeName, int localPort, String remoteHosts ){

        if( localPort == 0 ){
            localPort = Starter.PORT;
        }

        this.localPort = localPort;
        this.name = nodeName;

        if( remoteHosts != null ) {
            nodes = RemoteNode.parseMultiHosts(remoteHosts);
        }
    }

    /**
     * Connects to localhost and remote hosts
     * @param remoteHosts
     * @param localPort
     */
    public GossipClient(String remoteHosts, int localPort ) {
        nodes = RemoteNode.parseMultiHosts(remoteHosts);
        this.localPort = localPort;
    }

    public static void main(String[] args ){
        GossipClient client = new GossipClient(LOCAL_HOST, PORT );
        client.start();
    }


    public void start(){

        startLocalChannel();
        startRemoteChannels();

        startCli();
    }

    public void startCli(){

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        for (;;) {

            String line = null;

            try {
                line = in.readLine();

                sendLocal(line);
                sendRemote(line);

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

    public void startRemoteChannels(){

        if( nodes == null || nodes.isEmpty() ){
            return;
        }

        try{

            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            remoteLoopGroup = new NioEventLoopGroup();

            for( RemoteNode node : nodes ) {

                Bootstrap bootstrap = new Bootstrap();

                bootstrap.group(remoteLoopGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new SecureClientInitializer(sslContext));

                System.out.println("Remote connection to " + node.getHost() + ":" + node.getPort());
                Channel remoteChannel = bootstrap.connect( node.getHost(), node.getPort()).sync().channel();

                // add the remote channels to the list for future reference
                outgoingChannels.add(remoteChannel);
            }

        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SSLException e) {
            e.printStackTrace();
        } finally{
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

            System.out.println("Local connection to "+ LOCAL_HOST + ":" + localPort );
            localChannel = bootstrap.connect(LOCAL_HOST, localPort ).sync().channel();

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

    public void sendRemote( String msg ) throws InterruptedException {


        for( Channel channel : outgoingChannels){

            if( channel == localChannel){
                continue;
            }

            System.out.println("Sending remote message to [ "+ channel.remoteAddress() + "]"+ msg);

            ChannelFuture channelFuture = channel.writeAndFlush(msg + "\r\n");

            if( channelFuture != null ){
                channelFuture.sync();
            }

            if( msg.toLowerCase().equals("bye")){
                channel.closeFuture().sync();
            }
        }
    }

    public void shutdownLocal(){
        localLoopGroup.shutdownGracefully();
    }
}