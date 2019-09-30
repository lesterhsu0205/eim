package eims.web.controller;

import java.util.List;

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

import eims.web.dto.table.MenuDto;
import eims.web.dto.ui.UiMenuTreeInfo;
import eims.web.service.MenuService;

@Controller
public class MenuController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MenuService menuService;

	@RequestMapping(value = "/menus", method = RequestMethod.GET)
	public ResponseEntity<List<UiMenuTreeInfo>> getMenus(HttpSession session) {

		List<UiMenuTreeInfo> out = menuService.getTreeList();

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<List<UiMenuTreeInfo>>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/menus", method = RequestMethod.POST)
	public ResponseEntity<Integer> addMenu(@RequestBody MenuDto menuDto) {

		logger.debug(" INPUT : menuDto [{}]", menuDto);

		int out = menuService.add(menuDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/menus", method = RequestMethod.PUT)
	public ResponseEntity<Integer> updateMenu(@RequestBody MenuDto menuDto, HttpSession session) {
		logger.debug(" INPUT : MenuDto [{}]", menuDto);

		int out = menuService.update(menuDto);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/menus/{menuId}", method = RequestMethod.DELETE)
	public ResponseEntity<Integer> deleteMenu(@PathVariable(value = "menuId", required = true) String menuId) {

		logger.debug(" INPUT : menuId : [{}]", menuId);

		int out = menuService.delete(menuId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<Integer>(out, HttpStatus.OK);
	}

	@RequestMapping(value = "/menus/{menuId}", method = RequestMethod.GET)
	public ResponseEntity<MenuDto> getMenu(@PathVariable(value = "menuId", required = true) String menuId,
			HttpSession session) {

		logger.debug(" INPUT : menuId : [{}]", menuId);

		MenuDto out = menuService.get(menuId);

		logger.debug(" OUTPUT : {}", out);

		return new ResponseEntity<MenuDto>(out, HttpStatus.OK);
	}
}
