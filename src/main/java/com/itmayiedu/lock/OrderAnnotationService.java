package com.itmayiedu.lock;

import com.itmayiedu.lock.aop.RLock;

public class OrderAnnotationService implements Runnable {
	private OrderNumberGenerator orderNumberGenerator = new OrderNumberGenerator();
	
	// private ExtLock lock = new ZookeeperDistrbuteLock();

	@Override
	public void run() {
		getNumber();
	}
	
	@RLock
	public void getNumber() {
		try {
			//lock.getLock();
			String number = orderNumberGenerator.getNumber();
			System.out.println(Thread.currentThread().getName() + "-" + number);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//lock.releaseLock();
		}
	}
	
	// 如果服务器是单机版的，生成订单号可以使用synchronized，或者是Lock保证线程安全
	public static void main(String[] args) {
		// OrderService orderService = new OrderService();
		
		// 多个线程共享一个全局id
		for (int i = 0; i < 100; i++) {
			new Thread(new OrderAnnotationService()).start();;
		}
	}
	
}
