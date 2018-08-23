package com.mybatis.generator.plugin;

import java.lang.reflect.Field;
import java.util.List;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

/**
 * 解决mapper文件重新生成不会覆盖原文件
 * 原因：在IntrospectedTableMyBatis3Impl.getGeneratedXmlFiles方法中，isMergeable值被写死为true了
 * 
 * @author zengwangming
 */
public class MyOverIsMergeablePlugin extends PluginAdapter{

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
		try {
			Field field = sqlMap.getClass().getDeclaredField("isMergeable");
			field.setAccessible(true);
			field.setBoolean(sqlMap, false);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	
}
