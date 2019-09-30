package eims.web.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SequenceDao {

	int getNextVal();
}
