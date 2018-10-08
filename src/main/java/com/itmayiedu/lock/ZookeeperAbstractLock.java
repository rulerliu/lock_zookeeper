package com.itmayiedu.lock;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.ZkClient;

/**
 * 将重复代码抽象到子类型，模板方法设计模式
 * @author mayn
 *
 */
public abstract class ZookeeperAbstractLock implements ExtLock {

	protected static final String CONNECTION = "127.0.0.1:2181";
	
	protected static final String LOCK_PATH = "/lockPath";
	
	protected ZkClient zkClient = new ZkClient(CONNECTION);
	
	protected CountDownLatch countDownLatch = null;
	
	/**
	 * 获取锁
	 */
	@Override
	public void getLock() {
		// 白话文
		// 1.连接zkClient，在zk上面创建一个/lock节点，节点类型为临时节点
		// 2.如果节点创建成功，直接执行业务逻辑，否则继续等待
		if (tryLock()) {
			System.out.println("#######获取锁成功###########");
		} else {
			// 3.使用事件通知监听该节点是否被删除，如果被删除直接进入获取锁的资源
			waitLock();
			getLock();
		}
		
	}
	
	/**
	 * 如果节点创建失败，进行等待，等待事件监听通知该节点
	 */
	abstract void waitLock();
	
	/**
	 * 获取锁资源，如果能获取到锁返回true，否则返回false
	 */
	abstract boolean tryLock();
	
	/**
	 * 释放锁
	 */
	@Override
	public void releaseLock() {
		// 当程序执行完的时候，直接关闭连接
		if (zkClient != null) {
			System.out.println("#########释放锁成功#########");
			zkClient.close();
		}
	}
	
}
