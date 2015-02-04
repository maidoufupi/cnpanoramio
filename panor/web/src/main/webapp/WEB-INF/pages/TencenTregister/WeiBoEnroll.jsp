<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content>
<meta name="author" content>
<script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
<script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
<%-- <link rel="stylesheet" type="text/css" href="<c:url value="/bower_components/fileupload/css/bootstrap.min.css"/>"> --%>
<link rel="stylesheet" href="<c:url value="/bower_components/bootstrap/dist/css/bootstrap.css"/>">
     <title>来一场说走就走的旅行</title>

    <style type="text/css">
        body{
            text-align: center;
            background-color: #ededef;
        }
        .main-or{
            text-align: left;
            margin: auto;
        }
        .photo-in{
            /*margin-top: 100px;*/
            background-color: #ffffff ;
            padding: 40px 187px 0;
            min-height: 300px;
            margin:32px auto;
            /*border-style:ridge;*/
            border-left: 1px solid #000;
            border-right: 1px solid #000;
            width:  724px;
            height: 590px;
            box-shadow: none;
        }
        .photo-uhead{
            margin-bottom: 58px;
        }
        .photo-uhead img {
            float: left;
            width: 100px;
        }
        .photo-uhead .txt{
            margin-left: 80px;
            line-height: 26px;
            font-size: 16px;
            color: #868686;
        }
        .txt-nickname{
            width: 260px;
            line-height: 38px;
            font-size: 26px;
            color: #3791ff;
            font-weight:bold;
            font-style:italic;
        }
        .qq{
            font-size: 14px;
            color: #868686;
            /*margin: 30px auto;*/
        }
        .photo-in .info {
            margin-bottom: 15px;
            color: #868686;
            font-size: 12px;
            margin-top: 58px;
        }
        .main-or .photo-nlogin {
            padding-bottom: 0;
        }
        .photo-nlogin {
            position: relative;
            zoom: 1;
            padding: 0 0 70px;
        }
        .photo-detect {
            background-position: -79px -1809px;
            color: #e3696d;
            position: absolute;
            left: 95%;
            top: 5%;
            height: 22px;
            margin: -11px 0 0 10px;
            padding-left: 25px;
            white-space: nowrap;
            font-size: 12px;
            line-height: 22px;
            /*top: 20px;*/
        }
        .photo-ok{
            background-position: -79px -1783px;
            color: #75a500;
            position: absolute;
            left: 100%;
            top: 6%;
            height: 22px;
            margin: -11px 0 0 10px;
            padding-left: 25px;
            white-space: nowrap;
            font-size: 15px;
            line-height: 22px;
        }
        .photo-email {
        background-position:-79px -1809px;
             color: #e3696d;
             position: absolute;
             left: 95%;
             top: 25%;
             height: 22px;
             margin: -11px 0 0 10px;
             padding-left: 25px;
             white-space: nowrap;
             font-size: 12px;
             line-height: 22px;
             /*top: 20px;*/
         }
        .photo-pass {
        background-position: -79px -1809px;
            color: #e3696d;
            position: absolute;
            left: 95%;
            top: 44%;
            height: 22px;
            margin: -11px 0 0 10px;
            padding-left: 25px;
            white-space: nowrap;
            font-size: 12px;
            line-height: 22px;
            /*top: 20px;*/
        }
		.photo-Again{
        background-position: -79px -1809px;
            color: #e3696d;
            position: absolute;
            left: 95%;
            top: 68%;
            height: 22px;
            margin: -11px 0 0 10px;
            padding-left: 25px;
            white-space: nowrap;
            font-size: 12px;
            line-height: 22px;
            /*top: 20px;*/
        }

    </style>
    <script type="text/javascript">
        function selName(){
            var name=document.getElementById("nickname").value;
            if(name==""){
//                document.getElementById("button").disabled=true;
                document.getElementById("nick").style.display="block";
                return ;
            }
            var nickname={nickname:name};
            $.ajax({
                type:"POST",
                url :"saveNickname",
                data:nickname,
                cache:false,
                datatype:"json",
                success:function(data){
                    if(data==1){
                        var name=data ;
                        //查询用户昵称是否存在:1为存在不给于注册
                        document.getElementById("nickRepeat").style.display="block";
//                        document.getElementById("button").disabled=true;
                    }else{
                        document.getElementById("button").disabled=false;
                    }

                }

            })
        }
        function nullNick(){
            document.getElementById("button").disabled=false;
            document.getElementById("nick").style.display="none";
            document.getElementById("nickRepeat").style.display="none";
        }
        function EmailEL(){
            var email=document.getElementById("Email").value;
            if(email=="") {
//                document.getElementById("button").disabled=true;
                document.getElementById("email_null").style.display = "block";
                return ;
            }
            var szReg=/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
            var bChk=szReg.test(email);
            if(bChk){
            }else {
                document.getElementById("emailError").style.display="block";
        }
        }
        function EmailOut(){
            document.getElementById("email_null").style.display = "none";
            document.getElementById("emailError").style.display="none";
        }
        function rightPass(){
//            document.getElementById("button").disabled=true;
            document.getElementById("passAgain").style.display="block";

        }
        function buttonLogin(){
            var name=document.getElementById("nickname").value;
            if(name==""){
//                document.getElementById("button").disabled=true;
                document.getElementById("nick").style.display="block";
                return ;
            }

            var email=document.getElementById("Email").value;
            if(email=="") {
//                 alert("!!!!!!!");
//                document.getElementById("button").disabled=true;
                document.getElementById("email_null").style.display = "block";
                return ;
            }
            var passAgain=document.getElementById("Email").value;
            
            var szReg=/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
            var bChk=szReg.test(email);
            if(bChk){
            }else {
                document.getElementById("emailError").style.display="block";
            }
            
            if(document.getElementById("pass").value==""){
                document.getElementById("passnull").style.display = "block";
                return ;
            }
            var passAgain=document.getElementById("passAgain").value;
           if(passAgain!=document.getElementById("pass").value){
        	   document.getElementById("passAgain").style.display = "block";
        	   return;       	   
           }
            document.forms[0].submit();
        }

    </script>
