package com.bjpowernode.microweb.Controller;

import com.bjpowernode.Consts.YLBConsts;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.api.model.MyInvestVo;
import com.bjpowernode.api.model.ServiceResult;
import com.bjpowernode.api.po.FinanceAccount;
import com.bjpowernode.api.po.RechargeRecord;
import com.bjpowernode.api.po.User;
import com.bjpowernode.api.service.UserService;
import com.bjpowernode.code.ResponseCode;
import com.bjpowernode.microweb.Service.UserAndSmsCheckService;
import com.bjpowernode.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
public class UserController extends BaseController {
    @Resource
    private UserAndSmsCheckService USservice;
    @Resource
    private UserService userService;
    @GetMapping("/user/page/register")
    public String Register(Model model){

        return "register";
    }

    /**
     * @param phone 注册用户的手机号
     * @return 结果信息，包括状态码，提示信息，true/false，data
     */
    @ResponseBody
    @GetMapping("/user/phone")
    public Result Phone(@RequestParam("phone")  String phone){
    Result result=Result.fail();
        if(StringUtils.isBlank(phone)){
            result=Result.erro(ResponseCode.PARAM_EMPTY);
        }else if(!YLBUtil.regexphone(phone)){
            result=Result.erro(ResponseCode.PHONE_ERR);
        }else{
            User user=userService.selectUserByPhone(phone);
            if(user!=null){
                result=Result.erro(ResponseCode.User_EXISTS);
            }else{
                result=Result.ok();
            }
        }
        System.out.println(result.toString()+"-------------------------------------");
        return result;
    }

    @GetMapping("/user/realName")
    public String RealName(String phone,Model model,HttpSession session){
        User user=null;
        if(phone==null){
            user=(User)session.getAttribute(YLBKEY.USER_SESSION);//当前登录用户
            if(user==null){
                 user= (User) session.getAttribute("user");//注册时记录的用户
            }
            phone=user.getPhone();
        }
        model.addAttribute("phone",phone);
        return "realName";
    }

    /**跳转到个人中心/小金库
     * @param session session域
     * @param model
     * @return
     */
    @GetMapping("/user/myCenter")
    public String myCenter(HttpSession session,Model model){

        User user= (User) session.getAttribute(YLBKEY.USER_SESSION);
        if(user!=null){
            FinanceAccount account=accountService.queryAccountMoney(user.getId());
            model.addAttribute("availableMoney",account.getAvailableMoney());//获取用户金额

            List<MyInvestVo> listVo=investService.selectMyInvestByUid(user.getId(),1, YLBConsts.YLB_PRODUCT_INVESTPAGESIZE);
            model.addAttribute("myInvest",listVo);//获取用户投资列表

            //最近的5条充值记录
            List<RechargeRecord> rechargeList = rechargeService.queryByUserId(user.getId(),1,5);
            model.addAttribute("rechargeList",rechargeList);




        }
        return "myCenter";
    }

    /**
     * 用户认证，没天认证次数不能超过三次
     * @todo 后期会做定时任务，在那时对无效的key进行删除
     * @param phone 认证手机号
     * @param idCard 身份证号
     * @param realName 真实姓名
     * @param registerPhone 注册时手机号
     * @return 认证结果
     */
    @ResponseBody
    @PostMapping("/user/checked")
    public Result<String> checked(String phone, String idCard, String realName, String registerPhone, HttpSession session){
        Result<String> result=Result.fail();
        String date= DateFormatUtils.format(new Date(),"yyyyMMdd");
        if(StringUtils.isAllBlank(phone,idCard,realName,registerPhone)){
            result=Result.erro(ResponseCode.PARAM_EMPTY);//参数空
        }else if(!YLBUtil.regexphone(phone)){
            result=Result.erro(ResponseCode.PHONE_ERR);//手机号格式错误
        }else if(!registerPhone.equals(phone)){
            result=Result.erro(ResponseCode.PHONE_DIFF_ERR);//手机号不同
        } else if(!YLBUtil.regexIdCard(idCard)) {
            result=Result.erro(ResponseCode.IDCARD_REALNAME_ERR);
        } else if(realName.length()<2){//姓名格式错误
            result=Result.erro(ResponseCode.REAL_NAME_ERR);
        }else if(USservice.checkErrTimes(phone,date)){
            result=Result.erro(ResponseCode.RNE_TIMES_ERR);
        }else if(!USservice.checkIdcard(idCard,realName)){//验证姓名和身份证号是否匹配
            //失败时做记录，超过三当日停止继续验证
            USservice.addErrTimes(phone,date);

            result=Result.erro(ResponseCode.IDCARD_REALNAME_DIFF);
        }else{
            result=Result.ok();
            User user=new User();
            user.setPhone(phone);
            user.setIdCard(idCard);
            user.setName(realName);
            user=userService.update(user);
            if(user!=null){
                session.setAttribute("user",user);
            }
        }
        //根据结果完善数据库用户信息
        return result;

    }



