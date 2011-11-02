<%@ page session="false" import="harvester.web, java.util.*, harvester.harvester" %>

<%
    web.init();

    Iterator iter = web.config.keySet().iterator();
    String harvester_name = request.getParameter("name");
    String name = "";

    while (iter.hasNext()) {
        name = (String)iter.next();
        if (harvester_name.equalsIgnoreCase(name)) {
             out.println(web.get_status(harvester_name));
        }
    }
%>

