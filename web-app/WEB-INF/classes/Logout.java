import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Logout extends HttpServlet{
	public void init(ServletConfig config) throws ServletException{
		super.init(config);		
	}	
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		HttpSession session=req.getSession(false); 		
		PrintWriter out=res.getWriter();		
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>ExamOnline - LogOut</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY>");		
		if(session!=null)
		  session.invalidate();
		out.println("<body bgcolor='#B3B3D9'>");
		out.println("<Script language='javascript'>document.location='../examonline/logout.html'</script>");
		out.println("</BODY></HTML>");
		out.close();
	}    
    public String getServletInfo(){
		return "Logout Servlet for ExamOnline ";
	}
}