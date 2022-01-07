package com.fundamentos.springboot.fundamentos.entity;

import javax.persistence.*;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_post")
    private Long id;


}
