package com.ea975.repfin.components;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="republicas")
public class Republicas {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer republica_id;

    @NotNull
    @Column(name = "name")
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