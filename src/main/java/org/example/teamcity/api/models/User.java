package org.example.teamcity.api.models;

import lombok.*;

import java.sql.ConnectionBuilder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String user;
    private String password;
}
