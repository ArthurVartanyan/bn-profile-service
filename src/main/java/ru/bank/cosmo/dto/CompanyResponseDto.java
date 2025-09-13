package ru.bank.cosmo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyResponseDto {

    private Long id;

    private String name;

    private String about;

    private boolean isActive;

    private boolean isReliability;

    private String vkLink;

    private String websiteLink;

    private String ogrn;

    private String inn;

    private String kpp;

    private String okpo;

    private String legalAddress;
}
