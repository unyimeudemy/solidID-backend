package com.unyime.solidID.unitTests.services;

import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.domain.entities.StaffMemberEntity;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.repository.StaffMemberRepository;
import com.unyime.solidID.services.impl.JwtServiceImpl;
import com.unyime.solidID.services.impl.StaffMemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StaffMemberServiceImplUnitTest {


    @Mock
    private StaffMemberRepository staffMemberRepository;

    @Mock
    private JwtServiceImpl jwtServiceImpl;

    @InjectMocks
    private StaffMemberServiceImpl underTest;


    @Test
    public void testThatOrgCanAddANewMember(){
        String reqHeader = "Bearer jwtToken";
        StaffMemberEntity staffMemberEntity = TestDataUtility.createTestStaffMemberEntity();

        when(staffMemberRepository.save(staffMemberEntity)).thenReturn(staffMemberEntity);
        when(jwtServiceImpl.extractUsername(any(String.class)))
                .thenReturn(staffMemberEntity.getOrgEmail());

        Optional<StaffMemberEntity> result = underTest.addMember(reqHeader, staffMemberEntity);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(staffMemberEntity);
    }

    @Test
    public void testThatNullIsReturnedIfOrgAccDifferFromOrgInStaffMemberEntity(){
        String reqHeader = "Bearer jwtToken";
        StaffMemberEntity staffMemberEntity = TestDataUtility.createTestStaffMemberEntity();

        when(jwtServiceImpl.extractUsername(any(String.class)))
                .thenReturn("in correct email");

        Optional<StaffMemberEntity> result = underTest.addMember(reqHeader, staffMemberEntity);
        assertThat(result).isEmpty();
    }


    @Test
    public void testThatOrgCanGetAStaffByStaffEmail(){
        StaffMemberEntity staffMemberEntity = TestDataUtility.createTestStaffMemberEntity();

        when(staffMemberRepository.findByStaffEmail(staffMemberEntity.getStaffEmail()))
                .thenReturn(Optional.of(staffMemberEntity));

        Optional<StaffMemberEntity> result =
                underTest.getMember(staffMemberEntity.getStaffEmail(), staffMemberEntity.getOrgEmail());

        assertThat(result).isPresent();
        assertThat(result).isEqualTo(Optional.of(staffMemberEntity));
    }


    @Test
    public void testThatNullIsReturnedIfStaffIsNotRegisteredWithOrg(){
        StaffMemberEntity staffMemberEntity = TestDataUtility.createTestStaffMemberEntity();

        when(staffMemberRepository.findByStaffEmail(any(String.class)))
                .thenReturn(Optional.empty());

        Optional<StaffMemberEntity> result =
                underTest.getMember(staffMemberEntity.getStaffEmail(), staffMemberEntity.getOrgEmail());

        assertThat(result).isEmpty();
    }

//    @Override
//    public List<StaffMemberEntity> getMembers() {
//        return staffMemberRepository.findAll();
//    }

    @Test
    public void testThatOrgCanListOfAllStaffMembers(){
        StaffMemberEntity staffMemberEntity = TestDataUtility.createTestStaffMemberEntity();
        List<StaffMemberEntity> response = List.of(staffMemberEntity);
        String orgEmail = "";

        when(staffMemberRepository.findAll())
                .thenReturn(List.of(staffMemberEntity));

        List<StaffMemberEntity> result = underTest.getMembers(orgEmail);

        assertThat(result).isEqualTo(response);
    }

    @Test
    public void testThatEmptyListIsReturnedIfThereIsNoRegisteredStaffMember(){

        String orgEmail = "";
        when(staffMemberRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<StaffMemberEntity> result = underTest.getMembers(orgEmail);

        assertThat(result).isEmpty();
    }

}
