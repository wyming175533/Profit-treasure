//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}


//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

//注册协议确认
$(function() {
	//手机号验证
	$("#phone").on("focus",function (){
		hideError("phone")
	})
	$("#phone").on("blur",function (){

	let phone=$.trim($("#phone").val());
	if(phone===undefined || phone===null ||phone===""){
		showError("phone","请输入手机号")
	}else if(phone.length!=11){
		showError("phone","请输入有效信息")
	}else if( !/^1[1-9]\d{9}$/.test(phone)){

		showError("phone","请输入有效信息")
	}else{
		showSuccess("phone")
		$.ajax({
			url:contextPath+"/user/phone",
			async:false,
			data:{
				phone:phone
			},
			dataType:"json",
			success:function(resp){
				//resp:        {"code":304,"msg":"用户已经存在","data":"","reslut":false}
				alert(resp.result)
				//if( !resp.result){
				if(resp.result==false){
					showError("phone",resp.msg);
				}
			},
			error:function (){
				showError("phone","出错啦，请稍后重试")
			}
			}

		)
	}


})

	//密码部分验证
	$("#loginPassword").on("focus",function (){
		hideError("loginPassword")
	})
	$("#loginPassword").on("blur",function (){

		let loginPassword=$.trim($("#loginPassword").val());
		if(loginPassword===undefined || loginPassword===null ||loginPassword===""){
			showError("loginPassword","请输入密码");
		}else if(loginPassword.length<6){
			showError("loginPassword","最小长度为6位");
		}else if(  ! /^[0-9a-zA-Z]+$/.test(loginPassword)){

			showError("loginPassword","密码只能由数字和英文字母组成");
		}else if( !/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)){
			showError("loginPassword","密码必须同时包含英文和数字");
		}else{
			showSuccess("loginPassword");

		}
	})

	//验证码部分

	//验证码文本框
	$("#messageCode").on("focus",function (){
		hideError("messageCode")
	})
	$("#messageCode").on("blur",function(){
		let code = $.trim( $("#messageCode").val());
		if( code === undefined || code === null || code === ""){
			showError("messageCode","必须输入验证码");
		} else if( code.length != 6){
			showError("messageCode","验证码是6位数字");
		} else {
			showSuccess("messageCode");

		}
	});

	//按钮
	let $codeBtn = $("#messageCodeBtn");
	$("#messageCodeBtn").on("click",function (){
		$("#phone").blur();
		$("#loginPassword").blur();
		$("#messageCode").blur();
		if($("#phoneErr")==""&&$("#loginPasswordErr")==""&&$("#messageCode")==""){


		}
		let second=-1;
		$.leftTime(10,function(d){
			//d.status,值true||false,倒计时是否结束;
			//d.s,倒计时秒;
			second=parseInt(d.s);
			if(second==0){
				$codeBtn.text("获取验证码");
			}else{
				$codeBtn.text(second+"后重新获取");
			}

		});

	})












	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
});
