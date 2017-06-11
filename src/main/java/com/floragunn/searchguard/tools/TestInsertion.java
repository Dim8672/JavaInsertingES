/*
 * Copyright 2017 dimitri.mella.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.floragunn.searchguard.tools;

import com.floragunn.searchguard.SearchGuardPlugin;
import com.floragunn.searchguard.ssl.util.SSLConfigConstants;
import com.floragunn.searchguard.support.ConfigConstants;
import static com.floragunn.searchguard.tools.SearchGuardAdmin.asCollection;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.Netty4Plugin;

/**
 *
 * @author dimitri.mella
 */
public class TestInsertion {
    
    private static final String SG_TS_PASS = "SG_TS_PASS";
    private static final String SG_KS_PASS = "SG_KS_PASS";
    //not used in multithreaded fashion
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MMM-dd_HH-mm-ss", Locale.ENGLISH);
    private static final Settings ENABLE_ALL_ALLOCATIONS_SETTINGS = Settings.builder()
            .put("cluster.routing.allocation.enable", "all")
            .build();
    
    public static void main(String[] args) {
        try {
            System.out.println("Search Guard Admin v5");
            System.setProperty("sg.nowarn.client","true");
            
            final HelpFormatter formatter = new HelpFormatter();
            Options options = new Options();
            options.addOption( "nhnv", "disable-host-name-verification", false, "Disable hostname verification" );
            options.addOption( "nrhn", "disable-resolve-hostname", false, "Disable hostname beeing resolved" );
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
            options.addOption( "sniff", "enable-sniffing", false, "Enable client.transport.sniff" );
            options.addOption( "icl", "ignore-clustername", false, "Ignore clustername" );
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
            
            String hostname = "localhost";
            int port = 9300;
            String kspass = System.getenv(SG_KS_PASS) != null ? System.getenv(SG_KS_PASS) : "changeit";
            String tspass = System.getenv(SG_TS_PASS) != null ? System.getenv(SG_TS_PASS) : kspass;
            String cd = ".";
            String ks;
            String ts;
            String kst = null;
            String tst = null;
            boolean nhnv = false;
            boolean nrhn = false;
            boolean sniff = false;
            boolean icl = false;
            String clustername = "elasticsearch";
            String file = null;
            String type = null;
            boolean retrieve = false;
            String ksAlias = null;
            String tsAlias = null;
            String[] enabledProtocols = new String[0];
            String[] enabledCiphers = new String[0];
            Integer updateSettings = null;
            String index = ConfigConstants.SG_DEFAULT_CONFIG_INDEX;
            Boolean replicaAutoExpand = null;
            boolean reload = false;
            boolean failFast = false;
            boolean diagnose = false;
            boolean deleteConfigIndex = false;
            boolean enableShardAllocation = false;
            
            CommandLineParser parser = new DefaultParser();
            
            final Settings.Builder settingsBuilder = Settings
                    .builder()
                    .put("path.home", ".")
                    .put("path.conf", ".")
                    .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_FILEPATH, "C:\\Users\\dimitri.mella\\Desktop\\ElasticAuthentification\\elasticsearch-5.3.0-localhost\\config\\CN=localhost-keystore.jks")
                    .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_FILEPATH, "C:\\Users\\dimitri.mella\\Desktop\\ElasticAuthentification\\elasticsearch-5.3.0-localhost\\config\\truststore.jks")
                    .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_PASSWORD, "fa5f481f1a5df359de86")
                    .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_PASSWORD, "1b2775b49b7905d36c76")
                    .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENFORCE_HOSTNAME_VERIFICATION, !nhnv)
                    .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENFORCE_HOSTNAME_VERIFICATION_RESOLVE_HOST_NAME, !nrhn)
                    .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENABLED, true)
//                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_TYPE, kst==null?(ks.endsWith(".jks")?"JKS":"PKCS12"):kst)
//                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_TYPE, tst==null?(ts.endsWith(".jks")?"JKS":"PKCS12"):tst)
                    
                    .putArray(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENABLED_CIPHERS, enabledCiphers)
                    .putArray(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENABLED_PROTOCOLS, enabledProtocols)
                    
                    .put("cluster.name", clustername)
                    .put("client.transport.ignore_cluster_name", icl)
                    .put("client.transport.sniff", sniff);
            
            Settings settings = settingsBuilder.build();
            
            TransportClient tc = new SearchGuardAdmin.TransportClientImpl(settings, asCollection(Netty4Plugin.class, SearchGuardPlugin.class))
                    .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(hostname, port)));
            
            BulkRequestBuilder bulkRequest = tc.prepareBulk();
            
            bulkRequest.add(tc.prepareIndex("cityflow3", "test").setSource(jsonBuilder()
                    .startObject()
                    .field("macAdresse", "TestMac")
                    .endObject()));
            
            BulkResponse bulkResponse = bulkRequest.get();
        } catch (IOException ex) {
            Logger.getLogger(TestInsertion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     protected static class TransportClientImpl extends TransportClient {

        public TransportClientImpl(Settings settings, Collection<Class<? extends Plugin>> plugins) {
            super(settings, plugins);
        }

        public TransportClientImpl(Settings settings, Settings defaultSettings, Collection<Class<? extends Plugin>> plugins) {
            super(settings, defaultSettings, plugins, null);
        }       
    }
    
}
