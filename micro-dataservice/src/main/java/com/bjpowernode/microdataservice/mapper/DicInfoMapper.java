package com.bjpowernode.microdataservice.mapper;

import com.bjpowernode.api.po.DicInfo;


import java.util.List;

public interface DicInfoMapper {
    /**
     * @param category  字典中的产品
     * @return 返回字典集合
     */
    List<DicInfo> selectListByCategory(String category);

}
