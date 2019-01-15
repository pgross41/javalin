/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.examples;

import io.javalin.Javalin;
import io.javalin.serversentevent.SseClient;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class HelloWorldSse {

    public static void main(String[] args) throws InterruptedException {

        Queue<SseClient> clients = new ConcurrentLinkedQueue<>();

        Javalin app = Javalin.create().start(7000);
        app.get("/", ctx -> ctx.html("<script>new SseClient('http://localhost:7000/sse').addEventListener('hi', msg => console.log(msg));"));
        app.sse("/sse", client -> {
            clients.add(client);
            client.onClose(() -> clients.remove(client));
        });

        while (true) {
            for (SseClient sse : clients) {
                sse.sendEvent("hi", "hello world");
            }
            TimeUnit.SECONDS.sleep(1);
        }

    }

}