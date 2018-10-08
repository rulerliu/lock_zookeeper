package com.itmayiedu.lock;

public class OrderService implements Runnable {
	private OrderNumberGenerator orderNumberGenerator = new OrderNumberGenerator();
	
	private ExtLock lock = new ZookeeperDistrbuteLock();

	@Override
	public void run() {
		getNumber();
	}
	
	// 使用synchronized，目的保证线程安全，只能让一个线程进行操作
	public void getNumber() {
		try {
			lock.getLock();
			String number = orderNumberGenerator.getNumber();
			System.out.println(Thread.currentThread().getName() + "-" + number);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			lock.releaseLock();
		}
	}
	
	// 如果服务器是单机版的，生成订单号可以使用synchronized，或者是Lock保证线程安全
	public static void main(String[] args) {
		// OrderService orderService = new OrderService();
		
		// 多个线程共享一个全局id
		for (int i = 0; i < 100; i++) {
			new Thread(new OrderService()).start();;
		}
	}
	
}
