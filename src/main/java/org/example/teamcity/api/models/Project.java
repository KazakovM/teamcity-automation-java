package org.example.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.example.teamcity.api.annotations.Optional;
import org.example.teamcity.api.annotations.Parameterizable;
import org.example.teamcity.api.annotations.Random;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
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
