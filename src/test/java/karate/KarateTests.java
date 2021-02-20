package karate;

import com.intuit.karate.junit5.Karate;
import com.waes.diffservice.DiffServiceApplication;
import org.junit.jupiter.api.BeforeAll;

class KarateTests {

    @BeforeAll
    static void classSetUp() {
        DiffServiceApplication.main(new String[0]);
    }

    @Karate.Test
    Karate testAll() {
        return Karate.run().relativeTo(this.getClass());
    }
}
