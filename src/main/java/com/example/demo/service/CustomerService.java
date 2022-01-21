package com.example.demo.service;

import com.example.demo.Exceptions.UserException;
import com.example.demo.Utlility.PasswordValidator;
import com.example.demo.dto.technicianpoint.OutPutPointDto;
import com.example.demo.dto.users.core.UserOutputDto;
import com.example.demo.dto.users.core.UserLoginDto;
import com.example.demo.dto.users.customer.input.RegisterCustomerInputDto;
import com.example.demo.model.Customer;
import com.example.demo.model.Technician;
import com.example.demo.model.TechnicianPoint;
import com.example.demo.model.enums.RegisterStatus;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.core.AbstractCRUD;

import com.example.demo.service.core.UsersConvertor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService extends AbstractCRUD<Customer, Integer> implements UsersConvertor<Customer, RegisterCustomerInputDto> {

    private final CustomerRepository customerRepository;
    private final PasswordValidator passwordValidator;
    private final TechnicianService technicianService;


    @PostConstruct
    public void init() {
        setJpaRepository(customerRepository);
    }

    @Transactional
    public UserOutputDto addCustomer(RegisterCustomerInputDto customerInputDto) {
        if (emailAndPasswordIsValid(customerInputDto)) {
            Customer savedCustomer = super.save(convertInputToEntity(customerInputDto));
            return new UserOutputDto(savedCustomer.getId(), savedCustomer.getFirstName(),
                    savedCustomer.getLastName());
        } else
            throw new UserException("Unable to save the Customer");
    }


    @Transactional
    public void updatePassword(UserLoginDto customerLoginDto, String newPassword) {

        if (findCustomerByUsernameAndPassword(customerLoginDto) != null
                && passwordValidator.passwordChecker(newPassword)) {
            Customer customer = super.loadById(customerLoginDto.getId());
            customer.setPassword(newPassword);
        } else
            throw new UserException("Unable to change the password!");
    }

    public Customer findCustomerByUsernameAndPassword(UserLoginDto customerLoginDto) {
        return customerRepository
                .findCustomerByUsernameAndPassword(customerLoginDto.getUsername()
                        , customerLoginDto.getPassword());
    }

    public List<UserOutputDto> loadCustomerByFirstName(String firstName) {
        List<UserOutputDto> submittedCustomerList = new ArrayList<>();
        for (Customer customer : customerRepository.findCustomerByFirstName(firstName)) {
            UserOutputDto userOutputDto = convertEntityToOutputDto(customer);
            submittedCustomerList.add(userOutputDto);
        }
        return submittedCustomerList;
    }

    public List<UserOutputDto> loadCustomerByLastname(String lastName) {
        List<UserOutputDto> submittedCustomerList = new ArrayList<>();
        for (Customer customer : customerRepository.findCustomerByFirstName(lastName)) {
            UserOutputDto userOutputDto = convertEntityToOutputDto(customer);
            submittedCustomerList.add(userOutputDto);
        }
        return submittedCustomerList;
    }

    public boolean emailAndPasswordIsValid(RegisterCustomerInputDto customerInputDto) {
        return (emailIsAcceptable(customerInputDto) &&
                passwordValidator.passwordChecker(customerInputDto.getPassword()));
    }

    public boolean emailIsAcceptable(RegisterCustomerInputDto customerInputDto) {
        return loadCustomerByEmail(customerInputDto.getEmail()) == null;
    }

    public Customer loadCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    public List<Customer> filterCustomersByRegisterDate(Date date){
        return customerRepository.findCustomersByRegisterDateAfter(date);
    }

    public UserOutputDto filterCustomerByEmail(String email) {
        return convertEntityToOutputDto(customerRepository.findCustomerByEmail(email));
    }

    @Transactional
    public OutPutPointDto setPointForTheTechnicians(TechnicianPoint technicianPoint) {
        Technician technician = technicianPoint.getTechnician();
        Double totalSum = 0.0;
        List<TechnicianPoint> technicianPoints = technician.getTechnicianPoints();
        technicianPoints.add(technicianPoint);
        for (TechnicianPoint point : technicianPoints) {
            totalSum += point.getPoint();
        }
        technician.setAveragePoint(totalSum / technicianPoints.size());
        technicianService.update(technician);
        return convertInputToOutputPoint(technician);
    }


    @Override
    public UserOutputDto convertEntityToOutputDto(Customer customer) {
        return UserOutputDto.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastname(customer.getLastName())
                .build();
    }


    public Customer convertInputToEntity(RegisterCustomerInputDto customerInputDto) {
        return Customer.builder()
                .firstName(customerInputDto.getFirstName())
                .lastName(customerInputDto.getLastName())
                .username(customerInputDto.getUsername())
                .password(customerInputDto.getPassword())
                .email(customerInputDto.getEmail())
                .registerDate(new Date(System.currentTimeMillis()))
                .status(RegisterStatus.NEW)
                .credit(0d)
                .build();
    }

    public OutPutPointDto convertInputToOutputPoint(Technician technician){
        int size = technician.getTechnicianPoints().size();
        return OutPutPointDto.builder()
                .technicianId(technician.getId())
                .point(technician.getTechnicianPoints().get(size-1).getPoint())
                .build();

    }


}
