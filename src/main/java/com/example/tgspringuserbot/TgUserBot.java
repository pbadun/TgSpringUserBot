package com.example.tgspringuserbot;

import it.tdlight.Init;
import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class TgUserBot {

    private SimpleTelegramClient client;

    @Value("${tg.api-id}")
    private Integer apiId;

    @Value("${tg.api-key}")
    private String apiKey;

    @Value("${tg.phone}")
    private String phone;

    @PostConstruct
    public void init() {

        try {
            main();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void main() throws Exception {
        Init.init();
        SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory();
        var apiToken = new APIToken(apiId, apiKey);
        TDLibSettings settings = TDLibSettings.create(apiToken);
        Path sessionPath = Paths.get("example-tdlight-session");
        settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
        settings.setDownloadedFilesDirectoryPath(sessionPath.resolve("downloads"));
        SimpleTelegramClientBuilder clientBuilder = clientFactory.builder(settings);
        SimpleAuthenticationSupplier<?> authenticationData = AuthenticationSupplier.user(phone);
        clientBuilder.addUpdateHandler(TdApi.UpdateAuthorizationState.class, this::onUpdateAuthorizationState);
        clientBuilder.addUpdateHandler(TdApi.UpdateNewMessage.class, this::onUpdateNewMessage);
        this.client = clientBuilder.build(authenticationData);
    }

    private void onUpdateAuthorizationState(TdApi.UpdateAuthorizationState update) {
        System.out.println(update.toString());
    }

    private void onUpdateNewMessage(TdApi.UpdateNewMessage update) {
        System.out.println(update.toString());
    }

}
