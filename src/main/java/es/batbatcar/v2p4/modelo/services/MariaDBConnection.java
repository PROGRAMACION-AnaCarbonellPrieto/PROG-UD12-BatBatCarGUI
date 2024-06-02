package es.batbatcar.v2p4.modelo.services;

import es.batbatcar.v2p4.exceptions.DatabaseConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class MariaDBConnection {

   private static Connection connection;
   private String ip;
   private String database;
   private String userName;
   private String password;

   public MariaDBConnection() {

	   // Modifica estos datos para que se adapte a tu desarrollo
	   
       this.ip = "localhost";
       this.database = "batbatcar";
       this.userName = "root";
       this.password = "1234";
   }
   
   public Connection getConnection() {
	   
	   if (this.connection == null) {
           try {
               String dbURL = "jdbc:mariadb://" + ip + "/" + database;
               Connection connection = DriverManager.getConnection(dbURL, userName, password);
               this.connection = connection;
               System.out.println("Conexion valida: " + connection.isValid(20));

           } catch (SQLException e) {
               throw new RuntimeException(e.getMessage());
           }
       }

       return this.connection;

   }
}
