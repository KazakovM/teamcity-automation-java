package org.example.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.teamcity.api.annotations.Random;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Build extends BaseModel {
    @Random
    private String id;
    private String state;
    private String status;
    @Random
    private BuildType buildType;
}
