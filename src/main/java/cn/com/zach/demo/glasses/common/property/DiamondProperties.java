package cn.com.zach.demo.glasses.common.property;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
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
 *
 */
public class DiamondProperties extends DynamicProperties {

	private ConfigurableEnvironment env;

	private final static Logger logger = LoggerFactory.getLogger(SystemMessage.class);

	public static final String CONFIG_KEY = "diamondProperties";

	private static PropertiesPropertySource diamondProperties = new PropertiesPropertySource(CONFIG_KEY,
			new Properties());

	private DiamondProperties() {
	}

	public static DynamicProperties initInstance(ConfigurableEnvironment cenv) {
		Assert.notNull(cenv, "ConfigurableEnvironment is null");
		Assert.isNull(dynamicProperties, "dynamicProperties initial is not null");
		DiamondProperties diamondProperties = new DiamondProperties();
		diamondProperties.setEnv(cenv);
		diamondProperties.init();
		DiamondProperties.dynamicProperties = diamondProperties;

		return diamondProperties;
	}

	private void setEnv(ConfigurableEnvironment env) {
		this.env = env;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.com.flaginfo.common.support.DynamicProperties#getEnv()
	 */
	public ConfigurableEnvironment getEnv() {
		return env;
	}

	public Map<String, Object> getAllConfig() {
		env.getPropertySources();
		MutablePropertySources propertySources = env.getPropertySources();
		MapPropertySource ps = (MapPropertySource) propertySources.get(CONFIG_KEY);
		if (ps == null) {
			return null;
		}
		return ps.getSource();
	}

	private void init() {

		// final String propGroup = env.getProperty("prop_group");
		// final String propDataId = env.getProperty("prop_data_id");
		// 读取服务配置信息
		// initDiamond(propGroup,propDataId);
	}

	public void initDiamond(String propGroup, String propDataId) {
//		DiamondManager manager = new DefaultDiamondManager(propGroup, propDataId, new ManagerListener() {
//			@Override
//			public Executor getExecutor() {
//				return null;
//			}
//
//			@Override
//			public void receiveConfigInfo(String configInfo) {
//				updateData(configInfo);
//			}
//		});
//		updateData(manager.getAvailableConfigureInfomation(5000));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.com.flaginfo.common.support.DynamicProperties#getProperty(java.lang.
	 * String)
	 */
	@Override
	public String getProperty(String key) {
		try {
			return env.getProperty(key);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}

	public void updateData(String data) {
		Map<String, Object> tmp = this.getAllConfig();
		preData = tmp == null ? null : tmp;
		Properties properties = getPropertiesForString(data);
		diamondProperties.getSource().putAll(getAllConfig(properties));
		MutablePropertySources propertySources = env.getPropertySources();
		propertySources.addLast(diamondProperties);
		messageChange();
	}

	private Properties getPropertiesForString(String configInfo) {
		Properties p = new Properties();
		if (configInfo == null)
			return p;
		try {
			p.load(new StringReader(configInfo));
			if (logger.isDebugEnabled()) {
				Set<Entry<Object, Object>> set = p.entrySet();
				for (Entry<Object, Object> e : set) {
					logger.debug(e.getKey() + "==>" + e.getValue());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	private Map<String, Object> getAllConfig(Properties prop) {
		Set<Entry<Object, Object>> set = prop.entrySet();
		Map<String, Object> allMap = new HashMap<String, Object>();
		for (Entry<Object, Object> e : set) {
			allMap.put((String) e.getKey(), e.getValue());
		}
		return allMap;
	}

}
