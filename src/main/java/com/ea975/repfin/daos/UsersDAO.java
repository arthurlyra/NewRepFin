package com.ea975.repfin.daos;

import com.ea975.repfin.components.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersDAO extends CrudRepository<Users,Integer> {

    @Query("SELECT * FROM users u WHERE u.republica_id = :id")
    List<Users> findByRepublicaId(@Param("id") Integer republicaId);
}
