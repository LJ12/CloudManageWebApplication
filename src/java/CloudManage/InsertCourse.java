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
public class InsertCourse extends HttpServlet {
    Map<String, String> params = new HashMap<>(); 
    JSONObject jsonObject = new JSONObject(); 
    
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
            String courseName = request.getParameter("CourseName").trim();
            String courseID = request.getParameter("CourseID").trim();
            String Type = request.getParameter("Type").trim();
            String addType=request.getParameter("addType").trim();
            url = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC&characterEncoding=utf8";
            String sql="";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
             PreparedStatement state;
             ResultSet result;
             String data="";
             if(addType.equals("1")){
                   String [] coursesID=courseID.split(",");
                   String [] coursesName = courseName.split(",");
                   params.put("Result", "success");
                   for(int k=0;k<coursesID.length;k++){
                        sql = "insert into user_course(UserID,CourseID,UserType) values("+ID+","+coursesID[k]+",'"+Type+"')";
                        state = conn.prepareStatement(sql);
                        if(state.executeUpdate(sql)>0){
                           data=data+"添加课程 "+coursesID[k]+" : "+coursesName[k]+" 成功\n";
                         }
                         else{ 
                           data=data+"添加课程 "+coursesID[k]+" : "+coursesName[k]+" 失败\n";
                         }
                   }
                   params.put("Data",data);
             }
             else if(addType.equals("2")){
             sql="select * from course_message where CourseID ="+courseID;    
             state = conn.prepareStatement(sql);
             result = state.executeQuery();
             if(result.next()){
                 if(result.getString("CourseName").equals(courseName)){
                        sql="select * from user_course where CourseID ="+courseID+" and UserID="+ID ;    
                        state = conn.prepareStatement(sql);
                        result = state.executeQuery();
                        if(result.next()){
                            params.put("Result", "failed");
                            params.put("Data","课程已经添加，请勿重复提交");
                        }
                        else{
                        sql = "insert into user_course(UserID,CourseID,UserType) values("+ID+","+courseID+",'"+Type+"')";
                        state = conn.prepareStatement(sql);
                        if(state.executeUpdate(sql)>0){
                            params.put("Result", "success");
                             params.put("Data","添加课程信息成功");
                         }
                         else{ 
                                   params.put("Result", "failed");
                                   params.put("Data","数据库忙，请稍候重试");
                           }
                        }
                 }
                 else{
                     params.put("Result", "failed");
                     params.put("Data","课程ID和课程名不符，请检查输入");
                 }
             }
             else{
              params.put("Result", "failed");
                     params.put("Data","课程ID不存在，请检查输入");
             }
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
