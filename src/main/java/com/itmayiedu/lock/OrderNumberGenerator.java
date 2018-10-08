package com.itmayiedu.lock;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生成订单号，使用时间戳
 * @author mayn
 *
 */
public class OrderNumberGenerator {

	// 区分不同的订单号
	private static int count = 0;
	
	/**
	 * 单台服务器上，多线程同时生成订单号，线程安全
	 * @return
	 */
	public String getNumber () {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		return sdf.format(new Date()) + "-" + ++count;
	}
	
}
