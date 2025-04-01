package org.example.teamcity.api.models;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Steps extends BaseModel{
    private Integer count;
    private List<Step> step;
}
