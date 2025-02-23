package org.example.teamcity.api.constants;

public class ErrorMessages {
    public static final String ERROR_ID_STARTS_WITH_LATIN_LETTER = "ID should start with a latin letter";
    public static final String ERROR_PROJECT_ID_EMPTY = "Project ID must not be empty";
    public static final String ERROR_PROJECT_NAME_EMPTY = "Project name cannot be empty";
    public static final String ERROR_ID_ALREADY_USED = "already used by another project";
    public static final String ERROR_PROJECT_NAME_EXISTS = "Project with this name already exists";
    public static final String ERROR_PARENT_PROJECT_NOT_FOUND = "No project found by name or internal/external id";
    public static final String ERROR_NO_PROJECT_SPECIFIED = "No project specified";
    public static final String ERROR_AUTHENTICATION_REQUIRED = "Authentication required";
    public static final String ERROR_NO_CREATE_SUBPROJECT_PERMISSION = "You do not have \"Create subproject\" permission";
    public static final String ERROR_BAD_LOCATOR = "Bad locator syntax: Invalid dimension name";

    public static String getLocatorErrorMessage(String locator) {
        return "Locator dimension [" + locator + "] is unknown";
    }

}
