package com.ea975.repfin.components;


import javax.jws.soap.SOAPBinding;
import javax.persistence.*;

@Entity
@Table(name="transactions")
public class Transactions {

    protected Transactions(){};

    public Transactions(String description, Float total_value, Users user, Republicas republica){
        this.description = description;
        this.total_value = total_value;
        this.user = user;
        this.republica = republica;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="transaction_id")
    private Integer transaction_id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "total_value", nullable = false)
    private Float total_value;

    @ManyToOne
    @JoinColumn(name = "responsible_user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "republica_id", nullable = false)
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