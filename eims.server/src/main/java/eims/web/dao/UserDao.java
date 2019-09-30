package eims.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import eims.web.dto.table.UserDto;

@Mapper
public interface UserDao {

	List<UserDto> selectAll(@Param("userId") String userId,
			@Param("userNm") String userNm,
			@Param("dutyNm") String dutyNm,
			@Param("deptNm") String deptNm,
			@Param("email") String email,
			@Param("roleId") String roleId,
			@Param("userPwd") String userPwd,
			@Param("pageSize") int pageSize,
			@Param("pageNumber") int pageNumber);

	int selectAllCnt(
			@Param("userId") String userId,
			@Param("userNm") String userNm,
			@Param("dutyNm") String dutyNm,
			@Param("deptNm") String deptNm,
			@Param("email") String email,
			@Param("roleId") String roleId,
			@Param("userPwd") String userPwd);

	UserDto selectUser(@Param("userId") String userId);

	int insertUser(UserDto user);

	int updateUser(UserDto user);

	int deleteUser(@Param("userId") String userId);
}
