package com.ea975.repfin.components;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="transactions")
public class Transactions {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Integer transaction_id;

    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "total_value")
    private Float total_value;

    @ManyToOne
    @JoinColumn(name = "responsible_user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "republica_id")
    private Republicas republica;

    public Integer getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(Integer transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getTotal_value() {
        return total_value;
    }

    public void setTotal_value(Float total_value) {
        this.total_value = total_value;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Republicas getRepublica() {
        return republica;
    }

    public void setRepublica(Republicas republica) {
        this.republica = republica;
    }
}