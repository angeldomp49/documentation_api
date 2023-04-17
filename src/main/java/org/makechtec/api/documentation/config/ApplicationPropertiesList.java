package org.makechtec.api.documentation.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class ApplicationPropertiesList {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${db_user}")
    private String dbUser;
    @Value("${db_password}")
    private String dbPassword;

    @Value("${db_host}")
    private String dbHost;

    @Value("${db_port}")
    private String dbPort;

    @Value("${db_name}")
    private String dbName;

    @Value("${cors_client_host}")
    private String corsClientHost;

    @Value("${cors_client_port}")
    private String corsClientPort;

    @Value("${cors_protocol}")
    private String corsProtocol;

    @Value("${cors_methods}")
    private String corsMethods;

}
