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
public class Project extends BaseModel{
    @Random
    @Parameterizable
    private String id;
    @Random
    @Parameterizable
    private String name;
    private ParentProject parentProject;
    @Optional
    private Boolean copyAllAssociatedSettings;
    @Optional
    private SourceProject sourceProject = null;
}
