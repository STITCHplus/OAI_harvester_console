<%@ page session="false" import="harvester.web, java.util.*, harvester.harvester" %>

<% 
    String harvester_name = request.getParameter("name");
    Integer pid = web.get_pid(harvester_name);
    if (pid == -1) web.start_harvester(harvester_name);
    response.sendRedirect(response.encodeRedirectURL("index.jsp"));
%>
