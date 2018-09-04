package cn.com.zach.demo.test.mongo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.com.zach.demo.glasses.Application;
import cn.com.zach.demo.glasses.common.property.PropertiesSourceInitializer;
import cn.com.zach.demo.glasses.service.MongoService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration(initializers = PropertiesSourceInitializer.class)
public class MongoUnitTest extends AbstractJUnit4SpringContextTests{

	@Autowired
	private MongoService<User> mongoService;
	
	@Test
	public void testMock() {
		User user = new User(1L, "张三", 18);
		mongoService.insert(user);
	}
}
