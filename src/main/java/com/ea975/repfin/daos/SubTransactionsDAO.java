package com.ea975.repfin.daos;

import com.ea975.repfin.components.SubTransactions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubTransactionsDAO extends CrudRepository<SubTransactions, Integer> {
}
