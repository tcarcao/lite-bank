package com.litebank.service.presentation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.litebank.service.presentation.api.configuration.Path;
import com.litebank.service.presentation.api.di.DI;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;

public class Application {
    private Javalin javalin;
    private int port;

    private Application(Javalin javalin, int port) {
        this.javalin = javalin;
        this.port = port;
    }

    public static void main(String[] args) {
        init(7000);
    }

    public static Application init(int port) {
        var moneyTransferEventsConsumer = DI.getInstance().getMoneyTransferEventsConsumer();
        moneyTransferEventsConsumer.initialize();

        var projectionsEventsConsumer = DI.getInstance().getProjectionsEventsConsumer();
        projectionsEventsConsumer.initialize();

        configureJavalin();
        Javalin app = Javalin.create().start(port);

        app.post(Path.TRANSFERS, DI.getInstance().getMoneyTransfersController().startTransfer);
        app.get(Path.TRANSFERS_ID, DI.getInstance().getMoneyTransfersController().getMoneyTransfer);

        app.post(Path.ACCOUNTS, DI.getInstance().getAccountsController().openAccount);
        app.get(Path.ACCOUNTS_ID, DI.getInstance().getAccountsController().getAccount);

        return new Application(app, port);
    }

    public int getPort() {
        return port;
    }

    public void stop() {
        this.javalin.stop();

        DI.rebuild();
    }

    private static void configureJavalin() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JavalinJackson.configure(objectMapper);
    }
}
