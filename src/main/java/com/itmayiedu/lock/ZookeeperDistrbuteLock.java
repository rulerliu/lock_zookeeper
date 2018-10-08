package com.itmayiedu.lock;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;

public class ZookeeperDistrbuteLock extends ZookeeperAbstractLock {

	@Override
	boolean tryLock() {
		try {
			zkClient.createEphemeral(LOCK_PATH);
			return true;
		} catch (Exception e) {
			// 如果创建节点失败的话，直接返回false
			return false;
		}
	}
	
	@Override
	void waitLock() {
		
		// 事件通知
		IZkDataListener iZkDataListener = new IZkDataListener() {
			// 节点被删除
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				if (countDownLatch != null) {
					// System.out.println("---节点监听：被删除---");
					countDownLatch.countDown();// 计数器一旦为0的情况下
				}
			}
			// 节点被修改
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				
			}
		};
		
		// 注册监听事件通知
		zkClient.subscribeDataChanges(LOCK_PATH, iZkDataListener);
		
		// 控制程序等待
		if (zkClient.exists(LOCK_PATH)) {
			countDownLatch = new CountDownLatch(1);
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// 为了不影响程序的效率，建议删除事件的监听
		zkClient.unsubscribeDataChanges(LOCK_PATH, iZkDataListener);
	}

}
