import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateTest {

    @Test
    void test01() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        now.plus(Duration.ofDays(3));
        System.out.println(now);
    }

}
