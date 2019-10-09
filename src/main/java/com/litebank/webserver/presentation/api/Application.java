package com.litebank.webserver.presentation.api;

import com.litebank.webserver.presentation.api.configuration.Path;
import com.litebank.webserver.presentation.api.di.DI;
import io.javalin.Javalin;

public class Application {
    public static void main(String[] args) {
        init(7000);
    }

    public static Javalin init(int port) {
        var moneyTransferEventsConsumer = DI.getInstance().getMoneyTransferEventsConsumer();
        moneyTransferEventsConsumer.initialize();

        Javalin app = Javalin.create().start(port);

        app.post(Path.TRANSFERS, DI.getInstance().getMoneyTransfersController().startTransfer);

        app.post(Path.ACCOUNTS, DI.getInstance().getAccountsController().openAccount);

        return app;
    }
}
