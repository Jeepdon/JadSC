package com.jadonvb.plugin;

import com.jadonvb.Message;
import com.jadonvb.MessageListener;
import com.jadonvb.plugin.Main;

import java.util.ArrayList;

public class MessageHandler implements MessageListener {

    private com.jadonvb.plugin.Main main;

    public MessageHandler(Main main) {
        this.main = main;
    }

    @Override
    public void getMessage(Message message) {
        switch (message.getType()) {
            case PORT_REQUEST -> {
                int port = main.getLowestPort();

                if (port == -1) {
                    main.getLogger().error("NO PORT FOUND");
                }

                Message returnMessage = new Message();
                returnMessage.setSender("velocity");
                returnMessage.setReceiver(message.getSender());
                ArrayList<String> arguments = new ArrayList<>();

                arguments.add(String.valueOf(port));

                returnMessage.setArguments(arguments);

                main.getClient().sendMessage(returnMessage);

                main.registerServer(message.getSender(),port);
            }
        }
    }
}
