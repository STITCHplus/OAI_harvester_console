<%@ page session="false" import="harvester.web, java.util.*, harvester.harvester" %>

<% 
    String harvester_name = request.getParameter("name");
    Integer pid = web.get_pid(harvester_name);
    if (pid != -1) web.pause_harvester(harvester_name);
    Thread.sleep(2000);
    response.sendRedirect(response.encodeRedirectURL("index.jsp"));
%>
