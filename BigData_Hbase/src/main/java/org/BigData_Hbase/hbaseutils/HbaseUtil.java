package org.BigData_Hbase.hbaseutils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HbaseUtil {

	@Autowired
	private HbaseTemplate hbaseTemplate;

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

	public void insertData(String tableName, String rowKey, String familyName, String column, String value) {
		hbaseTemplate.put(tableName, rowKey, familyName, column, value.getBytes());
	}

	/**
	 * 通过表名和key获取一行数据
	 * 
	 * @param tableName
	 * @param rowName
	 * @return
	 */
	public Map<String, Object> get(String tableName, String rowName) {
		return hbaseTemplate.get(tableName, rowName, new RowMapper<Map<String, Object>>() {
			public Map<String, Object> mapRow(Result result, int rowNum) throws Exception {
				List<Cell> ceList = result.listCells();
				Map<String, Object> map = new HashMap<String, Object>();
				if (ceList != null && ceList.size() > 0) {
					for (Cell cell : ceList) {
						map.put(Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength())
								+ "_"
								+ Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
										cell.getQualifierLength()),
								Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
					}
				}
				return map;
			}
		});
	}

	/**
	 * 通过表名 key 和 列族 和列 获取一个数据
	 * 
	 * @param tableName
	 * @param rowName
	 * @param familyName
	 * @param qualifier
	 * @return
	 */
	public String get(String tableName, String rowName, String familyName, String qualifier) {
		return hbaseTemplate.get(tableName, rowName, familyName, qualifier, new RowMapper<String>() {
			public String mapRow(Result result, int rowNum) throws Exception {
				List<Cell> ceList = result.listCells();
				String res = "";
				if (ceList != null && ceList.size() > 0) {
					for (Cell cell : ceList) {
						res = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
					}
				}
				return res;
			}
		});
	}

	/**
	 * 通过表名，开始行键和结束行键获取数据
	 * 
	 * @param tableName
	 * @param startRow
	 * @param stopRow
	 * @return
	 */
	public List<Map<String, Object>> find(String tableName, String startRow, String stopRow) {
		Scan scan = new Scan();
		if (startRow == null) {
			startRow = "";
		}
		if (stopRow == null) {
			stopRow = "";
		}
		scan.setStartRow(Bytes.toBytes(startRow));
		scan.setStopRow(Bytes.toBytes(stopRow));
		/*
		 * PageFilter filter = new PageFilter(5); scan.setFilter(filter);
		 */
		return hbaseTemplate.find(tableName, scan, new RowMapper<Map<String, Object>>() {
			public Map<String, Object> mapRow(Result result, int rowNum) throws Exception {

				List<Cell> ceList = result.listCells();
				Map<String, Object> map = new HashMap<String, Object>();
				Map<String, Map<String, Object>> returnMap = new HashMap<String, Map<String, Object>>();
				String row = "";
				if (ceList != null && ceList.size() > 0) {
					for (Cell cell : ceList) {
						row = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
						String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
								cell.getValueLength());
						String family = Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(),
								cell.getFamilyLength());
						String quali = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
								cell.getQualifierLength());
						map.put(family + "_" + quali, value);
					}
					map.put("row", row);
				}
				return map;
			}
		});
	}

}
