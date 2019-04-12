package com.github.gossip;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public class GossipServer {

    static final int PORT = Integer.parseInt(System.getProperty("port", "8992"));

    private Thread runner;

    public static void main( String[] args ){

        GossipServer server = new GossipServer();
        server.startListening(PORT);

    }

    public void startListening(int port ){

        try {

            SelfSignedCertificate cert = new SelfSignedCertificate();
            SslContext sslContext = SslContextBuilder.forServer( cert.certificate(), cert.privateKey()).build();

            EventLoopGroup boosGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler( new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new SecureServerInitializer(sslContext));

            System.out.println("Listening for connections on "+ port);

            bootstrap.bind( port )
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (SSLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(final int port){

        Runnable r = new Runnable() {
            public void run() {
                startListening( port);
            }
        };

        runner = new Thread(r);
        runner.start();
    }

    public void waitForCompletion(){
        try {
            runner.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
