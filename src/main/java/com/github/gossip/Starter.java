package com.github.gossip;

import org.apache.commons.cli.*;

public class Starter {

    public static final int REMOTE_PORT = 9002;
    public static final int LISTEN_PORT = 9002;

    public static void main( String[] args ){

        Options options = new Options();

        Option remoteNodeOpt = new Option("n", "remoteNode", true,
                "list of node ip addresses separated by comma");
        remoteNodeOpt.setRequired(false);
        options.addOption(remoteNodeOpt);

        Option remotyePortOpt = new Option("p", "port", true,
                "default port is 9002. If you want to override the port use the -p option");
        remotyePortOpt.setRequired(false);
        options.addOption(remotyePortOpt);

        Option listenPortOpt = new Option("l", "listen", true,
                "by default listen on port 9002. If you want to override the listen port use the -l option");
        listenPortOpt.setRequired(false);
        options.addOption(listenPortOpt);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("remoteNode", options);

            System.exit(1);
        }

        String remoteNodeStr = cmd.getOptionValue("remoteNode");
        String remotePortStr = cmd.getOptionValue("port");
        String listenPortStr = cmd.getOptionValue("listen");

        String remoteNode = remoteNodeStr;
        int remotePort = REMOTE_PORT;
        int listenPort = LISTEN_PORT;

        if( remotePortStr != null && !remotePortStr.isEmpty() ){
            remotePort = Integer.parseInt(remotePortStr);
        }
        if( listenPortStr != null && !listenPortStr.isEmpty()) {
            listenPort = Integer.parseInt(listenPortStr);
        }


        System.out.println("Localserver: 127.0.0.1 port:"+ listenPort);
        if( remoteNode != null ){
            System.out.println("RemoteHost: "+ remoteNode + ", port: "+ remotePort);
        }



        // By Default, Start the server
        GossipServer server = new GossipServer();
        server.start(listenPort);

        // if a remote host ip address is provided, connect to that address
        GossipClient client = new GossipClient( remoteNode, remotePort);
        client.start();

        server.waitForCompletion();
    }
}
