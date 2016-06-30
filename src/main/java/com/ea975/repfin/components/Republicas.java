package com.ea975.repfin.components;

import javax.persistence.*;

@Entity
@Table(name="republicas")
public class Republicas {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer republica_id;

    @Column(name = "name", nullable = false)
    private String name;

    public Integer getRepublica_id() {
        return republica_id;
    }

    public void setRepublica_id(Integer republica_id) {
        this.republica_id = republica_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}