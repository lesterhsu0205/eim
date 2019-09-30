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
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import eims.web.constants.BxConstants;

@Configuration
@MapperScan(value = "eims.web.meta.dao", sqlSessionFactoryRef = "metaSqlSessionFactory")
@EnableTransactionManagement
public class MetaDatabaseConfig {

	@Bean(name = "metaDatasource")
	@ConfigurationProperties(prefix = "spring.meta.datasource")
	public DataSource metaDatasource() {
		if(BxConstants.Default.IS_SERVER) {
			JndiDataSourceLookup datasourceLookup = new JndiDataSourceLookup();
			DataSource datasource = datasourceLookup.getDataSource("java:/EIMSNXA");
			return datasource;
		} else {
		//톰캣
			return DataSourceBuilder.create().build();
		}
	}

	@Bean(name = "metaSqlSessionFactory")
	public SqlSessionFactory metaSqlSessionFactory(@Qualifier("metaDatasource") DataSource metaDatasource, ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(metaDatasource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:eims/web/meta/dao/xml/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "metaSqlSessionTemplate")
	public SqlSessionTemplate metaSqlSessionTemplate(SqlSessionFactory metaSqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(metaSqlSessionFactory);
	}

}
