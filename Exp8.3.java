Steps to Develop a JSP-Based Student Portal with Attendance Submission
  
1. Set Up the Database (MySQL)
Create a database named StudentDB.
Create a table attendance with columns: id, student_name, date, status.
Insert sample records for testing.

2. Configure Your Java Project
Add MySQL JDBC Driver (via Maven or manually).
Set up Apache Tomcat in your IDE (Eclipse/VScode).

3. Create a Database Connection Class (DBConnection.java)
This utility class connects to MySQL.

4. Create the JSP Form (attendance.jsp)
A form where users enter student name, date, and attendance status (Present/Absent).

5. Develop the Servlet (AttendanceServlet.java)
Handles form submission and saves attendance to the database.
Uses JDBC to insert data into MySQL.

6. Configure web.xml (If Needed)
Map AttendanceServlet to handle form submissions.

7. Deploy & Test
Run on Tomcat.
Access via http://localhost:8080/YourAppName/attendance.jsp.
Submit attendance, check database for saved records.

Enhancements (Optional)
- Display submitted attendance records in attendance.jsp.
- Add CSS/Bootstrap for better UI.
- Implement session handling for authentication.

  MySQL
  CREATE DATABASE StudentDB;

USE StudentDB;

CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(100),
    date DATE,
    status VARCHAR(10)
);

-- Optional test records
INSERT INTO attendance (student_name, date, status)
VALUES
('Alice Johnson', '2025-04-10', 'Present'),
('Bob Smith', '2025-04-10', 'Absent');


DBconnection.java
  import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/StudentDB",
                "yourUsername", "yourPassword"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


attendance.jsp

  <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Student Attendance</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body class="p-4">
    <h2>Submit Attendance</h2>
    <form action="AttendanceServlet" method="post" class="mb-4">
        <div class="form-group">
            <label>Student Name:</label>
            <input type="text" name="student_name" class="form-control" required />
        </div>
        <div class="form-group">
            <label>Date:</label>
            <input type="date" name="date" class="form-control" required />
        </div>
        <div class="form-group">
            <label>Status:</label>
            <select name="status" class="form-control">
                <option value="Present">Present</option>
                <option value="Absent">Absent</option>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>

    <!-- Optional: Display attendance -->
    <jsp:include page="ViewAttendance.jsp" />
</body>
</html>


  attendanceservlet.java

  import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class AttendanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        String name = request.getParameter("student_name");
        String date = request.getParameter("date");
        String status = request.getParameter("status");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO attendance (student_name, date, status) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setDate(2, Date.valueOf(date));
            ps.setString(3, status);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("attendance.jsp");
    }
}



