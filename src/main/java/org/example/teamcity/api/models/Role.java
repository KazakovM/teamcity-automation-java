package org.example.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.example.teamcity.api.annotations.Random;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends BaseModel{
    @Builder.Default
    private String roleId = "SYSTEM_ADMIN";
    @Builder.Default
    private String scope = "g";
}