    @ResponseBody
    @PostMapping("/user/register")
    public Result<String> Register(String phone, String code, String password, HttpServletRequest request){
        Result<String> result=Result.fail();
        //参数判断
        if(StringUtils.isAllBlank(phone,code,password)){
            result.setEnum(ResponseCode.PARAM_EMPTY);
        }else if(!YLBUtil.regexphone(phone)){
            result.setEnum(ResponseCode.PHONE_ERR);
        }else if(password.length()<32){
            result.setEnum(ResponseCode.PASSWORD_LENGTH_ERR);
        }else if(!USservice.checkAuthCode(phone,code)){
            result.setEnum(ResponseCode.AuthCode_ERR);
        }else{
        //注册用户
            String login_ip=request.getRemoteAddr();
            String login_device=request.getHeader("User-Agent");
            User user =userService.insertUser(phone,password,login_ip,login_device);
            if(user!=null){
                HttpSession session=request.getSession();
                session.setAttribute("user",user);
                result=Result.ok();
                USservice.removeAuthCode(code,phone);
            }
         //跳转到实名认证界面
        }

        return  result;
    }

    /**跳转到用户登录页面
     * @param returnUrl  返回地址
     * @param request 用于获取无参时候返回地址
     * @param model 存储
     * @return login.html
     */
    @GetMapping("/user/login")
    public String login(String returnUrl,HttpServletRequest request,Model model){
        if(StringUtils.isBlank(returnUrl)){
            returnUrl=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+
                    request.getContextPath()+"/index.html";
            System.out.println(returnUrl);
        }
       Integer userCount= userService.registerAllUserCount();
        BigDecimal allMoney=investService.statisticsInvestSumAllMoney();
        BigDecimal rate=productService.computeAvgRate();
        model.addAttribute("returnUrl",returnUrl);
        model.addAttribute("userCount",userCount);
        model.addAttribute("allMoney",allMoney);
        model.addAttribute("rate",rate);


        return "login";
    }

    @PostMapping("/user/loginCheck")
    @ResponseBody
    public Result<User> loginCheck(String phone,String password,HttpServletRequest request){
        Result<User> result=Result.fail();
        if(StringUtils.isAllBlank(phone,password)){
            result=Result.erro(ResponseCode.PARAM_EMPTY);
        }else if(!YLBUtil.regexphone(phone)){
            result=Result.erro(ResponseCode.PHONE_ERR);
        }else if(password.length()<32){
            result.setEnum(ResponseCode.PASSWORD_LENGTH_ERR);
        }else {
            String login_ip=request.getRemoteAddr();
            String login_device=request.getHeader("User-Agent");
            ServiceResult serviceResult= userService.loginCheck(phone,password,login_ip,login_device);
            result.setMsg(serviceResult.getMsg());
            if(serviceResult.getData()!=null){
                result.setData((User) serviceResult.getData());
                request.getSession().setAttribute(YLBKEY.USER_SESSION,(User)serviceResult.getData());
            }

            result.setCode(serviceResult.getCode());
            result.setResult(serviceResult.isResult());

        }



        return result;
    }

    @GetMapping("/user/logout")
    public String logout (HttpServletRequest request){
        HttpSession session=request.getSession();
        session.removeAttribute(YLBKEY.USER_SESSION);
        session.invalidate();

        return "redirect:/index";
    }

    @ResponseBody
    @GetMapping("/user/account")
    public Result<BigDecimal> account(HttpSession session){
        Result<BigDecimal> result=Result.fail();
        User user= (User) session.getAttribute(YLBKEY.USER_SESSION);
        if(user!=null){
            FinanceAccount account=accountService.queryAccountMoney(user.getId());
            if(account!=null){
                result=Result.ok();
                result.setData(account.getAvailableMoney());
            }
        }else{
            result=Result.erro(ResponseCode.USER_LOGIN_ERR);
        }


        return result;
    }

}
