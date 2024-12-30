package com.example.mossymobile.MossFramework.Systems.Messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MessageHub {

    ///Hashmap of all the registered message receivers under an ID
    private static final HashMap<String, List<IMessageReceiver>> Receivers = new HashMap<>();

    ///List of all the queued messages, queued messages will be handled at the end of each frame in the scene's SystemUpdate()
    private static final List<Message> QueuedMessages = new ArrayList<>();

    /**
     * Adds a new {@code IMessageReceiver} to a group ID. A {@code IMessageReceiver} can be placed under multiple groups. A group can have multiple {@code IMessageReceiver}s.
     *
     * @param ID the group ID to add the {@code IMessageReceiver} to
     * @param receiver the {@code IMessageReceiver} to register under the group
     *
     */
    public static void AddReceiver(String ID, IMessageReceiver receiver)
    {
        if (ID.isEmpty() || receiver == null) { return; } //if invalid
        if (Receivers.containsKey(ID))
        {
            Objects.requireNonNull(Receivers.get(ID)).add(receiver);
        }
        else
        {
            List<IMessageReceiver> receiversOfID = new ArrayList<>();
            receiversOfID.add((receiver));

            Receivers.put(ID, receiversOfID);
        }
    }


    /**
     * Adds a new {@code Message} to a list of messages. {@code Message}s are processed at the end of each frame.
     *
     * @param msg the {@code Message} object to add to the cache for later processing.
     */
    public static void SendMessage(Message msg)
    {
        if (msg == null) { return; } //Message was invalid, unable to add

        QueuedMessages.add(msg); //Add the messages to a separate list for events where a receiver responds to another messages by sending another message
    }

    ///Sends the list of {@code Message}s to the respective receivers from a list, clears the list when complete. Runs at the end of each frame.
    public void HandleMessages()
    {
        if (QueuedMessages.isEmpty()) { return; } //no new messages, we do not need to do anything

        List<Message> Messages = new ArrayList<>(QueuedMessages);   //create a separate list for handling the queued messages and copy the data into it
        QueuedMessages.clear(); //clear the queued messages at the start (any messages that result in a response via sending another message will be unaffected)

        for (Message msg : Messages) //check through all the new messages
        {
            if (msg == null ||                                                      //message is null, cannot be handled
                msg.GetReceivers().isEmpty() && msg.GetReceiverIDs().isEmpty())     //message contains no parameters for which entities to send the data to
            { continue; }


            if (msg.GetReceivers().isEmpty()) //no specific receivers were specified, assume the list instead
            {
                List<IMessageReceiver> receiverCache = new ArrayList<>(); //caches all the receivers the message is sending to (prevents duplicates)
                for (String receiverID : msg.GetReceiverIDs())
                {
                    List<IMessageReceiver> receivers =  Receivers.get(receiverID); //
                    if (receivers == null) { continue; }

                    for (IMessageReceiver receiver : receivers)
                    {
                        if (receiverCache.contains(receiver)) { continue; } //this prevents us from sending multiple messages to 1 entity if they are within multiple groups

                        receiver.HandleMessage(msg); //tell the receiver to handle the message (code for handling is defined in the individual component)
                        receiverCache.add(receiver); //cache this receiver to prevent sending the same message to the same entity multiple times
                    }
                }

                receiverCache.clear();  //empty the cache of receivers for garbage collection
            }
        }

        Messages.clear();   //empty the list of messages for garbage collection
        System.gc(); //handle garbage collection of the messages
    }


}
