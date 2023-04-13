package org.makechtec.api.documentation.components.version;

import org.makechtec.software.properties_loader.PropertyLoader;
import org.makechtec.software.sql_support.SQLSupport;

public class VersionMapper {

    private final SQLSupport sqlSupport;
    private final PropertyLoader propertyLoader;

    public VersionMapper(SQLSupport sqlSupport, PropertyLoader propertyLoader) {
        this.sqlSupport = sqlSupport;
        this.propertyLoader = propertyLoader;
    }

    public void create(String projectName, String id, String details){
        sqlSupport.runSQLQuery( connection -> {
            var statement = connection.prepareStatement(propertyLoader.getProperty("CREATE_VERSION").orElse(""));

            statement.setString(1, id);
            statement.setString(2, details);
            statement.setString(3, projectName);

            statement.executeUpdate();
        });

    }

    public boolean existVersion(String projectName, String id){

        class Result{
            public int quantity;
        }

        var result = new Result();

        sqlSupport.runSQLQuery( connection -> {
            var statement = connection.prepareStatement(propertyLoader.getProperty("CHECK_VERSION").orElse(""));

            statement.setString(1, id);
            statement.setString(2, projectName);

            statement.execute();

            var resultSet = statement.getResultSet();

            resultSet.next();

            result.quantity = resultSet.getInt("quantity");

        });

        return result.quantity > 0;
    }
}
