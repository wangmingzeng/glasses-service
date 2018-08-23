package com.mybatis.generator.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class MyInsertElementGeneratedPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		element.addAttribute(new Attribute("keyProperty", "id"));
		element.addAttribute(new Attribute("useGeneratedKeys", "true"));
		return true;
	}

	@Override
	public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		element.addAttribute(new Attribute("keyProperty", "id"));
		element.addAttribute(new Attribute("useGeneratedKeys", "true"));
		return true;
	}
	
}
