package com.waes.diffservice.enitities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "DIFF_DATA")

public final class DiffData {

    @Id
    private Long id;

    private String leftJson;

    private String rightJson;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiffData diffData = (DiffData) o;
        return Objects.equals(id, diffData.id) && Objects.equals(leftJson, diffData.leftJson) && Objects.equals(rightJson, diffData.rightJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, leftJson, rightJson);
    }
}
