package com.deinyon.aip.jobhub.database;

import com.deinyon.aip.jobhub.users.Employee;
import com.deinyon.aip.jobhub.users.Employer;
import com.deinyon.aip.jobhub.users.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * The Data Access Object which accesses and mutates User DTOs with the database.
 * @author Deinyon Davies <deinyond@gmail.com>
 */
public class UserDAO extends ResourceDAO<String, User>
{
    /**
     * Initializes the DAO using a new database context.
     * @remarks This DAO opens un-managed network connections, and should be
     * instantiated within a try-with-resources block, or should be closed
     * manually after use by calling close().
     * @throws IOException If a database connection could not be established
     */
    public UserDAO() throws IOException
    {
        super();
    }
    
    /**
     * Initializes the DAO using an existing database context
     * @remarks If this DAO is closed, the underlying connection will also be closed.
     * @param connection The database connection that should be used by this DAO
     * to access the database.
     */
    public UserDAO(Connection connection)
    {
        super(connection);
    }
    
    /**
     * Constructs a User DTO object from a database table row
     * @param results A ResultSet which has been initialized to point to a row of User
     * @return A User DTO which represents the contents of the row
     * @throws SQLException If the query failed to execute
     * @throws IOException If the User Classifier field specified an undefined user type
     */
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
                // Only Employee and Employer user types are defined
                throw new IOException("User found with an undefined classifier.");
        }
        
        return buildUserFromRow(results, user);
    }
    
    /**
     * Constructs a User DTO object from a database table row
     * @param results A ResultSet which has been initialized to point to a row of User
     * @param user The User TO which should be populated by row values
     * @return A User DTO which represents the contents of the row
     * @throws SQLException If the query failed to execute
     * @throws IOException If the User Classifier field specified an undefined user type
     */
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
    
    /**
     * Locates the user with the specified classification (type), and username.
     * @param <T> The type of user to retrieve (Employee or Employer)
     * @param username The username (identifier) of the user to retrieve
     * @param classifier The classification identifier of the type of user to retrieve
     * @return The user identified by the specified username
     * @throws IOException If the query failed to execute
     */
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
        
    /**
     * Locates the user with the specified username.
     * @param username The username (identifier) of the user to retrieve
     * @return The user identified by the specified username
     * @throws IOException If the query failed to execute
     */
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

    /**
     * Gathering data of all users is not supported
     */
    @Override
    public List<User> getAll() throws IOException
    {
        throw new UnsupportedOperationException("Obtaining a list of users is not supported");
    }

    /**
     * Inserts a new User into the database
     * @param user The User DTO to insert into the database
     * @throws IOException If a database error prevented the query from executing successfully.
     */
    @Override
    public void save(User user) throws IOException
    {
        String query =
                "INSERT INTO Users (user_id, classifier, password, first_name, last_name, email, biography, company) VALUES " +
                "( ?,?,?,?,?,?,?,?)";
        
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
            
            // If the company name has been specified, set it
            if(user.getCompany() != null)
                preparedStatement.setString(8, user.getCompany());
            else
                preparedStatement.setNull(8, Types.VARCHAR);
            
            // Execute the query
            preparedStatement.executeUpdate();
        }
        catch(SQLException ex)
        {
            throw new IOException(ex);
        }
    }
    
    @Override
    public boolean delete(User resource) throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(User resource) throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int count() throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
