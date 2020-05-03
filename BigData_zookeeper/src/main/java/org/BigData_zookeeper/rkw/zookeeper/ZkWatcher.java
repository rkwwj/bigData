package org.BigData_zookeeper.rkw.zookeeper;

import org.BigData_zookeeper.rkw.zookeeper.config.ZkApi;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

public class ZkWatcher implements Watcher{

	ZkApi zk;
	String path;
	
	public ZkWatcher(ZkApi zk,String path) {
		super();
		this.zk=zk;
		this.path=path;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
		if (event.getType()==EventType.NodeDataChanged) {
			System.out.println(event.getType());
			System.out.println("节点发生变化");
		}
		String data = zk.getData(path, this);
		System.out.println(data);
	}

}
