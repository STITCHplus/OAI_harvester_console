package harvester;

import java.io.*;
import java.util.*;
import javax.servlet.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public final class web
{

    public static Map config = new HashMap();
    public static Map harvesters = new HashMap();

    public static void init()
    {
        parse_config();
    }

    public static void get_running_harvesters()
    {
        try {
            harvesters = new HashMap();
            String s = null;
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(harvester.PATH_TO_JPS+" "+harvester.PS_ARGUMENTS);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            p.waitFor();
            StringBuffer buffer = new StringBuffer();

            while ((s = stdInput.readLine()) != null) {
                if ((s.indexOf(harvester.PATH_TO_HARVESTER)  > 0)) {
                    String a[] = s.split(" ");
                    Integer pid = -1;
                    for (int i=0;i<a.length;i++) {
                        if (i==3) {
                            harvesters.put(a[i], pid);
                        }
                        if (i==0) {
                            try {
                                pid = Integer.valueOf(a[i]);
                            } catch (Exception e) { };
                        }

                    }
                }
            }
            stdInput.close();
        } catch (Exception e) {};
    }


    static void parse_config() 
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            doc = factory.newDocumentBuilder().parse(new File(harvester.PATH_TO_CONFIG));
        } catch (Exception e) { };

        NodeList nodeChildren = doc.getElementsByTagName ("harvester");

        for (int i=0; i<nodeChildren.getLength();i++) {
            Map hhash = new HashMap();
            Element har = (Element) nodeChildren.item(i);
            for (int j=0; j<harvester.config_format.length;j++) {
                try{
                    Node nd = har.getElementsByTagName(harvester.config_format[j]).item(0);
                    hhash.put(harvester.config_format[j], nd.getFirstChild().getNodeValue());
                } catch (Exception e) { }
            }
            config.put(nodeChildren.item(i).getAttributes().getNamedItem("name").getTextContent(), hhash);
        }
    }

    public static String get_time(Integer pid, String harvester_name) {
        if (pid> 0) {
            try {
                Runtime r = Runtime.getRuntime();
                Process p = r.exec(harvester.PATH_TO_JPS+" -o start_time= -p "+pid.toString());
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                p.waitFor();
                String time = stdInput.readLine();
                stdInput.close();
                return(time);
            } catch (Exception e) { };

        } else {
            return("");
        }
        return("Unknown");
    }

    public static String get_status(String harvester_name)
    {
        Integer pid = get_pid(harvester_name);
        String sep = java.io.File.separator;

        if ( pid == -1 ) {

            File status_file = new File(harvester.PATH_TMP+sep+harvester_name+"_status"); 
            if (status_file.exists()) {
                try {
                        FileInputStream fstream = new FileInputStream(harvester.PATH_TMP+sep+harvester_name+"_status");
                        DataInputStream in = new DataInputStream(fstream);
                        String status = in.readLine();
                        fstream.close();
                        if (!(status == null)) {
                            if (status.indexOf(",") > 0) return status.split(",")[0];
                            return status;
                        } else {
                            return "Undefined";
                        }
                } catch(Exception e) { 
                    return("Error reading status file.");
                }
            } else {
                return("Not started");
            }
        } else {
            return("Running");
        }
    }

    public static String get_action(Integer pid, String harvester_name)
    {
        if (pid == -1) {
            if (get_status(harvester_name).equalsIgnoreCase("pauzed")) {
                return("<a href='stop.jsp?name="+harvester_name+"'>Stop</a><br><a href='resume.jsp?name="+harvester_name+"'>Resume</a>");
            }
            return("Start");
        } else {
            return("<a href='stop.jsp?name="+harvester_name+"'>Stop</a><br><a href='pause.jsp?name="+harvester_name+"'>Pause</a>");
        }
    }


    public static Integer get_pid(String harvester_name) 
    {
        try {
            String s = null;
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(harvester.PATH_TO_JPS+" "+harvester.PS_ARGUMENTS);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            p.waitFor();
            StringBuffer buffer = new StringBuffer();

            while ((s = stdInput.readLine()) != null) {
                if ((s.indexOf(harvester.PATH_TO_HARVESTER)  > 0)  && (s.indexOf(harvester_name) > 0)) {
                    String a[] = s.split(" ");
                    for (int i=0;i<a.length;i++) {
                    try {
                        Integer pid = Integer.valueOf(a[i]);
                        return(pid);
                    } catch (Exception e) { };
                    }
                }
            }
            stdInput.close();
        } catch (Exception e) {};

        return(-1);
    }

    public static void pause_harvester(String harvester_name)
    {

        try{
            Integer pid = get_pid(harvester_name);
            Runtime r = Runtime.getRuntime();
            String[] kill = { "kill",  pid.toString()};
            Process p = r.exec(kill);
            try {
                p.waitFor();
            } catch (Exception e) { };
            BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
            is.readLine();
            is.close();
        } catch (Exception e) { };
    }

    public static String get_cause(String harvester_name)
    {
        String sep = java.io.File.separator;
        File status_file = new File(harvester.PATH_TMP+sep+harvester_name+"_status");

        if (status_file.exists()) {
            try {
                    FileInputStream fstream = new FileInputStream(harvester.PATH_TMP+sep+harvester_name+"_status");
                    DataInputStream in = new DataInputStream(fstream);
                    String status = in.readLine();
                    if (!(status == null)) {
                        if (status.indexOf(",") > 0) return status.split(",")[1];
                        return status;
                    } else {
                        return "Undefined";
                    }
            } catch(Exception e) { 
                return("Error reading status file.");
            }
        }
        return "Undefined";
    }


    public static void start_harvester(String harvester_name, String from)
    {
        try{
            ProcessBuilder pb = null;
            if ((from == null) || (from.equalsIgnoreCase(""))) {
                pb = new ProcessBuilder("/usr/local/bin/harvest.sh",  harvester_name);
            } else {
                pb = new ProcessBuilder("/usr/local/bin/harvest.sh",  harvester_name, "-from" ,from);
            }
            Process p = pb.start();
            p.waitFor();
            BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
            is.readLine();
            is.close();
        } catch (Exception e) { };
        sendRedirect("/OAI_harvester_console");
    }

    public static void resume_harvester(String harvester_name)
    {
        String sep = java.io.File.separator;
        File status_file = new File(harvester.PATH_TMP+sep+harvester_name+"_status");

        String token = "";

        if (status_file.exists()) {
            try {
                    FileInputStream fstream = new FileInputStream(harvester.PATH_TMP+sep+harvester_name+"_status");
                    DataInputStream in = new DataInputStream(fstream);
                    String status = in.readLine();
                    if (!(status == null)) {
                        if (status.indexOf(",") > 0) token=status.split(",")[2];
                    }
            } catch(Exception e) { } 
        }

        try{
            ProcessBuilder pb = new ProcessBuilder("/usr/local/bin/harvest.sh", harvester_name, token);
            Process p = pb.start();
            p.waitFor();
            BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
            is.readLine();
            is.close();
        } catch (Exception e) { };
    }


}
