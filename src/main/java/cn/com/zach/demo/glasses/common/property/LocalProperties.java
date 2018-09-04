package cn.com.zach.demo.glasses.common.property;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.util.Assert;

/**
 * 动态配置文件加载
 * @author zengwangming
 *
 */
public class LocalProperties extends DynamicProperties {
	
	private final static Logger logger = LoggerFactory.getLogger(SystemMessage.class);
	
	private ConfigurableEnvironment env;
	
	public static final String CONFIG_KEY = "localProperties";
	
	private static PropertiesPropertySource diamondProperties = new PropertiesPropertySource(CONFIG_KEY, new Properties());
	
	public static final String[]	RESOURCE_DIRECTORY = new String[]{"context", "config"};
	
	public static final String[]	suffixFilter	 = new String[]{ "properties.xml", "properties", "config.xml", "configuration.xml"};	/*配置文件后缀过滤*/
	
	private LocalProperties(){}
	
	public static DynamicProperties initInstance(ConfigurableEnvironment cenv){
		Assert.notNull(cenv, "ConfigurableEnvironment is null");
		Assert.isNull(dynamicProperties, "dynamicProperties initial is not null");
		LocalProperties diamondProperties = new LocalProperties();
		diamondProperties.setEnv(cenv);
		diamondProperties.init();
		LocalProperties.dynamicProperties = diamondProperties;
		
		return diamondProperties;
	}
	
	private void setEnv(ConfigurableEnvironment env) {
		this.env = env;
	}
	
	public ConfigurableEnvironment getEnv() {
		return env;
	}
	
	//读取服务配置信息
	private void init(){
		try {
			//属性
			Properties properties = new Properties();
			List<File> directores = getResources();
			if (directores != null && directores.size() > 0) {
				for (File dir : directores) {
					List<File> list = new ArrayList<File>();
					traverse(dir, list);
					Properties prop = null;
					for (File file : list) {
						prop = loadProperties(file);
						properties.putAll(prop);
					}
				}
			}
			//配置更新
			diamondProperties.getSource().putAll(getAllConfig(properties));
			MutablePropertySources propertySources = env.getPropertySources();
			propertySources.addLast(diamondProperties);
		}catch(Exception e) {
			e.printStackTrace();
			logger.info("init properties exception");
		}
	}
	
	private List<File> getResources() {
		List<File> answer = new ArrayList<File>();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			File file = null;
			for (String resource : RESOURCE_DIRECTORY) {
				java.net.URL url = classLoader.getResource(resource);
				if (url != null) {
					file = new File(url.getFile());
					answer.add(file);
				}
			}
			File classpath = new File(classLoader.getResource("").getFile());
			if(classpath != null && classpath.getPath().endsWith("test-classes")) {	
				//处理单元测试时读取不到正确路径下的配置文件
				classpath = new File(classpath.getParent() + File.separator + "classes");
			}
			answer.add(classpath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return answer;
	}
	
	private void traverse(File node, List<File> aFiles) {
		if (node.canRead()) {
			if (node.isDirectory()) {
				final File[] nodes = node.listFiles();
				for (File element : nodes) {
					String filename = element.getName();
					if(filename.startsWith("cn")||filename.startsWith("mapper")) {
						continue;
					}
					traverse(element, aFiles);
				}
			} else if (node.isFile()) {
				String nodename = node.getName().toLowerCase();
				if (suffixFilter != null && suffixFilter.length > 0) {
					for (String filter : suffixFilter) {
						if (nodename.indexOf(filter) >= 0) {
							aFiles.add(node);
							break;
						}
					}
				} else {
					aFiles.add(node);
				}
			}
		}
	}
	
	/** 
	 * 加载Properties 文件
	 * @param path
	 * @return
	 * @throws Exception
	 */
	private Properties loadProperties(File path) throws Exception {
		Properties answer = null;
		BufferedInputStream buffer = null;
		if (path != null) {
			String name = path.getName().toLowerCase();
			if(name.endsWith("xml") || name.endsWith("properties")) {
				try {
					buffer = new BufferedInputStream(new FileInputStream(path));
					answer = new Properties();
					if (name.endsWith("xml")) {
						answer.loadFromXML(buffer);
					} else if (name.endsWith("properties")) {
						answer.load(buffer);
					}
				} finally {
					if (buffer != null) {
						buffer.close();
					}
				}
			}
		}
		return answer;
	}
	
	@Override
	public String getProperty(String key){
		try {
			return env.getProperty(key);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}
	
	
	public Map<String,Object> getAllConfig() {
		MutablePropertySources propertySources = env.getPropertySources();
		MapPropertySource ps = (MapPropertySource)propertySources.get(CONFIG_KEY);
		if(ps == null){
			return null;
		}
		return ps.getSource();
	}
	
	private Map<String,Object> getAllConfig(Properties prop) {
		Set<Entry<Object, Object>> set = prop.entrySet();
		Map<String,Object> allMap = new HashMap<String,Object>();
		for (Entry<Object, Object> e : set) {
			allMap.put((String)e.getKey(), e.getValue());
		}
		return allMap;
	}
	
}
