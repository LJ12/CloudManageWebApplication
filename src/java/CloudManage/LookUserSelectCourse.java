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
public class LookUserSelectCourse extends HttpServlet {
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
            url = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC&characterEncoding=utf8";
            String sql="";
             Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
             PreparedStatement state;
             ResultSet result,result_course,result_temp;
             Map<String, String> params = new HashMap<>(); 
             JSONObject jsonObject = new JSONObject(); 
             sql="select CourseID from user_course where UserID ="+ ID;    
             state = conn.prepareStatement(sql);
             result = state.executeQuery();
             String CourseID;
             String temp="";
             int jieshu=0;
             while(result.next()){
                        CourseID = result.getString("CourseID");
                        sql="select * from course_message where CourseID ="+ CourseID;    
                        state = conn.prepareStatement(sql);
                        result_course = state.executeQuery();
                        if(result_course.next()){
                           temp=temp+result_course.getString("CourseID")+" "+result_course.getString("CourseName")+" "+
                                   result_course.getString("Course_sum")+" ";
                        }
                        sql="select Course_jieshu from student_sign where CourseID ="+ CourseID ;
                        state = conn.prepareStatement(sql);
                        result_temp = state.executeQuery();
                        jieshu=0;
                        int jieshu_temp;
                        while(result_temp.next()){
                            jieshu_temp = Integer.parseInt(result_temp.getString("Course_jieshu"));
                            if(jieshu_temp>jieshu){
                                jieshu=jieshu_temp;
                            }
                       
                        }
                        temp=temp+jieshu+"\t";
             }
             if(temp.isEmpty()){
                 params.put("Result", "failed");
                 params.put("Data", "暂无选课信息");
             }
             else{
                 params.put("Result", "success");
                 params.put("Data", temp);
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
