package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.IntrfcdeployhisthsDto;
import eims.web.dto.table.MsglayoutdtDto;

@Mapper
public interface MsglayoutdtDao {

	List<MsglayoutdtDto> selectMsglayoutdt(@Param("msgLayoutId") String msgLayoutId);

	int insertMsglayoutdt(MsglayoutdtDto msglayoutdt);

	int selectAllCnt(@Param("msgLayoutId") String msgLayoutId);

	List<MsglayoutdtDto> selectAll(@Param("msgLayoutId") String msgLayoutId,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	int updateMsglayoutdt(MsglayoutdtDto msglayoutdt);

	int deleteMsglayoutdt(@Param("msgLayoutId") String msgLayoutId);
	
	List<String> selectSameIONmChildNm(@Param("childDtoNm") String childDtoNm, 
			@Param("msgLayoutId") String msgLayoutId);
	
	
}