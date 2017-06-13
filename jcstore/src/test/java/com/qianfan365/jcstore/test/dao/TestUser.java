package com.qianfan365.jcstore.test.dao;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qianfan365.jcstore.common.pojo.Client;
import com.qianfan365.jcstore.main.StartUp;
import com.qianfan365.jcstore.service.ClientService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(StartUp.class)
public class TestUser {
	@Autowired
	private ClientService clientService;

	@Test
	public void testSave() {
		Integer months = 10;
		Client client = new Client();
		client.setUsername("wo de ke fu ");
		client.setCompanyName("wo de ke fu");
		client.setExpiredTime(new Date());
		client.setUserNumber(2);
		client.setUid(100);
		client.setType(10);
		client.setClientName("我的客服");
		Client save = clientService.save(client, months);
		System.out.println("client:"+client);
		System.out.println("save:"+save);
	}
}
