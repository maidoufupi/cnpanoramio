<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<head>
<title><fmt:message key="userProfile.title" /></title>
<meta name="menu" content="SettingsMenu" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="<c:url value="/styles/settings.css"/>" rel="stylesheet" />

<title>Panoramio - [设置]</title>
<meta name="description" content="照片分享社群 - 以照片发掘全世界">
<meta http-equiv="X-UA-Compatible" content="IE=9">
</head>

<body id="generic">

	<script type="text/javascript" src="<c:url value="/bower_components/jquery.rest/dist/jquery.rest.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/scripts/settings.js"/>"></script>

	<div class="content_wrapper">
		<div class="alert alert-success" ata-alert="alert">保存成功</div>
        <div class="alert alert-warning" ata-alert="alert">保存失败</div>
		<div id="contenido" class="wide" style="height: 34px;">
			<h1>
				您的<span>设置</span>页面
			</h1>

			<form id="settings" method="post" action="">

				<div id="person" class="panel panel-primary">
					<div class="panel-heading">个人档案</div>
					<div class="panel-body">
						<table class="table table-hover">
							<tr class="row">
								<td><label for="name">用户名称：</label></td>
								<td><input class="text" id="name" name="name" value="${settings.name}"
									size="40"> <span class="form-help">

										用户名称将显示在您的照片下方、讨论区帖子等等… </span></td>
							</tr>

							<tr class="row">
								<td><label for="username">易记名字 <span>（可选）</span>︰
								</label></td>
								<td><input class="text" id="username" name="urlName"
									value="${settings.urlName}" size="40"> <span
									class="form-help twolines"> 它将会出现在您的网址中。 <br>

										比如：http://panoramio.cn/user/<strong>eduardo</strong>
								</span></td>
							</tr>
							<tr class="row">
								<td><label for="homepage_url">您的网页<span>（可选）</span>：
								</label></td>
								<td><input class="text" id="homepage_url"
									name="homepageUrl" value="${settings.homepageUrl}" size="55"></td>
							</tr>
							<tr class="row">
								<td><label for="homepage_url"> 您的头像 <span>（可选）</span>：
								</label></td>
								<td>
									<div id="current-picture">
										<img id="avatar_picture" src="user_avatar.png" width="96"
											height="96" alt="您的头像"> <a href="#" id="change_avatar">更改头像</a>
									</div>
								</td>
							</tr>
							<tr class="row">
								<td><label for="description"> 写一些关于您自己 <span>（可选）</span>︰
								</label></td>
								<td><textarea id="description" name="description" cols="55"
										rows="9">${settings.description}</textarea></td>
							</tr>
						</table>
					</div>
				</div>

				<div id="delete_account" class="panel panel-primary">
					<div class="panel-heading">删除帐户</div>
					<div class="panel-body">
						<p>
							如果您希望删除自己的帐户，请点击<a href="#">此处</a>。点击后，我们会先向您确认，然后再删除帐户。
						</p>
					</div>
				</div>


				<div id="alert" class="panel panel-primary">
					<div class="panel-heading">电子邮件提醒和私密信息</div>
					<div class="panel-body">
						<table class="table table-hover">
							<tr>
								<td>
									<div class="checkbox">
										<label for="alert_comments"><input type="checkbox"
											id="alert_comments" name="alertComments" <c:if test='${settings.alertComments}'>
											                                 <c:out value='checked'/>
											                               </c:if>
											class="alert-checkbox" onclick="this.blur()" value="true">
											我的照片有新的评论
										</label>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="checkbox">
										<label for="alert_photos"> <input type="checkbox"
											id="alert_photos" name="alertPhotos" <c:if test='${settings.alertPhotos}'>
											                                 <c:out value='checked'/>
											                               </c:if>
											class="alert-checkbox" onclick="this.blur()" value="true">
											我订阅的用户上传了新照片
										</label>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="checkbox">
										<label for="alert_group_invitations"> <input
											type="checkbox" id="alert_group_invitations"
											name="alertGroupInvitations" <c:if test='${settings.alertGroupInvitations}'>
											                                 <c:out value='checked'/>
											                               </c:if>
											class="alert-checkbox" onclick="this.blur()" value="true">
											新的和更新后的群组加入邀请
										</label>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="checkbox">
										<label for="private_messages"> <input type="checkbox"
											id="private_messages" name="privateMessages" <c:if test='${settings.privateMessages}'>
											                                 <c:out value='checked'/>
											                               </c:if> 
											class="alert-checkbox"
											onclick="this.blur()" value="true"> 启用私密信息
										</label>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>

				<div id="mapsel" class="panel panel-primary">
					<div class="panel-heading">地图选择</div>
					<div class="panel-body">
						<p>请选择一个地图供应商</p>

						<table class="table table-hover">
							<tr>
								<td>
									<div class="radio">
										<label> <input type="radio" name="mapVendor"
											id="map_vendor1" value="baidu" <c:if test='${settings.mapVendor == "baidu"}'>
											                                 <c:out value='checked'/>
											                               </c:if>>百度地图
										</label>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="radio">
										<label> <input type="radio" name="mapVendor"
											id="map_vendor2" value="qq" <c:if test='${settings.mapVendor == "qq"}'>
											                                 <c:out value='checked'/>
											                               </c:if>> 腾讯地图
										</label>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="radio">
										<label> <input type="radio" name="mapVendor"
											id="map_vendor3" value="gaode" <c:if test='${settings.mapVendor == "gaode"}'>
											                                 <c:out value='checked'/>
											                               </c:if>> 高德地图
										</label>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div class="radio">
										<label> <input type="radio" name="mapVendor"
											id="map_vendor4" value="ali" <c:if test='${settings.mapVendor == "ali"}'>
											                                 <c:out value='checked'/>
											                               </c:if>> 阿里地图
										</label>
									</div>
								</td>
							</tr>

						</table>
					</div>
				</div>

				<div id="sharing" class="panel panel-primary">

					<div class="panel-heading">分享，许可和版权</div>

					<p class="sublegend">为您的照片设置默认许可协议：</p>

					<table class="table table-hover">
						<tr>
							<td>
								<div class="inline">
									<input onclick="this.blur()" type="radio"
										id="all_rights_reserved" name="allRightsReserved"
										value="true" <c:if test='${settings.allRightsReserved}'>
											           <c:out value='checked'/>
											         </c:if>> <label
										for="all_rights_reserved">保留所有权利</label>

									<p class="explanation">任何人在未获得您明确许可的情况下，都不得复制或使用您的照片。</p>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div class="inline">
									<input onclick="this.blur()" type="radio"
										id="some_rights_reserved" name="allRightsReserved"
										value="true" <c:if test='${settings.commercialUse || settings.modify}'>
											           <c:out value='checked'/>
											         </c:if>> <label for="some_rights_reserved">保留部份权利</label>

									<p class="explanation">保留版权，但允许有条件的使用，请选择：</p>

									<div id="conditions" class="panel-body">

										<p class="sublegend">是否允许用作商业用途？</p>

										<input onclick="this.blur()" type="radio" id="comm-use-yes"
											name="commercialUse" value="true" disabled=""
											<c:if test='${settings.commercialUse}'>
											  <c:out value='checked'/>
										    </c:if>><label for="comm-use-yes">是</label> 
									    <input onclick="this.blur()"
											type="radio" id="comm-use-no"
											name="commercialUse" value="false" disabled=""
											<c:if test='${not settings.commercialUse}'>
											  <c:out value='checked'/>
											</c:if>> <label for="comm-use-no">否</label>

										<p class="sublegend">是否允许修改？</p>

										<input onclick="this.blur()" type="radio" id="modify-yes"
											name="modify" value="true" disabled=""
											<c:if test='${settings.modify}'>
											           <c:out value='checked'/>
											         </c:if>><label for="modify-yes">是</label> 
										<input onclick="this.blur()"
											type="radio" id="modify-no" name="modify" disabled=""
											value="false" <c:if test='${not settings.modify}'>
											           <c:out value='checked'/>
											         </c:if>> <label for="modify-no">否</label>

										<input onclick="this.blur()" type="radio" id="modify-sa"
											name="modify" value="false" disabled=""> <label
											for="modify-sa"> 是，条件是其它人必须以相同方式共享 </label>
									</div>
								</div>
							</td>
						</tr>
					</table>
				</div>

				<div id="botones">
					<input class="btn btn-primary" type="submit" value="保存更改">
				</div>
			</form>

		</div>
	</div>

	<div class="pac-container"
		style="position: absolute; z-index: 1000; display: none; width: 215px; left: 477px; top: 47px;"></div>
	<div class="pac-container"
		style="position: absolute; z-index: 1000; display: none; width: 0px; left: 0px; top: 0px;"></div>
</body>
</html>

