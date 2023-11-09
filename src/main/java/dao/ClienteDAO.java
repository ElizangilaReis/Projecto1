package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;

public class ClienteDAO {
	
	//PREENCHER E CRIAR UMA BASE DE DADOS
	private String jdbcURL = "jdbc:mysql://127.0.0.1:3306/?user=root";
	private String jdbcUsername = "elizangilareis";
	private String jdbcPassword = "Elsamaria12!";
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES " + " (?, ?, ?)";		
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
	private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
	
	public ClienteDAO() {
		
	}
	
	protected Connection getConnection() {
		
		Connection connection = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public void insertUser(Cliente cliente) throws SQLException{
		
		System.out.println(INSERT_USERS_SQL);
		
		try (Connection connection = getConnection();PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)){
			preparedStatement.setString(1, cliente.getName());
			preparedStatement.setString(2, cliente.getEmail());
			preparedStatement.setString(3, cliente.getCountry());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e); //verificar
		}		
	}
	
	public Cliente selectCliente(int id) {
		Cliente cliente = null;
		
		try(Connection connection = getConnection()){
		
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				 String name = rs.getString("name");
	             String email = rs.getString("email");
	             String country = rs.getString("country");
	             cliente = new Cliente(id, name, email, country);
			}
		}catch (SQLException e ) {
			System.out.println(e);
		}
		return cliente;
	}
	
	public List <Cliente> selectAllClientes(){
		List < Cliente > clientes = new ArrayList < > ();
		
		 try (Connection connection = getConnection()){
			 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
		     System.out.println(preparedStatement); 
		     
		     ResultSet rs = preparedStatement.executeQuery();
		     
		     while (rs.next()) {
	                int id = rs.getInt("id");
	                String name = rs.getString("name");
	                String email = rs.getString("email");
	                String country = rs.getString("country");
	                clientes.add(new Cliente(id, name, email, country));
	            }
		 }catch (SQLException e) {
	            System.out.println(e);
		 }
		 return clientes;
	}
	
	public boolean deleteCliente(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateUser(Cliente cliente) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            statement.setString(1, cliente.getName());
            statement.setString(2, cliente.getEmail());
            statement.setString(3, cliente.getCountry());
            statement.setInt(4, cliente.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}