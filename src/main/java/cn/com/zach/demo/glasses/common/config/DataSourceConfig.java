package cn.com.zach.demo.glasses.common.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.github.pagehelper.PageHelper;

import cn.com.zach.demo.glasses.common.property.SystemMessage;

@Configuration
public class DataSourceConfig {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

	// 配置数据源
	@Bean(name = "dataSource")
	@Primary
	public DataSource dataSource() {
		logger.info("mysqlDataSource is running...");
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(SystemMessage.getString("spring.datasource.url"));
		dataSource.setUsername(SystemMessage.getString("spring.datasource.username"));
		dataSource.setPassword(SystemMessage.getString("spring.datasource.password"));
		dataSource.setDriverClassName(SystemMessage.getString("spring.datasource.driverClassName"));
		dataSource.setInitialSize(SystemMessage.getInteger("spring.datasource.initialSize"));
		dataSource.setMinIdle(SystemMessage.getInteger("spring.datasource.minIdle"));
		dataSource.setMaxActive(SystemMessage.getInteger("spring.datasource.maxActive"));
		dataSource.setMaxWait(SystemMessage.getInteger("spring.datasource.maxWait"));
		dataSource.setTimeBetweenEvictionRunsMillis(SystemMessage.getInteger("spring.datasource.timeBetweenEvictionRunsMillis"));
		dataSource.setMinEvictableIdleTimeMillis(SystemMessage.getInteger("spring.datasource.minEvictableIdleTimeMillis"));
		dataSource.setValidationQuery(SystemMessage.getString("spring.datasource.validationQuery"));
		dataSource.setTestWhileIdle(SystemMessage.getBoolean("spring.datasource.testWhileIdle"));
		dataSource.setTestOnBorrow(SystemMessage.getBoolean("spring.datasource.testOnBorrow"));
		dataSource.setTestOnReturn(SystemMessage.getBoolean("spring.datasource.testOnReturn"));
		dataSource.setPoolPreparedStatements(SystemMessage.getBoolean("spring.datasource.poolPreparedStatements"));
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(SystemMessage.getInteger("spring.datasource.maxPoolPreparedStatementPerConnectionSize"));
		return dataSource;
	}

	@Bean(name = "transactionManager")
	@Primary
	public DataSourceTransactionManager transactionManager() {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource());
		return dataSourceTransactionManager;
	}
	
	@Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource masterDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(SystemMessage.getString("mybatis.mapper-locations")));
        sessionFactory.setTypeAliasesPackage(SystemMessage.getString("mybatis.type-aliases-package"));
        return sessionFactory.getObject();
    }
	
	@Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", SystemMessage.getString("druid.username"));
        reg.addInitParameter("loginPassword", SystemMessage.getString("druid.password"));
        reg.addInitParameter("logSlowSql", SystemMessage.getString("druid.logSlowSql"));
        return reg;
    }
	
	@Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }
	
	//配置mybatis的分页插件pageHelper
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("helperDialect", "mysql");    //配置mysql数据库的方言
        properties.setProperty("params","count=countSql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
