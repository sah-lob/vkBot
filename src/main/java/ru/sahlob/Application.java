package ru.sahlob;

import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.sahlob.vk.VKServer;

@SpringBootApplication
@Data
public class Application implements CommandLineRunner {

    private final VKServer vkServer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        vkServer.run();
    }
}