package com.example.advcontrol.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String raisonSociale;
    private String ice;
    private String rc;
    private String ifNumber;
    private String cnss;
    private String email;
    private String phone;
    private String city;
    private String address;

    private Boolean active = true;
}
