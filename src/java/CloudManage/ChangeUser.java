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

public class ChangeUser extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ChangeUser</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChangeUser at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
            String Type = request.getParameter("Type").trim(); 
            String UserID = request.getParameter("UserID").trim();
            String UserName = request.getParameter("Name").trim();
            String UserEmil = request.getParameter("Emil").trim();
            String UserPassword = request.getParameter("Password").trim();
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","lijing0151122387");
            PreparedStatement state;
           int result;
            Map<String, String> params = new HashMap<>(); 
            JSONObject jsonObject = new JSONObject(); 
            String sql="";
            if(Type.equals("Student"))
               sql="update student_message set StudentName='"+UserName+"',Student_emil='"+UserEmil+"',Student_password='"+UserPassword+"' where StudentID="+UserID;
            else if(Type.equals("Teacher"))
                sql="update teacher_message set TeacherName='"+UserName+"',Teacher_emil='"+UserEmil+"',Teacher_password='"+UserPassword+"' where TeacherID="+UserID;
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
            Logger.getLogger(ChangeUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ChangeUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
