package com.waes.diffservice.enitities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class EqualsAndHashCodeContractTest {

    @Test
    public void diffData() {
        EqualsVerifier.forClass(DiffData.class).verify();
    }
}