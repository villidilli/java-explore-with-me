package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Service
public class TestService {

    private final StatServiceClient client;

    @Autowired
    public TestService(StatServiceClient client) {
        this.client = client;
    }

    public static void main(String[] args) {

    }

    public void post() {
        String app = "app";
        String uri = "uri";
        String ip = "ip";
        LocalDateTime date = LocalDateTime.now();



    }

}