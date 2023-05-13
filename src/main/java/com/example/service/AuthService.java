package com.example.service;

import com.example.dto.auth.AuthDTO;
import com.example.dto.auth.AuthResponseDTO;
import com.example.dto.auth.RegistrationDTO;
import com.example.dto.auth.RegistrationResponseDTO;
import com.example.dto.profile.ProfileDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.GeneralStatus;
import com.example.enums.ProfileRole;
import com.example.exps.AppBadRequestException;
import com.example.exps.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.util.JwtUtil;
import com.example.util.MD5Util;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MailSenderService mailSenderService;

    public String register(@Valid ProfileDTO dto) {
        isValidProfile(dto);
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setRole(ProfileRole.USER);
        entity.setPassword(MD5Util.getMd5Hash(dto.getPassword()));
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setStatus(GeneralStatus.ACTIVE);
        profileRepository.save(entity);
        return "Successfully registered";

    }

    public void isValidProfile(ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndPasswordAndVisible(dto.getEmail(),
                dto.getPassword(),
                true);
        if (!optional.isEmpty()) {
            throw new AppBadRequestException("This profile already exist");
        }
    }
//    public RegistrationResponseDTO registration(RegistrationDTO dto) {
//        // check -?
//        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
//        if (optional.isPresent()) {
//            throw new ItemNotFoundException("Email already exists mazgi.");
//        }
//        ProfileEntity entity = new ProfileEntity();
//        entity.setName(dto.getName());
//        entity.setSurname(dto.getSurname());
//        entity.setRole(ProfileRole.USER);
//        entity.setPhone(dto.getPhone());
//        entity.setEmail(dto.getEmail());
//        entity.setPassword(MD5Util.getMd5Hash(dto.getPassword()));
//        entity.setStatus(GeneralStatus.REGISTER);
//        profileRepository.save(entity);
//        String s = "Verification link was send to email: " + dto.getEmail();
//        return new RegistrationResponseDTO(s);
//    }

    public AuthResponseDTO login(@Valid AuthDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndPasswordAndVisible(dto.getEmail(), MD5Util.getMd5Hash(dto.getPassword()), true);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Email or password incorrect");
        }
        ProfileEntity entity = optional.get();
        if (!entity.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new AppBadRequestException("Wrong status");
        }
        AuthResponseDTO responseDTO = new AuthResponseDTO();
        responseDTO.setName(entity.getName());
        responseDTO.setSurname(entity.getSurname());
        responseDTO.setRole(entity.getRole());
        responseDTO.setJwt(JwtUtil.encode(entity.getId(), entity.getRole()));
        return responseDTO;
    }

    public RegistrationResponseDTO registration(@Valid RegistrationDTO dto) {
        // TODO check -?
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            throw new ItemNotFoundException("Email already exists mazgi.");
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setRole(ProfileRole.USER);
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.getMd5Hash(dto.getPassword()));
        entity.setStatus(GeneralStatus.REGISTER);
        // send email
        mailSenderService.sendRegistrationEmailMime(dto.getEmail());
        // save
        profileRepository.save(entity);
        String s = "Verification link was send to email: " + dto.getEmail();
        return new RegistrationResponseDTO(s);
    }

    public RegistrationResponseDTO emailVerification(String jwt) {
        String email = JwtUtil.decodeEmailVerification(jwt);
        Optional<ProfileEntity> optional = profileRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Email not found.");
        }
        ProfileEntity entity = optional.get();
        if (!entity.getStatus().equals(GeneralStatus.REGISTER)) {
            throw new AppBadRequestException("Wrong status");
        }
        entity.setStatus(GeneralStatus.ACTIVE);
        profileRepository.save(entity);
        return new RegistrationResponseDTO("Registration Done");
    }

}
