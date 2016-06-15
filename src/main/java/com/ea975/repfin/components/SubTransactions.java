package com.ea975.repfin.components;



import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "subtransactions")
public class SubTransactions {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column(name = "subTransaction_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transactions transaction;

    @Column(name = "value", nullable = false)
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