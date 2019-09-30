package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.AppcdDto;
import eims.web.dto.table.CodeInterfaceDto;
import eims.web.dto.table.MaskcdDto;

@Mapper
public interface MaskcdDao {
   

	int insertMaskcd(MaskcdDto maskCd);

	int selectAllCnt();

	List<MaskcdDto> selectAll();

	int updateMaskcd(MaskcdDto maskCd);

	int deleteMaskcd(@Param("maskCd") String maskCd);
	
	
	List<CodeInterfaceDto> selectMaskCodeInterfaceList();
	
	
	void updateMaskCodeInterface(CodeInterfaceDto codeInterfaceDto);
}