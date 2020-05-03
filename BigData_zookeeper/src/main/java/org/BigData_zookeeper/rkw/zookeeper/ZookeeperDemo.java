package org.BigData_zookeeper.rkw.zookeeper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.junit.Before;
import org.junit.Test;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperDemo {
	ZooKeeper zk = null;
	@Before
	public void inti() throws Exception{
		 zk = new ZooKeeper("namenode001:2181,datanode001:2181,datanode002:2181", 2000,  new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					// TODO Auto-generated method stub
					if (event.getType()==EventType.NodeDataChanged) {
						System.out.println(event.getType());
						System.out.println("节点发生变化");
					}
					try {
						byte[] data = zk.getData("/aa",true,null);
						try {
							System.out.println(new String(data,"utf8"));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (KeeperException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
	}
	@Test
	public void rkw()  throws IOException, KeeperException, InterruptedException {
		byte[] data = zk.getData("/aa",true,null);
		System.out.println(new String(data,"utf8"));
		Thread.sleep(Long.MAX_VALUE);
	}
}
