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
public class InsertUser extends HttpServlet {
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
            String Type = request.getParameter("Type").trim(); 
            String ID = request.getParameter("ID").trim(); 
            String Name = request.getParameter("Name").trim();
            String Emil = request.getParameter("Emil").trim();
            url = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC&characterEncoding=utf8";
            String sql="";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
            
             Map<String, String> params = new HashMap<>(); 
             JSONObject jsonObject = new JSONObject(); 
             PreparedStatement state;
             if(Type.equals("Student")){
                sql = "select * from student_message where StudentID="+ID;
                state =conn.prepareStatement(sql);
                ResultSet result = state.executeQuery(); 
                if(result.next()){
                      params.put("Result", "failed");
                      params.put("Data","添加失败，用户id已经被注册");
                       jsonObject.put("params", params);
                       out.write(jsonObject.toString()); 
                      return;
                }
                  
                else{
                      sql = "insert into student_message(StudentID,StudentName,Student_emil) values("+ID+",'"+Name+"','"+Emil+"')";
                }
             }
             else if(Type.equals("Teacher")){
                sql = "select * from teacher_message where TeacherID ="+ID;
                state =conn.prepareStatement(sql);
                ResultSet result = state.executeQuery(); 
                if(result.next()){
                      params.put("Result", "failed");
                      params.put("Data","添加失败，用户id已经被注册");
                      jsonObject.put("params", params);
                      out.write(jsonObject.toString()); 
                      return;
                }
                else{
                      sql = "insert into teacher_message(TeacherID,TeacherName,Teacher_emil) values("+ID+",'"+Name+"','"+Emil+"')";
                }
             }
             state = conn.prepareStatement(sql);
             if(state.executeUpdate(sql)>0){
                    params.put("Result", "success");
                    params.put("Data","添加用户信息成功");
                  }
                    else{
                    
                    params.put("Result", "failed");
                    params.put("Data","添加失败，请重试");
                   }
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
