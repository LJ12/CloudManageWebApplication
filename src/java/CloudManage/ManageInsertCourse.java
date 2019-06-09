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
public class ManageInsertCourse extends HttpServlet {
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
            String ID = request.getParameter("ID").trim(); 
            String Name = request.getParameter("Name").trim();
            int num=Integer.parseInt(request.getParameter("Number").trim());
            url = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC&characterEncoding=utf8";
            String sql="";
            Class.forName("com.mysql.jdbc.Driver");
             conn = DriverManager.getConnection(url,"root","lijing0151122387");
            
             Map<String, String> params = new HashMap<>(); 
             JSONObject jsonObject = new JSONObject(); 
             PreparedStatement state;
             sql = "select * from course_message where CourseID="+ID;
             state =conn.prepareStatement(sql);
             ResultSet result = state.executeQuery(); 
             if(result.next()){
                  params.put("Result", "failed");
                  params.put("Data","添加失败，此id已经被注册");
                  jsonObject.put("params", params);
                  out.write(jsonObject.toString()); 
                  return;
              }   
                else{
                      sql = "insert into course_message(CourseID,CourseName,Course_sum) values("+ID+",'"+Name+"',"+num+")";
                }
            
             state = conn.prepareStatement(sql);
             if(state.executeUpdate(sql)>0){
                    params.put("Result", "success");
                    params.put("Data","添加课程信息成功");
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

