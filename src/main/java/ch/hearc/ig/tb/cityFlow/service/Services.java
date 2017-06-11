/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.ig.tb.cityFlow.service;

import ch.hearc.ig.tb.cityFlow.service.insert.InsertElasticSearch;
import ch.hearc.ig.tb.cityFlow.service.search.SearchElasticSearch;
import ch.hearc.ig.tb.cityFlow.utilitaire.Utilitaire;
import com.floragunn.searchguard.SearchGuardPlugin;
import com.floragunn.searchguard.ssl.util.SSLConfigConstants;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.Netty4Plugin;

/**
 *
 * @author Dimitri
 * Services Classes used for inserting Data in ElasticSearch
 */
public class Services {

    private Utilitaire utils;
    private SimpleDateFormat formatter;
    private Settings settings;
    private Map<String, ArrayList<String>> users;
    private TransportClient client;
    private SearchElasticSearch searchElastic;
    private InsertElasticSearch insertElastic;

    /**
     * Constructor for Services
     */
    public Services() {
        utils = Utilitaire.getInstance();
        formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE);
        this.settings = null;
        this.users = new HashMap<String, ArrayList<String>>();
        this.searchElastic = new SearchElasticSearch();
        this.insertElastic = new InsertElasticSearch();
    }
    /**
     * Method used for generating random data for ElasticSearch
     * @param numberPerson the number of Person to generate
     */
    public void generateStaticData(Integer numberPerson) {
        utils.generatePerson(numberPerson);
        utils.generateDevice();
    }

    public TransportClient getClient() {
        return client;
    }

    /**
     * Settings options for being able to contact ElasticSerach with SearchGuard
     */
    public void setOptions() {
        Options options = new Options();
        options.addOption("nhnv", "disable-host-name-verification", false, "Disable hostname verification");
        options.addOption("nrhn", "disable-resolve-hostname", false, "Disable hostname beeing resolved");
        options.addOption(Option.builder("ts").longOpt("truststore").hasArg().argName("file").required().desc("Path to truststore (JKS/PKCS12 format)").build());
        options.addOption(Option.builder("ks").longOpt("keystore").hasArg().argName("file").required().desc("Path to keystore (JKS/PKCS12 format").build());
        options.addOption(Option.builder("tst").longOpt("truststore-type").hasArg().argName("type").desc("JKS or PKCS12, if not given use file ext. to dectect type").build());
        options.addOption(Option.builder("kst").longOpt("keystore-type").hasArg().argName("type").desc("JKS or PKCS12, if not given use file ext. to dectect type").build());
        options.addOption(Option.builder("tspass").longOpt("truststore-password").hasArg().argName("password").desc("Truststore password").build());
        options.addOption(Option.builder("kspass").longOpt("keystore-password").hasArg().argName("password").desc("Keystore password").build());
        options.addOption(Option.builder("cd").longOpt("configdir").hasArg().argName("directory").desc("Directory for config files").build());
        options.addOption(Option.builder("h").longOpt("hostname").hasArg().argName("host").desc("Elasticsearch host").build());
        options.addOption(Option.builder("p").longOpt("port").hasArg().argName("port").desc("Elasticsearch transport port (normally 9300)").build());
        options.addOption(Option.builder("cn").longOpt("clustername").hasArg().argName("clustername").desc("Clustername").build());
        options.addOption("sniff", "enable-sniffing", false, "Enable client.transport.sniff");
        options.addOption("icl", "ignore-clustername", false, "Ignore clustername");
        options.addOption(Option.builder("r").longOpt("retrieve").desc("retrieve current config").build());
        options.addOption(Option.builder("f").longOpt("file").hasArg().argName("file").desc("file").build());
        options.addOption(Option.builder("t").longOpt("type").hasArg().argName("file-type").desc("file-type").build());
        options.addOption(Option.builder("tsalias").longOpt("truststore-alias").hasArg().argName("alias").desc("Truststore alias").build());
        options.addOption(Option.builder("ksalias").longOpt("keystore-alias").hasArg().argName("alias").desc("Keystore alias").build());
        options.addOption(Option.builder("ec").longOpt("enabled-ciphers").hasArg().argName("cipers").desc("Comma separated list of TLS ciphers").build());
        options.addOption(Option.builder("ep").longOpt("enabled-protocols").hasArg().argName("protocols").desc("Comma separated list of TLS protocols").build());
        //TODO mark as deprecated and replace it with "era" if "era" is mature enough
        options.addOption(Option.builder("us").longOpt("update_settings").hasArg().argName("number of replicas").desc("update the number of replicas and reload configuration on all nodes and exit").build());
        options.addOption(Option.builder("i").longOpt("index").hasArg().argName("indexname").desc("The index Searchguard uses to store its configs in").build());
        options.addOption(Option.builder("era").longOpt("enable-replica-autoexpand").desc("enable replica auto expand and exit").build());
        options.addOption(Option.builder("dra").longOpt("disable-replica-autoexpand").desc("disable replica auto expand and exit").build());
        options.addOption(Option.builder("rl").longOpt("reload").desc("reload configuration on all nodes and exit").build());
        options.addOption(Option.builder("ff").longOpt("fail-fast").desc("fail-fast if something goes wrong").build());
        options.addOption(Option.builder("dg").longOpt("diagnose").desc("Log diagnostic trace into a file").build());
        options.addOption(Option.builder("dci").longOpt("delete-config-index").desc("Delete 'searchguard' config index and exit.").build());
        options.addOption(Option.builder("esa").longOpt("enable-shard-allocation").desc("Enable all shard allocation and exit.").build());

        System.setProperty("sg.nowarn.client", "true");
    }

    /**
     * Setting settings for elasticSearch and SearchGuard
     */
    public void setSettings() {
        final Settings.Builder settingsBuilder = Settings
                .builder()
                .put("path.home", ".")
                .put("path.conf", ".")
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_FILEPATH, "C:\\Users\\dimitri.mella\\Desktop\\ElasticAuthentification\\elasticsearch-5.3.0-localhost\\config\\CN=localhost-keystore.jks")
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_FILEPATH, "C:\\Users\\dimitri.mella\\Desktop\\ElasticAuthentification\\elasticsearch-5.3.0-localhost\\config\\truststore.jks")
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_PASSWORD, "fa5f481f1a5df359de86")
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_PASSWORD, "1b2775b49b7905d36c76")
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENFORCE_HOSTNAME_VERIFICATION, !true)
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENFORCE_HOSTNAME_VERIFICATION_RESOLVE_HOST_NAME, !true)
                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENABLED, true)
                .putArray(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENABLED_CIPHERS, new String[0])
                .putArray(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENABLED_PROTOCOLS, new String[0])
                .put("cluster.name", "elasticsearch")
                .put("client.transport.ignore_cluster_name", false)
                .put("client.transport.sniff", false);

        this.settings = settingsBuilder.build();
    }

    /**
     * Generate random Facts in ElasticSearch
     * @param numberFact number of Fact to Generate
     * @param start Start Date
     * @param end End Date
     * @param deviceName The name of the devices which the facts will be link 
     */
    public void generateDataFacts(Integer numberFact, String start, String end, String deviceName, boolean traject) {
        this.generateClient(); // Generating the client which will be used to communicate with ES
        if (this.searchElastic.factsEmtpy(this.client)){
            this.insertElastic.putDataRealFacts(client, numberFact, start, end, deviceName, formatter, formatter.format(new Date())); // Put the new Facts in ElasticSearch
            this.client.close();
            this.generateClient();
            // this.insertElastic.putDataTrajectoryFacts(client, "2017-01-01T01:00:00", formatter, formatter.format(new Date()), this); // Put the trajectory Facts for the first time
            this.client.close();
        } else {
            Date lastImport = this.searchElastic.getLastImportDate(client, formatter); // Get the last Import done
            this.insertElastic.putDataRealFacts(client, numberFact, start, end, deviceName, formatter, formatter.format(new Date())); // Put the new Facts in ElasticSearch
            this.client.close();
            this.generateClient();
            if(traject){
                this.insertElastic.putDataTrajectoryFacts(client, "2017-01-01T01:00:00", formatter, formatter.format(new Date()), this); // Put the trajectory Facts generating with the news facts
            }    
        } // Chnager la date en dure pour le else, pour le moment je fais comme ça à cause de mes données fictives, mais dans un environnement réelle, on prend la dernière date d'insert.
        this.closeClient(); // closing the ES Client
    }
    
    /**
     * Method used for generating a client connexion for ES
     */
    public void generateClient(){
        this.client = new Services.TransportClientImpl(this.settings, asCollection(Netty4Plugin.class, SearchGuardPlugin.class))
                .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("157.26.64.147", 9300)));
    }
    
    /**
     * Method for closing the client connexion for ES
     */
    private void closeClient(){
        this.client.close();
    }
    
    /**
     * Under Classes for the TransportClientImplementation
     */
    protected static class TransportClientImpl extends TransportClient {

        public TransportClientImpl(Settings settings, Collection<Class<? extends Plugin>> plugins) {
            super(settings, plugins);
        }

        public TransportClientImpl(Settings settings, Settings defaultSettings, Collection<Class<? extends Plugin>> plugins) {
            super(settings, defaultSettings, plugins, null);
        }
    }

    /**
     * Under classes for the collection
     * @param plugins
     * @return a collection
     */
    protected static Collection<Class<? extends Plugin>> asCollection(Class<? extends Plugin>... plugins) {
        return Arrays.asList(plugins);
    }

}
