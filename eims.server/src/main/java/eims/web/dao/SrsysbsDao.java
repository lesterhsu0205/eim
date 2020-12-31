package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.CodeInterfaceDto;
import eims.web.dto.table.SrsysbsDto;

@Mapper
public interface SrsysbsDao {

	SrsysbsDto selectSrsysbs(@Param("sysCd") String sysCd);

	int insertSrsysbs(SrsysbsDto srsysbs);

	int selectAllCnt(@Param("sysCd") String sysCd,
			@Param("sysNm") String sysNm,
			@Param("sysCdDesc") String sysCdDesc,
			@Param("crgManNm") String crgManNm,
			@Param("noncoreYn") String noncoreYn);

	List<SrsysbsDto> selectAll(@Param("sysCd") String sysCd,
			@Param("sysNm") String sysNm,
			@Param("sysCdDesc") String sysCdDesc,
			@Param("crgManNm") String crgManNm,
			@Param("noncoreYn") String noncoreYn,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	int updateSrsysbs(SrsysbsDto srsysbs);

	int deleteSrsysbs(@Param("sysCd") String sysCd);

	int deleteSrsysbsAll();
	
	List<CodeInterfaceDto> selectSysCodeInterfaceList();
	
	void updateSysCodeInterface(CodeInterfaceDto codeInterfaceDto);
	
}