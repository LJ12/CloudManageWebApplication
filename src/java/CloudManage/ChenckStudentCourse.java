/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CloudManage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class ChenckStudentCourse extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8"); 
         response.setCharacterEncoding("utf-8"); 
         try (PrintWriter out = response.getWriter()) { 
            Connection conn=null;
            String  url = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC&characterEncoding=utf8";
            String CourseID = request.getParameter("CourseID").trim(); 
            String StudentID = request.getParameter("StudentID").trim(); 
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
            PreparedStatement state;
            ResultSet result;
            Map<String, String> params = new HashMap<>(); 
            JSONObject jsonObject = new JSONObject(); 
            String sql="select * from user_course where CourseID = " + CourseID+" and UserID="+StudentID+" and UserType= 'Student'";
            state =conn.prepareStatement(sql);
            result = state.executeQuery();
            if(result.next()){
                sql="select CourseName from course_message where CourseID = " + CourseID;
                state =conn.prepareStatement(sql);
                result = state.executeQuery();
                result.next();
                params.put("Result","success");
                params.put("Data",result.getString("CourseName"));
            }else{
                params.put("Result","failed");
                params.put("Data","学生ID或者课程ID输入错误");
            }
             jsonObject.put("params", params);
            out.write(jsonObject.toString()); 
         } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChenckStudentCourse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ChenckStudentCourse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
