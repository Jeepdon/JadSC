package com.jadonvb.plugin;

import com.jadonvb.enums.MessageType;
import com.jadonvb.messages.Message;
import com.jadonvb.messages.MessageListener;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MessageHandler implements MessageListener {

    private final Main main;

    public MessageHandler(Main main) {
        this.main = main;
    }

    @Override
    public void getMessage(Message message) {
        System.out.println(message);
        switch (message.getType()) {
            case PORT_REQUEST -> portRequest(message);
            case UNREGISTER -> unRegister(message);
        }
    }

    private void unRegister(Message message) {
        main.unRegisterServer(message.getSender());
    }

    private void portRequest(Message message) {

        RegisteredServer registeredServer = main.getServerFromIp(message.getSender());

        // Setting port
        int port;
        if (registeredServer != null) {
            port = registeredServer.getServerInfo().getAddress().getPort();
        } else {
            port = main.getAvailablePort();

            if (port == -1) {
                main.getLogger().error("No available port found for " + message.getSender());
                return;
            }

            registerServer(message.getSender(),port);
        }

        // Sending message
        Message returnMessage = new Message();

        returnMessage.setSender("velocity");
        returnMessage.setType(MessageType.PORT_REQUEST);
        returnMessage.setReceiver(message.getSender());

        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(String.valueOf(port));
        returnMessage.setArguments(arguments);

        main.getClient().sendMessage(returnMessage);
    }

    private void registerServer(String ip, int port) {
        main.getProxyServer().getScheduler()
                .buildTask(main, () -> main.registerServer(ip,port))
                .delay(1L, TimeUnit.SECONDS)
                .schedule();
    }
}
