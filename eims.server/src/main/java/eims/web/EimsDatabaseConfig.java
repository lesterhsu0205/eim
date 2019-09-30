package eims.web;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import eims.web.constants.BxConstants;

@Configuration
@MapperScan(value = "eims.web.dao", sqlSessionFactoryRef = "eimsSqlSessionFactory")
@EnableTransactionManagement
public class EimsDatabaseConfig {

	@Bean(name = "eimsDatasource")
	@Primary
	@ConfigurationProperties(prefix = "spring.eims.datasource")
	public DataSource eimsDatasource() {
		//서버was
//		JndiDataSourceLookup datasourceLookup = new JndiDataSourceLookup();
//		DataSource datasource = datasourceLookup.getDataSource("java:/EIMSNXA");
//		return datasource;

		if(BxConstants.Default.IS_SERVER) {
			JndiDataSourceLookup datasourceLookup = new JndiDataSourceLookup();
			DataSource datasource = datasourceLookup.getDataSource("java:/EIMSNXA");
			return datasource;
		} else {
		//톰캣
			return DataSourceBuilder.create().build();
		}
	}

// Added : wizdev
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(eimsDatasource());
	}


	@Bean(name = "eimsSqlSessionFactory")
	@Primary
	public SqlSessionFactory eimsSqlSessionFactory(@Qualifier("eimsDatasource") DataSource eimsDatasource,
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(eimsDatasource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:eims/web/dao/xml/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}


	@Bean(name = "eimsSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate eimsSqlSessionTemplate(SqlSessionFactory eimsSqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(eimsSqlSessionFactory);
	}

}
