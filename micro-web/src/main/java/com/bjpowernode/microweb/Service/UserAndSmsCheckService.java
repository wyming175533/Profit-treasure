package com.bjpowernode.microweb.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.Util.RedisOpreation;
import com.bjpowernode.microweb.settings.JdwxCheckConfig;
import com.bjpowernode.microweb.settings.JdwxSmsConfig;
import com.bjpowernode.vo.BusinessResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.html.parser.Entity;
import java.io.IOException;

@Service
public class UserAndSmsCheckService {
    @Resource
    private JdwxSmsConfig smsConfig;
    @Resource
    private RedisOpreation opreation;
    @Resource
    private  JdwxCheckConfig checkConfig;

    /**
     * @param phone
     * @return true，短信发送成功
     */
    public boolean userRegisterSmsSend(String phone){
        //随机验证码
        String authCode = RandomStringUtils.randomNumeric(6);
        //短信内容
        String content=smsConfig.getContent();
        content=String.format(content,authCode);
        //调用工具方法发送短信
        BusinessResult businessResult= incokeSendsms(phone,content);
       //验证码存放到redis中
        if(businessResult.isResult()){
            String key= YLBKEY.SMS_REGISTER_KEY+phone;
            opreation.setStringKey(key,authCode,3);
        }
          //返回结果
        return businessResult.isResult();
    }

