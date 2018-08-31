package cn.com.zach.demo.glasses.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.zach.demo.glasses.dto.AdminMenu;
import cn.com.zach.demo.glasses.dto.AdminMenuExample;
import cn.com.zach.demo.glasses.mapper.AdminMenuMapper;

@Service
public class AdminMenuService {

	@Autowired
	private AdminMenuMapper adminMenuMapper;
	
	public AdminMenu insert(AdminMenu record) throws Exception{
		record.setSorter(0);
		record.setCtime(new Date());
		adminMenuMapper.insertSelective(record);
		return record;
	}
	
	public List<AdminMenu> list() throws Exception{
		AdminMenuExample example = new AdminMenuExample();
		example.or().andIdIsNotNull();
		List<AdminMenu> list = adminMenuMapper.selectByExample(example);
		return list;
	}
	
	public AdminMenu detail(Long id) throws Exception{
		return adminMenuMapper.selectByPrimaryKey(id);
	}
}
