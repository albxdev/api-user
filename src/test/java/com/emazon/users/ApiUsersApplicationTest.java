package com.emazon.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:/test-data.sql")
@SpringBootTest

public class ApiUsersApplicationTest {

    @Test
    void contextLoads() {
    }

}
