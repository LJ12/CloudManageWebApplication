/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import CloudManage.SendEmil;
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
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

public class LosePassword extends HttpServlet {
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
            String Emil = request.getParameter("Emil").trim();
           
             url = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC&characterEncoding=utf8";
            String sql="";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
            PreparedStatement state;
            ResultSet result;
             Map<String, String> params = new HashMap<>(); 
            JSONObject jsonObject = new JSONObject(); 
            
            sql = "select * from manage_message Where ManageID = "+ ID;
            state =conn.prepareStatement(sql);
            result = state.executeQuery();
            
            if(!result.next()){
                  sql = "select * from student_message Where StudentID = "+ ID;
                  state =conn.prepareStatement(sql);
                  result = state.executeQuery(); 
                  if(!result.next()){
                         sql = "select * from teacher_message Where TeacherID = "+ ID;
                         state =conn.prepareStatement(sql);
                         result = state.executeQuery();
                         if(!result.next()){
                             params.put("Result", "failed");
                             params.put("Data","用户ID输入错误，数据库中没有此ID用户信息");
                         }
                         else{
                             if(result.getString("Teacher_emil").equals(Emil)){
                                  SendEmil.send(Emil, result.getString("TeacherName"), result.getString("Teacher_password"));
                                  params.put("Result", "sucess");
                                  params.put("Data","已经将您的密码发送到你的Emil,请注意查收");
                             }
                             else{
                                 params.put("Result", "failed");
                                 params.put("Data","用户ID与用户Emil不符，请检查输入");
                            }
                         }
                    }
                  else{
                             if(result.getString("Student_emil").equals(Emil)){
                                  SendEmil.send(Emil, result.getString("StudentName"), result.getString("Student_password"));
                                  params.put("Result", "sucess");
                                  params.put("Data","已经将您的密码发送到你的Emil,请注意查收");
                             }
                             else{
                                 params.put("Result", "failed");
                                 params.put("Data","用户ID与用户Emil不符，请检查输入");
                            }
                    }
            }
            else{
                        if(result.getString("Manage_emil").equals(Emil)){
                                  SendEmil.send(Emil, result.getString("ManageName"), result.getString("Manage_password"));
                                  params.put("Result", "sucess");
                                  params.put("Data","已经将您的密码发送到你的Emil,请注意查收");
                         }
                         else{
                                 params.put("Result", "failed");
                                 params.put("Data","用户ID与用户Emil不符，请检查输入");
                            }
                         }
            jsonObject.put("params", params);
            out.write(jsonObject.toString()); 
        } catch (SQLException ex) {
            Logger.getLogger(LosePassword.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(LosePassword.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ClassNotFoundException ex) {
            Logger.getLogger(LosePassword.class.getName()).log(Level.SEVERE, null, ex);
        } 
       
 }
   
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
