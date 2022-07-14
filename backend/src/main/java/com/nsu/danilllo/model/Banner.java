package com.nsu.danilllo.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Banners")
@Getter
@Setter
public class Banner implements Comparable<Banner> {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;
    private String text;
    private Double price;
    private boolean deleted = Boolean.FALSE;
    @ManyToMany
    private List<Category> categories;

    @Override
    public int compareTo(Banner o) {
        if(Objects.equals(name, o.getName()) && Objects.equals(text, o.getText())) return 0;
        return price >= o.getPrice() ? 1 : -1;
    }
}
