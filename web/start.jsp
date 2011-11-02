<%@ page session="false" import="harvester.web, java.util.*, harvester.harvester" %>

<% 
    String harvester_name = request.getParameter("harvester_name");
    String from = request.getParameter("from");
    Integer pid = web.get_pid(harvester_name);
    out.println(pid);
    out.println("<BR>");
    if (pid == -1) web.start_harvester(harvester_name, from);
    Thread.sleep(20000);
    response.sendRedirect(response.encodeRedirectURL("index.jsp"));
%>
