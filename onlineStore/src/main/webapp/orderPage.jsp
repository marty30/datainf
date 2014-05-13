<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="coreservlets.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
OrderPage op = new OrderPage();
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=op.getTitle() %></title>
</head>
<BODY BGCOLOR="#FDF5E6">
<H1 ALIGN="CENTER"><%=op.getTitle() %></H1>
<HR>
<%= op.getPage(request, response) %>
</body>
</html>