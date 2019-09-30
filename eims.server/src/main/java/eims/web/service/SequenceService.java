package eims.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eims.web.dao.SequenceDao;

@Service
public class SequenceService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SequenceDao sequenceDao;

//	public String createPrefixGuid(String prefix) {
//		return String.format("%s_%010d", prefix, sequenceDao.getNextVal());
//	}

	public int getNextVal() {
		return sequenceDao.getNextVal();
	}
}
