package com.example.bankingapp.dto;


import lombok.Data;

import java.util.List;


@Data
public class UserDTO {
    private String email;
    private String username;
    private String password;
    private String role;
    private List<AccountDTO> accountEntities;
}
