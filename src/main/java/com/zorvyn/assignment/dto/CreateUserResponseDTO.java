package com.zorvyn.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponseDTO {

    private String name;
    private String email;
    private String role;
    private String temporaryPassword;
    

    
}
