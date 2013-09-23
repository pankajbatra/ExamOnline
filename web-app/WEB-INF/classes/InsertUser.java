import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class InsertUser extends HttpServlet{
	private ConnectionPool pool;
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		try{
			pool=new ConnectionPool(1,1);
		}
		catch(Exception e){
			throw new UnavailableException(this,"could not create connection pool");
		}
	}	
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		Connection con=null;
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		res.setHeader("Cache-Control","no-store"); 
		String name,middle,last,username,passwd,address,city,state,country,pin,dob,sex,tele,email,useremin,useansr;
		try{
			out.println("<HTML>");
			out.println("<HEAD>");
			out.println("<TITLE>ExamOnline - Registration Successfull</TITLE>");
			out.println("</HEAD>");
			out.println("</BODY>");
			name=req.getParameter("name");
			middle=req.getParameter("middle");
			last=req.getParameter("last");
			username=req.getParameter("username");
			passwd=req.getParameter("passwd");
			address=req.getParameter("address");
			city=req.getParameter("city");
			state=req.getParameter("state");
			country=req.getParameter("country");
			pin=req.getParameter("pin");
			dob=req.getParameter("dob");
			sex=req.getParameter("sex");
			tele=req.getParameter("tele");
			email=req.getParameter("email");
			useremin=req.getParameter("useremin");
			useansr=req.getParameter("useansr");
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			stmt.executeUpdate("insert into users values('"+username+"','"+name+"','"+middle+"','"+last+"','"+passwd+"','"+address+"','"+city+"','"+state+"','"+country+"',"+pin+",'"+dob+"',"+sex+","+tele+",'"+email+"','"+useremin+"','"+useansr+"')");
			out.println("<body bgcolor='#fofoff'>");
			out.println("<p align='center'><font color='#000000' face='verdana'><big><big><strong>User Registration successful</strong></big></big></font></p>");
			out.println("<hr>"); 			
			out.println("<p align='center'><small><strong><font color='#000000' face='verdana'>Congrats, "+name+" you have Successfully Registered with us <br>with a Username:- "+username+"</strong></small></p>");			
		    out.println("<p align='center'><font color='#000000' face='verdana'><br><a href='../examonline/user.html'>Click Here</a> to Login <br></p></BODY></HTML>");			
		    stmt.close();
		}
	
		catch(Exception e){
			try{
				out.println("<H2>An Error has occured: "+e.getMessage()+"</H2>");
				out.println("<a href='javascript:history.go(-2)'>Click Here</a> to go back & Try Again");
				con.rollback();				
			}
			catch (Exception ignored) { }
			
		}
		finally{
			
				if(con!=null) pool.returnConnection(con);
				out.close();		
		}
    }
    public String getServletInfo(){
		return "This servlet insert new users into database";
	}
}