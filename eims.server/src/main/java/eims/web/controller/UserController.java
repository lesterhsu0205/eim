package eims.web.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eims.web.dto.IntrfcDeploy;
import eims.web.dto.IntrfcInfo;
import eims.web.dto.table.UserDto;
import eims.web.dto.ui.UiUserOut;
import eims.web.service.UserService;

@Controller
public class UserController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<UiUserOut> getUsers(
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "userNm", required = false) String userNm,
			@RequestParam(value = "dutyNm", required = false) String dutyNm,
			@RequestParam(value = "deptNm", required = false) String deptNm,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "roleId", required = false) String roleId,
			@RequestParam(value = "userPwd", required = false) String userPwd,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "1000") int pageSize) {
		logger.debug(
				"INPUT   userId : [{}],  userNm : [{}],  dutyNm : [{}],  deptNm : [{}],  email : [{}],  roleId : [{}]",
				userId, userNm, dutyNm, deptNm, email, roleId);

		UiUserOut out = userService.getList(userId, userNm, dutyNm, deptNm, email, roleId, userPwd, pageSize, pageNumber);

		logger.debug(" OUTPUT : {}", out.getTotalCnt());

		return new ResponseEntity<UiUserOut>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	public ResponseEntity<UserDto> getUser(@PathVariable(value = "userId", required = true) String userId,
			HttpSession session) {

		logger.debug(" INPUT : userId : [{}]", userId);

		UserDto out = userService.get(userId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<UserDto>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/users", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> addUser(@RequestBody UserDto userDto, HttpSession session) {

		logger.debug(" INPUT : UserDto [{}]", userDto);

		int out = userService.add(userDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/users", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateUser(@RequestBody UserDto userDto, HttpSession session) {

		logger.debug(" INPUT : UserDto [{}]", userDto);

		int out = userService.update(userDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteUser(@PathVariable(value = "userId", required = true) String userId,
			HttpSession session) {

		logger.debug(" INPUT : userId : [{}]", userId);

		int out = userService.delete(userId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/userstest/test", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Integer> test(@RequestBody IntrfcDeploy in, HttpSession session) {

		logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~TTTTTTTEEEEEEESSSSSSSTTTTTT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		try {
			logger.debug("threadSleep Start~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			Thread.sleep(20000);
			logger.debug("threadSleep End~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		} catch (InterruptedException e) {
			logger.error("{}", e);
		}
		
		return new ResponseEntity<Integer>(0, HttpStatus.CREATED);
	}

}
