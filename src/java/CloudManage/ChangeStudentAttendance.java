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
public class ChangeStudentAttendance extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         response.setContentType("text/html;charset=utf-8"); 
         request.setCharacterEncoding("utf-8"); 
         response.setCharacterEncoding("utf-8"); 
         try (PrintWriter out = response.getWriter()) { 
            Connection conn=null;
            String  url = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC&characterEncoding=utf8";
            String StudentID = request.getParameter("StudentID").trim(); 
            String CourseID = request.getParameter("CourseID").trim(); 
            String [] jieshus = request.getParameter("jieshu").trim().split(",");
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
            PreparedStatement state;
            ResultSet result;
            boolean flag=false;
            Map<String, String> params = new HashMap<>(); 
            JSONObject jsonObject = new JSONObject(); 
            String sql1,sql2;
            int i;
            String data="";
            for (i=0;i<jieshus.length;i++){
                sql1="select Course_Time,Course_Latitude,Course_Longitude from student_sign where StudentID="+StudentID+" and CourseID="+CourseID+" and Course_jieshu="+ jieshus[i];
                state =conn.prepareStatement(sql1);
                result = state.executeQuery();
                if(result.next()){
                    sql2="update student_sign set Course_loginTime='"+result.getString("Course_Time")+"',Course_isLated=0,"+
                           "Login_Latitude='"+result.getString("Course_Latitude")+"',Login_Longitude='"+result.getString("Course_Longitude")
                            +"' where StudentID="+StudentID+" and CourseID="+CourseID+" and Course_jieshu="+ jieshus[i];
                    state =conn.prepareStatement(sql2);
                    int t=state.executeUpdate();
                    if(t==1){
                      data=data+"第"+jieshus[i]+"节课签到记录修改成功\n";
                    }else{
                        flag=true;
                      data=data+"第"+jieshus[i]+"节课签到记录修改失败\n";
                    }
                }
            }
            if(flag){
                params.put("Result","failed");
                params.put("Data",data);
            }else
                params.put("Result","success");
            jsonObject.put("params", params);
            out.write(jsonObject.toString()); 
         }   catch (ClassNotFoundException ex) {
            Logger.getLogger(ChangeStudentAttendance.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ChangeStudentAttendance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
