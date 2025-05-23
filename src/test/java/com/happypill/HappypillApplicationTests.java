package com.happypill;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HappypillApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void tt() {
        throw new RuntimeException();
    }

}
