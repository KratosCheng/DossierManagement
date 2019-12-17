package edu.njusoftware.dossiermanagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("edu.njusoftware.dossiermanagement.mapper")
public class DossierManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(DossierManagementApplication.class, args);
    }

}
