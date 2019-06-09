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
public class ChangeCourse extends HttpServlet {

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
            String ID = request.getParameter("CourseID").trim();
            String Name = request.getParameter("Name").trim();
            String Sum = request.getParameter("Sum").trim();
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
            PreparedStatement state;
            int result;
            Map<String, String> params = new HashMap<>(); 
            JSONObject jsonObject = new JSONObject(); 
            String  sql="update course_message set CourseName='"+Name+"',Course_sum="+Sum+" where CourseID="+ID;
             state =conn.prepareStatement(sql);
            result = state.executeUpdate();
            if(result>=1){
                params.put("Result", "success");
            }else{
                 params.put("Result", "failed");
                 params.put("Data", "数据库访问失败，请重试");
            }
             jsonObject.put("params", params);
            out.write(jsonObject.toString()); 
         } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChangeCourse.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ChangeCourse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
