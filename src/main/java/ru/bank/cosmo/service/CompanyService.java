package ru.bank.cosmo.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bank.cosmo.dto.CompanyCreateRequestDto;
import ru.bank.cosmo.dto.CompanyResponseDto;
import ru.bank.cosmo.exception.CompanyNotFoundException;
import ru.bank.cosmo.model.Company;
import ru.bank.cosmo.repository.CompanyRepository;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    @Value("${minio.bucket}")
    private String bucket;

    private final MinioClient minio;
    private final CompanyRepository companyRepository;

    public CompanyResponseDto createCompany(CompanyCreateRequestDto request) {
        Company company = getCompany(request);

        Company saved = companyRepository.save(company);

        return new CompanyResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getAbout(),
                saved.isActive(),
                saved.isReliability(),
                saved.getVkLink(),
                saved.getWebsiteLink(),
                saved.getOgrn(),
                saved.getInn(),
                saved.getKpp(),
                saved.getOkpo(),
                saved.getLegalAddress()
        );
    }

    @SneakyThrows
    public Map<String, String> uploadLogo(Long companyId, MultipartFile file) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        String objectKey = "logo/companies/" + companyId + "/" +
                UUID.randomUUID() + "-" + file.getOriginalFilename();

        minio.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectKey)
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build()
        );

        company.setLogoPath(objectKey);
        companyRepository.save(company);

        return Map.of(
                "companyId", companyId.toString(),
                "objectKey", objectKey
        );
    }

    @SneakyThrows
    public Map<String, String> deleteLogo(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        minio.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucket)
                        .object(company.getLogoPath())
                        .build()
        );
            company.setLogoPath(null);
            companyRepository.save(company);


        return Map.of(
                "status", "deleted",
                "companyId", companyId.toString()
        );
    }

    public InputStream getLogoAsStream(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        if (company.getLogoPath() == null) {
            throw new IllegalStateException("У компании id=" + companyId + " нет логотипа");
        }

        try {
            return minio.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(company.getLogoPath())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении логотипа из MinIO", e);
        }
    }

    public String getLogoContentType(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        return company.getLogoPath() != null ? "image/png" : "application/octet-stream";
        // тут можно хранить contentType в базе при загрузке и возвращать его
    }

    private static @NotNull Company getCompany(CompanyCreateRequestDto request) {
        Company company = new Company();

        company.setName(request.getName());
        company.setAbout(request.getAbout());
        company.setActive(request.getIsActive());
        company.setReliability(request.getIsReliability());
        company.setVkLink(request.getVkLink());
        company.setWebsiteLink(request.getWebsiteLink());
        company.setOgrn(request.getOgrn());
        company.setInn(request.getInn());
        company.setKpp(request.getKpp());
        company.setOkpo(request.getOkpo());
        company.setLegalAddress(request.getLegalAddress());
        return company;
    }
}