package org.example.teamcity.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.teamcity.api.models.*;

@AllArgsConstructor
@Getter
public enum Endpoint {
    BUILD_TYPES("/app/rest/buildTypes", BuildType.class),
    PROJECTS("/app/rest/projects", Project.class),
    USERS("/app/rest/users", User.class),
    BUILD_QUEUE("/app/rest/buildQueue", Build.class),
    BUILD_LOGS("/app/rest/builds/id:{id}/log", BuildLog.class);

    private final String url;
    private final Class<? extends BaseModel> modelClass;
}
