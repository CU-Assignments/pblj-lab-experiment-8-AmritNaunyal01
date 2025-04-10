Steps to Create a JDBC-Integrated Servlet for Employee Management
1. Set Up Database
Create a MySQL database (EmployeeDB).
Create an employees table with columns (id, name, department, salary).
Insert sample employee data.

2. Set Up Your Java Project
Add MySQL JDBC Driver (via Maven or manually).
Configure Apache Tomcat in your IDE.

3. Create Database Connection Class
Write a utility class (DBConnection.java) to establish a connection with the MySQL database.

4. Develop the Servlet (EmployeeServlet.java)
- Handle GET requests to fetch all employees or search by Employee ID.
- Use JDBC to query data and display it in HTML format.
- Implement a search form for filtering employees by ID.

5. Deploy and Run
Deploy the servlet on Tomcat.
Access it via http://localhost:8080/YourAppName/EmployeeServlet.
(The page displays employee records and allows searching by ID.)

Note : Enhancements (Optional)
Improve UI with CSS & Bootstrap.


  MySQL
  CREATE DATABASE EmployeeDB;

USE EmployeeDB;

CREATE TABLE employees (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    department VARCHAR(100),
    salary DOUBLE
);

INSERT INTO employees (id, name, department, salary) VALUES
(1, 'Alice', 'HR', 50000),
(2, 'Bob', 'Engineering', 75000),
(3, 'Charlie', 'Sales', 60000);


DBConnection.java
  
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/EmployeeDB", "yourUsername", "yourPassword"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}



employeeservlet.java
  
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class EmployeeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        String empId = request.getParameter("id");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><title>Employees</title></head><body>");
        out.println("<h2>Employee Records</h2>");
        out.println("<form method='get'>Search by ID: <input type='text' name='id'/> <input type='submit' value='Search'/></form>");
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM employees";
            if (empId != null && !empId.isEmpty()) {
                sql += " WHERE id = ?";
            }
            
            PreparedStatement ps = conn.prepareStatement(sql);
            if (empId != null && !empId.isEmpty()) {
                ps.setInt(1, Integer.parseInt(empId));
            }
            
            ResultSet rs = ps.executeQuery();
            
            out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Department</th><th>Salary</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getString("department") + "</td>");
                out.println("<td>" + rs.getDouble("salary") + "</td></tr>");
            }
            out.println("</table>");
        } catch (Exception e) {
            e.printStackTrace(out);
        }
        out.println("</body></html>");
    }
}


web.xml
<web-app>
    <servlet>
        <servlet-name>EmployeeServlet</servlet-name>
        <servlet-class>EmployeeServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>EmployeeServlet</servlet-name>
        <url-pattern>/EmployeeServlet</url-pattern>
    </servlet-mapping>
</web-app>



