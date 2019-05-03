package com.github.gossip;

import com.github.gossip.incoming.IncomingConnectionManager;
import com.github.gossip.outgoing.OutgoingConnectionManager;
import org.apache.commons.cli.*;

public class Starter {

    public static final int PORT = 9002;
    private String name;

    // TODO: this is similar to singleton pattern. Refactor this to a better approach.
    private static MessageRouter messageRouter;

    public static void main( String[] args ){

        Options options = new Options();

        Option remoteNodeOpt = new Option("n", "remoteNodes", true,
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

        String remoteNodesStr = cmd.getOptionValue("remoteNodes");
        String portStr = cmd.getOptionValue("port");


        String remoteNodes = remoteNodesStr;
        int port = PORT;

        if( portStr != null && !portStr.isEmpty() ){
            port = Integer.parseInt(portStr);
        }


        System.out.println("Localserver: 127.0.0.1 port:"+ port);
        if( remoteNodes != null ){
            System.out.println("RemoteHost: "+ remoteNodes );
        }

        // Generate a name for the "node"
        String nodeName = RandomString.generateString(5);

        // By Default, Start the server
        IncomingConnectionManager incoming = new IncomingConnectionManager(nodeName);


        // if a remote host ip address is provided, connect to that address
        OutgoingConnectionManager outgoing = new OutgoingConnectionManager(nodeName, port, remoteNodes);


        messageRouter = new MessageRouter(incoming, outgoing);

        incoming.start(port);
        outgoing.start();

        incoming.waitForCompletion();
    }

    public static MessageRouter getMessageRouter(){
        return messageRouter;
    }
}
