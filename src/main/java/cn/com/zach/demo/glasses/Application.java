package cn.com.zach.demo.glasses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.zach.demo.glasses.common.property.PropertiesSourceInitializer;
import cn.com.zach.demo.glasses.mode.Result;


@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@RestController
public class Application {

	@RequestMapping("/")
	public String index() {
		return "glasses index";
	}
	
	@RequestMapping("/healthy")
	public Result healthy() {
		return Result.success("Service is alive.");
	}
	
	public static void main(String[] args) {
		SpringApplication sa = new SpringApplication(Application.class);
		sa.addInitializers(new PropertiesSourceInitializer());
		sa.run(args);
		System.out.println("System boot up...");
	}
}
