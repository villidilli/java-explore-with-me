//package ru.practicum.server;
//
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import org.springframework.transaction.annotation.Transactional;
//import ru.practicum.server.model.EndpointHit;
//import ru.practicum.server.repository.StatRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@Transactional
//@DataJpaTest(properties = "spring.datasource.url = jdbc:h2:mem:test")
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//public class StatRepositoryTest {
//    @Autowired
//    private final StatRepository repository;
//
//    private EndpointHit hit1;
//    private EndpointHit hit2;
//    private EndpointHit hit3;
//    private EndpointHit hit4;
//    private EndpointHit hit5;
//
//    @BeforeEach
//    public void beforeEach() {
//        hit1 = new EndpointHit();
//        hit1.setUri("uri1");
//        hit1.setIp("1.1.1.1");
//        hit1.setTimestamp(LocalDateTime.of(2023, 1, 1, 0, 0));
//        hit1.setApp("app1");
//
//        hit2 = new EndpointHit();
//        hit2.setUri("uri1");
//        hit2.setIp("1.1.1.2");
//        hit2.setTimestamp(LocalDateTime.of(2023, 2, 1, 0, 0));
//        hit2.setApp("app1");
//
//        hit3 = new EndpointHit();
//        hit3.setUri("uri1");
//        hit3.setIp("1.1.1.1");
//        hit3.setTimestamp(LocalDateTime.of(2023, 3, 1, 0, 0));
//        hit3.setApp("app1");
//    }
//
//    @Test
//    public void s () {
//        repository.save(hit1);
//        repository.save(hit2);
//        repository.save(hit3);
//
//        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0 ,0);
//        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 0 ,0);
//
//        String[] uris = new String[] {hit1.getUri(), hit2.getUri(), hit3.getUri()};
//
//        List<EndpointHit> actual = repository.findAllByUriInAndTimestampBetween(uris, start, end);
//
//        assertNotNull(actual);
//    }
//}
