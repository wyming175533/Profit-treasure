
//同意实名认证协议
$(function() {
	$("#phone").on("blur",function (){
		if($("#phone").val()!=$("#registerPhone")){
			showError("phone","注册手机号和登录手机号不一致")
		}
	})
	$("#btnRegist").on("click",function (){



			if(!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test($("#idCard").val())){
				showError("idCard","身份证信息格式不正确")
			}

			$.ajax({
				url:contextPath+"/user/checked",
				type:"post",
				data:{
					registerPhone:$.trim($("#registerPhone").val()),
					phone:$.trim($("#phone").val()),
					idCard:$.trim($("#idCard").val()),
					realName:$.trim($("#realName").val())
				},
				dataType:"json",
				success:function (resp){
					if(resp.result){
						//	window.location.href=contextPath+"/user/myCenter";
						alert(resp)
					}
					else {
						alert(resp.msg);
					}
				},
				error:function (resp){
					alert("请稍后重试");
				}



			})

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