package com.github.gossip;

import java.util.ArrayList;
import java.util.List;

public class RemoteNode {

    private String host;
    private int port;

    public RemoteNode(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static RemoteNode parseStr( String remoteNodeStr ){

        RemoteNode node = null;

        if( remoteNodeStr != null && !remoteNodeStr.isEmpty()){

            if( remoteNodeStr.contains(":")){

                String[] arr = remoteNodeStr.split(":");
                if( arr.length == 2 ){
                    int port = Integer.parseInt( arr[1] );
                    node = new RemoteNode(arr[0], port);
                }
            }
        }

        return node;
    }

    public static List<RemoteNode> parseMultiHosts( String data ){

        List<RemoteNode> nodes = new ArrayList<RemoteNode>();

        if( data != null && !data.isEmpty()){
            if( data.contains(",")){
                String[] arr = data.split(",");
                if( arr.length > 1){
                    for( String a : arr ){
                        RemoteNode n = parseStr(a);
                        nodes.add(n);
                    }
                }
            }else{
                RemoteNode n = parseStr(data);
                nodes.add(n);
            }
        }

        return nodes;
    }
}
