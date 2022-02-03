package com.example.demo.service;

import com.example.demo.Exceptions.UserException;
import com.example.demo.Utlility.PasswordValidator;
import com.example.demo.dto.users.core.UserLoginDto;
import com.example.demo.dto.users.core.UserOutputDto;
import com.example.demo.dto.users.technicin.input.RegisterTechnicianInputDto;
import com.example.demo.model.Technician;
import com.example.demo.model.enums.RegisterStatus;
import com.example.demo.repository.TechnicianRepository;
import com.example.demo.service.core.AbstractCRUD;
import com.example.demo.service.core.UsersConvertor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TechnicianService extends AbstractCRUD<Technician, Integer>
        implements UsersConvertor<Technician, RegisterTechnicianInputDto> {


    private final TechnicianRepository technicianRepository;
    private final PasswordValidator passwordValidator;

    @PostConstruct
    public void init() {
        setJpaRepository(technicianRepository);
    }

    @Transactional
    public UserOutputDto addTechnician(RegisterTechnicianInputDto technicianInputDto) {
        if (!emailAndPasswordIsValid(technicianInputDto)) {
            log.error("Unable to save technician with the username {}", technicianInputDto.getUsername());
            throw new UserException("Unable to save the technician");
        }
        log.debug("the  user with username {} saved to the database.", technicianInputDto.getUsername());
        Technician savedTechnician = super.save(convertInputToEntity(technicianInputDto));
        return new UserOutputDto(savedTechnician.getId(), savedTechnician.getFirstName(),
                savedTechnician.getLastName());
    }

    @Transactional
    public void updatePassword(UserLoginDto customerLoginDto) {
        if (findTechByUsernameAndPassword(customerLoginDto) == null
                || !passwordValidator.passwordChecker(customerLoginDto.getNewPassword())) {
            log.error("Unable to change password for the user with id {}", customerLoginDto.getId());
            throw new UserException("Unable to change the password!");
        }
        if (customerLoginDto.getNewPassword().equals(customerLoginDto.getOldPassword())) {
            log.error("Duplicated input password for the user with id {} ", customerLoginDto.getId());
            throw new UserException("Duplicated password");
        }
        Technician technician = super.loadById(customerLoginDto.getId());
        technician.setPassword(customerLoginDto.getNewPassword());
        super.update(technician);
    }



    public Technician findTechByUsernameAndPassword(UserLoginDto customerLoginDto) {
        return technicianRepository
                .findTechnicianByUsernameAndPassword(customerLoginDto.getUsername()
                        , customerLoginDto.getOldPassword());
    }


    public boolean emailAndPasswordIsValid(RegisterTechnicianInputDto technicianInputDto) {
        return (emailIsAcceptable(technicianInputDto) &&
                passwordValidator.passwordChecker(technicianInputDto.getPassword()));
    }

    public boolean emailIsAcceptable(RegisterTechnicianInputDto technicianInputDto) {
        return loadTechsByByEmail(technicianInputDto.getEmail()) == null;
    }

    public List<Technician> filterTechniciansByRegisterDate(Date date) {
        return technicianRepository.findTechniciansByRegisterDateAfter(date);
    }

    public List<Technician> filterTechniciansByRegStatus(RegisterStatus status) {
        return technicianRepository.findTechniciansByStatus(status);
    }

    public List<Technician> listTechsOrderByPoints() {
        return technicianRepository.findAll(Sort.by(Sort.Direction.ASC, "points"));
    }

    public List<UserOutputDto> loadTechsByFirstName(String firstName) {
        List<UserOutputDto> submittedTechsList = new ArrayList<>();
        for (Technician technician : technicianRepository.findTechnicianByFirstName(firstName)) {
            UserOutputDto userOutputDto = convertEntityToOutputDto(technician);
            submittedTechsList.add(userOutputDto);
        }
        return submittedTechsList;
    }

    public List<UserOutputDto> loadTechsByByLastname(String lastName) {
        List<UserOutputDto> submittedTechsList = new ArrayList<>();
        for (Technician technician : technicianRepository.findTechnicianByFirstName(lastName)) {
            UserOutputDto userOutputDto = convertEntityToOutputDto(technician);
            submittedTechsList.add(userOutputDto);
        }
        return submittedTechsList;
    }

    public Technician loadTechsByByEmail(String email) {
        return technicianRepository.findTechnicianByEmail(email);
    }

    public UserOutputDto filterTechsByEmail(String email) {
        return convertEntityToOutputDto(technicianRepository.findTechnicianByEmail(email));
    }


    public List<UserOutputDto> loadTechsWithPointsGreaterThanLimit(Double point) {
        List<UserOutputDto> submittedTechsList = new ArrayList<>();
        for (Technician technician : technicianRepository.findTechnicianByAveragePointGreaterThan(point)) {
            UserOutputDto userOutputDto = convertEntityToOutputDto(technician);
            submittedTechsList.add(userOutputDto);
        }
        return submittedTechsList;
    }


    @Override
    public UserOutputDto convertEntityToOutputDto(Technician technician) {
        return UserOutputDto.builder()
                .id(technician.getId())
                .firstName(technician.getFirstName())
                .lastname(technician.getLastName())
                .build();
    }

    public UserOutputDto convertLoginToOutputDto(UserLoginDto loginDto) {
        return UserOutputDto.builder()
                .id(loginDto.getId())
                .firstName(loginDto.getFirstName())
                .lastname(loginDto.getLastname())
                .build();
    }

    @Override
    public Technician convertInputToEntity(RegisterTechnicianInputDto technicianInputDto) {
        return Technician.builder()
                .firstName(technicianInputDto.getFirstName())
                .lastName(technicianInputDto.getLastName())
                .username(technicianInputDto.getUsername())
                .password(technicianInputDto.getPassword())
                .email(technicianInputDto.getEmail())
                .registerDate(new Date(System.currentTimeMillis()))
                .status(RegisterStatus.NEW)
                .credit(0d)
                .build();
    }



}
