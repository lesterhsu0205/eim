package eims.web.meta.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.BcMetaAppCdDto;
import eims.web.dto.table.BcMetaDto;
import eims.web.dto.table.BcMetaSysCdDto;

@Mapper
public interface BcMetaDao {

	int selectAllCnt();

	List<BcMetaDto> selectAll(@Param("tobeAreaId") String tobeAreaId, @Param("appAreaId") String appAreaId);

	List<BcMetaDto> selectTobe(@Param("tobeAreaId") String tobeAreaId);

	List<BcMetaDto> selectApp(@Param("appAreaId") String appAreaId);
	
	List<BcMetaDto> selectTobeAll(@Param("tobeAreaId") String tobeAreaId);

	List<BcMetaDto> selectAppAll(@Param("appAreaId") String appAreaId);

	List<BcMetaAppCdDto> selectAppCode(
			@Param("stdAreaId") String stdAreaId,
			@Param("pCdValId") String pCdValId);

	List<BcMetaSysCdDto> selectSrSysCode(
			@Param("stdAreaId") String stdAreaId,
			@Param("cdId") String cdId);

}