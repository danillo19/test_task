package com.nsu.danilllo.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Categories")
@Getter
@Setter
public class Category {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String requestedID;
    private boolean deleted = Boolean.FALSE;

}
