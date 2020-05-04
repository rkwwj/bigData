package org.BigData_Hbase.config;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

@Configuration
@EnableConfigurationProperties(HBaseProperties.class)
public class HBaseConfig {
	 @Bean("conf")
	    public org.apache.hadoop.conf.Configuration configuration(
	        @Value("${hbase.zookeeper.quorum}") String quorum,                                               @Value("${hbase.zookeeper.port}") String port) {
	        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
	        conf.set("hbase.zookeeper.quorum", quorum);
	        conf.set("hbase.zookeeper.port", port);
	        return conf;
	    }

	    @Bean
	    public HbaseTemplate hbaseTemplate(org.apache.hadoop.conf.Configuration conf) {
	        HbaseTemplate hbaseTemplate = new HbaseTemplate();
	        hbaseTemplate.setConfiguration(conf);
	        hbaseTemplate.setAutoFlush(true);
	        return hbaseTemplate;
	    }

	    @Bean("hBaseAdmin")
	    public Admin createHbaseAdmin(org.apache.hadoop.conf.Configuration conf) throws IOException {
	        Connection connection = ConnectionFactory.createConnection(conf);
	        Admin admin = connection.getAdmin();
	        return admin;
	    }
	/*
	 * private final HBaseProperties properties;
	 * 
	 * public HBaseConfig(HBaseProperties properties) { this.properties =
	 * properties; }
	 * 
	 * @Bean public HbaseTemplate hbaseTemplate() { HbaseTemplate hbaseTemplate =
	 * new HbaseTemplate(); hbaseTemplate.setConfiguration(configuration());
	 * hbaseTemplate.setAutoFlush(true); return hbaseTemplate; }
	 * 
	 * public org.apache.hadoop.conf.Configuration configuration() {
	 * 
	 * org.apache.hadoop.conf.Configuration configuration =
	 * HBaseConfiguration.create();
	 * 
	 * Map<String, String> config = properties.getConfig(); Set<String> keySet =
	 * config.keySet(); for (String key : keySet) { configuration.set(key,
	 * config.get(key)); }
	 * 
	 * return configuration; }
	 */
}
