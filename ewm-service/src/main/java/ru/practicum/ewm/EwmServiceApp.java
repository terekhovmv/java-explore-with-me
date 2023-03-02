package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.practicum.stats.client.HitSpy;

@SpringBootApplication(scanBasePackageClasses = {
        EwmServiceApp.class,
        HitSpy.class
})
@EnableJpaAuditing
@EnableScheduling
public class EwmServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(EwmServiceApp.class, args);
    }
}