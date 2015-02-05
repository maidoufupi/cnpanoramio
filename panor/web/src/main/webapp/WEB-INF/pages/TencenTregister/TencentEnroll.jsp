<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册</title>
<link rel="stylesheet" href="<c:url value="/bower_components/fileupload/css/bootstrap.min.css"/>">
 <style type="text/css">
        #main {
            min-height: 0;
        }
        .wcontainer {
            width: 1200px;
            margin: 0 auto;
            min-height: 660px;
        }
        .wrap-boxes {
            border: 1px solid #e6e8e9;
            border-top: none;
            box-shadow: 0 3px 3px -2px rgba(0,0,0,0.25);
            background: #fff;
            height: 600px;
        }
        .wheader-wrap {
            padding-bottom: 20px;
            border-bottom: 1px solid #d0d6d9;
        }
        h1, h2, h3, h4, h5, h6 {
            font-size: 100%;
            font-weight: normal;
            font-family: "Microsoft YaHei";
        }
        .cprofile-wrap {
            padding: 80px;
        }
        .cprofile-inner {
            width: 650px;
            margin: 0 auto;
        }
        .l {
            float: left;
        }
        .avator-inner {
            width: 160px;
            height: 160px;
            position: relative;
        }
        .avator-inner img {
            width: 160px;
            height: 160px;
            border: none;
            box-shadow: 0 1px 4px rgba(0,0,0,.25);
        }
        body{

            font-family: "Microsoft YaHei" ! important;
            font-style: normal;
            font-size: 14px;
            background-color:#EDEFF0;
        }
        .main{
           border-bottom: 5px;
           background-color:#ffffff;
           width: 1200px;
           height: 600px;
           margin-top: 50px;
           margin-left: auto;
           margin-right: auto;

        }
        .main_top{

            width: 1200px;
            padding-bottom: 20px;
            margin-top: 50px;
            text-align: left;

        }

    </style>
    <script type="text/javascript">
		function selName(){
			var name=document.getElementById("nickname").value;
			var nickname={nickname:name};
			$.ajax({
				type:"POST",
				url :"saveNickname",
				data:nickname,
				datatype:"json",
				success:function(data){
					if(data !=null){
						var name=data ;  
						alert("$$$$$$$$$$$"+name);
					}else{
						alert("！！！！！！！！！！！！！");
					}
					
				}
				
			})
		}
    
    </script>
</head>
<body>
<div id="main">
    <div class="wcontainer">
        <div class="wrap-boxes">
            <div class="wheader-wrap">
                <h1>完善个人资料</h1>
            </div>
            <div class="cprofile-wrap">
                <div class="cprofile-inner">
                    <!--<div class="l">-->
                        <!--<div class="avator-inner">-->

                        <!--</div>-->
                    </div>
                    <div style="margin-left: 400px ; margin-top: 100px" >
                        <form role="form" class="form-horizontal">
                            <div class="form-group col-lg-10">
                                <label for="name" class="col-sm-3 control-label">昵称</label>
                                <input  style="width: 370px ;" type="text" name="newname" class="form-control " id="nickname" value="${user.getFirstName()}" placeholder="输入昵称"
                                 onchange="selName()">
                            </div>
                            <div class="form-group col-lg-10">
                                <label for="emain" class="col-sm-3 control-label">邮箱</label>
                                <input style="width: 370px ;" type="email" class="form-control " name="em" id="emain" placeholder="请输入邮箱" >

                            </div>
                            <div class="form-group col-lg-10">
                                <label for="pass" class="col-sm-3 control-label">密码</label>
                                <input style="width: 370px ;"type="password"class="form-control  " name="pass" id="pass" placeholder="输入密码">
                            </div>
                            <div class="form-group col-lg-10">
                                <label for="passan"class="col-sm-3 control-label">确认密码</label>
                                <input style="width: 370px ; "type="password" class="form-control " name="passan" id="passan" placeholder="再次确认密码">
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-10">
                                    <div class="checkbox">
                                        <label>
                                            <input type="checkbox"> 请记住我
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-sm-10">
                                    <button style="background-color: #39b94e ;width:300px;height: 35px" type="submit" class="btn btn-default">登录</button>
                                </div>
                            </div>

                        </form>


                    </div>
                </div>
            </div>
        </div>
    </div>


</div>

</body>
</html>