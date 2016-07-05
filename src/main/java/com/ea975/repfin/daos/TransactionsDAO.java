package com.ea975.repfin.daos;

import com.ea975.repfin.components.Transactions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsDAO extends CrudRepository<Transactions, Integer> {

    @Query("FROM Transactions t WHERE t.republica.republica_id = :id")
    List<Transactions> findByRepublicaId(@Param("id") Integer republicaId);
}
