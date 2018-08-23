package cn.com.zach.demo.glasses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class Application {
	
	@ResponseBody
	@RequestMapping("/")
	public String index() {
		return "Hello World";
	}

    public static void main(String[] args) {
        // 启动服务
        SpringApplication.run(Application.class, args);
    }
}