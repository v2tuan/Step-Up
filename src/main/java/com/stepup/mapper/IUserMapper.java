package com.stepup.mapper;

import com.stepup.dtos.requests.UserDTO;
import com.stepup.dtos.responses.UserRespone;
import com.stepup.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    User toUser(UserDTO userDTO);
    UserRespone toUserRespone(User user);
}
