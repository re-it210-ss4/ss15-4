package org.example.demoss12maven;

import org.example.demoss12maven.service.BankingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(excludeName = {"org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"})
public class Ss14ThApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Ss14ThApplication.class, args);
        BankingService bankingService = context.getBean(BankingService.class);

        try {
            bankingService.testCache(1L);
        } catch (Exception e) {
            System.err.println("Chưa có dữ liệu ID=1 trong DB để test cache!");
        }
    }
}