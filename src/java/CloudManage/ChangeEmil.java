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
public class ChangeEmil extends HttpServlet {

   
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
            String Password = request.getParameter("Password").trim();
            String Emil = request.getParameter("Emil").trim();
             url = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC&characterEncoding=utf8";
            String sql="";
             if(Type.equals("Student"))
                 sql = "select Student_password from student_message Where StudentID = "+ ID;
             else if(Type.equals("Teacher"))
                 sql = "select Teacher_password from teacher_message Where TeacherID = "+ ID;
              else if(Type.equals("Manage"))
                  sql = "select Manage_password from manage_message Where ManageID = "+ ID;
            
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
            
            PreparedStatement state =conn.prepareStatement(sql);
            ResultSet result = state.executeQuery();
            
             Map<String, String> params = new HashMap<>(); 
            JSONObject jsonObject = new JSONObject(); 
             result.next();
             if(result.getString(""+Type+"_password").equals(Password)){
                    if(Type.equals("Student"))
                           sql = "update student_message set Student_emil = '" + Emil + "' Where StudentID = "+ ID;
                     else if(Type.equals("Teacher"))
                           sql = "update teacher_message set Teacher_emil = '" + Emil + "' Where TeacherID = "+ ID;
                     else if(Type.equals("Manage"))
                         sql = "update manage_message set Manage_emil = '" + Emil + "' Where ManageID = "+ ID;
                    if(state.executeUpdate(sql)>0){
                    params.put("Result", "success");
                    params.put("Data","修改Emil成功");
               
                    }
                    else{
                    
                    params.put("Result", "failed");
                    params.put("Data","数据库忙，请稍后重试");
                    }
                }
                else{
                    params.put("Result", "failed");
                    params.put("Data","密码输入错误");
                 }
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
