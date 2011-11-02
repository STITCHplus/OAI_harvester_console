package harvester;

import javax.servlet.*;


public final class harvester 
implements ServletContextListener 
{
    public final static String PATH_TO_JPS = "/bin/ps";
    public final static String PS_ARGUMENTS = "-u tomcat6 -o pid,start_time,command";

    public final static String PATH_TO_CONFIG = "/usr/local/etc/harvester.xml";
    public final static String PATH_TO_HARVESTER= "/usr/local/bin/OAI_harvester.jar";

    public final static String [] config_format = {"oai_baseurl", "oai_setname", "oai_metadataprefix", "oai_resumption_token", "oai_from", "oai_to", "oai_optional", "solr_pre_xslt", "solr_target"};

    public final static String PATH_TMP = "/usr/local/log/harvester/";

    public void harvester()
    {


    }

    public void contextInitialized (ServletContextEvent servletContextEvent)
    {
        ServletContext servletContext = servletContextEvent.getServletContext();

    }

    public void contextDestroyed(ServletContextEvent event) {};
}
