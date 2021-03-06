package com.ea975.repfin.daos;

import com.ea975.repfin.components.Republicas;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepublicasDAO extends CrudRepository<Republicas, Integer> {

    @Query("FROM Republicas r WHERE r.name = :name")
    Republicas findByName(@Param("name") String name);
}
