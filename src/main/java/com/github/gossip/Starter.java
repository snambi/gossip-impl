package com.github.gossip;

import org.apache.commons.cli.*;

public class Starter {

    public static final int PORT = 9002;
    private String name;

    public static void main( String[] args ){

        Options options = new Options();

        Option remoteNodeOpt = new Option("n", "remoteNode", true,
                "list of node ip addresses:port numbers separated by comma");
        remoteNodeOpt.setRequired(false);
        options.addOption(remoteNodeOpt);

        Option portOpt = new Option("p", "port", true,
                "default port is 9002. If you want to override the port use the -p option");
        portOpt.setRequired(false);
        options.addOption(portOpt);

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
        String portStr = cmd.getOptionValue("port");


        String remoteNode = remoteNodeStr;
        int port = PORT;

        if( portStr != null && !portStr.isEmpty() ){
            port = Integer.parseInt(portStr);
        }


        System.out.println("Localserver: 127.0.0.1 port:"+ port);
        if( remoteNode != null ){
            System.out.println("RemoteHost: "+ remoteNode );
        }

        // Generate a name for the "node"
        String nodeName = RandomString.generateString(5);

        // By Default, Start the server
        GossipServer server = new GossipServer();
        server.start(port);

        // if a remote host ip address is provided, connect to that address
        GossipClient client = new GossipClient(nodeName, port, remoteNode);
        client.start();

        server.waitForCompletion();
    }
}
