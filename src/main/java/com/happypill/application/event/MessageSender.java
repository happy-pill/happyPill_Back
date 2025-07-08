package com.happypill.application.event;

import java.util.concurrent.CompletableFuture;

public interface MessageSender {

    void sendMessage(String data);

    CompletableFuture<Void> sendAsyncMessage(String data);
}
