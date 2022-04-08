package com.bjpowernode.microdataservice.service;

import com.bjpowernode.Util.RedisOpreation;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.api.po.User;
import com.bjpowernode.api.service.UserService;
import com.bjpowernode.microdataservice.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService(interfaceClass = UserService.class,version = "1.0")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisOpreation redisOpreation;
    /**
     * @return 返回注册总人数
     */
    @Override
    public Integer registerAllUserCount() {
        //首先从redis中查数据
        Integer register= (Integer) redisOpreation.getKey(YLBKEY.USR_REGISTER_COUNT);
        System.out.println("1从redis中查"+register);

        if(register==null) {
            //如果redis总没有数据，从mysql数据库中读取
            synchronized (this){//加线程锁，防止多线程情况下对数据库的无效访问
                if((register=(Integer) redisOpreation.getKey(YLBKEY.USR_REGISTER_COUNT))==null){//同时进入if语句下可能有多个线程，再加判断
                    System.out.println("2从redis中查"+register);
                register = userMapper.selectRegisterCount();
                redisOpreation.setKey(YLBKEY.USR_REGISTER_COUNT,register,30);
                    System.out.println("3从mysql中查"+register);
                }
            }

        }
        return register;
    }

    @Override
    public User selectUserByPhone(String phone) {
        User user=userMapper.selectUserByPhone(phone);
        return user;
    }
}
