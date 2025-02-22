package org.example.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.example.teamcity.api.annotations.Parameterizable;
import org.example.teamcity.api.annotations.Random;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseModel {
    private String id;
    @Random
    private String username;
    @Random
    private String password;
    private Roles roles;
}
