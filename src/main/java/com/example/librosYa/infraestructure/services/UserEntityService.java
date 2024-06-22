package com.example.librosYa.infraestructure.services;

import com.example.librosYa.api.dto.DtoUser;
import com.example.librosYa.domain.entities.UserEntity;
import com.example.librosYa.domain.repositories.UserRepository;
import com.example.librosYa.infraestructure.abstract_services.IUserService;
import com.example.librosYa.util.exceptions.ResourceNotFoundException;
import com.example.librosYa.util.mappers.LoanMapper;
import com.example.librosYa.util.mappers.ReservationMapper;
import com.example.librosYa.util.mappers.UserMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class UserEntityService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
   


   
    public UserEntityService(UserRepository userRepository, UserMapper userMapper, ReservationMapper reservationMapper, LoanMapper loanMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
      
    }

    @Override
    public Page<DtoUser> getAll(int page, int size) {
        if (page < 0) page = 0;
        PageRequest pagination = PageRequest.of(page, size);
        return userRepository.findAll(pagination).map(user -> userMapper.toGetDTO(user));
    }

    @Override
    public DtoUser getById(Long id) {
        UserEntity userEntity = find(id);
        return userMapper.toGetDTO(userEntity);
    }

    @Override
    public DtoUser create(DtoUser dtoRequest) {
        UserEntity userEntity = userMapper.toEntity(dtoRequest);
        UserEntity savedUser = userRepository.save(userEntity);
        return userMapper.toGetDTO(userEntity);
    }

    @Override
    public DtoUser update(DtoUser dtoRequest, Long id) {
        UserEntity userEntity = find(id);
        userMapper.toEntity(dtoRequest);
        userEntity = userRepository.save(userEntity);
        return userMapper.toGetDTO(userEntity);
    }

    @Override
    public void delete(Long id) {
        UserEntity userEntity = find(id);
        userRepository.delete(userEntity);
    }

    private UserEntity find(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException ("User not found with id: " + id));
    }
}