package org.BigData_zookeeper.rkw.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperDemo {

	
	
	
	public static void main(String[] args) throws IOException {
		Watcher watcher = null;
		ZooKeeper zk = new ZooKeeper("", 2000, watcher);
		
		
	}
}
