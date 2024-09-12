package com.emazon.users.application.usecase;

import com.emazon.users.application.dto.UserDTO;

public interface CreateUserUseCase {
    void execute(UserDTO userDTO);
}
