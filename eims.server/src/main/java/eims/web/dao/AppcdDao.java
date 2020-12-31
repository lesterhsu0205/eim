package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.AppcdDto;
import eims.web.dto.table.CodeInterfaceDto;

@Mapper
public interface AppcdDao {

	AppcdDto selectAppcd(@Param("appCd") String appCd, @Param("parentAppCd") String parentAppCd,
			@Param("lvCd") String lvCd);

	int insertAppcd(AppcdDto appcd);

	int selectAllCnt(@Param("appCd") String appCd, @Param("appCdNm") String appCdNm,
			@Param("parentAppCd") String parentAppCd, @Param("lvCd") String lvCd,
			@Param("alignOrderNo") int alignOrderNo, @Param("appCdDesc") String appCdDesc);

	List<AppcdDto> selectAll(@Param("appCd") String appCd, @Param("appCdNm") String appCdNm,
			@Param("parentAppCd") String parentAppCd, @Param("lvCd") String lvCd,
			@Param("alignOrderNo") int alignOrderNo, @Param("appCdDesc") String appCdDesc,
			@Param("pageSize") int pageSize, @Param("pageNumber") int pageNumber);

	int updateAppcd(AppcdDto appcd);

	int deleteAppcd(@Param("appCd") String appCd, @Param("parentAppCd") String parentAppCd, @Param("lvCd") String lvCd);

	int deleteAppCdAll();
	
	List<CodeInterfaceDto> selectAppCodeInterfaceList();
	
	void updateAppCodeInterface(CodeInterfaceDto codeInterfaceDto);
}