package org.example.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.teamcity.api.annotations.Optional;
import org.example.teamcity.api.annotations.Parameterizable;
import org.example.teamcity.api.annotations.Random;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildType extends BaseModel{
    @Random
    @Parameterizable
    private String id;
    @Random
    private String name;
    @Parameterizable
    private Project project;
    @Optional
    private Steps steps;
}
