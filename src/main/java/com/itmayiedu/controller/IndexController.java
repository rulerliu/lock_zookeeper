package com.itmayiedu.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itmayiedu.lock.OrderAnnotationService;

@RestController
public class IndexController {

	@RequestMapping("/index")
	public String index() {
		// 多个线程共享一个全局id
		for (int i = 0; i < 100; i++) {
			new Thread(new OrderAnnotationService()).start();
			;
		}
		return "index";
	}

}
