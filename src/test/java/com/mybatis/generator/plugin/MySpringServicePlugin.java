package com.mybatis.generator.plugin;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

public class MySpringServicePlugin extends PluginAdapter {

	private String targetDir; // 目标文件夹

	private String serviceInterfaceTargetPackage; // 接口包

	private String serviceImplTargetPackage; // 接口实现类包

	private ShellCallback shellCallback = null;

	@Override
	public boolean validate(List<String> warnings) {
		
		targetDir = properties.getProperty("targetDir");
        boolean valid1 = stringHasValue(targetDir);

        serviceInterfaceTargetPackage = properties.getProperty("serviceInterfaceTargetPackage");
        boolean valid2 = stringHasValue(serviceInterfaceTargetPackage);


        serviceImplTargetPackage =  properties.getProperty("serviceImplTargetPackage");
        boolean valid3 = stringHasValue(serviceImplTargetPackage);
        return valid1 && valid2 && valid3;
//		return true;
	}

	public MySpringServicePlugin() {
		shellCallback = new DefaultShellCallback(false);
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
		// TODO Auto-generated method stub
		return super.contextGenerateAdditionalJavaFiles();
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();
		for (GeneratedJavaFile javaFile : introspectedTable.getGeneratedJavaFiles()) {
			CompilationUnit unit = javaFile.getCompilationUnit();
			FullyQualifiedJavaType baseModelJavaType = unit.getType();

			String shortName = baseModelJavaType.getShortName();
			GeneratedJavaFile springServiceJavafile = null;
			GeneratedJavaFile springServiceImplJavaFile = null;
			if (shortName.endsWith("Mapper")) {/// 复制Mapper代码创建Spring Service 接口
				try {
					// 生成Service Interface 文件
					springServiceJavafile = generaeteSpringServiceFile(shortName, javaFile);

					if (checkFileIsExist(targetDir, serviceInterfaceTargetPackage, springServiceJavafile)) {
						mapperJavaFiles.add(springServiceJavafile);
					}
					// 生成Service Interface Impl 文件
					springServiceImplJavaFile = generateSpringServiceImplFile(baseModelJavaType, javaFile, springServiceJavafile);
					if (checkFileIsExist(targetDir, serviceImplTargetPackage, springServiceImplJavaFile)) {
						mapperJavaFiles.add(springServiceImplJavaFile);
					}
				} catch (ShellException e) {
					e.printStackTrace();
				}
			}
		}

		return super.contextGenerateAdditionalJavaFiles(introspectedTable);
	}

	public GeneratedJavaFile generaeteSpringServiceFile(String shortName, GeneratedJavaFile javaFile) {
		JavaFormatter javaFormatter = context.getJavaFormatter();
		Interface mapperInterface = new Interface(
				serviceInterfaceTargetPackage + "." + shortName.replace("Mapper", "Service"));
		mapperInterface.setVisibility(JavaVisibility.PUBLIC);

		// Copy Mapper中的方法
		mapperInterface.addImportedTypes(javaFile.getCompilationUnit().getImportedTypes());
		for (Method method : ((Interface) javaFile.getCompilationUnit()).getMethods()) {
			mapperInterface.addMethod(method);
		}
		GeneratedJavaFile springServiceJavafile = new GeneratedJavaFile(mapperInterface, targetDir, javaFormatter);
		return springServiceJavafile;
	}

	public boolean checkFileIsExist(String targetDir, String targetPackage, GeneratedJavaFile file) throws ShellException {
		File dir = shellCallback.getDirectory(targetDir, targetPackage);
		File serviceFile = new File(dir, file.getFileName());
		if (!serviceFile.exists()) {
			return true;
		}
		return false;
	}
	
	public GeneratedJavaFile generateSpringServiceImplFile(FullyQualifiedJavaType baseModelJavaType,GeneratedJavaFile javaFile,GeneratedJavaFile springServiceJavafile){
        JavaFormatter javaFormatter = context.getJavaFormatter();

        String shortName = baseModelJavaType.getShortName();
        //生成类
        TopLevelClass topLevelClass = getToplevelClass(shortName);
        //实现接口
        implementInterface(topLevelClass, shortName);
        //添加Import Type
        addImportType(topLevelClass, baseModelJavaType, springServiceJavafile);
        //添加Field
        addField(topLevelClass, shortName);
        //添加方法
        addMethod(topLevelClass, javaFile, shortName);

        GeneratedJavaFile springServiceImplJavaFile = new GeneratedJavaFile(topLevelClass, targetDir, javaFormatter);

        return springServiceImplJavaFile;
    }
	
	public TopLevelClass getToplevelClass(String shortName) {
		FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(
				serviceImplTargetPackage + "." + shortName.replace("Mapper", "ServiceImpl"));
		TopLevelClass topLevelClass = new TopLevelClass(fullyQualifiedJavaType);
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);
		topLevelClass.addAnnotation("@Service");
		return topLevelClass;
	}
	
	public void implementInterface(TopLevelClass topLevelClass,String shortName){
        FullyQualifiedJavaType interfaceJavaType = new FullyQualifiedJavaType(
                serviceInterfaceTargetPackage + "." + shortName.replace("Mapper", "Service"));
        topLevelClass.addSuperInterface(interfaceJavaType);
    }
	
	public void addImportType(TopLevelClass topLevelClass,FullyQualifiedJavaType baseModelJavaType,GeneratedJavaFile springServiceJavafile){
        topLevelClass.addImportedTypes(springServiceJavafile.getCompilationUnit().getImportedTypes());
        topLevelClass.addImportedType(baseModelJavaType.getFullyQualifiedName());
        topLevelClass.addImportedTypes(topLevelClass.getSuperInterfaceTypes());
        topLevelClass.addImportedType("org.springframework.stereotype.Service");
        topLevelClass.addImportedType("org.springframework.beans.factory.annotation.Autowired");
    }
	
	public void addField(TopLevelClass topLevelClass,String shortName){
        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(new FullyQualifiedJavaType(shortName));
        String mapperVariableName = shortName.substring(0, 1).toLowerCase() + shortName.substring(1);
        field.setName(mapperVariableName);
        field.addAnnotation("@Autowired");
        topLevelClass.addField(field);
    }
	
	public void addMethod(TopLevelClass topLevelClass, GeneratedJavaFile javaFile, String shortName) {
		String mapperVariableName = shortName.substring(0, 1).toLowerCase() + shortName.substring(1);
		for (Method method : ((Interface) javaFile.getCompilationUnit()).getMethods()) {
			Method implMethod = new Method(method);
			method.setVisibility(JavaVisibility.PUBLIC);
			StringBuffer bodyLine = new StringBuffer("return ").append(mapperVariableName).append(".").append(method.getName()).append("(");
			if (null != method.getParameters() && method.getParameters().size() > 0) {
				int paramterCount = 1;
				for (Parameter parameter : method.getParameters()) {
					String[] parameterArray = parameter.toString().split(" ");
					String parameterName = parameterArray[parameterArray.length - 1];
					if (paramterCount > 1) {
						bodyLine.append(",").append(parameterName);
					} else {
						bodyLine.append(parameterName);
					}
					paramterCount++;
				}
			}
			bodyLine.append(");");
			implMethod.addBodyLine(bodyLine.toString());
			topLevelClass.addMethod(implMethod);
		}
	}
}
