package com.mybatis.generator.plugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * 用Java方法执行生成
 * @author zengwangming
 */
public class GeneratorMain {
	
	
	public static void main(String[] args) throws IOException, XMLParserException, SQLException, InterruptedException, InvalidConfigurationException {
		//System.getProperty("user.dir") 获取程序当前路径
		//new File("").getCanonicalFile() 返回文件抽象路径名的规范形式
		File configFile = new File(System.getProperty("user.dir"), "generatorConfig.xml");
		System.out.println(configFile.getPath());
		if(configFile.exists()) {
			List<String> warnings = new ArrayList<String>();
			boolean overwrite = true;
			ConfigurationParser cp = new ConfigurationParser(warnings);
			Configuration config = cp.parseConfiguration(configFile);
			DefaultShellCallback callback = new DefaultShellCallback(overwrite);
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
			myBatisGenerator.generate(null);		
			System.out.println("successful");
		}else
			System.out.println("no generatorConfig.xml file was found");
	}
}
