package ru.sahlob.vk;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import java.util.concurrent.Executors;

public class VKServer {

    public static VKCore vkCore;

    static {
        try {
            vkCore = new VKCore();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public void run() throws NullPointerException, ApiException, InterruptedException {

        System.out.println("Running server...");
        while (true) {
            Thread.sleep(300);
            try {
                var message = vkCore.getMessage();
                if (message != null) {
                    System.out.println(message);
                    var exec = Executors.newCachedThreadPool();
                    exec.execute(new Messenger(message));
                }
            } catch (ClientException e) {
                System.out.println("Возникли проблемы");
                final int RECONNECT_TIME = 10000;
                System.out.println("Повторное соединение через " + RECONNECT_TIME / 1000 + " секунд");
                Thread.sleep(RECONNECT_TIME);

            }
        }
    }

//    public static void main(String[] args) throws InterruptedException {
//        System.out.println("Running server...");
//        while (true) {
//            Thread.sleep(300);
//            try {
//                var message = vkCore.getMessage();
//                if (message != null) {
//                    System.out.println(message);
//                    var exec = Executors.newCachedThreadPool();
//                    exec.execute(new Messenger(message));
//                }
//            } catch (ClientException e) {
//                System.out.println("Возникли проблемы");
//                final int RECONNECT_TIME = 10000;
//                System.out.println("Повторное соединение через " + RECONNECT_TIME / 1000 + " секунд");
//                Thread.sleep(RECONNECT_TIME);
//
//            } catch (ApiException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
