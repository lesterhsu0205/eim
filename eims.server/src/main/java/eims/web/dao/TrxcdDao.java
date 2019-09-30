package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.TrxcdDto;

@Mapper
public interface TrxcdDao {

	TrxcdDto selectTrxcd(@Param("trxCd") String trxCd, @Param("mngSysCd") String mngSysCd);


	int insertTrxcd(TrxcdDto trxcd);


	int selectAllCnt(@Param("trxCd") String trxCd, @Param("trxCdNm") String trxCdNm, @Param("mngSysCd") String mngSysCd,
			@Param("trxCdDesc") String trxCdDesc);


	List<TrxcdDto> selectAll(@Param("trxCd") String trxCd, @Param("trxCdNm") String trxCdNm,
			@Param("mngSysCd") String mngSysCd, @Param("trxCdDesc") String trxCdDesc, @Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);


	int updateTrxcd(TrxcdDto trxcd);


	int deleteTrxcd(@Param("trxCd") String trxCd, @Param("mngSysCd") String mngSysCd);
}