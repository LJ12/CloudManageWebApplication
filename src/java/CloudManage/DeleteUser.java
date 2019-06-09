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
public class DeleteUser extends HttpServlet {
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
            String [] IDS = request.getParameter("ID").trim().split(","); 
            String Type = request.getParameter("Type").trim(); 
             url = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC&characterEncoding=utf8";
             String sql="";
             Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
             PreparedStatement state;
            ResultSet result;
             Map<String, String> params = new HashMap<>(); 
            JSONObject jsonObject = new JSONObject(); 
            String ID,data="";
            params.put("Result","success");
            for(int j=0;j<IDS.length;j++){
              ID=IDS[j];
             if(Type.equals("Student")){
                 sql = "select * from student_message Where StudentID = "+ ID;
                 state =conn.prepareStatement(sql);
                 result = state.executeQuery();
               if(result.next()){
                 sql="delete from student_message where StudentID = " + ID;
                 state =conn.prepareStatement(sql);
                 if(state.executeUpdate()>0){
                     data=data+"学生："+ID+"删除成功\n";
                 }
                 else{
                     data=data+"学生："+ID+"删除失败，数据库忙，请稍后重试\n";
                 }
               }
               else{
                 data=data+"学生："+ID+"删除失败，用户ID输入错误，数据库无此用户信息\n";
               }
            }
           else if (Type.equals("Teacher")){
                sql = "select * from teacher_message Where TeacherID = "+ ID;
                state =conn.prepareStatement(sql);
                result = state.executeQuery();
                 if(result.next()){
                      sql="delete from teacher_message where TeacherID = " + ID;
                      state =conn.prepareStatement(sql);
                      if(state.executeUpdate()>0){
                        data=data+"教师："+ID+"删除成功\n";
                      }
                     else{
                       data=data+"教师："+ID+"删除失败，数据库忙，请稍后重试\n";
                     }
                 }
                 else{
                    data=data+"学生："+ID+"删除失败，用户ID输入错误，数据库无此用户信息\n";
                 }
           }
           else if (Type.equals("Course")){
                sql = "select * from course_message Where CourseID = "+ ID;
                state =conn.prepareStatement(sql);
                result = state.executeQuery();
                 if(result.next()){
                      sql="delete from course_message where CourseID = " + ID;
                      state =conn.prepareStatement(sql);
                      if(state.executeUpdate()>0){
                         data=data+"课程："+ID+"删除成功\n";
                      }
                     else{
                          data=data+"课程："+ID+"删除失败，数据库忙，请稍后重试\n";
                     }
                 }
                 else{
                    data=data+"课程："+ID+"删除失败，课程ID输入错误，数据库无此课程信息\n";
                 }
            }
            }
             params.put("Data",data);
             jsonObject.put("params", params);
             out.write(jsonObject.toString()); 
              } catch (SQLException ex) {
                      Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
