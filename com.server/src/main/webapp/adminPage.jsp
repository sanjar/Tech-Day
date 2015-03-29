<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String collapseKey = "GCM_Message";
    String message = "E.g, How was today's IT Department Meeting?";

    Object collapseKeyObj =  request.getAttribute("CollapseKey");
    
    if (collapseKeyObj != null) {
    	collapseKey = collapseKeyObj.toString();
    }
    
    Object msgObj =  request.getAttribute("Message");
    
    if (msgObj != null) {
    	message = msgObj.toString();
    }
        
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Android GCM Broadcast</title>

</head>
<body bgcolor="LightBlue">
<center>
    <h2>IT Department Feedback Management Admin</h2>
	
    <form action="GCMBroadcast" method="post">
		<label>Put Your Question Here </label>
		<br />
		<!--<br /><input type="text" name="CollapseKey" value="<%=collapseKey %>" />-->
		<br/><textarea name="Message" rows="3" cols="60" ><%=message %> </textarea> 
		<br/>
		<select title="Groups" id="groups" name="groups">
			<c:forEach items="${groupList}" var="group">
				<option value="${group}">${group}</option>
			</c:forEach>
		</select>
		<input name="starting_url" value="<%=request.getRequestURL().toString()%>" type="hidden"/>
		<br/><input type="submit" name="submit" value="Submit" />
		</form>	
		</center>
	</body>
</html>