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

public class StudentSign extends HttpServlet {
      private static final double EARTH_RADIUS = 6378.137;
      
         @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request,response);
    }
  
    private  double rad(double d) {
        return d * Math.PI / 180.0;
    }

   private double getDistance(double longitude1, double latitude1,double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        //有小数的情况;注意这里的10000d中的“d”
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;//单位：米
        return s;
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
            String userID = request.getParameter("UserID").trim(); 
            String courseID = request.getParameter("CourseID").trim(); 
            String jieshu = request.getParameter("jieshu").trim(); 
            Double La=Double.parseDouble(request.getParameter("Latitude").trim()); 
            Double Lo=Double.parseDouble(request.getParameter("Longitude").trim()); 
            Double C_La,C_Lo;
            url = "jdbc:mysql://localhost/mydatabase?serverTimezone=UTC&characterEncoding=utf8";
            String sql="";
             Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
             PreparedStatement state;
             ResultSet result;
             Map<String, String> params = new HashMap<>(); 
             JSONObject jsonObject = new JSONObject(); 
             sql="select Course_Latitude,Course_Longitude,Course_loginTime,Course_Time,Course_LastTime from student_sign where  CourseID ="+courseID+" and StudentID="+userID+" and Course_jieshu="+jieshu; 
             state = conn.prepareStatement(sql);
             result = state.executeQuery();
             if(result.next()){
             C_La=Double.parseDouble(result.getString("Course_Latitude"));
             C_Lo=Double.parseDouble(result.getString("Course_Longitude"));
             if(result.getString("Course_loginTime")!=null){
                 params.put("Result", "failed");
                 params.put("Data", "本节课程您已经签到，请勿重复扫码");
                 jsonObject.put("params", params);
              out.write(jsonObject.toString()); 
              return;
             }
             
             SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             Date date1 = new Date(System.currentTimeMillis());
             Date date2;
             date2=simpleDateFormat.parse(result.getString("Course_Time"));
             Date date3= new Date(simpleDateFormat.parse(result.getString("Course_Time")).getTime()+60*60*1000*Integer.parseInt(result.getString("Course_LastTime")));
             double distent=getDistance(Lo,La,C_Lo,C_La);
             if(date1.compareTo(date2)<0){
                 if(distent<=50){
                  params.put("Result", "success");
                  params.put("Data", "签到成功");
                  sql="update student_sign set Course_loginTime='"+simpleDateFormat.format(date1)+"',Course_isLated=0 "+
                          ",Login_Latitude="+La+",Login_Longitude="+Lo+
                          " where  CourseID ="+courseID+" and StudentID="+userID+" and Course_jieshu="+jieshu; 
                  state = conn.prepareStatement(sql);
                  state.executeUpdate();
                 }
                 else{
                     params.put("Result", "failed");
                     params.put("Data", "签到失败，你距离教室约"+distent+"米，不在教室范围内");
                 }
             }
             else if(date1.compareTo(date3)<0){
                   if(distent<=30){
                  params.put("Result", "success");
                  params.put("Data", "签到成功，您已经迟到");
                  sql="update student_sign set Course_loginTime='"+simpleDateFormat.format(date1)+"',Course_isLated=1 "+
                            ",Login_Latitude="+La+",Login_Longitude="+Lo+
                          "where  CourseID ="+courseID+" and StudentID="+userID+" and Course_jieshu="+jieshu; 
                  state = conn.prepareStatement(sql);
                  state.executeUpdate();
                   }
                 else{
                     params.put("Result", "failed");
                     params.put("Data", "签到失败，你距离教室约"+distent+"米，不在教室范围内");
                 }
             }
             else{
                 params.put("Result", "failed");
                 params.put("Data", "签到失败，本节课程已经结束");
             }
             }else{
              params.put("Result", "failed");
                 params.put("Data", "签到失败,您并未选择此节课程");
             }
         
             jsonObject.put("params", params);
              out.write(jsonObject.toString()); 
              } catch (SQLException ex) {
             Logger.getLogger(InsertCourse.class.getName()).log(Level.SEVERE, null, ex);
         } catch (ClassNotFoundException ex) {
             Logger.getLogger(InsertCourse.class.getName()).log(Level.SEVERE, null, ex);
         } 
         catch (ParseException ex) {
                 Logger.getLogger(StudentSign.class.getName()).log(Level.SEVERE, null, ex);
         } 
    }         
        
            

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
