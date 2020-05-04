package org.BigData_Hbase.hbaseutils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * HBase工具类 Author zzq
 */
@Component
@Slf4j
public class HBaseUtils {
	@Autowired
	private Connection connection;

	@Autowired
	private Admin hBaseAdmin;

	/**
	 * 创建表
	 * 
	 * @param tableName
	 * @param familys
	 */
	public void createTable(String tableName, String[] familys) {
		try {
			TableName table = TableName.valueOf(tableName);
			if (hBaseAdmin.tableExists(table)) {
				log.info("table[{}] already exists!", tableName);
			} else {
				HTableDescriptor tableDesc = new HTableDescriptor(table);
				for (int i = 0; i < familys.length; i++) {
					tableDesc.addFamily(new HColumnDescriptor(familys[i]));
				}
				hBaseAdmin.createTable(tableDesc);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 单行插入数据
	 * 
	 * @param tablename
	 * @param rowkey
	 * @param family
	 * @param cloumns
	 * @throws IOException
	 */
	public void put(String tablename, String rowkey, String family, Map<String, String> cloumns) throws IOException {
		Table table = null;
		try {
			table = connection
					.getTable(TableName.valueOf(tablename));
			Put put = new Put(rowkey.getBytes());
			for (Map.Entry<String, String> entry : cloumns.entrySet()) {
				put.addColumn(family.getBytes(), entry.getKey().getBytes(), entry.getValue().getBytes());
			}
			table.put(put);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			table.close();
		}
	}

	/**
	 * 获取单条数据
	 * 
	 * @param tablename
	 * @param row
	 * @return
	 * @throws IOException
	 */
	public Result getRow(String tablename, String row) throws IOException {
		Table table = null;
		Result result = null;
		try {
			table = connection.getTable(TableName.valueOf(tablename));
			Get get = new Get(row.getBytes());
			result = table.get(get);
		} finally {
			table.close();
		}
		return result;
	}

	/**
	 * 查询多行信息
	 * 
	 * @param tablename
	 * @param rows
	 * @return
	 * @throws IOException
	 */
	public Result[] getRows(String tablename, List<byte[]> rows) throws IOException {
		Table table = null;
		List<Get> gets = null;
		Result[] results = null;
		try {
			table = connection.getTable(TableName.valueOf(tablename));
			gets = new ArrayList<Get>();
			for (byte[] row : rows) {
				if (row != null) {
					gets.add(new Get(row));
				}
			}
			if (gets.size() > 0) {
				results = table.get(gets);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			table.close();
		}
		return results;
	}

	/**
	 * 获取整表数据
	 * 
	 * @param tablename
	 * @return
	 */
	public ResultScanner get(String tablename) throws IOException {
		Table table = null;
		ResultScanner results = null;
		try {
			table = connection.getTable(TableName.valueOf(tablename));
			Scan scan = new Scan();
			scan.setCaching(1000);
			results = table.getScanner(scan);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			table.close();
		}
		return results;
	}

	/**
	 * 删除数据
	 * 
	 * @param tablename
	 * @param family
	 * @param column
	 * @param row
	 * @throws IOException
	 */
	public void delete(String tablename, String family, String column, String row) throws IOException {
		Table table = null;

		try {
			table =connection.getTable(TableName.valueOf(tablename));
			Delete del = new Delete(row.getBytes());
			del.addColumns(family.getBytes(), column.getBytes());
			table.delete(del);
		} finally {
			table.close();
		}
	}
}
