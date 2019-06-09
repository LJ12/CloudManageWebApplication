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
public class GetCourseJieshuSum extends HttpServlet {

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
            String teacherID = request.getParameter("teacherID").trim(); 
            String CourseID = request.getParameter("CourseID").trim(); 
            String CourseName = request.getParameter("CourseName").trim(); 
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
            PreparedStatement state;
            ResultSet result;
            Map<String, String> params = new HashMap<>(); 
            JSONObject jsonObject = new JSONObject(); 
            String sql="select Course_jieshu,Course_Time,Course_LastTime from student_sign where CourseID = " + CourseID;
            state =conn.prepareStatement(sql);
            result = state.executeQuery();
            String data="",temp="";
             SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while(result.next()){
                if(temp.indexOf(""+result.getInt("Course_jieshu"))<0){
                    temp=temp+result.getString("Course_jieshu")+",";
                    Date date2=simpleDateFormat.parse(result.getString("Course_Time"));
                    Date date3= new Date(simpleDateFormat.parse(result.getString("Course_Time")).getTime()+60*60*1000*Integer.parseInt(result.getString("Course_LastTime")));     
                    data=data+"课程名："+CourseName+" 课程节数："+result.getInt("Course_jieshu")+" 开始时间："+result.getString("Course_Time")+" 结束时间："+simpleDateFormat.format(date3)+
                            " &CourseID="+CourseID+"&jieshu="+result.getInt("Course_jieshu")+"\n";
                }
            }
              if(data==""){
                params.put("Result","failed");
                params.put("Data","暂无记录");
            }else{
                params.put("Result","success");
                params.put("Data",data);
              }
            jsonObject.put("params", params);
            out.write(jsonObject.toString()); 
        }   catch (ClassNotFoundException ex) {
            Logger.getLogger(GetCourseJieshuSum.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GetCourseJieshuSum.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(GetCourseJieshuSum.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
