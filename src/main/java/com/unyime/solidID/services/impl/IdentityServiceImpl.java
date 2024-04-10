package com.unyime.solidID.services.impl;

import com.unyime.solidID.domain.VerificationResponse;
import com.unyime.solidID.domain.dto.ErrorResponseDto;
import com.unyime.solidID.domain.entities.IdentityURLEntity;
import com.unyime.solidID.domain.entities.IdentityUsageRecordEntity;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.domain.entities.UserOrganizationEntity;
import com.unyime.solidID.repository.IdentityRepository;
import com.unyime.solidID.repository.IdentityUsageRecordRepository;
import com.unyime.solidID.repository.UserOrganizationRepository;
import com.unyime.solidID.repository.UserRepository;
import com.unyime.solidID.services.IdentityService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class IdentityServiceImpl implements IdentityService {

    private final IdentityRepository identityRepository;

    private final UserRepository userRepository;

    private final UserOrganizationRepository userOrganizationRepository;

    private final IdentityUsageRecordRepository identityUsageRecordRepository;

    public IdentityServiceImpl(
            IdentityRepository identityRepository,
            UserRepository userRepository,
            UserOrganizationRepository userOrganizationRepository,
            IdentityUsageRecordRepository identityUsageRecordRepository
    ) {
        this.identityRepository = identityRepository;
        this.userRepository = userRepository;
        this.userOrganizationRepository = userOrganizationRepository;
        this.identityUsageRecordRepository = identityUsageRecordRepository;
    }

    @Override
    public String generate(String currentUserEmail, String orgEmail) {
        Random random = new Random();
        int key = random.nextInt(1000000);
        String keyStr = String.valueOf(key);

        IdentityURLEntity identityURLEntity = IdentityURLEntity.builder()
                .encodedEmail(currentUserEmail)
                .key(keyStr)
                .orgEmail(orgEmail)
                .build();

        IdentityURLEntity savedUrl = identityRepository.save(identityURLEntity);
        return savedUrl.getKey();
    }

    @Override
    public Optional<VerificationResponse> verify(String currentUserEmail, String key) {
        Optional<IdentityURLEntity> identityURLEntity = identityRepository.findByKey(key);
        identityRepository.deleteByKey(key);

        if(identityURLEntity.isPresent()){
            String userEmail = identityURLEntity.get().getEncodedEmail();
            String orgEmail = identityURLEntity.get().getOrgEmail();
            if(!identityURLEntity.get().getOrgEmail().equals("Profile")){
                Optional<UserOrganizationEntity> staffUser = userOrganizationRepository.
                        findByUserEmailAndOrgEmail(userEmail, orgEmail);
                keepRecordOfIdentityVerification(currentUserEmail, userEmail);
                    return Optional.of(
                            VerificationResponse.builder()
                                    .firstName(staffUser.get().getStaffName())
                                    .staffRole(staffUser.get().getStaffRole())
                                    .orgName(staffUser.get().getOrgName())
                                    .staffId(staffUser.get().getStaffId())
                                    .build()
                    );
            }else{
                Optional<UserEntity> verifiedUser = userRepository.findByEmail(userEmail);
                keepRecordOfIdentityVerification(currentUserEmail, verifiedUser.get().getEmail());
                return Optional.of(
                        VerificationResponse.builder()
                                .firstName(verifiedUser.get().getFirstName())
                                .lastName(verifiedUser.get().getLastName())
                                .otherName(verifiedUser.get().getOtherName())
                                .stateOfOrigin(verifiedUser.get().getStateOfOrigin())
                                .email(verifiedUser.get().getEmail())
                                .image(verifiedUser.get().getImage())
                                .nationality(verifiedUser.get().getNationality())
                                .build()
                );
            }
        }else{
            return Optional.empty();
        }
    }

    private void keepRecordOfIdentityVerification(String currentUserEmail, String  userEmail){
        IdentityUsageRecordEntity newRecord = IdentityUsageRecordEntity.builder()
                .currentUserEmail(currentUserEmail)
                .userVerifiedEmail(userEmail)
                .date(LocalDateTime.now())
                .build();

        identityUsageRecordRepository.save(newRecord);
    }
}
