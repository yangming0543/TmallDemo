<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/header.jsp" %>
<head>
    <script src="${pageContext.request.contextPath}/res/js/admin/admin_login.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/admin/admin_login.css"/>
    <title>Tmall 管理后台 - 登录</title>
</head>
<body>
<div id="div_background">
    <div id="div_nav">
        <span id="txt_date"></span>
        <span id="txt_peel">换肤</span>
        <ul id="div_peelPanel">
            <li value="url(${pageContext.request.contextPath}/res/images/admin/loginPage/background-1.jpg)">
                <img src="${pageContext.request.contextPath}/res/images/admin/loginPage/background-mini-1.jpg"/>
            </li>
            <li value="url(${pageContext.request.contextPath}/res/images/admin/loginPage/background-2.jpg)">
                <img src="${pageContext.request.contextPath}/res/images/admin/loginPage/background-mini-2.jpg"/>
            </li>
            <li value="url(${pageContext.request.contextPath}/res/images/admin/loginPage/background-3.jpg)">
                <img src="${pageContext.request.contextPath}/res/images/admin/loginPage/background-mini-3.jpg"/>
            </li>
            <li value="url(${pageContext.request.contextPath}/res/images/admin/loginPage/background-4.jpg)">
                <img src="${pageContext.request.contextPath}/res/images/admin/loginPage/background-mini-4.jpg"/>
            </li>
            <li value="url(${pageContext.request.contextPath}/res/images/admin/loginPage/background-5.jpg)">
                <img src="${pageContext.request.contextPath}/res/images/admin/loginPage/background-mini-5.jpg"/>
            </li>
        </ul>
    </div>
    <div id="div_main">
        <div id="div_head"><p>Tmall <span>管理后台</span></p></div>
        <div id="div_content">
            <img id="img_profile_picture"
                 alt="头像" title="头像">
            <form id="form_login">
                <input type="text" class="form-control form_control" placeholder="用户名" id="input_username"
                       title="请输入用户名"/>
                <input type="password" class="form-control form_control" placeholder="密码" id="input_password"
                       title="请输入密码" autocomplete="on">

                <input type="text" class="form-control form_control" placeholder="验证码" id="input_code"
                       title="请输入验证码"/>
                <img id="img_code" alt="验证码" title="验证码" onclick="getHomeCode()"/>
                <span id="txt_error_msg"></span>
                <input type="button" class="btn btn-danger" id="btn_login" value="登录"/>
            </form>
        </div>
    </div>
</div>
</body>