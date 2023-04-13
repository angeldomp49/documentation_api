package org.makechtec.api.documentation.components.dependency_tag;

import org.makechtec.software.properties_loader.PropertyLoader;
import org.makechtec.software.sql_support.SQLSupport;

import java.util.Optional;

public class DependencyTagMapper {

    private final SQLSupport sqlSupport;
    private final PropertyLoader propertyLoader;

    public DependencyTagMapper(SQLSupport sqlSupport, PropertyLoader propertyLoader) {
        this.sqlSupport = sqlSupport;
        this.propertyLoader = propertyLoader;
    }

    public void create(String projectName, String groupId, String artifactId){
        sqlSupport.runSQLQuery( connection -> {
            var statement = connection.prepareStatement(propertyLoader.getProperty("CREATE_DEPENDENCY_TAG").orElse(""));

            statement.setString(1, groupId);
            statement.setString(2, artifactId);
            statement.setString(3, projectName);

            statement.executeUpdate();
        });

    }

    public boolean existsDependencyTag(String groupId, String artifactId){

        class Result{
            public int quantity;
        }

        var result = new Result();

        sqlSupport.runSQLQuery( connection -> {
            var statement = connection.prepareStatement(propertyLoader.getProperty("CHECK_DEPENDENCY_TAG").orElse(""));

            statement.setString(1, groupId);
            statement.setString(2, artifactId);

            statement.execute();

            var resultSet = statement.getResultSet();

            resultSet.next();

            result.quantity = resultSet.getInt("quantity");

        });

        return result.quantity > 0;
    }

    public boolean existsDependencyTag(String projectName){

        class Result{
            public int quantity;
        }

        var result = new Result();

        sqlSupport.runSQLQuery( connection -> {
            var statement = connection.prepareStatement(propertyLoader.getProperty("CHECK_DEPENDENCY_TAG_BY_PROJECT_NAME").orElse(""));

            statement.setString(1, projectName);

            statement.execute();

            var resultSet = statement.getResultSet();

            resultSet.next();

            result.quantity = resultSet.getInt("quantity");

        });

        return result.quantity > 0;
    }

    public Optional<DependencyTag> byProjectName(String projectName){

        if(!existsDependencyTag(projectName)){
            return Optional.empty();
        }

        class Result{
            public String groupId;
            public String artifactId;
        }

        var result = new Result();

        sqlSupport.runSQLQuery( connection -> {
            var statement = connection.prepareStatement(propertyLoader.getProperty("SHOW_BY_PROJECT_NAME").orElse(""));

            statement.setString(1, projectName);

            statement.execute();

            var resultSet = statement.getResultSet();

            resultSet.next();

            var groupId = resultSet.getString("group_id");
            var artifactId = resultSet.getString("artifact_id");

            result.groupId = groupId;
            result.artifactId = artifactId;

        });

        return Optional.of(new DependencyTag(result.groupId, result.artifactId));
    }

    public void deleteDependencyTag(String groupId, String artifactId){

    }

}
