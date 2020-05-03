package org.BigData_zookeeper.rkw.zookeeper.listener;

import javax.annotation.Resource;

import org.BigData_zookeeper.rkw.zookeeper.ZkWatcher;
import org.BigData_zookeeper.rkw.zookeeper.config.ZkApi;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ZookeeperListener implements ApplicationRunner {

	@Resource
	private ZkApi zk;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("监听启动");
		zk.getData("/aa",new ZkWatcher(zk,"/aa"));
	    	try {
				Thread.sleep(Long.MAX_VALUE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
