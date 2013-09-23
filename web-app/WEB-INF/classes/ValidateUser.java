import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;

public class ValidateUser extends HttpServlet{
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
		res.setHeader("Cache-Control","no-store"); 		
		PrintWriter out=res.getWriter();		
		String name,middle,last,username,passwd,vpasswd,address,city,state,country,pin,dob,sex,tele,email,useremin,useansr;
		name=req.getParameter("name");
		middle=req.getParameter("middle");
		last=req.getParameter("last");
		username=req.getParameter("username");
		passwd=req.getParameter("passwd");
		vpasswd=req.getParameter("vpasswd");
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
		if((name==null)||(last==null)||(username==null)||(passwd==null)||(vpasswd==null)||(address==null)||(city==null)||(state==null)||(country==null)||(pin==null)||(tele==null)||(dob==null)||(email==null)||(useansr==null)||name.equals("")||last.equals("")||username.equals("")||passwd.equals("")||vpasswd.equals("")||address.equals("")||city.equals("")||state.equals("")||country.equals("")||pin.equals("")||dob.equals("")||tele.equals("")||email.equals("")||useansr.equals("")||(!(validate1(name)&&validate1(last)&&validate2(username)&&validate3(passwd)&&validate5(city)&&validate5(state)&&validate6(country)&&validate7(pin)&&validate8(tele)&&validate9(useansr)))||(!checkdate(dob))||(!checkmail(email))){
			out.println("<HTML>");
			out.println("<HEAD>");
			out.println("<TITLE>ExamOnline - Fill User Registration Form</TITLE>");
			out.println("</HEAD>");
			out.println("<BODY>");
			out.println("<!--webbot bot='HTMLMarkup' startspan TAG='XBOT' -->");
			out.println("<IFRAME SRC='../header.html' NAME='head' WIDTH='100%' HEIGHT='175' MARGINWIDTH='0' MARGINHEIGHT='0' FRAMEBORDER='0' noresize SCROLLING=NO>");
			out.println("<!--webbot bot='HTMLMarkup' endspan -->");
			out.println("<!--webbot bot='HTMLMarkup' startspan TAG='XBOT' -->");
			out.println("</IFRAME><!--webbot bot='HTMLMarkup' endspan -->");
			out.println("<table border='1' width='100%' bgcolor='#f0f0ff' bordercolor='#000000' cellspacing='0' cellpadding='0'>");
			out.println("<tr><td width='100%'>&nbsp;<p align='left'><font face='Verdana'color='#000000'>");
			out.println("<big>&nbsp;&nbsp;&nbsp;&nbsp;New User Registration</big></font></p>");
			out.println("<hr size='1' color='#000000' width='90%'>");
			out.println("<p align='left'><small><strong><font face='Verdana'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			out.println("Some of the fields were left blank or not according to rules<BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			out.println("Please fill the item shown in RED COLOR, again");
			out.println("</font></strong></small><br></p>");
			out.println("<form action='/servlet/ValidateUser' method='POST'>");
			out.println("<div align='center'><center><table border='0' width='104%' cellspacing='0' cellpadding='0'>");
			if(name!=null&&validate1(name)&&(!name.equals(""))){
				out.println("<tr>");
				out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>First Name:</font></strong></small></td>");
				out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+name+"</font></small></td>");
				out.println("<input type=hidden name='name' value='"+name+"'>");
				out.println("</tr>");
			}
			else{
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='right'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>First Name:</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><input type='text' name='name' size='20' tabindex='1'  style='border: 1px solid rgb(0,0,128)'></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
			}
			if(middle!=null&&(!middle.equals(""))){
				out.println("<tr>");
				out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>Middle Name:</font></strong></small></td>");
				out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+middle+"</font></small></td>");
				out.println("<input type=hidden name='middle' value='"+middle+"'>");
				out.println("</tr>");
			}
			if(middle.equals("")){
				out.println("<tr>");
				out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>Middle Name:</font></strong></small></td>");
				out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'> </font></small></td>");
				out.println("<input type=hidden name='middle' value=''>");
				out.println("</tr>");
			}
        	if(last!=null&&validate1(last)&&(!last.equals(""))){
				out.println("<tr>");
				out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>Last Name:</font></strong></small></td>");
				out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+last+"</font></small></td>");
				out.println("<input type=hidden name='last' value='"+last+"'>");
				out.println("</tr>");
			}
			else{
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='right'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>Last Name:&nbsp;</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><input type='text' name='last' size='20'  style='border: 1px solid rgb(0,0,128)'></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
			}
			if(username!=null&&validate2(username)&&(!username.equals(""))){
				out.println("<tr>");
				out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>User Name:</font></strong></small></td>");
				out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+username+"</font></small></td>");
				out.println("<input type=hidden name='username' value='"+username+"'>");
				out.println("</tr>");
			}
			else{
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='right'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>User Name:</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><input type='text' name='username' size='20'  style='border: 1px solid rgb(0,0,128)'></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
			}			
			if(passwd!=null&&validate3(passwd)&&passwd.equals(vpasswd)&&(!passwd.equals(""))){
				out.println("<tr>");				
				out.println("<input type=hidden name='passwd' value='"+passwd+"'>");
				out.println("<input type=hidden name='vpasswd' value='"+passwd+"'>");
				out.println("</tr>");
			}
			else{
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='rightt'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>Password:</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><input type='password' name='passwd' size='20'  style='border: 1px solid rgb(0,0,128)'></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='right'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>Confirm Password:</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><input type='password' name='vpasswd' size='20' style='border: 1px solid rgb(0,0,128)'></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
			}
			if((address!=null)&&(!address.equals(""))){
				out.println("<tr>");
				out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>Address:</font></strong></small></td>");
				out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+address+"</font></small></td>");
				out.println("<input type=hidden name='address' value='"+address+"'>");
				out.println("</tr>");
			}
			else{
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='right'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>Address:</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><textarea rows='2' name='address' cols='15' style='border: 1px solid rgb(0,0,128)'></textarea></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
			}
			if(city!=null&&validate5(city)&&(!city.equals(""))){
				out.println("<tr>");
				out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>City:</font></strong></small></td>");
				out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+city+"</font></small></td>");
				out.println("<input type=hidden name='city' value='"+city+"'>");
				out.println("</tr>");
			}
			else{
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='right'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>City:</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><input type='text' name='city' size='20' style='border: 1px solid rgb(0,0,128)'></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
			}
			if(state!=null&&validate5(state)&&(!state.equals(""))){
				out.println("<tr>");
				out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>State:</font></strong></small></td>");
				out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+state+"</font></small></td>");
				out.println("<input type=hidden name='state' value='"+state+"'>");
				out.println("</tr>");
			}
			else{
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='right'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>State:</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><input type='text' name='state' size='20'  style='border: 1px solid rgb(0,0,128)'></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
			}
			if(country!=null&&validate6(country)&&(!country.equals(""))){
				out.println("<tr>");
				out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>Country:</font></strong></small></td>");
				out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+country+"</font></small></td>");
				out.println("<input type=hidden name='country' value='"+country+"'>");
				out.println("</tr>");
			}
			else{
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='right'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>Country:</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><input type='text' name='country' size='20'  style='border: 1px solid rgb(0,0,128)'></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
			}
			if(pin!=null&&validate7(pin)&&(!pin.equals(""))){
				out.println("<tr>");
				out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>Pin:</font></strong></small></td>");
				out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+pin+"</font></small></td>");
				out.println("<input type=hidden name='pin' value='"+pin+"'>");
				out.println("</tr>");
			}
			else{
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='right'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>Pin:</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><input type='text' name='pin' size='20' style='border: 1px solid rgb(0,0,128)'></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
			}
			if(tele!=null&&validate8(tele)&&(!tele.equals(""))){
				out.println("<tr>");
				out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>Telephone:</font></strong></small></td>");
				out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+tele+"</font></small></td>");
				out.println("<input type=hidden name='tele' value='"+tele+"'>");
				out.println("</tr>");
			}
			else{
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='right'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>Telephone:</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><input type='text' name='tele' size='20' style='border: 1px solid rgb(0,0,128)'></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
			}
			if(dob!=null&&checkdate(dob)&&(!dob.equals(""))){
				out.println("<tr>");
				out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>Date of Birth:</font></strong></small></td>");
				out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+dob+"</font></small></td>");
				out.println("<input type=hidden name='dob' value='"+dob+"'>");
				out.println("</tr>");
			}
			else{
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='right'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>Date of Birth(YYYY-MM-DD):</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><input type='text' name='dob' size='20' style='border: 1px solid rgb(0,0,128)'></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
			}
			out.println("<tr>");
			out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>Sex:</font></strong></small></td>");
			out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			if(Integer.parseInt(sex)==1)out.println("Male");
			else out.println("Female");
			out.println("</font></small></td>");
			out.println("<input type=hidden name='sex' value='"+sex+"'>");
			out.println("</tr>");
			if(email!=null&&checkmail(email)&&(!email.equals(""))){
				out.println("<tr>");
				out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>Email ID:</font></strong></small></td>");
				out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+email+"</font></small></td>");
				out.println("<input type=hidden name='email' value='"+email+"'>");
				out.println("</tr>");
			}
			else{
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='right'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>Email ID:</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><input type='text' name='email' size='20'  style='border: 1px solid rgb(0,0,128)'></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
			}
			out.println("<tr>");
			out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>Password Reminder Question:</font></strong></small></td>");
			out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			if(Integer.parseInt(useremin)==1)out.println("What is your pet name");
			if(Integer.parseInt(useremin)==2)out.println("Which is your favourite team");
			if(Integer.parseInt(useremin)==3)out.println("Which is your favourite recipe");
			if(Integer.parseInt(useremin)==4)out.println("What is your birthplace");
			out.println("</font></small></td>");
			out.println("<input type=hidden name='useremin' value='"+useremin+"'>");
			out.println("</tr>");
			if(useansr!=null&&validate9(useansr)&&(!useansr.equals(""))){
				out.println("<tr>");				
				out.println("<input type=hidden name='useansr' value='"+useansr+"'>");
				out.println("</tr>");
			}
			else{
				out.println("<tr>");
				out.println("<td width='42%' valign='top' align='right'>");
				out.println("<div align='right'><p><small><strong>");
				out.println("<fontface='Verdana' color='#FF0000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color='#FF0000'>Password Reminder answer:</font></strong></small></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'><blockquote>");
				out.println("<div align='left'><p><input type='password' name='useansr' size='20'  style='border: 1px solid rgb(0,0,128)'></p></div></blockquote></td>");
				out.println("<td width='33%' height='30' align='left' valign='top'></td>");
				out.println("</tr>");
			}
			out.println("<tr align='center'><td width='100%' align='right' valign='top' colspan='3'><div align='center'><center><p><input type='submit' value='Submit' name='B1' style='background-color: rgb(0,0,128); color: rgb(255,255,255); font-size: smaller; border: thin solid rgb(255,255,255); background-position: center'></td></tr>");			
			out.println("</table></center></div></form></BODY></HTML>");
			out.println("<!--webbot bot='HTMLMarkup' startspan TAG='XBOT' -->");
			out.println("<IFRAME SRC='../footer.html' NAME='bol'WIDTH='100%' HEIGHT='97' MARGINWIDTH='0' MARGINHEIGHT='0' FRAMEBORDER='0' noresize SCROLLING=NO>");
			out.println("<!--webbot bot='HTMLMarkup' endspan -->");
			out.println("<!--webbot bot='HTMLMarkup' startspan TAG='XBOT' --></IFRAME>");
			out.println("<!--webbot bot='HTMLMarkup'endspan -->");
			return;
		}
		try{
			out.println("<HTML>");
			out.println("<HEAD>");
			out.println("<TITLE>ExamOnline - Confirm User Registration</TITLE>");
			out.println("</HEAD>");
			out.println("<BODY>");
			/*if(validate1(name)&&validate1(last)&&validate2(username)&&validate3(passwd)&&validate5(city)&&validate5(state)&&validate6(country)&&validate7(pin)&&validate8(tele)&&validate9(useansr));
			else{
				out.println("<H2>Some of the rules given to fill form were not followed</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				out.println("</BODY></HTML>");
				return;
			}
			if(middle!=null&&(!middle.equals(""))){
				if(validate1(middle));
				else{
					out.println("<H2>Some of the rules given to fill form were not followed</H2>");
					out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
					out.println("</BODY></HTML>");
					return;
				}
			}			
			if(checkdate(dob));
			else{
				out.println("<H2>Invalid Date of birth </H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				out.println("</BODY></HTML>");
				return;
			}
			if(checkmail(email));
			else{
				out.println("<H2>Invalid Email Address</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				out.println("</BODY></HTML>");
				return;
			}	*/		
			con=pool.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery("Select * from users where username='"+username+"'");
			int c=0;
			while(rs.next()) c++;
			if(c!=0){
				out.println("<H2>SomeBody has already chosen this username</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back to previous page & Choose a Different User Name");
				out.println("</BODY></HTML>");
				return;
			}
			out.println("<body bgcolor='#B3B3D9'>");
			out.println("<p align='center'><font color='#000000' face='verdana'><big><big><strong>Confirm User Registration</strong></big></big></font></p>");    
			out.println("<hr>");  
			out.println("<p align='left'><small><strong><font face='Verdana'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#FFFF00'>Hi, "+name+" please check the information below and confirm your registration:-</font></strong></small></p>");                   
			out.println("<div align='center'><center><table border='0' width='73%' cellspacing='0' cellpadding='0'>");
			out.println("<tr>");
			out.println("<td width='33%' height='35' align='right' valign='middle'><small><small><strong><font face='verdana'>First Name:-</font></strong></small></td>");
			out.println("<td width='33%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+name+"</font></small></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='50%' height='35' align='right' valign='middle'><small><small><small><strong><font face='verdana'>");			
			out.println("Middle Name:-    ");
			out.println("</font></strong></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			if(middle!=null){
				out.println(middle);
			}
			else{
				out.println("Not Specified");
			}
			out.println("</font></small></td>");	
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='50%' height='45' align='right' valign='middle'><small><small><strong><font face='verdana'>Last Name:-    </font></strong></small></td>");	
			out.println("<td width='50%' height='45' align='left' valign='middle'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "+last+"</font></small></td>");						
			out.println("</tr>");		
			out.println("<tr>");
			out.println("<td width=50%' height='35' align='right' valign='baseline'><small><small><strong><font face='verdana'>User Name:-    </font></strong></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='top'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "+username+"</font></small></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=50%' height='35' align='right' valign='baseline'><small><small><strong><font face='verdana'>Password:-    </font></strong></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='top'><small><font face='Arial'> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Not Shown</font></small></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=50%' height='35' align='right' valign='baseline'><small><small><strong><font face='verdana'>Address:-    </font></strong></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='top'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "+address+"</font></small></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=50%' height='35' align='right' valign='baseline'><small><small><strong><font face='verdana'>City:-    </font></strong></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='top'><small><font face='Arial'> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+city+"</font></small></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=50%' height='35' align='right' valign='baseline'><small><small><strong><font face='verdana'>State:-    </font></strong></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='top'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "+state+"</font></small></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=50%' height='35' align='right' valign='baseline'><small><small><strong><font face='verdana'>Country:-    </font></strong></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='top'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "+country+"</font></small></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=50%' height='35' align='right' valign='baseline'><small><small><strong><font face='verdana'>Pin:-    </font></strong></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='top'><small><font face='Arial'> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+pin+"</font></small></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=50%' height='35' align='right' valign='baseline'><small><small><strong><font face='verdana'>Telephone No.:-    </font></strong></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='top'><small><font face='Arial'> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+tele+"</font></small></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=50%' height='35' align='right' valign='baseline'><small><small><strong><font face='verdana'>Date Of Birth:-    </font></strong></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='top'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "+dob+"</font></small></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=50%' height='35' align='right' valign='baseline'><small><small><strong><font face='verdana'>Sex:-    </font></strong></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='top'><small><font face='Arial'>");
			if(Integer.parseInt(sex)==1)out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  Male </font></small></td>");
			else out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  Female </font></small></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=50%' height='35' align='right' valign='baseline'><small><small><strong><font face='verdana'>E-mail Address:-</font></strong></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='top'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+email+"</font></small></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=50%' height='35' align='right' valign='baseline'><small><small><strong><font face='verdana'>Password reminder question:-</font></strong></small></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='top'><small><font face='Arial'>");
			if(Integer.parseInt(useremin)==1)out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;What is your pet name</font></small></td>");
			if(Integer.parseInt(useremin)==2)out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Which is your favourite team</font></small></td>");
			if(Integer.parseInt(useremin)==3)out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Which is your favourite recipe</font></small></td>");
			if(Integer.parseInt(useremin)==4)out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;What is your birthplace</font></small></td>");			
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width=50%' height='35' align='right' valign='baseline'><small><small><strong><font face='verdana'>Password Reminder Answer:-</font></strong></small></td>");
			out.println("<td width='50%' height='35' align='left' valign='top'><small><font face='Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Not Shown</font></small></td>");
			out.println("</tr>");
			out.println("<tr>");			
			out.println("</tr>");
			out.println("</table></center></div>");
			out.println("<p align='center'><small><strong><font color='#FFFF00'>If the information displayed above is Correct, Click on confirm to complete your Registration.<br>Or<br> <a href='javascript:history.go(-1)'>Click Here</a> to go back &amp; fill the information again.</strong></p>");
			out.println("<form method='POST' action='/servlet/InsertUser'><input type=hidden name='name' value='"+name+"'>");
			if(middle!=null)out.println("<input type=hidden name='middle' value='"+middle+"'>");
			out.println("<input type=hidden name='last' value='"+last+"'>");
			out.println("<input type=hidden name='username' value='"+username+"'>");
			out.println("<input type=hidden name='passwd' value='"+passwd+"'>");
			out.println("<input type=hidden name='address' value='"+address+"'>");
			out.println("<input type=hidden name='city' value='"+city+"'>");
			out.println("<input type=hidden name='state' value='"+state+"'>");
			out.println("<input type=hidden name='country' value='"+country+"'>");
			out.println("<input type=hidden name='pin' value='"+pin+"'>");
			out.println("<input type=hidden name='dob' value='"+dob+"'>");
			out.println("<input type=hidden name='sex' value='"+sex+"'>");
			out.println("<input type=hidden name='tele' value='"+tele+"'>");
			out.println("<input type=hidden name='email' value='"+email+"'>");
			out.println("<input type=hidden name='useremin' value='"+useremin+"'>");
			out.println("<input type=hidden name='useansr' value='"+useansr+"'>");
			out.println("<p><center><input type='submit' value='Confirm' name='B1'></center></p></form></BODY></HTML>");
			stmt.close();
		}
		catch(Exception e){
			try{
				out.println("<H2>An Error has occured: "+e.getMessage()+"</H2>");
				out.println("<br><br><a href='javascript:history.go(-1)'>Click Here</a> to go back to previous page & Try Again");
				out.println("</BODY></HTML>");
				con.rollback();				
			}
			catch (Exception ignored) { }
			
		}
		finally{
			
				if(con!=null) pool.returnConnection(con);
				out.close();		
		}
    }
    public boolean validate1(String value){
    	String valid=" abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	boolean ok = true;
    	String temp;
    	for (int i=0; i<value.length(); i++) {
    		temp = "" + value.substring(i, i+1);
    		if (valid.indexOf(temp) == -1){
    			ok =false;
    			break;
    		}
    	}
    	return ok;
    }
    public boolean validate2(String value){
    	String valid="0123456789_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	String start="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	boolean ok = true;
    	String temp;
    	if (value.length()>20||value.length()<5){
    		return false;
    	}
    	char st = value.charAt(0);
    	if (start.indexOf(st) == -1){
    		return false;
    	}    	
    	for (int i=0; i<value.length(); i++) {
    		temp = "" + value.substring(i, i+1);
    		if (valid.indexOf(temp) == -1){
    			ok =false;
    			break;
    		}
    	}
    	return ok;
    }
    public boolean validate3(String value){    	    	
    	if (value.length()>10||value.length()<5) return false;    	
    	return true;
    }
    public boolean validate5(String value){
    	String valid=" .0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	boolean ok = true;
    	String temp;    	
    	for (int i=0; i<value.length(); i++) {
    		temp = "" + value.substring(i, i+1);
    		if (valid.indexOf(temp) == -1){
    			ok =false;
    			break;
    		}
    	}
    	return ok;
    }
    public boolean validate6(String value){
    	String valid=" .abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	boolean ok = true;
    	String temp;    	
    	for (int i=0; i<value.length(); i++) {
    		temp = "" + value.substring(i, i+1);
    		if (valid.indexOf(temp) == -1){
    			ok =false;
    			break;
    		}
    	}
    	return ok;
    }
    public boolean validate7(String value){
    	String valid="0123456789";
    	boolean ok = true;
    	String temp;
    	if (value.length()>6){
    		return false;
    	}
    	for (int i=0; i<value.length(); i++) {
    		temp = "" + value.substring(i, i+1);
    		if (valid.indexOf(temp) == -1){
    			ok =false;
    			break;
    		}
    	}
    	return ok;
    }
    public boolean validate8(String value){
    	String valid="0123456789";
    	boolean ok = true;
    	String temp;
    	if (value.length()>20){
    		return false;
    	}
    	for (int i=0; i<value.length(); i++) {
    		temp = "" + value.substring(i, i+1);
    		if (valid.indexOf(temp) == -1){
    			ok =false;
    			break;
    		}
    	}
    	return ok;
    }
    public boolean validate9(String value){    	    	
    	if (value.length()>20||value.length()<5) return false;    	
    	return true;
    }
    public boolean checkdate(String value){    	    	
    	boolean ok=true;
    	String date,month,year;
    	int d=value.indexOf("-");
    	int m=value.lastIndexOf("-");
    	if(d==-1||d==m) return false;    	
    	year=value.substring(0,d);
    	month=value.substring(d+1,m);
    	date=value.substring(m+1);    	
    	if(Integer.parseInt(date)>31||Integer.parseInt(date)<1) return false;
    	if(Integer.parseInt(month)>12||Integer.parseInt(month)<1) return false;
    	java.util.Date today=new java.util.Date();
    	int y=today.getYear()+1900;    	
    	if(Integer.parseInt(year)>y-3||Integer.parseInt(year)<y-120) return false;
    	return true;
    }
    public boolean checkmail(String value) {
    	int atsign,lastsign;
    	atsign=value.indexOf("@");
    	lastsign=value.lastIndexOf("@");    	
    	if(atsign==-1||lastsign!=atsign) return false;    	
    	atsign=value.lastIndexOf(".");
    	if(atsign==-1) return false;
    	int len=value.length();
    	if(atsign==len-2);
    	else{
    		String domain=value.substring(atsign+1);
    		if(domain.equals("com")||domain.equals("COM")||domain.equals("NET")||domain.equals("ORG")||domain.equals("net")||domain.equals("org")||domain.equals("edu")||domain.equals("EDU")||domain.equals("int")||domain.equals("INT")||domain.equals("MIL")||domain.equals("mil")||domain.equals("gov")||domain.equals("GOV")||domain.equals("arpa")||domain.equals("ARPA")||domain.equals("biz")||domain.equals("BIZ")||domain.equals("aero")||domain.equals("name")||domain.equals("coop")||domain.equals("info")||domain.equals("INFO")||domain.equals("pro")||domain.equals("museum"));
    		else return false;
    	}
    	atsign=value.indexOf(" ");    	
    	if(atsign!=-1)return false;
    	return true;
    }
    public String getServletInfo(){
		return "This servlet validates all information sent for new user registration & asks user for confirmation";
	}
}