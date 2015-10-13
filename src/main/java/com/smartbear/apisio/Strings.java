package com.smartbear.apisio;

public final class Strings {
    private Strings() {}

    public static final class PluginInfo {
        public static final String NAME = "APIs.io Plugin";
        public static final String DESCRIPTION = "Adds actions to search, import and publish APIs with APIs.io service";
    }

    public static final class AddApiAction {
        public static final String NAME = "Add API With API Search Engine";
        public static final String DESCRIPTION = "Imports APIs from an Azure API Management service instance";
    }

    public static final class ExportApiAction {
        public static final String NAME = "Export APIs.json";
        public static final String DESCRIPTION = "Create an APIs.json definition for selected REST API";
    }

    public static final class NewProjectAction {
        public static final String NAME = "Create Project With API Search Engine";
        public static final String DESCRIPTION = "Creates a new project with APIs founded by API Search engine";
        public static final String UNABLE_CREATE_ERROR = "Failed to create the project due to %s exception with \"%s\" message";
        public static final String CAPTION = "API Search Engine";
    }

    public static class BaseDialog {
        public static final String DESCRIPTION_LABEL = "API Description";
        public static final String GEN_TEST_SUITE = "Generate Test Suite";
        public static final String GEN_LOAD_TEST = "Generate Load Test";
        public static final String GEN_SECUR_TEST = "Generate Security Test";
        public static final String GEN_VIRT_HOST = "Generate Virtual Host";
        public static final String NOTHING_SELECTED_WARNING = "Please select at least one API to import";
    }

    public static final class NewProjectDialog {
        public static final String CAPTION = "Create Project From APIs registered at the APIs.io service";
        public static final String DESCRIPTION = "Creates a new project for APIs founded by API Search engine in this workspace";
        public static final String PROJECT_LABEL = "Project Name";
        public static final String PROJECT_DESCRIPTION = "Name of the project";
        public static final String EMPTY_PROJECT_WARNING = "Please enter project name";
    }

    public static class SearchApiDialog {
        public static final String CAPTION = "Select APIs to Import";
        public static final String DESCRIPTION = "Please select APIs you would like to import into the project from the list below";
        public static final String SEARCH_HINT = "Enter search keyword or leave empty to get all entries";
    }

    public static final class Executing {
        public static final String QUERY_API_PROGRESS = "Getting APIs list...";
        public static final String UNAVAILABLE_HOST_ERROR = "The \"%s\" host is unavailable or invalid.";
        public static final String UNAVAILABLE_DATA_ERROR = "No data available at the \"%s\" location. You can search and create issues for this plugin at https://github.com/SmartBear/ready-apisio-plugin/issues.";
        public static final String UNEXPECTED_RESPONSE_FORMAT_ERROR = "Unexpected response format of the request to the \"%s\" location. You can search and create issues for this plugin at https://github.com/SmartBear/ready-apisio-plugin/issues.";
        public static final String UNSUCCESSFUL_STATUS_ERROR = "The request to the API Search Engine is finished with unsuccessful status";
        public static final String IMPORT_PROGRESS = "Importing APIs...";
        public static final String IMPORT_ERROR = "Failed to read API description for [%s] - [%s]\n";
    }

    public static final class ExportApiDialog {
        public static final String CAPTION = "Export APIs.json definition";
        public static final String DESCRIPTION = "Creates an APIs.json definition for selected REST API in this project";

        public static final String FOLDER_NAME = "Folder";
        public static final String FOLDER_NAME_HINT = "Folder to save the APIs.json definition";

        public static final String DOMAIN_SECTION = "Domain";
        public static final String DOMAIN_NAME = "Name";
        public static final String DOMAIN_NAME_HINT = "Domain name";
        public static final String DOMAIN_DESCRIPTION = "Description";
        public static final String DOMAIN_DESCRIPTION_HINT = "Domain description";
        public static final String DOMAIN_URL = "URL";
        public static final String DOMAIN_URL_HINT = "Domain URL";
        public static final String DOMAIN_IMAGE = "Image";
        public static final String DOMAIN_IMAGE_HINT = "Domain icon's image";
        public static final String DOMAIN_MAINTAINER = "Maintainer";
        public static final String DOMAIN_MAINTAINER_HINT = "Domain maintainer";
        public static final String DOMAIN_CREATED = "Created";
        public static final String DOMAIN_CREATED_HINT = "Date of creation of the definition file";
        public static final String DOMAIN_MODIFIED = "Modified";
        public static final String DOMAIN_MODIFIED_HINT = "Date of modification of the definition file";
        public static final String DOMAIN_TAGS = "Tags";
        public static final String DOMAIN_TAGS_HINT = "Comma separated list of the key words which identify the content of the APIs.json file";

