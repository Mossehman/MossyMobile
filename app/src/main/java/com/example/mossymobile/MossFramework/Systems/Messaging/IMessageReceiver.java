package com.example.mossymobile.MossFramework.Systems.Messaging;

public interface IMessageReceiver {
    void HandleMessage(Message message);
    default void Register(String receiverID)
    {
        MessageHub.AddReceiver(receiverID, this);
    }
}
