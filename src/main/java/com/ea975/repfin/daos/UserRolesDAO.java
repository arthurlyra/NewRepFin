package com.ea975.repfin.daos;

import com.ea975.repfin.components.UserRoles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesDAO extends CrudRepository<UserRoles, Integer>{

    @Query("FROM UserRoles u WHERE u.user.user_id = :id")
    UserRoles findByUserId(@Param("id") Integer user_id);
}
