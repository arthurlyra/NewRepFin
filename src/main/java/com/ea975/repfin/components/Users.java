package com.ea975.repfin.components;

import com.ea975.repfin.components.Republicas;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table (name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    private Integer user_id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "balance")
    private String balance;

    @NotNull
    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "republica_id")
    private Republicas republica;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Republicas getRepublica() {
        return republica;
    }

    public void setRepublica(Republicas republica) {
        this.republica = republica;
    }
}