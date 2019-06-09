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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class Login extends HttpServlet {
    
  
    
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
            System.out.println(Type+"    "+ID+"     "+Password);
            
            
             url = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC&characterEncoding=utf8";
            String sql="";
             if(Type.equals("Student"))
                 sql = "select * from student_message Where StudentID = "+ ID;
             else if(Type.equals("Teacher"))
                 sql = "select * from teacher_message Where TeacherID = "+ ID;
             else if(Type.equals("Manage"))
                 sql = "select * from manage_message Where ManageID = "+ ID;
           
             Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
            
            PreparedStatement state =conn.prepareStatement(sql);
            ResultSet result = state.executeQuery();
            
            Map<String, String> params = new HashMap<>(); 
            JSONObject jsonObject = new JSONObject(); 
            
            if(!result.next()){
                 params.put("Result", "failed");
                 params.put("Data","用户ID输入错误");
            }
            else{
              
                if(result.getString(""+Type+"_password").equals(Password)){
                    int sum=0,un_attendance_num=0,lated_num=0;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date1 = new Date(System.currentTimeMillis());
                    Date date2;
                    params.put("Result", "success");
                    params.put("Data","登陆成功");
                    params.put("Name",result.getString(""+Type+"Name"));
                    params.put("Emil",result.getString(""+Type+"_emil"));
                    if(Type.equals("Student")){
                    sql="select Course_isLated,Course_loginTime,Course_Time from student_sign where  StudentID ="+ ID;
                    state = conn.prepareStatement(sql);
                    result = state.executeQuery();
                    while(result.next()){
                       date2=simpleDateFormat.parse(result.getString("Course_Time"));
                       if(date1.compareTo(date2)<0)
                           continue;
                       sum=sum+1;
                       if(result.getString("Course_loginTime")!=null)
                           lated_num=lated_num+Integer.parseInt(result.getString("Course_isLated"));
                       else
                          un_attendance_num=un_attendance_num+1;
                       
                    }
                    params.put("Sum",""+sum );
                    params.put("lated_num",""+ lated_num);
                    params.put("un_attendance_num",""+un_attendance_num);
                    }
                }
                else{
                 
                    params.put("Result", "failed");
                    params.put("Data","密码输入错误");
                 }
              
            }
       
            jsonObject.put("params", params);
            out.write(jsonObject.toString()); 
            conn.close();
            
         } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
