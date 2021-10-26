package com.skillbox.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "global_settings")
@Data
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer setting;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "value", nullable = false)
    private String value;
}
