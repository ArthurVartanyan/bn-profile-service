package ru.bank.cosmo.controller;

import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bank.cosmo.dto.CompanyCreateRequestDto;
import ru.bank.cosmo.dto.CompanyResponseDto;
import ru.bank.cosmo.service.CompanyService;

import java.util.Map;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(@RequestBody CompanyCreateRequestDto request) {
        CompanyResponseDto response = companyService.createCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/{companyId}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadLogo(@PathVariable Long companyId,
                                                          @RequestPart("file") MultipartFile file) {
        return new ResponseEntity<>(companyService.uploadLogo(companyId, file), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{companyId}/logo")
    public ResponseEntity<Map<String, String>> deleteAvatar(@PathVariable Long companyId) {
        return new ResponseEntity<>(companyService.deleteLogo(companyId), HttpStatus.OK);
    }
}