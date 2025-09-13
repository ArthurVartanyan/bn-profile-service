package ru.bank.cosmo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "company", schema = "public")
@Getter
@Setter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "about")
    private String about;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_reliability")
    private boolean isReliability;

    @Column(name = "vk_link")
    private String vkLink;

    @Column(name = "website_link")
    private String websiteLink;

    @Column(name = "ogrn")
    private String ogrn;

    @Column(name = "inn")
    private String inn;

    @Column(name = "kpp")
    private String kpp;

    @Column(name = "okpo")
    private String okpo;

    @Column(name = "legal_address")
    private String legalAddress;

    @Column(name = "logo_path")
    private String logoPath;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Contact> contacts;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Product> products;
}