package com.ea975.repfin.components;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users_transactions")
public class UsersTransactions {

    @Id
    @JoinColumn(name = "user_id")
    private Users user;

    @Id
    @JoinColumn(name = "transaction_id")
    private Transactions transaction;

    @NotNull
    @Column(name = "value")
    private Float value;


}