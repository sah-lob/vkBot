package ru.sahlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.sahlob.vk.VKServer;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private VKServer vkServer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        vkServer.run();
    }
}