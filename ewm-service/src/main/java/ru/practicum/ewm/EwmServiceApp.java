package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.practicum.stats.client.HitSpy;

@SpringBootApplication(scanBasePackageClasses = {
        EwmServiceApp.class,
        HitSpy.class
})
public class EwmServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(EwmServiceApp.class, args);
    }
}