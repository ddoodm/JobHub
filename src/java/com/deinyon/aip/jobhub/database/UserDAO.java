/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinyon.aip.jobhub.database;

import com.deinyon.aip.jobhub.Configuration;
import com.deinyon.aip.jobhub.users.Employee;
import com.deinyon.aip.jobhub.users.Employer;
import com.deinyon.aip.jobhub.users.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import javax.imageio.IIOException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class UserDAO extends ResourceDAO<String, User>
{
    public UserDAO() throws IOException
    {
        super();
    }
    
    public UserDAO(Connection connection)
    {
        super(connection);
    }
    
    private User buildUserFromRow(ResultSet results) throws SQLException, IOException
    {
        String userClassString = results.getString("classifier");
        UserClassification classifier = UserClassification.valueOf(userClassString);
        
        User user = null;
        switch(classifier)
        {
            case Employee: user = new Employee(); break;
            case Employer: user = new Employer(); break;
            default:
                throw new IOException("User found with an undefined classifier.");
        }
        
        return buildUserFromRow(results, user);
    }
    
    private User buildUserFromRow(ResultSet results, User user) throws SQLException, IOException
    {        
        // Suppresses compiler warning about dereferencing possible null-pointer
        if(user != null)
        {
            user.setUsername(results.getString("user_id"));
            user.setGivenName(results.getString("first_name"));
            user.setSurname(results.getString("last_name"));
            user.setCompany(results.getString("company"));
            user.setEmail(results.getString("email"));
            user.setBio(results.getString("biography"));
        }
        
        return user;
    }
    
    public <T extends User> T findUserOfType(String username, UserClassification classifier) throws IOException
    {
        // The SQL query selects the User
        String query =
                "SELECT user_id, classifier, first_name, last_name, company, email, biography " +
                "FROM Users " +
                "WHERE user_id = ? " +
                "AND classifier = ?";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            // Substitute query parameters
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, classifier.name());
            
            // Execute the query
            ResultSet results = preparedStatement.executeQuery();

            // One one row is anticipated
            if(!results.next())
                return null;
            
            // Build Job DTOs
            User user = null;
            switch (classifier)
            {
                case Employee: user = new Employee(); break;
                case Employer: user = new Employer(); break;
                default:
                    throw new IllegalArgumentException("Encountered undefined user classification");
            }
            
            buildUserFromRow(results, user);
            return (T)user;
        }
        catch(Exception ex)
        {
            throw new IOException("Could not find the specified resource", ex);
        }
    }
        
    @Override
    public User find(String username) throws IOException
    {
        // The SQL query selects the User
        String query =
                "SELECT user_id, classifier, first_name, last_name, company, email, biography " +
                "FROM Users " +
                "WHERE user_id = ?";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            // Substitute query parameters
            preparedStatement.setString(1, username);

            // Execute the query
            ResultSet results = preparedStatement.executeQuery();

            // One one row is anticipated
            if(!results.next())
                return null;
            
            // Build Job DTOs
            return buildUserFromRow(results);
        }
        catch(Exception ex)
        {
            throw new IOException("Could not find the specified resource", ex);
        }
    }

    @Override
    public List<User> getAll() throws IOException {
        throw new UnsupportedOperationException("Obtaining a list of users is not supported");
    }

    @Override
    public void save(User user) throws IOException
    {
        String query =
                "INSERT INTO Users (user_id, classifier, password, first_name, last_name, email, biography) VALUES " +
                "( ?,?,?,?,?,?,? )";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            // Substitute query parameters
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getClassifier().name());
            preparedStatement.setString(3, user.getPassword().toString());
            preparedStatement.setString(4, user.getGivenName());
            preparedStatement.setString(5, user.getSurname());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getBio());
            
            // Execute the query
            preparedStatement.executeUpdate();
        }
        catch(SQLException ex)
        {
            throw new IOException(ex);
        }
    }

    @Override
    public boolean delete(User resource) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(User resource) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int count() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
