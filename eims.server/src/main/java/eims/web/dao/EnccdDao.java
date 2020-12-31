package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.CodeInterfaceDto;
import eims.web.dto.table.EnccdDto;
import eims.web.dto.table.MaskcdDto;

@Mapper
public interface EnccdDao {
   

	int insertEnccd(EnccdDto maskCd);

	int selectAllCnt();

	List<EnccdDto> selectAll();

	int updateEnccd(EnccdDto maskCd);

	int deleteEnccd(@Param("encCd") String encCd);
	
	
	List<CodeInterfaceDto> selectEncCodeInterfaceList();
	
	
	void updateEncCodeInterface(CodeInterfaceDto codeInterfaceDto);
}