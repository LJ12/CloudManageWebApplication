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
import java.util.Calendar;
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
public class CreatQRCode extends HttpServlet {
      @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request,response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       // TeacherID=111112&CourseID=2111112&jieshu=1&StartTimeHour=4&StartTimeMinute=20&LastTime=1
         response.setContentType("text/html;charset=utf-8"); 
         request.setCharacterEncoding("utf-8"); 
         response.setCharacterEncoding("utf-8"); 
         try (PrintWriter out = response.getWriter()) { 
            Connection conn=null;
            String url="";
            String teacherID = request.getParameter("TeacherID").trim(); 
            String courseID = request.getParameter("CourseID").trim(); 
            String jieshu = request.getParameter("jieshu").trim(); 
            String startTime = request.getParameter("StartTimeHour").trim()+":"+request.getParameter("StartTimeMinute").trim()+":00";
            String lastTime=request.getParameter("LastTime").trim(); 
            double La=Double.parseDouble(request.getParameter("Latitude").trim()); 
            double Lo=Double.parseDouble(request.getParameter("Longitude").trim()); 
            url = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC&characterEncoding=utf8";
            String sql="";
             Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
             PreparedStatement state;
             ResultSet result;
             Map<String, String> params = new HashMap<>(); 
             JSONObject jsonObject = new JSONObject(); 
             sql="select *  from  course_message where CourseID ="+ courseID;    
             state = conn.prepareStatement(sql);
             result = state.executeQuery();
             String jieshu_sum;
           
             if(result.next()){
                  jieshu_sum=result.getString("Course_sum");
                  if(Integer.parseInt(jieshu_sum)<Integer.parseInt(jieshu)){
                        params.put("Result", "failed");
                        params.put("Data", "这门课程学时已经完成，请重新选择");
                         jsonObject.put("params", params);
                         out.write(jsonObject.toString()); 
                         return;
                  }
             }
             else{
                 params.put("Result", "failed");
                 params.put("Data", "这门课程已经结课，请重新选择");
                 jsonObject.put("params", params);
                  out.write(jsonObject.toString()); 
                   return;
             }
             sql="select *  from  user_course where CourseID ="+ courseID+" and UserType = 'Student'";    
             state = conn.prepareStatement(sql);
             result = state.executeQuery();
             String temp="";
             String studentID;
             String date;
             Calendar calendar = Calendar.getInstance(); 
             int month = calendar.get(Calendar.MONTH)+1;
             date=""+calendar.get(Calendar.YEAR)+"-"+month+"-"+calendar.get(Calendar.DAY_OF_MONTH);
             while(result.next()){  
                 studentID=result.getString("UserID");
                 sql="insert into student_sign(StudentID,CourseID,Course_jieshu,Course_LastTime,Course_Time,Course_Latitude,Course_Longitude) values("+studentID+","+
                        courseID+","+jieshu+","+lastTime+",'"+date+" "+startTime+"',"+La+","+Lo+")"; 
                 state = conn.prepareStatement(sql);
                 state.executeUpdate();
            }
        
             params.put("Result", "success");
             params.put("Data", "");
            
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
