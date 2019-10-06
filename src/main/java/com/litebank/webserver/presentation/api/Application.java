package com.litebank.webserver.presentation.api;

import com.litebank.webserver.presentation.api.configuration.Path;
import io.javalin.Javalin;

public class Application {
    public static void main(String[] args) {
        init(7000);
    }

    public static Javalin init(int port) {
        Javalin app = Javalin.create().start(port);

        app.post(Path.TRANSFERS, ctx -> ctx.result("transfers"));

        return app;
    }
}