    /**
     * @param phone 手机号
     * @param content 短信内容
     * @return BusinessResult对象，result为真则返回成功
     */
    public BusinessResult incokeSendsms(String phone,String content){
        BusinessResult result=new BusinessResult(false,0,"requesting...");
        //创建httpclient
        CloseableHttpClient client = HttpClients.createDefault();
        //地址样例  //https://way.jd.com/chuangxin/dxjk?mobile=13568813957&content=【创信】你的验证码是：5873，3分钟内有效！&appkey=您申请的APPKEY
        String url=smsConfig.getUrl()+"?mobile="+phone+"&content="+content+"&appkey="+smsConfig.getAppkey();

        //创建连接
        HttpGet get=new HttpGet(url);
        boolean isOk=false;
        try{
            //发起请求
            CloseableHttpResponse  resp=client.execute(get);
            //获取状态行的状态码
            if(resp.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                //获取相应结果

                String res= EntityUtils.toString(resp.getEntity());
                System.out.println(res);
                 res = "{\n" +
                        "    \"code\": \"10000\",\n" +
                        "    \"charge\": false,\n" +
                        "    \"remain\": 1305,\n" +
                        "    \"msg\": \"查询成功\",\n" +
                        "    \"result\": {\n" +
                        "        \"ReturnStatus\": \"Success\",\n" +
                        "        \"Message\": \"ok\",\n" +
                        "        \"RemainPoint\": 420842,\n" +
                        "        \"TaskID\": 18424321,\n" +
                        "        \"SuccessCounts\": 1\n" +
                        "    }\n" +
                        "}";
                if(StringUtils.isNoneBlank(res)){
                    JSONObject jsonObject= JSONObject.parseObject(res);
                    /**
                     *{"code":"10010","charge":false,"remain":0,
                     * "msg":"接口需要付费，请充值",
                     * "requestId":"2e86e3bbf45440dc8a82a084b33b1a3a"}
                     */
                    //处理查询结果
                    result.setCode(jsonObject.getInteger("code"));
                    result.setMsg(jsonObject.getString("msg"));
                    if("10000".equals(jsonObject.getString("code"))){
                        JSONObject objectResult=jsonObject.getJSONObject("result");
                        System.out.println(result);
                        if(objectResult!=null){
                            isOk="Success".equals(objectResult.getString("ReturnStatus"));

                        }
                    }
                }
                else{
                    result.setMsg("访问短信接口失败");
                }
                result.setResult(isOk);

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**检查验证马
     * @param phone 手机号
     * @param code 验证码
     * @return true， 验证码输入正确
     */
    public boolean checkAuthCode(String phone, String code) {
        if(code==null)
            return false;
        String key=YLBKEY.SMS_REGISTER_KEY+phone;
        String AuthCode=  opreation.getStringKey(key);

        return  code.equals(AuthCode);
    }
    public void removeAuthCode(String code,String phone) {
        String key=YLBKEY.SMS_REGISTER_KEY+phone;
        opreation.deleteRedisKey(key);
    }

    /**
     * @param idCard 身份证
     * @param realName 姓名
     * @return boolean true 查询结果，用户和姓名匹配
     */
    public boolean checkIdcard(String idCard, String realName) {
            BusinessResult result=invokeCheckID(realName,idCard);
            return result.isResult();

    }


    /**检查用户认证信息是否正确
     * @param realName 认证姓名
     * @param idCard 身份证号
     * @return 匹配结果，属性result为true则身份证和姓名匹配
     */
    public BusinessResult invokeCheckID(String realName,String idCard){
        BusinessResult businessResult=new BusinessResult(false,0,"身份信息不匹配");
        checkConfig.setCardNo(idCard);
        checkConfig.setRealName(realName);
        //https://way.jd.com/youhuoBeijing/test?cardNo=150429198407091210&realName=乐天磊&appkey=您申请的APPKEY
        String url=checkConfig.getUrl()+"?cardNo="+checkConfig.getCardNo()+"&realName="+checkConfig.getRealName()+
                "&appkey="+checkConfig.getAppkey();
        System.out.println(url);
        CloseableHttpClient client=HttpClients.createDefault();

        HttpGet get=new HttpGet(url);
        boolean isok=false;
        try{//发起请求
            CloseableHttpResponse  resp=client.execute(get);
            if(resp.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
                //String res=EntityUtils.toString(resp.getEntity());
               // System.out.println(res);
                  String  res="{\n" +
                            "    \"code\": \"10000\",\n" +
                            "    \"charge\": false,\n" +
                            "    \"remain\": 1305,\n" +
                            "    \"msg\": \"查询成功\",\n" +
                            "    \"result\": {\n" +
                            "        \"error_code\": 0,\n" +
                            "        \"reason\": \"成功\",\n" +
                            "        \"result\": {\n" +
                            "            \"realname\": \"乐天磊\",\n" +
                            "            \"idcard\": \"350721197702134399\",\n" +
                            "            \"isok\": true\n" +
                            "        }\n" +
                            "    }\n" +
                            "}";
                JSONObject jsonObject=JSONObject.parseObject(res);
                if("10000".equals(jsonObject.getString("code"))){//状态码10000，请求结果成功
                    jsonObject=jsonObject.getJSONObject("result");
                    if("成功".equals(jsonObject.getString("reason"))){//查询结果
                        businessResult.setMsg(jsonObject.getString("reason"));
                        businessResult.setCode(jsonObject.getInteger("error_code"));
                        jsonObject=jsonObject.getJSONObject("result");
                        if (jsonObject.getBoolean("isok")){//身份证和用户名是否匹配，true，匹配
                            isok=true;
                        }
                    }
                }

            }else{
                businessResult.setMsg("访问身份验证接口失败");
            }
            businessResult.setResult(isok);
        }catch (Exception e){
            e.printStackTrace();
        }

        return businessResult;
    }


    /**
     * 实现redis中错误次数的累加
     * @param phone 手机号
     * @param date 日期
     */
    public void addErrTimes(String phone, String date) {
        String key=YLBKEY.REAL_NAME_TIMES+phone+":"+date;
       String times= opreation.getStringKey(key);
        if(times==null||times=="")
            times="0";
       Integer time=Integer.parseInt(times);

       try{
           opreation.setStringKey(key,String.valueOf(time+1));
       }
       catch (Exception e){
           opreation.setKey(key,time);
       }
    }

    /**
     * 判断当日验证次数
     * @param phone
     * @param date
     * @return true，大于三次，不能继续认证
     */
    public boolean checkErrTimes(String phone, String date) {
        String key=YLBKEY.REAL_NAME_TIMES+phone+":"+date;
        String times= opreation.getStringKey(key);
        if(times==null||times==""){
            times="0";
        }
        Integer time=Integer.parseInt(times);
        if(time<3){
            return  false;
        }
        return true;
    }
}
