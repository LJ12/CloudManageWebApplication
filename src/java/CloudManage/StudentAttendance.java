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
public class StudentAttendance extends HttpServlet {
       @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request,response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
         response.setContentType("text/html;charset=utf-8"); 
         request.setCharacterEncoding("utf-8"); 
         response.setCharacterEncoding("utf-8"); 
         try (PrintWriter out = response.getWriter()) { 
            Connection conn=null;
            String url="";
            String userID = request.getParameter("UserID").trim(); 
            String courseID = request.getParameter("CourseID").trim(); 
            String Type = request.getParameter("Type").trim(); 
            url = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC&characterEncoding=utf8";
            String sql="";
             Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
             PreparedStatement state;
             ResultSet result,result_course;
             Map<String, String> params = new HashMap<>(); 
             JSONObject jsonObject = new JSONObject(); 
             if(Type.equals("Student"))
                 sql="select *  from  student_sign where StudentID ="+ userID+" and CourseID ="+courseID;   
             else if(Type.equals("Teacher"))
                 sql="select *  from  student_sign where  CourseID ="+courseID; 
             state = conn.prepareStatement(sql);
             result = state.executeQuery();
             String temp="";
             while(result.next()){  
               if(Type.equals("Student")){
                 if(result.getString("Course_loginTime")==null)
                   temp=temp+"第"+result.getString("Course_jieshu")+"节课\n"+
                          "上课时间："+result.getString("Course_Time")+"\n" +
                          "签到时间：未签到"+"\n"+
                          "是否迟到："+result.getString("Course_isLated")+"\n\n";     
                 else
                      temp=temp+"第"+result.getString("Course_jieshu")+"节课\n"+
                          "上课时间："+result.getString("Course_Time")+"\n" +
                          "签到时间："+ result.getString("Course_loginTime")+"\n"+
                          "是否迟到："+result.getString("Course_isLated")+"\n\n";
               }
               else if(Type.equals("Teacher")){
                   sql="select StudentName  from  student_message where  StudentID ="+result.getString("StudentID"); 
                   state = conn.prepareStatement(sql);
                   result_course = state.executeQuery();
                   result_course.next();
                if(result.getString("Course_loginTime")==null)
                   temp=temp + "学生ID："+ result.getString("StudentID")+"\n"+
                           "学生姓名："+ result_course.getString("StudentName")+"\n"+
                           "第"+result.getString("Course_jieshu")+"节课\n"+
                          "上课时间："+result.getString("Course_Time")+"\n" +
                          "签到时间：未签到"+"\n"+
                          "是否迟到："+result.getString("Course_isLated")+"\n\n";     
                 else
                      temp=temp+ "学生ID："+ result.getString("StudentID")+"\n"+
                              "第"+result.getString("Course_jieshu")+"节课\n"+
                          "上课时间："+result.getString("Course_Time")+"\n" +
                          "签到时间："+ result.getString("Course_loginTime")+"\n"+
                          "是否迟到："+result.getString("Course_isLated")+"\n\n";
               
               
               }
                 
            }
           
                 params.put("Result", "success");
                 params.put("Data", temp);
            
             jsonObject.put("params", params);
            out.write(jsonObject.toString()); 
              } catch (SQLException ex) {
             Logger.getLogger(InsertCourse.class.getName()).log(Level.SEVERE, null, ex);
         } catch (ClassNotFoundException ex) {
             Logger.getLogger(InsertCourse.class.getName()).log(Level.SEVERE, null, ex);
         }
    }         
            
            

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
