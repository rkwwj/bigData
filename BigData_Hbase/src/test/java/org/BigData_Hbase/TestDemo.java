package org.BigData_Hbase;

import java.util.Map;

import org.BigData_Hbase.hbaseutils.HbaseUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestDemo {

	@Autowired
	private HbaseUtil temserver;
	@Test
	public void test() {
//		temserver.createTable("rkwtable", new String[] {"userinfo","familyinfo"});
		temserver.insertData("rkwtable", "001", "userinfo", "name", "rkwnew001");
		
		Map<String, Object> map = temserver.get("rkwtable", "001");
		System.out.println(map);
//		temserver.createTable("rkw", "user_info","family_info");
	}
}
