package eims;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import eims.web.dao.CommCodeDao;
import eims.web.dao.UserDao;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	ApplicationContextProvider provider;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		ApplicationContext context = provider.getContext();
		// 공통코드 Loading
		CommCodeDao codeDao = context.getBean(CommCodeDao.class);
//		
		ServiceContext.setCommCode(codeDao);
	}
}
