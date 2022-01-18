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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianService extends AbstractCRUD<Technician,Integer> implements UsersConvertor<Technician, RegisterTechnicianInputDto> {


    private final TechnicianRepository technicianRepository;
    private final PasswordValidator passwordValidator;

    @PostConstruct
    public void init(){
        setJpaRepository(technicianRepository);
    }

    @Transactional
    public UserOutputDto addTechnician(RegisterTechnicianInputDto technicianInputDto) {
        if (emailAndPasswordIsValid(technicianInputDto)) {
            Technician technician = convertInputToEntity(technicianInputDto);
            super.save(technician);
            return new UserOutputDto(technician.getId(), technician.getFirstName(), technician.getLastName());
        } else
            throw new UserException("Unable to save the Technician");
    }

    @Transactional
    public void updatePassword(UserLoginDto technicianLoginDto, String newPassword) {
        if (findTechByUsernameAndPassword(technicianLoginDto) != null
                && passwordValidator.passwordChecker(newPassword)) {
            Technician technician = super.loadById(technicianLoginDto.getId());
            technician.setPassword(newPassword);
        } else
            throw new UserException("Unable to change the password!");
    }

    public Technician findTechByUsernameAndPassword(UserLoginDto customerLoginDto) {
        return technicianRepository
                .findTechnicianByUsernameAndPassword(customerLoginDto.getUsername()
                        , customerLoginDto.getPassword());
    }


    public boolean emailAndPasswordIsValid(RegisterTechnicianInputDto technicianInputDto) {
        return (emailIsAcceptable(technicianInputDto) &&
                passwordValidator.passwordChecker(technicianInputDto.getPassword()));
    }

    public boolean emailIsAcceptable(RegisterTechnicianInputDto technicianInputDto) {
        return loadTechsByByEmail(technicianInputDto.getEmail()) == null;
    }

    public List<Technician> filterTechniciansByRegisterDate(Date date){
        return technicianRepository.findTechniciansByRegisterDate(date);
    }

    public List<Technician> filterTechniciansByRegStatus(RegisterStatus status){
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

    public UserOutputDto filterTechsByEmail(String email){
        return convertEntityToOutputDto(technicianRepository.findTechnicianByEmail(email));
    }


    public List<UserOutputDto> loadTechsWithPointsGreaterThanLimit(Integer point){
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
