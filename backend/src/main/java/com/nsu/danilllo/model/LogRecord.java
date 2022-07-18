package com.nsu.danilllo.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table
@Entity
@Getter
@Setter
public class LogRecord {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ip;
    private String userAgent;
    private Date requestedAt;
    @OneToOne
    private Banner selectedBanner;
    @ManyToMany
    private List<Category> selectedCategories;
    private Double selectedBannerPrice;
    private String reason;
}
