package com.nikita.todo;

import com.nikita.todo.business.service.impl.TaskServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner (TaskServiceImpl service){
//        return args -> {
//          service.createTask();
//        };
//    }

}
