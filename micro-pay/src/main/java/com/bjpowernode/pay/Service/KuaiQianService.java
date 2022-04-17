package com.bjpowernode.pay.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.Consts.YLBKEY;
import com.bjpowernode.Util.RedisOpreation;
import com.bjpowernode.api.model.ServiceResult;
import com.bjpowernode.api.service.RechargeService;
import com.bjpowernode.pay.util.HttpUtil;
import com.bjpowernode.pay.util.Pkipair;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import com.bjpowernode.pay.config.KqConfig;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class KuaiQianService {
    @Resource
    private KqConfig kqConfig;

    @DubboReference(interfaceClass = RechargeService.class,version="1.0")
    private RechargeService rechargeService;
    @Resource
    private RedisOpreation redisOpreation;
public String generateRechargeNo(String channel){
    String rechargeNo="";
    Date currDate=new Date();
    if("kq".equals(channel)){
    rechargeNo= "KQ" + DateFormatUtils.format(currDate,"yyyyMMddHHmmssSS")+redisOpreation.incre(YLBKEY.RECHARGE_NO);
    redisOpreation.zset(YLBKEY.RECHARGE_NO_LIST,rechargeNo,currDate.getTime());
    }

    return rechargeNo;
}


    //生成快钱form的数据 Map所有的参数名和值
    public Map<String,String> generateKqFormData(           String userId,
                                                            String rechargeNo,
                                                            BigDecimal money,
                                                            String phone){
        Map<String,String> param = new HashMap<>();

        //人民币网关账号，该账号为11位人民币网关商户编号+01,该参数必填。
        String merchantAcctId = kqConfig.getMerchant();//
        //编码方式，1代表 UTF-8; 2 代表 GBK; 3代表 GB2312 默认为1,该参数必填。
        String inputCharset = "1";
        //接收支付结果的页面地址，该参数一般置为空即可。
        String pageUrl = "";
        //服务器接收支付结果的后台地址，该参数务必填写，不能为空。
        String bgUrl = kqConfig.getBgUrl();
        //网关版本，固定值：v2.0,该参数必填。
        String version =  "v2.0";
        //语言种类，1代表中文显示，2代表英文显示。默认为1,该参数必填。
        String language =  "1";
        //签名类型,该值为4，代表PKI加密方式,该参数必填。
        String signType =  "4";
        //支付人姓名,可以为空。
        String payerName= "";
        //支付人联系类型，1 代表电子邮件方式；2 代表手机联系方式。可以为空。
        String payerContactType =  "2";
        //支付人联系方式，与payerContactType设置对应，payerContactType为1，则填写邮箱地址；payerContactType为2，则填写手机号码。可以为空。
        String payerContact =  phone;
        //指定付款人，可以为空
        String payerIdType =  "3";
        //付款人标识，可以为空
        String payerId =  String.valueOf(userId);
        //付款人IP，可以为空
        String payerIP =  "192.168.1.1";
        //商户订单号，以下采用时间来定义订单号，商户可以根据自己订单号的定义规则来定义该值，不能为空。
        String orderId = rechargeNo;
        //订单金额，金额以“分”为单位，商户测试以1分测试即可，切勿以大金额测试。该参数必填。
        String fen  = money.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString();
        String orderAmount = fen;
        //订单提交时间，格式：yyyyMMddHHmmss，如：20071117020101，不能为空。
        String orderTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        //快钱时间戳，格式：yyyyMMddHHmmss，如：20071117020101， 可以为空
        String orderTimestamp= new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());;
        //商品名称，可以为空。
        String productName= "账户充值";
        //商品数量，可以为空。
        String productNum = "1";
        //商品代码，可以为空。
        String productId = "10000";
        //商品描述，可以为空。
        String productDesc = "支付理财";
        //扩展字段1，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
        String ext1 = "";
        //扩展自段2，商户可以传递自己需要的参数，支付完快钱会原值返回，可以为空。
        String ext2 = "";
        //支付方式，一般为00，代表所有的支付方式。如果是银行直连商户，该值为10-1或10-2，必填。
        String payType = "00";
        //银行代码，如果payType为00，该值可以为空；如果payType为10-1或10-2，该值必须填写，具体请参考银行列表。
        String bankId = "";
        //同一订单禁止重复提交标志，实物购物车填1，虚拟产品用0。1代表只能提交一次，0代表在支付不成功情况下可以再提交。可为空。
        String redoFlag = "0";
        //快钱合作伙伴的帐户号，即商户编号，可为空。
        String pid = "";

        // signMsg 签名字符串 不可空，生成加密签名串
        String signMsgVal = "";
        signMsgVal = appendParam(signMsgVal, "inputCharset", inputCharset,param);
        signMsgVal = appendParam(signMsgVal, "pageUrl", pageUrl,param);
        signMsgVal = appendParam(signMsgVal, "bgUrl", bgUrl,param);
        signMsgVal = appendParam(signMsgVal, "version", version,param);
        signMsgVal = appendParam(signMsgVal, "language", language,param);
        signMsgVal = appendParam(signMsgVal, "signType", signType,param);
        signMsgVal = appendParam(signMsgVal, "merchantAcctId",merchantAcctId,param);
        signMsgVal = appendParam(signMsgVal, "payerName", payerName,param);
        signMsgVal = appendParam(signMsgVal, "payerContactType",payerContactType,param);
        signMsgVal = appendParam(signMsgVal, "payerContact", payerContact,param);
        signMsgVal = appendParam(signMsgVal, "payerIdType", payerIdType,param);
        signMsgVal = appendParam(signMsgVal, "payerId", payerId,param);
        signMsgVal = appendParam(signMsgVal, "payerIP", payerIP,param);
        signMsgVal = appendParam(signMsgVal, "orderId", orderId,param);
        signMsgVal = appendParam(signMsgVal, "orderAmount", orderAmount,param);
        signMsgVal = appendParam(signMsgVal, "orderTime", orderTime,param);
        signMsgVal = appendParam(signMsgVal, "orderTimestamp", orderTimestamp,param);
        signMsgVal = appendParam(signMsgVal, "productName", productName,param);
        signMsgVal = appendParam(signMsgVal, "productNum", productNum,param);
        signMsgVal = appendParam(signMsgVal, "productId", productId,param);
        signMsgVal = appendParam(signMsgVal, "productDesc", productDesc,param);
        signMsgVal = appendParam(signMsgVal, "ext1", ext1,param);
        signMsgVal = appendParam(signMsgVal, "ext2", ext2,param);
        signMsgVal = appendParam(signMsgVal, "payType", payType,param);
        signMsgVal = appendParam(signMsgVal, "bankId", bankId,param);
        signMsgVal = appendParam(signMsgVal, "redoFlag", redoFlag,param);
        signMsgVal = appendParam(signMsgVal, "pid", pid,param);
        Pkipair pki = new Pkipair();
        String signMsg = pki.signMsg(signMsgVal);
        //加入signMsg参数
        param.put("signMsg",signMsg);

        return param;
    }

    private String appendParam(String returns, String paramId, String paramValue,Map<String,String> map) {
        if (returns != "") {
            if (paramValue != "" && paramValue != null) {
                returns += "&" + paramId + "=" + paramValue;
                if( map != null){
                    map.put(paramId,paramValue);
                }
            }
        } else {
            if (paramValue != "" && paramValue != null ) {
                returns = paramId + "=" + paramValue;
                if( map != null){
                    map.put(paramId,paramValue);
                }
            }
        }
        return returns;
    }

    /**用于接收快钱返回的支付信息，并处理商家的业务逻辑
     * @param request 获取块钱返回的信息
     * @return
     */
    public ServiceResult handlerNotif(HttpServletRequest request) {
    ServiceResult serviceResult =new ServiceResult(false);
        String merchantAcctId = request.getParameter("merchantAcctId");
        String version = request.getParameter("version");
        String language = request.getParameter("language");
        String signType = request.getParameter("signType");
        String payType = request.getParameter("payType");
        String bankId = request.getParameter("bankId");
        String orderId = request.getParameter("orderId");//订单号
        String orderTime = request.getParameter("orderTime");
        String orderAmount = request.getParameter("orderAmount");
        String bindCard = request.getParameter("bindCard");
        if(request.getParameter("bindCard")!=null){
            bindCard = request.getParameter("bindCard");}
        String bindMobile="";
        if(request.getParameter("bindMobile")!=null){
            bindMobile = request.getParameter("bindMobile");}

        String dealId =request.getParameter("dealId");//块钱对应的账单号
        String bankDealId = request.getParameter("bankDealId");
        String dealTime = request.getParameter("dealTime");
        String payAmount = request.getParameter("payAmount");//订单金额
        String fee = request.getParameter("fee");
        String ext1 = request.getParameter("ext1");
        String ext2 = request.getParameter("ext2");
        String payResult = request.getParameter("payResult");//支付结果
        String aggregatePay = request.getParameter("aggregatePay");
        String errCode = request.getParameter("errCode");//错误码
        String signMsg = request.getParameter("signMsg");//错误信息
        String merchantSignMsgVal = "";
        merchantSignMsgVal = appendParam(merchantSignMsgVal,"merchantAcctId", merchantAcctId,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "version",version,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "language",language,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "signType",signType,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payType",payType,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankId",bankId,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderId",orderId,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderTime",orderTime,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "orderAmount",orderAmount,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bindCard",bindCard,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bindMobile",bindMobile,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealId",dealId,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "bankDealId",bankDealId,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "dealTime",dealTime,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payAmount",payAmount,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "fee", fee,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext1", ext1,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "ext2", ext2,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "payResult",payResult,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "aggregatePay",aggregatePay,null);
        merchantSignMsgVal = appendParam(merchantSignMsgVal, "errCode",errCode,null);
        System.out.println("merchantSignMsgVal="+merchantSignMsgVal);
        Pkipair pki = new Pkipair();
        boolean flag=pki.enCodeByCer(merchantSignMsgVal,signMsg);
        //flag是验签结果，true是通过
        System.out.println(flag+"--------------");
        if(flag){
            serviceResult=rechargeService.handlerKqNotify(orderId,payResult,payAmount);
        }else{
            //验签失败，记录日志
            serviceResult.setCode(30027);
            serviceResult.setMsg("验签失败");
            serviceResult.setResult(false);
        }

    return serviceResult;
    }

    public void removeOrderFromRedis(String orderId){
    redisOpreation.removeValueforZset(YLBKEY.RECHARGE_NO_LIST,orderId);
    }

    public ServiceResult handlerQuery(String rechargeNo) {
        Map<String, Object> request = new HashMap<String, Object>();
        //固定值：1代表UTF-8;
        String inputCharset = "1";
        //固定值：v2.0 必填
        String version = "v2.0";
        //1代表Md5，2 代表PKI加密方式  必填
        String signType = "2";
        //人民币账号 membcode+01  必填
        String merchantAcctId = "1001214035601";//
        //固定值：0 按商户订单号单笔查询，1 按交易结束时间批量查询必填
        String queryType = "0";
        //固定值：1	代表简单查询 必填
        String queryMode = "1";
        String startTime = "";//20200525000000
        String endTime = "";
        String requestPage = "";
        String orderId = rechargeNo;
        String key = "27YKWKBKHT2IZSQ4";

        request.put("inputCharset", inputCharset);
        request.put("version", version);
        request.put("signType", signType);
        request.put("merchantAcctId", merchantAcctId);
        request.put("queryType", queryType);
        request.put("queryMode", queryMode);
        request.put("startTime", startTime);
        request.put("endTime", endTime);
        request.put("requestPage", requestPage);
        request.put("orderId", orderId);

        String message="";
        message = appendParam(message,"inputCharset",inputCharset,null);
        message = appendParam(message,"version",version,null);
        message = appendParam(message,"signType",signType,null);
        message = appendParam(message,"merchantAcctId",merchantAcctId,null);
        message = appendParam(message,"queryType",queryType,null);
        message = appendParam(message,"queryMode",queryMode,null);
        message = appendParam(message,"startTime",startTime,null);
        message = appendParam(message,"endTime",endTime,null);
        message = appendParam(message,"requestPage",requestPage,null);
        message = appendParam(message,"orderId",orderId,null);
        message = appendParam(message,"key",key,null);

        Pkipair pki = new Pkipair();
        String sign = pki.signMsg(message);
        request.put("signMsg", sign);
        //sandbox提交地址
        String reqUrl="https://sandbox.99bill.com/gatewayapi/gatewayOrderQuery.do";
        String response="";
        ServiceResult serviceResult =new ServiceResult(false);
        try {
            response= HttpUtil.doPostJsonRequestByHttps(JSON.toJSONString(request),reqUrl,3000,8000);
            JSONObject jsonObject=JSONObject.parseObject(response);
            if(jsonObject!=null){
                Object orderDetail=jsonObject.get("orderDetail");
                System.out.println(orderDetail);
                if(orderDetail!=null){
                    JSONArray array=jsonObject.getJSONArray("orderDetail");//获取接送数组
                    JSONObject order = (JSONObject) array.get(0);//第一个json对象
                    String payAmount=order.getString("payAmount");
                    String payResult=order.getString("payResult");
                    String queryOrderID=order.getString("orderId");

                    serviceResult=rechargeService.handlerKqNotify(queryOrderID,payResult,payAmount);
                }else{
                    //订单失败，或者订单不存在
                    serviceResult.setResult(false);
                    serviceResult.setCode(30028);
                    serviceResult.setMsg("订单失败，或者订单不存在");
                    //更新充值记录是失败
                    rechargeService.modifyRechargeStatus(orderId,YLBKEY.RECHARGE_STATUS_RECHARGEERR);
                }
            }
        }catch (Exception e){

        }

return  serviceResult;
    }
}
