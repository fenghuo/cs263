<!-- A basic index.html file served from the "/" URL. -->
<%@ page import="cs263.cs263.DataStore" %>
<%@ page import="java.util.List" %>


<html>
  <body>
    <p>Enqueue a value, to be processed by a worker.</p>
    <hr>
    
    <form action="../enqueue" method="post">
      <input type="text" name="key">
      <input type="submit" value="check in">
    </form>
    
    <hr>
    <table border="1">
    <th><td>name&nbsp;of&nbsp;attendence
    <%
    	List<String> list=DataStore.Test();
    	for(String s:list)
    		out.print("<tr><td>"+s+"</td></tr>");
    %>
    <table>
    
    
  </body>
</html>
