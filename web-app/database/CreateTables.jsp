<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" %>
<%
Class.forName("org.gjt.mm.mysql.Driver").newInstance(); 
Connection con=DriverManager.getConnection("jdbc:mysql://localhost/net4engineers_com?user=net4engineers&password=database");			
Statement stmt=con.createStatement();	
stmt.executeUpdate("drop table if exists admin");
stmt.executeUpdate("drop table if exists users");
stmt.executeUpdate("drop table if exists result");
stmt.executeUpdate("drop table if exists global");
stmt.executeUpdate("drop table if exists test_master");
stmt.executeUpdate("drop table if exists test_info");
stmt.executeUpdate("drop table if exists paper");
stmt.executeUpdate("drop table if exists question_images");

stmt.executeUpdate("Create table admin (aduser char(15) PRIMARY KEY,adpasswd char(10) NOT NULL)");

stmt.executeUpdate("Create table result (username char(20) NOT NULL,test_code SMALLINT NOT NULL, test_subject_no TINYINT NOT NULL, total SMALLINT NOT NULL,attempt SMALLINT Not NULL,score decimal(4,2) NOT NULL, time_taken SMALLINT NOT NULL)");

stmt.executeUpdate("Create table global (field char(20) PRIMARY KEY,value char(200) NOT NULL)");
	
stmt.executeUpdate("Create table users (username char(20) PRIMARY KEY,name char(15) NOT NULL,middle char(15) DEFAULT NULL,last char(15) NOT NULL,passwd char(10) NOT NULL,address char(50) NOT NULL, city char(20) NOT NULL, state char(20) NOT NULL,country char(20) NOT NULL, pin MEDIUMINT NOT NULL, dob DATE NOT NULL, sex TINYINT NOT NULL, tele BIGINT NOT NULL, email char(50) NOT NULL, useremin TINYINT NOT NULL,useansr char(20) NOT NULL)");

stmt.executeUpdate("Create table test_master (test_code SMALLINT PRIMARY KEY, aduser char(15) NOT NULL, test_name char(20) NOT NULL, test_created DATE NOT NULL, test_updated DATE NOT NULL, test_duration SMALLINT NOT NULL, test_status TINYINT NOT NULL, test_no_subject TINYINT NOT NULL, test_details char(200))");

stmt.executeUpdate("Create table test_info(table_name char(25) NOT NULL PRIMARY KEY, test_code SMALLINT NOT NULL, subject_name char(25) NOT NULL, subject_no_ques SMALLINT NOT NULL)");

stmt.executeUpdate("Create table question_images(table_name char(25) NOT NULL,id int NOT NULL,file_name char(15) NOT NULL,image BLOB NOT NULL)");

/*stmt.executeUpdate("Create table paper (id int PRIMARY KEY,question char(200) NOT NULL,ch1 char(150) NOT NULL,ch2 char(150) NOT NULL,ch3 char(150) NOT NULL,ch4 char(150) NOT NULL,answer int NOT NULL)");*/

//stmt.executeUpdate("insert into global values('no_ques','25')");
//stmt.executeUpdate("insert into global values('total_time','0.50')");
stmt.executeUpdate("insert into admin values('admin','examonline')");

out.println("tables created are:- users,admin,result,global,test_master,test_info");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>

</body>
</html>
<%
con.close();
stmt.close();
%>
