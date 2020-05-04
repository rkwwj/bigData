package org.BigData_Hbase.config;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	public org.apache.hadoop.conf.Configuration configuration(@Value("${hbase.zookeeper.quorum}") String quorum,
			@Value("${hbase.zookeeper.port}") String port) {
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

	@Bean("connection")
	public Connection connection(@Value("${hbase.zookeeper.quorum}") String quorum,
			@Value("${hbase.zookeeper.port}") String port) {
		org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", quorum);
		conf.set("hbase.zookeeper.port", port);
		// HBase RPC请求超时时间，默认60s(60000)
		conf.setInt("hbase.rpc.timeout", 20000);
		// 客户端重试最大次数，默认35
		conf.setInt("hbase.client.retries.number", 10);
		// 客户端发起一次操作数据请求直至得到响应之间的总超时时间，可能包含多个RPC请求，默认为2min
		conf.setInt("hbase.client.operation.timeout", 30000);
		// 客户端发起一次scan操作的rpc调用至得到响应之间的总超时时间
		conf.setInt("hbase.client.scanner.timeout.period", 200000);
		Connection connection = null;
		// 获取hbase连接对象
		try {
			ExecutorService pool = Executors.newFixedThreadPool(20);
			connection = ConnectionFactory.createConnection(conf,pool);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

}
