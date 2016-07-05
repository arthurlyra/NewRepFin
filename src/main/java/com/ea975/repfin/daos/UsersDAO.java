package com.ea975.repfin.daos;

import com.ea975.repfin.components.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersDAO extends CrudRepository<Users,Integer> {

    @Query("FROM Users u WHERE u.republica.republica_id = :id AND u.status = 2")
    List<Users> findByRepublicaId(@Param("id") Integer republicaId);

    @Query("FROM Users u WHERE u.name = :name")
    Users findByName(@Param("name") String name);

    @Query("FROM Users u WHERE u.status = 1 AND u.republica.republica_id = :id")
    List<Users> findPendingUsersOfRepublica(@Param("id") Integer republicaId);
}
