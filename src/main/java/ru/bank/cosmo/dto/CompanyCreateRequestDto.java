package ru.bank.cosmo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyCreateRequestDto {

    private String name;

    private String about;

    private Boolean isActive;

    private Boolean isReliability;

    private String vkLink;

    private String websiteLink;

    private String ogrn;

    private String inn;

    private String kpp;

    private String okpo;

    private String legalAddress;
}