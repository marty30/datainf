<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="coreservlets.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
KidsBooksPage kbp = new KidsBooksPage();
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=kbp.getTitle() %></title>
</head>
<BODY BGCOLOR=#FDF5E6>
<H1 ALIGN="CENTER"><%=kbp.getTitle() %></H1>
<%=kbp.getPage() %>
</body>
</html>