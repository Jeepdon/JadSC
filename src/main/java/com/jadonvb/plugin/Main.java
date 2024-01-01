package com.jadonvb.plugin;

import com.google.inject.Inject;
import com.jadonvb.Client;
import com.jadonvb.enums.ServerType;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import org.slf4j.Logger;

import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.util.ArrayList;

@Plugin(
        id = "jadsc",
        name = "JadSC",
        version = "0.0.1",
        authors = {"Ja90n"}
)
public class Main {

    private final Logger logger;
    private final ProxyServer proxyServer;
    private Client client;

    @Inject
    public Main(ProxyServer proxyServer, Logger logger) {
        this.logger = logger;
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        client = new Client(ServerType.PROXY);
        client.addMessageListener(new MessageHandler(this));
    }


    public void registerServer(String name, int port) {
        ServerInfo serverInfo = new ServerInfo(name, InetSocketAddress.createUnresolved(name, port));
        proxyServer.registerServer(serverInfo);
    }

    public void unRegisterServer(String name) {
        int port = getPortFromIp(name);
        ServerInfo serverInfo = new ServerInfo(name, InetSocketAddress.createUnresolved(name,port));
        proxyServer.unregisterServer(serverInfo);
    }

    public RegisteredServer getServerFromIp(String ip) {
        for (RegisteredServer registeredServer : getProxyServer().getAllServers()) {
            String serverIp = getIpFromServer(registeredServer);
            if (serverIp.equals(ip)) {
                return registeredServer;
            }
        }
        return null;
    }


    public String getIpFromServer(RegisteredServer registeredServer) {
        ServerInfo serverInfo = registeredServer.getServerInfo();
        if (serverInfo.getName().equals("lobby")) {
            return "lobby";
        }
        System.out.println(serverInfo);
        System.out.println(serverInfo.getAddress());

        String[] address = serverInfo.getAddress().toString().split("/");

        return address[0];
    }

    public int getPortFromIp(String ip) {
        RegisteredServer registeredServer = getServerFromIp(ip);
        ServerInfo serverInfo = registeredServer.getServerInfo();

        return serverInfo.getAddress().getPort();
    }

    public int getAvailablePort() {
        int returnValue = -1;
        OUTER: for (int i = 30081; i < 32700; i++) {
            for (RegisteredServer registeredServer : proxyServer.getAllServers()) {
                if (i == registeredServer.getServerInfo().getAddress().getPort()) {
                    continue OUTER;
                }
            }
            returnValue = i;
        }

        return returnValue;
    }

    public Logger getLogger() {
        return logger;
    }

    public Client getClient() {
        return client;
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

}
