<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="java.io.*,java.util.*, javax.servlet.*" %>

<html>
<head>
    <title><spring:message code="label.login.title" text="Edit Application Details" /></title>
    <LINK rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/appEdit.css">
    
</head>
<body>
    <div class="page-header">
    <img src="${pageContext.request.contextPath}/styles/icons/Black_Duck_Logo.png" alt="Black Duck Logo"  width="200" height="50" id=""/>       	
        <h1><spring:message code="label.login.title" text="Edit Application Details" /></h1>
    </div>

		<form id='login_form' action="login/authenticate" method="POST" role="form">
                <!-- CSRF token -->
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

			<table id='input_table'>
				<tr>
					<td colspan="2" class=has-error>
						${fn:replace(SPRING_SECURITY_LAST_EXCEPTION.message, 'Bad credentials', 'Username/Password are incorrect')}
					</td>
				</tr>
				<tr>
					<td><label class=field-label><spring:message code="label.login.username" />:</label></td>
					<td><input id='username_field' type='text' name='username' value='' autofocus ></td>
				</tr>
				<tr>
					<td><label class=field-label><spring:message code="label.login.password" />:</label></td>
					<td><input type='password' name='password' /></td>
				</tr>
				<tr>
					<td colspan="2"><label class=advice><spring:message code="label.login.advice" /></td>
				</tr>
				<tr>
					<td>
					<button type="submit" class="btn btn-primary" name="action"
						value="submit">
						<spring:message code="label.login.submit" />
					</button>
					<button type="button" class="btn btn-primary" value="cancel"
						onclick="window.close()">
						<spring:message code="label.login.cancel" />
					</button>
					</td>
				</tr>
			</table>
		</form>
		<script type="text/javascript">
			$(document).ready(function(){
				document.getElementById('username_field').focus();
			});
		</script>
		
</body>
</html>