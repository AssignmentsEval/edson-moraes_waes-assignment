package com.waes.diffservice;

import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

class KarateTests {

    @BeforeAll
    static void classSetUp(){
        DiffServiceApplication.main(new String[0]);
    }

    @AfterAll
    static void classTearDown(){

    }

    @Karate.Test
    Karate testAll() {
        return Karate.run().feature("src/test/resources/Diff.feature");
    }
}
