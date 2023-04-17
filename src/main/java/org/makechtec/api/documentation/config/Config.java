package org.makechtec.api.documentation.config;

import com.google.gson.Gson;
import org.makechtec.api.documentation.components.dependency_tag.DependencyTagMapper;
import org.makechtec.api.documentation.components.project.ProjectMapper;
import org.makechtec.api.documentation.components.version.VersionMapper;
import org.makechtec.software.properties_loader.PropertyLoader;
import org.makechtec.software.sql_support.ConnectionInformation;
import org.makechtec.software.sql_support.SQLSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config {

    @Bean
    public SQLSupport sqlSupport(){

        var allProperties = this.applicationPropertiesList();

        ConnectionInformation connectionInformation = new ConnectionInformation(
                allProperties.getDbUser(),
                allProperties.getDbPassword(),
                allProperties.getDbHost(),
                allProperties.getDbPort(),
                allProperties.getDbName()
        );

        return new SQLSupport(connectionInformation);
    }

    @Bean
    public ProjectMapper projectMapper(){
        return new ProjectMapper(this.sqlSupport(), queryPropertyLoader(), dependencyTagMapper());
    }

    @Bean
    public VersionMapper versionMapper(){
        return new VersionMapper(this.sqlSupport(), versionQueriesLoader());
    }

    @Bean
    public DependencyTagMapper dependencyTagMapper(){
        return new DependencyTagMapper(this.sqlSupport(), dependencyTagQueriesLoader());
    }

    @Bean
    public Gson gson(){
        return new Gson();
    }

    @Bean
    public PropertyLoader queryPropertyLoader(){
        return new PropertyLoader("mapping/project_queries.properties");
    }

    @Bean
    public PropertyLoader versionQueriesLoader(){
        return new PropertyLoader("mapping/version_queries.properties");
    }

    @Bean
    public PropertyLoader dependencyTagQueriesLoader(){
        return new PropertyLoader("mapping/dependency_tag_queries.properties");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(){

        var allProperties = this.applicationPropertiesList();

        var clientURL =
                allProperties.getCorsProtocol() + "://" + allProperties.getCorsClientHost() + ":" + allProperties.getCorsClientPort();

        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry corsRegistry){
                corsRegistry.addMapping("/**")
                        .allowedMethods(allProperties.getCorsMethods())
                        .allowedOrigins(clientURL);
            }
        };
    }

    @Bean
    public ApplicationPropertiesList applicationPropertiesList(){
        return new ApplicationPropertiesList();
    }

}
