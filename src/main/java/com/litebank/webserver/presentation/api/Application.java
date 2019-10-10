package com.litebank.webserver.presentation.api;

import com.litebank.webserver.presentation.api.configuration.Path;
import com.litebank.webserver.presentation.api.di.DI;
import io.javalin.Javalin;

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

    public void start() {
        this.javalin.start();
    }

    public void stop() {
        this.javalin.stop();

        DI.rebuild();
    }
}