        /* WORKAROUND: ___ suffix is required to distinguish from the other controls with the same name.
         * In this case colon isn't automatically added so we add its self*/
        public static final String API_SECTION = "API";
        public static final String API_NAME = "Name:___";
        public static final String API_NAME_HINT = "API name";
        public static final String API_DESCRIPTION = "Description:___";
        public static final String API_DESCRIPTION_HINT = "API description";
        public static final String API_IMAGE = "Image:___";
        public static final String API_IMAGE_HINT = "API icon's image";
        public static final String API_BASE_URL = "Base URL";
        public static final String API_BASE_URL_HINT = "Base API URL";
        public static final String API_HUMAN_URL = "Human URL";
        public static final String API_HUMAN_URL_HINT = "Human API URL";
        public static final String API_TAGS = "Tags:___";
        public static final String API_TAGS_HINT = "Comma separated list of the key words which identify the content of the API";

        public static final String DOMAIN_ = "";
        public static final String DOMAIN__HINT = "";

        public static final String EMPTY_VALUE = "Please enter the %s";
        public static final String TOO_SHORT_VALUE = "The %s must be at least 5 characters long.";
        public static final String INVALID_DATE_VALUE = "Date does not match pattern YYYY-mm-dd";
        public static final String INVALID_URL_FORMAT = "%s does not match pattern: %s";
        public static final String INVALID_TAGS_FORMAT = "Invalid tags format";
    }

    public static final class DomainControl {
        public static final String TITLE = "Domain description";
        public static final String NAME = "Name:";
        public static final String NAME_HINT = "Domain name";
        public static final String DESCRIPTION = "Description:";
        public static final String DESCRIPTION_HINT = "Domain description";
        public static final String URL = "URL:";
        public static final String URL_HINT = "Domain URL";
        public static final String IMAGE = "Image:";
        public static final String IMAGE_HINT = "Domain icon's image";
        public static final String MAINTAINER = "Maintainer:";
        public static final String MAINTAINER_HINT = "Domain maintainer";
        public static final String CREATED = "Created:";
        public static final String CREATED_HINT = "Date of creation of the definition file";
        public static final String MODIFIED = "Modified:";
        public static final String MODIFIED_HINT = "Date of modification of the definition file";
        public static final String TAGS = "Tags:";
        public static final String TAGS_HINT = "Comma separated list of the key words which identify the content of the APIs.json file";
    }

    public static final class ApiControl {
        public static final String TITLE = "API description";
        public static final String NAME = "Name:";
        public static final String NAME_HINT = "API name";
        public static final String DESCRIPTION = "Description:";
        public static final String DESCRIPTION_HINT = "API description";
        public static final String IMAGE = "Image:";
        public static final String IMAGE_HINT = "API icon's image";
        public static final String BASE_URL = "Base URL:";
        public static final String BASE_URL_HINT = "Base API URL";
        public static final String HUMAN_URL = "Human URL:";
        public static final String HUMAN_URL_HINT = "Human API URL";
        public static final String TAGS = "Tags:";
        public static final String TAGS_HINT = "Comma separated list of the key words which identify the content of the API";
        public static final String DEFINITION = "Definition:";
        public static final String DEFINITION_HINT = "Definition URL and type";
    }

    public static final class SelectFolderControl {
        public static final String TITLE = "Select folder to save the APIs.json definition";
        public static final String FOLDER = "Folder:";
        public static final String FOLDER_HINT = "Folder to save the APIs.json definition";
    }

    public static final class FieldValidator {
        public static final String EMPTY_VALUE = "Please enter the %s";
        public static final String TOO_SHORT_VALUE = "The %s must be at least 5 characters long.";
        public static final String INVALID_DATE_VALUE = "The date %s does not match pattern YYYY-mm-dd";
        public static final String INVALID_URL_FORMAT = "%s does not match pattern: %s";
        public static final String INVALID_TAGS_FORMAT = "Invalid tags format";
        public static final String FOLDER_NOT_EXISTS = "Specified folder doesn't exists";
    }
}
