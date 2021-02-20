package com.waes.diffservice.repositories;

import com.waes.diffservice.enitities.DiffData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiffRepository extends CrudRepository<DiffData, Long> {
}
