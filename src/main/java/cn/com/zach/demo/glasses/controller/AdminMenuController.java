package cn.com.zach.demo.glasses.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.com.zach.demo.glasses.common.utils.StringUtil;
import cn.com.zach.demo.glasses.dto.AdminMenu;
import cn.com.zach.demo.glasses.mode.Result;
import cn.com.zach.demo.glasses.mode.ReturnCode;
import cn.com.zach.demo.glasses.service.AdminMenuService;

@RestController
@RequestMapping("/admin/menu")
public class AdminMenuController {

	@Autowired
	private AdminMenuService adminMenuService;
	
	@RequestMapping(value="/insert", method = RequestMethod.POST)
	public Result insert(@RequestBody AdminMenu record) {
		try {
			return Result.success(adminMenuService.insert(record));
		}catch(Exception e) {
			e.printStackTrace();
			return Result.fail(ReturnCode.SYSTEM_ERROR, e);
		}
	}
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public Result list(AdminMenu record) {
		try {
			List<AdminMenu> list = adminMenuService.list();
			return Result.out(list, null);
		}catch(Exception e) {
			e.printStackTrace();
			return Result.fail(ReturnCode.SYSTEM_ERROR, e);
		}
	}
	
	@RequestMapping(value="/detail", method = RequestMethod.GET)
	public Result detail(AdminMenu menu) {
		try {
			Long id = menu.getId();
			if(StringUtil.isEmpty(id)) {
				return Result.fail(ReturnCode.PARAMS_ERROR, "参数错误");
			}
			AdminMenu record = adminMenuService.detail(id);
			return Result.out(record, null);
		}catch(Exception e) {
			e.printStackTrace();
			return Result.fail(ReturnCode.SYSTEM_ERROR, e);
		}
	}
	
}
