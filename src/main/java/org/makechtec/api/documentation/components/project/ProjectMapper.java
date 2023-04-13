package org.makechtec.api.documentation.components.project;

import org.makechtec.api.documentation.components.dependency_tag.DependencyTag;
import org.makechtec.api.documentation.components.dependency_tag.DependencyTagMapper;
import org.makechtec.api.documentation.components.version.Version;
import org.makechtec.software.properties_loader.PropertyLoader;
import org.makechtec.software.sql_support.SQLSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectMapper {

    private final SQLSupport sqlSupport;
    private final PropertyLoader propertyLoader;

    private final DependencyTagMapper dependencyTagMapper;

    public ProjectMapper(SQLSupport sqlSupport, PropertyLoader propertyLoader, DependencyTagMapper dependencyTagMapper) {
        this.sqlSupport = sqlSupport;
        this.propertyLoader = propertyLoader;
        this.dependencyTagMapper = dependencyTagMapper;
    }

    public List<String> allProjectNames(){

        var projects = new ArrayList<String>();

        sqlSupport.runSQLQuery( connection -> {
            var statement = connection.prepareStatement(propertyLoader.getProperty("ALL_PROJECT_NAMES").orElse(""));

            statement.execute();

            var resultSet = statement.getResultSet();

            while (resultSet.next()) {

                projects.add(resultSet.getString("projects.name"));
            }

        });

        return projects;
    }

    public Optional<Project> byName(String projectName){

        if(!existProject(projectName)){
            return Optional.empty();
        }

        var versions = new ArrayList<Version>();

        sqlSupport.runSQLQuery( connection -> {
            var statement = connection.prepareStatement(propertyLoader.getProperty("PROJECT_BY_NAME").orElse(""));

            statement.setString(1, projectName);

            statement.execute();

            var resultSet = statement.getResultSet();

            while (resultSet.next()) {

                versions.add(new Version(resultSet.getString("versions.id"), resultSet.getString("details")));

            }
        });

        var dependencyTag = dependencyTagMapper.byProjectName(projectName);

        return Optional.of(new Project(projectName, dependencyTag.orElse(new DependencyTag("", "")) ,versions));
    }

    public boolean existProject(String projectName){

        class Result{
            public int quantity;
        }

        var result = new Result();

        sqlSupport.runSQLQuery( connection -> {
            var statement = connection.prepareStatement(propertyLoader.getProperty("CHECK_PROJECT").orElse(""));

            statement.setString(1, projectName);

            statement.execute();

            var resultSet = statement.getResultSet();

            resultSet.next();

            result.quantity = resultSet.getInt("quantity");

        });

        return result.quantity > 0;
    }

    public List<Project> allHydratedProjects(){

        return allProjectNames()
                .stream()
                .map(this::byName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();


    }

    public void create(String projectName){
        sqlSupport.runSQLQuery( connection -> {
            var statement = connection.prepareStatement(propertyLoader.getProperty("CREATE_PROJECT").orElse(""));

            statement.setString(1, projectName);

            statement.executeUpdate();
        });
    }

    public void deleteProject(String projectName){
        sqlSupport.runSQLQuery( connection -> {
            var statement = connection.prepareStatement(propertyLoader.getProperty("DELETE_PROJECT").orElse(""));

            statement.setString(1, projectName);

            statement.executeUpdate();
        });
    }

}
