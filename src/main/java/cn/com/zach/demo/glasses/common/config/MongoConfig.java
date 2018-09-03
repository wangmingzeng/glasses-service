package cn.com.zach.demo.glasses.common.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import cn.com.zach.demo.glasses.common.property.SystemMessage;
import cn.com.zach.demo.glasses.common.utils.StringUtil;


@Configuration
public class MongoConfig {

	private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

	private String database = SystemMessage.getString("spring.datasource.mongo.custom.database");
	private String address = SystemMessage.getString("spring.datasource.mongo.custom.address");
	private String username = SystemMessage.getString("spring.datasource.mongo.custom.username");
	private String password = SystemMessage.getString("spring.datasource.mongo.custom.password");
	private String authenticationDatabase = SystemMessage.getString("spring.datasource.mongo.custom.authenticationDatabase");
	private Integer minConnectionsPerHost = SystemMessage.getInteger("spring.datasource.mongo.custom.minConnectionsPerHost");
	private Integer connectionsPerHost = SystemMessage.getInteger("spring.datasource.mongo.custom.connectionsPerHost");

	@Bean
	public MongoDbFactory mongoDbFactory() {
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.connectionsPerHost(connectionsPerHost);
		builder.minConnectionsPerHost(minConnectionsPerHost);
//		builder.writeConcern(WriteConcern.SAFE);
//		builder.connectTimeout(connectTimeout);
//		if (replicaSet != null) {
//			builder.requiredReplicaSetName(replicaSet);
//		}
		MongoClientOptions mongoClientOptions = builder.build();
		List<ServerAddress> serverAddresses = new ArrayList<>();
		
		if(StringUtil.isEmpty(address)) {
			throw new IllegalArgumentException("server address is not legal...");
		}
		String addressArray[] =  StringUtil.split(address); 
		for(String addr: addressArray) {
			String targetAddr[] = addr.split(":");
			if(targetAddr.length == 2) {
				ServerAddress serverAddress = new ServerAddress(targetAddr[0], Integer.valueOf(targetAddr[1]));
				serverAddresses.add(serverAddress);
			}
		}
		logger.info("monggoDB serverAddresses : {}", serverAddresses.toString());
		List<MongoCredential> mongoCredentialList = new ArrayList<>();
		if (username != null) {
			mongoCredentialList.add(MongoCredential.createScramSha1Credential(username,
					authenticationDatabase != null ? authenticationDatabase : database, password.toCharArray()));
		}
		logger.info("monggoDB mongoCredentialList : {}", mongoCredentialList.toString());
		MongoClient mongoClient = new MongoClient(serverAddresses, mongoCredentialList, mongoClientOptions);
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, database);
		return mongoDbFactory;
	}

	@Bean(name = "mongoTemplate")
	public MongoTemplate mongoTemplate() throws Exception {
		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory()), new MongoMappingContext());
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		return new MongoTemplate(mongoDbFactory(), converter);
	}
}
