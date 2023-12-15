package com.jadonvb;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import org.slf4j.Logger;

import java.net.InetSocketAddress;

@Plugin(
        id = "jadsc",
        name = "JadSC",
        version = "0.0.1",
        authors = {"Ja90n"}
)
public class Main {

    private Logger logger;
    private ProxyServer proxyServer;

    @Inject
    public Main(ProxyServer proxyServer, Logger logger) {
        this.logger = logger;
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        ServerInfo serverInfo = new ServerInfo("sumo", InetSocketAddress.createUnresolved("0",30065));
        proxyServer.registerServer(serverInfo);
    }
}
