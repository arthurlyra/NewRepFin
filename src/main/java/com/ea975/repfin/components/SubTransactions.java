package com.ea975.repfin.components;



import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "subTransactions")
public class SubTransactions {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column(name = "subTransactions_id")
    private Integer id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "transaction_id")
    private Transactions transaction;

    @NotNull
    @Column(name = "value")
    private Float value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Transactions getTransaction() {
        return transaction;
    }

    public void setTransaction(Transactions transaction) {
        this.transaction = transaction;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}