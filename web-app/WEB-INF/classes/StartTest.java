import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class StartTest extends HttpServlet{
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
	public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		Connection con = null;
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);
		int count = 0,test_code=0,test_status=0,test_no_subject=0;
		String aduser=null;
		aduser = (String)session.getValue("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||aduser==null||accessed.before(time_comp)){
			session.invalidate();
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
			return;
		}
		try{
			con = pool.getConnection();
			String tcode = req.getParameter("test_code");
			test_code = Integer.parseInt(tcode);
			Statement stmt = con.createStatement();
			ResultSet rs=stmt.executeQuery("select * from test_master where aduser='"+aduser+"' AND test_code="+test_code);
			int c=0;
			count=0;
			while(rs.next()){
			 	c++;
			 	test_no_subject= rs.getInt("test_no_subject");
			 	count=test_no_subject;
			}
			if(c==0){
				session.invalidate();
		    	out.println("<H2>Your Session has expired </H2>");
				out.println("<a href='../examonline/admin.html'>Click Here</a> To Re-Login");
				return;
			}		
			int i=0;
			String s[] = new String[count];
			for(i=0;i<count;i++){
				s[i]="test_"+test_code+"_"+i;
			}
			rs = stmt.executeQuery("select * from test_info where test_code="+test_code);
			int k[] = new int[count];
			String p[] = new String[count];	
			i=0;	
			while(rs.next()){		 
			 	k[i] = rs.getInt("subject_no_ques");
		 		p[i] = rs.getString("subject_name");
		 		i++;
			}
			out.println("<html>");
        	out.println("<head>");
        	out.println("<title>Getting Started With ExamOnline</title>");
        	out.println("</head>");
        	out.println("<body topmargin='0' leftmargin='0' link='#000000' vlink='#000000' alink='#000000'>");
        	out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='24'>");
        	out.println("<tr>");
        	out.println("<td width='42%' height='24'></td>");
        	out.println("<td width='28%' height='24'></td>");
        	out.println("<td width='5%' height='24'></td>");
        	out.println("<td width='25%' height='24'></td>");
        	out.println("</tr>");
        	out.println("</table>");
        	out.println("<div align='right'>");
        	out.println("<table border='0' width='98%' cellspacing='0' cellpadding='0'>");
        	out.println("<tr>");
        	out.println("<td width='100%' height='10' colspan='2'></td>");
        	out.println("</tr>");
        	out.println("<tr>");
        	out.println("<td width='100%' colspan='2'><strong><small><font face='Verdana'>Welcome To ExamOnline Contol Panel</font></small></strong></td>");
        	out.println("</tr>");
        	out.println("<tr>");
        	out.println("<td width='64%' bgcolor='#C0C0C0' height='1'></td>");
        	out.println("<td width='16%' bgcolor='#FFFFFF' height='1'></td>");
        	out.println("</tr>");
        	out.println("</table>");
        	out.println("</div><div align='right'>");
        	out.println("<table border='0' width='93%' height='20' cellspacing='0' cellpadding='0'>");
        	out.println("<tr>");
        	out.println("<td width='100%'></td>");
        	out.println("</tr>");
        	out.println("</table>");
        	out.println("</div><div align='right'>");
        	out.println("<table border='1' width='97%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
        	out.println("<tr>");
        	out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Checking status...</strong></small></font></td>");
        	out.println("</tr>");
        	out.println("</table>");
        	out.println("</div><div align='right'>");
        	out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='181' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
        	out.println("<tr>");
        	out.println("<td width='100%' bgcolor='#F0F0FF' height='181' valign='top' align='center'><table border='0' width='100%' cellspacing='0' cellpadding='0'>");
        	c=0;
			int status=1;
			for(i=0;i<count;i++){
		 		rs = stmt.executeQuery("select count(*) from "+s[i]+"");
		 		while(rs.next()){
		 			c = rs.getInt("count(*)");
		 			if(c<k[i]){
		 				out.println("<tr>");
                    	out.println("<td width='100%' height='30'><p align='center'><strong><font face='Verdana' color='#FF0000'>!&nbsp;&nbsp;</font></strong><font face='Verdana' color='#8000FF'><small>Number of questions in subject:-<strong>"+p[i]+" </strong>is not sufficient. </small></font></td>");
                    	out.println("</tr>");
		 				status=status*0;
		 			}
		 			else{
		 				out.println("<tr>");
                		out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><small>Number of questions in subject:-<strong>"+p[i]+" </strong>is sufficient.</small></font></td>");
                		out.println("</tr>");	
		 				status=status*1;
		  			}		 		  	 
				}			
			}
			if(status==1){
				test_status = 1;
				stmt.executeUpdate("update test_master set test_status="+test_status+" where test_code="+test_code);
				out.println("<tr>");
        		out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><strong><small>Test started successfully.</small></strong></font></td>");
        		out.println("</tr>");
        	}
        	else{
        		test_status = 0;
        		stmt.executeUpdate("update test_master set test_status="+test_status+" where test_code="+test_code+"");
				out.println("<tr>");
        		out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#8000FF'><strong><small>Sorry! This test cannot be started <br>The number of questions to generate a paper are not enough.<br>Please add more questions to the test to start the test.</small></strong></font></td>");
        		out.println("</tr>");	
       		}
        	out.println("<tr>");
        	out.println("<td width='100%' height='30'><p align='center'><font face='Verdana' color='#6767B4'><small><a href='/servlet/AdminControlMiddle' target='_self'>Click here</a> to continue...</small></font></td>");
        	out.println("</tr>");
        	out.println("</table>");
	        out.println("</td>");
            out.println("</tr>");
        	out.println("</table>");
    	    out.println("</div>");
	        out.println("</body>");
        	out.println("</html>");        
			rs.close();
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
		return "Servlet to start an inactive test";
	}	
}