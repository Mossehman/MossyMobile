package com.example.mossymobile.MossFramework.Systems.Messaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

///Base class for a message (used in the messaging system), derive classes and add additional parameters to the constructors
public abstract class Message {

    ///The message type for easy identification (comparing ints is less expensive than typecasting)
    protected int MessageType;

    ///A list of the specific message receiver entities to send the data to
    protected List<IMessageReceiver> Receivers = new ArrayList<>();

    ///A list of the specific receiver IDs registered under MessageHub to send the data to
    protected List<String> ReceiverIDs = new ArrayList<>();

    public Message(int MessageType, String... Targets)
    {
        this.ReceiverIDs.addAll(Arrays.asList(Targets));
        this.MessageType = MessageType;
    }

    public Message(int MessageType, List<IMessageReceiver> receivers)
    {
        this.Receivers = receivers;
        this.MessageType = MessageType;
    }


    // We do not want to modify the data from the receivers, so we use a public getter and keep the data protected
    public int GetMessageType() { return MessageType; }
    public List<IMessageReceiver> GetReceivers() { return Receivers; }
    public List<String> GetReceiverIDs() { return ReceiverIDs; }
}
