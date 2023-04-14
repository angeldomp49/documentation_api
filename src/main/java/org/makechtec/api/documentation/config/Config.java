package org.makechtec.api.documentation.config;

import com.google.gson.Gson;
import org.makechtec.api.documentation.components.dependency_tag.DependencyTagMapper;
import org.makechtec.api.documentation.components.project.ProjectMapper;
import org.makechtec.api.documentation.components.version.VersionMapper;
import org.makechtec.software.properties_loader.PropertyLoader;
import org.makechtec.software.sql_support.SQLSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config {

    @Bean
    public SQLSupport sqlSupport(){
        return new SQLSupport("application.properties");
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

        var corsProperties = new PropertyLoader("application.properties");

        var clientURL =
                corsProperties.getProperty("cors_protocol").orElse("") + "://" + corsProperties.getProperty("cors_client_host").orElse("") + ":" + corsProperties.getProperty("cors_client_port").orElse("");

        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry corsRegistry){
                corsRegistry.addMapping("/**")
                        .allowedMethods(corsProperties.getProperty("cors_methods").orElse(""))
                        .allowedOrigins(clientURL);
            }
        };
    }

}