</head>
<body>

<div class="main-or">
    <div class="photo-in">
        <div class="photo-uhead">
            <img class="img-thumbnail" src="${user.getWebsite()}" alt="http://q.qlogo.cn/qqapp/101149911/87CE574E231C1EA7E4CC1E70519F3308/100">
            <div class="txt">
                <span class="txt-nickname">&nbsp;&nbsp;${user.getFirstName()}</span></br>
                <span class="qq">&nbsp;&nbsp;&nbsp;&nbsp;已使用腾讯QQ登陆</span>
            </div>
    </div>
        <p class="info">创建我的旅游社区，完善以下个人信息，更好地使用本站</p>
        <div class="photo-nlogin">
            <form action="/addsaveuser" method="post" role="form" class="form-horizontal">
                <div class="form-group">
                    <label for="nickname" class="control-label sr-only">昵称</label>
                    <div class="input-group">
                            <div class="input-group-addon">
                                <span class="glyphicon glyphicon-user" style="color: rgb(0, 55, 236);"></span>
                            </div>
                        <input type="text" class="form-control " onclick="nullNick()" onblur="selName()" name="FirstName" id="nickname" value="" style="width: 90%;height:38px;" placeholder="输入昵称">
                    </div>
                    <span class="photo-detect" id="nick" style="display: none;">请输入昵称</span>
                    <span class="photo-detect" id="nickRepeat" style="display: none;">该昵称已注册</span>
                    <span class="photo-ok" style="display:none;">漂亮昵称</span>
                </div>

                <div class="form-group">
                    <label for="Email" class="control-label sr-only">Email</label>
                    <div class="input-group">
                        <div class="input-group-addon"><span class="glyphicon glyphicon-envelope" style="color: rgb(0, 55, 236);"></span></div>
                        <input type="text" class="form-control " name="Email" onclick="EmailOut()" onblur="EmailEL()" id="Email" style="width: 90%;height: 38px;" placeholder="输入邮箱" >
                    </div>

                    <span class="photo-email" id="emailError" style="display:none;">邮箱错误</span>
                    <span class="photo-email" id="email_null" style="display:none;">邮箱不允许为空</span>
                </div>
                <div class="form-group">

                    <label for="pass" class="control-label sr-only">密码</label>
                    <div class="input-group">
                        <div class="input-group-addon"><span class="glyphicon glyphicon-pencil" style="color: rgb(0, 55, 236);"></span></div>
                        <input type="password" class="form-control"   name="Password" id="pass" style="width: 90%;height:38px;" placeholder="密码" >
                    </div>

                    <span class="photo-pass" id="passnull" style="display:none;">密码不允许为空</span>
                </div>
                <div class="form-group">
                    <label for="passAgain" class="control-label sr-only">再次密码</label>
                    <input type="password"class="form-control " id="passAgain" style="width: 90%;margin-left: 3.78%;height: 38px;" placeholder="再次密码" >
               		<span class="photo-Again" id="emailError" style="display:none;">密码不一致，请重新输入</span>
               </div>
                <button type="button" onclick="buttonLogin()" class="btn btn-primary" id="button" style="width: 343px;height: 46px;margin-left: -15px; margin-top: 25px; "><span style="font-size: 22px;">登&nbsp陆</span></button>

                <div class="checkbox" style="margin-top: 20px;">
                    <label>
                        <input type="checkbox" ><span style="font-size:14px;color: #868686;"><a href="#">美丽星球须知</a></span>
                    </label>

                </div>
                <input type="hidden" name="Id" value="${user.getId()}">
            </form>

        </div>
        <div style="margin-top: 109px;margin-left:-190px;">
            <img src="../images/LoginBatton.png" style="width:727px;">
        </div>
    </div>


</div>

</body>
</html>