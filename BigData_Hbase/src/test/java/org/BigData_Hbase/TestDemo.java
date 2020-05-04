package org.BigData_Hbase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.BigData_Hbase.hbaseutils.HBaseUtils;
import org.apache.hadoop.hbase.client.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDemo {

	@Autowired
	private HBaseUtils instance ;
	@Test
	public void test() {
		instance.createTable("personwj009", new String[] {"info","familyinfo"});
//		HBaseUtilsnew instance = HBaseUtilsnew.getInstance();
		long begin=System.currentTimeMillis();
		try {
			int i=1111;
			Map<String, String> cloumns = new HashMap<String, String>();
			for (int j = 0; j < 10000; j++) {
				cloumns.put("name009", "zzq".concat(String.valueOf(i)));
				cloumns.put("age", "22".concat(String.valueOf(i)));
				instance.put("personwj009", "1".concat(String.valueOf(i)), "info", cloumns);
			}
			System.out.println("增加成功");
			System.out.println(System.currentTimeMillis()-begin);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("增加失败");
		} 

		Result map = null;
		try {
			map = instance.getRow("personwj009", "11111");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println(map);
		System.out.println(System.currentTimeMillis()-begin);
//		temserver.createTable("rkwtable", new String[] {"userinfo","familyinfo"});
//		temserver.insertData("rkwtable", "001", "userinfo", "name", "rkwnew001");

//   for (int i = 10000; i < 20000; i++) {
//  temserver.insertData("rkwtable", "00".concat(String.valueOf(i)), "userinfo",
//  "name", "rkwnew00".concat(String.valueOf(i))); Map<String, String> map =new
//  HashMap<String, String>(); map.put("name",
//  "rkwnew00".concat(String.valueOf(i))); try { hBaseUtilsnew.put("rkwtable",
//  "00".concat(String.valueOf(i)), "userinfo", map); } catch (IOException e) {
//  // TODO Auto-generated catch block e.printStackTrace(); } }
//  System.out.println(System.currentTimeMillis()-begin); Map<String, Object> map
//  = temserver.get("rkwtable", "00101"); System.out.println(map);
//  System.out.println(System.currentTimeMillis()-begin);
 
//		temserver.createTable("rkw", "user_info","family_info");
	}
}
