package cn.com.zach.demo.glasses.mapper;

import cn.com.zach.demo.glasses.dto.AdminRoleMenu;
import cn.com.zach.demo.glasses.dto.AdminRoleMenuExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminRoleMenuMapper {
    long countByExample(AdminRoleMenuExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AdminRoleMenu record);

    int insertSelective(AdminRoleMenu record);

    List<AdminRoleMenu> selectByExample(AdminRoleMenuExample example);

    AdminRoleMenu selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AdminRoleMenu record, @Param("example") AdminRoleMenuExample example);

    int updateByExample(@Param("record") AdminRoleMenu record, @Param("example") AdminRoleMenuExample example);

    int updateByPrimaryKeySelective(AdminRoleMenu record);

    int updateByPrimaryKey(AdminRoleMenu record);
}