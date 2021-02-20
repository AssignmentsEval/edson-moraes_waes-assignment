package karate.diff;

import com.intuit.karate.junit5.Karate;

public class DiffRunner {

    @Karate.Test
    Karate testAll() {
        return Karate.run().relativeTo(this.getClass());
    }
}
