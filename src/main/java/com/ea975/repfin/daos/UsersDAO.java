package com.ea975.repfin.daos;

import com.ea975.repfin.components.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersDAO extends CrudRepository<Users,Integer> {

    @Query("FROM Users u WHERE u.republica.republica_id = :id")
    List<Users> findByRepublicaId(@Param("id") Integer republicaId);

    @Query("SELECT u.user_id FROM Users u WHERE u.name = :name")
    Integer findByName(@Param("name") String name);
}
