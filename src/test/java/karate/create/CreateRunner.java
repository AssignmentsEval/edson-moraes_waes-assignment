package karate.create;

import com.intuit.karate.junit5.Karate;

public class CreateRunner {

    @Karate.Test
    Karate testAll() {
        return Karate.run().relativeTo(this.getClass());
    }
}
