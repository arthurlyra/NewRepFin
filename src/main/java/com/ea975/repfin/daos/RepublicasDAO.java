package com.ea975.repfin.daos;

import com.ea975.repfin.components.Republicas;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepublicasDAO extends CrudRepository<Republicas, Integer> {

}
