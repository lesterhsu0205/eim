package eims.web.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eims.web.constants.BxMessages;
import eims.web.dao.UserDao;
import eims.web.dto.table.UserDto;
import eims.web.dto.ui.UiUserOut;
import eims.web.exception.ServiceException;
import eims.web.utils.CryptoUtils;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserDao userDao;

	public UiUserOut getList(String userId, String userNm, String dutyNm, String deptNm, String email, String roleId, String userPwd, int pageSize, int pageNumber) {
		UiUserOut out = new UiUserOut();
		List<UserDto> detailOut = new ArrayList<UserDto>();

		int totalCount = userDao.selectAllCnt(userId, userNm, dutyNm, deptNm, email, roleId, userPwd);
		List<UserDto> userList = userDao.selectAll(userId, userNm, dutyNm, deptNm, email, roleId, userPwd, pageSize, pageNumber);

		for (UserDto user : userList) {
			detailOut.add(user);
		}

		if (totalCount > 0) {
			out.setTotalCnt(totalCount);
			out.setUserOutList(detailOut);
		}
		return out;
	}

	public UserDto get(String userId) {
		UserDto userDto = userDao.selectUser(userId);

		if (userDto == null) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, userId);
		}

		String password = userDto.getUserPwd();
		try {
			userDto.setUserPwd(CryptoUtils.instance().decrypt(password, userDto.getUserId()));
		} catch (Exception e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION);
		}

		return userDto;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int add(UserDto in) {
		UserDto curUserInfo = userDao.selectUser(in.getUserId());

		if (curUserInfo != null) {
			throw new ServiceException(BxMessages.Error.DUPLICATE_KEY, in.getUserId());
		}

		String userPassword = in.getUserPwd();
		try {
			String encryptPw = CryptoUtils.instance().encrypt(userPassword, in.getUserId());
			in.setUserPwd(encryptPw);
		} catch (Exception e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION, in.getUserId());
		}

		return userDao.insertUser(in);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int update(UserDto in) {

		String userPassword = in.getUserPwd();
		try {
			String encryptPw = CryptoUtils.instance().encrypt(userPassword, in.getUserId());
			in.setUserPwd(encryptPw);
		} catch (Exception e) {
			logger.error("{}", e);
			throw new ServiceException(BxMessages.Error.SYSTEM_EXCEPTION, in.getUserId());
		}

		int out = userDao.updateUser(in);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, in.getUserId());
		}

		return out;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int delete(String userId) {
		int out = userDao.deleteUser(userId);

		if (out == 0) {
			throw new ServiceException(BxMessages.Error.NOT_FOUNDED, userId);
		}

		return out;
	}

}
