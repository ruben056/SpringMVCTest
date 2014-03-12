<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>Welcome</title>
	</head> 
	<body>
		<c:url value="/contacts" var="messageUrl" />

        <ul>
            <li><a href="${messageUrl}">Contacts</a></li>
            <li><a href="angular/basic.html">Angularjs: Basic</a></li>
            <li><a href="angular/todo.html">Angularjs: Todo</a></li>
        </ul>>
	</body>
</html>
