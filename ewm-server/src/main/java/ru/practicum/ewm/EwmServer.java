package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.practicum.stats.client.HitSpy;

@SpringBootApplication(scanBasePackageClasses = {
        EwmServer.class,
        HitSpy.class
})
public class EwmServer {
    public static void main(String[] args) {
        SpringApplication.run(EwmServer.class, args);
    }
}