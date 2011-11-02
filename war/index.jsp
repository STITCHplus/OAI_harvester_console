<%@ page session="false" import="harvester.web, java.util.*, harvester.harvester" %>

<HTML>
<HEAD>

<STYLE type="text/css">
    td {
        font-family:       verdana,helvetica,arial,sans-serif;
        font-size:100%;
    }

    a:link {color:#0000ff; margin: 1px;} 
    a:visited {color:#0000ff; margin: 1px;} 
    a:active {color:#0000ff; margin: 1px;}
    a:hover {color:#ff0000;}

    table { width: 80%;background-color: #fafafa;
    border: 1px #00 solid;
    border-collapse: collapse;
    border-spacing: 0px; margin: 3px; padding: 3px;} body {font-family:Arial,Sans-Serif;} td { padding: 2px; } 
    

    .buttonAsLink{
        font-size:100%;
        font-family:       verdana,helvetica,arial,sans-serif;
        text-decoration:    underline;
        color:             #0000ff;
        background-color:  #fafafa;
        padding:           0;
        margin:            0;
        padding-top:       4px;
        border-width:      0;  
        cursor:            pointer;
        font-weight:        normal
    }
</STYLE>

<script>


function getXmlHttpRequestObject() {
    if (window.XMLHttpRequest) {
        return new XMLHttpRequest(); //Not IE
    } else if(window.ActiveXObject) {
        return new ActiveXObject("Microsoft.XMLHTTP"); //IE
    } else {
        alert("Your browser doesn't support the XmlHttpRequest object.  Better upgrade to Firefox.");
    }
}

var receiveReq = getXmlHttpRequestObject();

function changeStatus(name) {
    if (receiveReq.readyState == 4) {
        document.getElementById('status_'+name).innerHTML = receiveReq.responseText;
        alert('ok');
    }
}

</script>

</HEAD>

<BODY>

<center><h1>OAI Harvester Console</h1></center>
<table border=1 style="margin-left: 50px;">



<TR><TD style="width: 200px;"><B><center>Name</center></B></TD><TD style="width: 200px;"><B><center>Status</center></B></TD><TD style="width: 100px;"><B><center>Timestamp</center></B></TD><TD style="width: 80px;"><B><center>Action</center></B></TD><TD style="width: 100px;"><B><center>From</center></B></TD><TD><B>Cause</B></TD></TR>

<%
    
    web.init();

    Iterator iter = web.config.keySet().iterator();
    String harvester_name = "";

    while (iter.hasNext()) {
        harvester_name = (String)iter.next();
        Integer pid = web.get_pid(harvester_name);

%>
    <TR><TD>

<%      //Name
        out.println(harvester_name);
%>

   </TD><TD><div id='status_<%=harvester_name%>'>
<%      //Status
       out.println(web.get_status(harvester_name));
%>
    </div></TD><TD>

<%      //Timestamp
        out.println(web.get_time(pid, harvester_name));
%>

    </TD><TD>

<%      //Action
        String ac=web.get_action(pid, harvester_name);
        if ( ac.equalsIgnoreCase("start") ) {
%>
        <form action="start.jsp">
            <input type="submit" name="Start" value="Start"  class="buttonAsLink">
            <input type="hidden" name="harvester_name" value="<%= harvester_name %>">

<%

        } else {
            out.println(web.get_action(pid, harvester_name));
        }
%>

    </TD><TD>

<%  //From
    if (web.get_action(pid, harvester_name).indexOf("Start") > -1) {
        out.println("<input type='text' name='from' value='' maxlength=10 size=10></form>");
    } else {
        out.println("<input type='text' name='from' value='' maxlength=10 size=10 DISABLED=true>");
    }
%>
    </TD><TD>

    <font color="red">

<%

    //Cause
    if (web.get_status(harvester_name).equalsIgnoreCase("error")) { 
            out.println(web.get_cause(harvester_name)); 
    }
%>
    </font>
    </TD></TR>
<%
    }
%>

</TABLE>

</BODY>
</HTML>
