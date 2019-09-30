package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.MetaEffectDto;
import eims.web.dto.table.MetaInterfaceDto;
import eims.web.dto.table.MetabsDto;

@Mapper
public interface MetabsDao {

	MetabsDto selectMetabs(@Param("metaKorNm") String metaKorNm);

	int insertMetabs(MetabsDto metabs);

	int insertMetabsList(List<MetabsDto> metabs);

	int selectAllCnt(@Param("metaEngNm") String metaEngNm,
			@Param("metaKorNm") String metaKorNm,
			@Param("dataTypeNm") String dataTypeNm,
			@Param("metaLen") int metaLen,
			@Param("decimalLen") int decimalLen);

	List<MetabsDto> selectAll(@Param("metaEngNm") String metaEngNm,
			@Param("metaKorNm") String metaKorNm,
			@Param("dataTypeNm") String dataTypeNm,
			@Param("metaLen") int metaLen,
			@Param("decimalLen") int decimalLen,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	int updateMetabs(MetabsDto metabs);

	int deleteMetabs(@Param("metaEngNm") String metaEngNm);

	int deleteMetaAll();

	List<MetaEffectDto> selectMetaEffect(MetaEffectDto metaEffectDto);
	
	List<MetaInterfaceDto> selectMetaInterfaceList();
	
	void updateInterface(MetaInterfaceDto metaInterfaceDto);

}