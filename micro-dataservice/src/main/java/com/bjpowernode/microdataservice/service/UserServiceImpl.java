package com.bjpowernode.microdataservice.service;

import com.bjpowernode.Util.RedisOpreation;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.api.model.ServiceResult;
import com.bjpowernode.api.po.FinanceAccount;
import com.bjpowernode.api.po.User;
import com.bjpowernode.api.service.UserService;
import com.bjpowernode.microdataservice.mapper.FinanceAccountMapper;
import com.bjpowernode.microdataservice.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@DubboService(interfaceClass = UserService.class,version = "1.0")
public class UserServiceImpl implements UserService {
    @Value("${md5.salt}")
    private String salt;

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisOpreation redisOpreation;
    //NullPointException,发现一直有空指针，并且没有访问到数据库，后来发现没有加resource
    @Resource
    private FinanceAccountMapper financeMapper;
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

    /**
     * @param phone 注册手机号
     * @param password 注册密码
     * @param loginIp 登录ip
     * @param device 登录设备
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User insertUser(String phone, String password, String loginIp, String device) {
        User userInfo=null;
        User  user=userMapper.selectUserByPhone(phone);
        if(user==null){
            userInfo=new User();
            String passwordMd5=password+salt;
            passwordMd5=DigestUtils.md5Hex(passwordMd5);

            userInfo.setAddTime(new Date());
            userInfo.setLoginPassword(passwordMd5);
            userInfo.setPhone(phone);
            userInfo.setLogin_device(device);
            userInfo.setLogin_ip(loginIp);

           userMapper.insertUserSelectID(userInfo);
                FinanceAccount financeAccount =new FinanceAccount();
                financeAccount.setUid(userInfo.getId());
                 BigDecimal bigDecimal=new BigDecimal("888");
                financeAccount.setAvailableMoney(bigDecimal);
                financeMapper.insertSelective(financeAccount);
        }





        return userInfo;
    }

    @Override
    public User update(User user) {
      User user1=userMapper.selectUserByPhone(user.getPhone());
      if(user1.getIdCard()!=null&&user1.getName()!=null)
      {
          return user1;
      }
      user.setId(user1.getId());
     int rows=userMapper.updateByPrimaryKeySelective(user);
        if(rows<0){
          return null;
        }
        user.setLogin_device(user1.getLogin_device());
        user.setLogin_ip(user1.getLogin_ip());
        user.setLoginPassword(user1.getLoginPassword());
        user.setAddTime(user1.getAddTime());
        user1=null;
        return user;
    }

    @Override
    public ServiceResult loginCheck(String phone, String password, String login_ip, String login_device) {
        ServiceResult serviceResult=new ServiceResult(false,000,"未知错误",null);
        String passwordMd5=password+salt;
        passwordMd5=DigestUtils.md5Hex(passwordMd5);
        User user=userMapper.selectUserByPhone(phone);
        if(user==null){
            serviceResult.setMsg("查无此人");
            serviceResult.setCode(1002);
        }else{
            if(!passwordMd5.equals(user.getLoginPassword())){
                serviceResult.setMsg("密码不正确");
                serviceResult.setCode(1003);
            }
            else{
                serviceResult.setResult(true);
                serviceResult.setCode(111);
                serviceResult.setMsg("成功");
            }
            if(user.getLogin_ip()!=null||user.getLogin_device()!=null){
                //@todo 这里不仅要判断后更新，还要进行异地登录验证操作，这里省略没有做
                if(!login_ip.equals(user.getLogin_ip())){
                   // user.setLogin_ip(login_ip);
                    serviceResult.setMsg("异地登录，账号异常，请联系客服或稍后重试");
                    serviceResult.setCode(1009);
                    serviceResult.setResult(false);
                }
                if(!login_device.equals(user.getLogin_device())){
                    user.setLogin_device(login_device);
                }
            }
            user.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
            serviceResult.setData(user);
        }



        return serviceResult;
    }
}
