package com.ea975.repfin.daos;

import com.ea975.repfin.components.Transactions;
import org.springframework.data.repository.CrudRepository;

public interface TransactionsDAO extends CrudRepository<Transactions, Integer> {
}
