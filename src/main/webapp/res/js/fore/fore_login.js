$(function () {
    getHomeCode();
    //二维码动画
    $("#qrcodeA").hover(
        function () {
            $(this).stop().animate({left: "13px"}, 450, function () {
                $("#qrcodeB").stop().animate({opacity: 1}, 300)
            });
        }
        , function () {
            $("#qrcodeB").css("opacity", "0");
            $(this).stop().animate({left: "80px"}, 450);
        });
    //登录方式切换
    $("#loginSwitch").click(function () {
        const messageSpan = $(".loginMessageMain").children("span");
        if ($(".pwdLogin").css("display") === "block") {
            $(".pwdLogin").css("display", "none");
            $(".qrcodeLogin").css("display", "block");
            messageSpan.text("密码登录在这里");
            $(this).removeClass("loginSwitch").addClass("loginSwitch_two");
        } else {
            $(".pwdLogin").css("display", "block");
            $(".qrcodeLogin").css("display", "none");
            messageSpan.text("扫码登录更安全");
            $(this).removeClass("loginSwitch_two").addClass("loginSwitch");
        }
    });
    $("#pwdLogin").click(function () {
        const messageSpan = $(".loginMessageMain").children("span");
        $(".pwdLogin").css("display", "block");
        $(".qrcodeLogin").css("display", "none");
        messageSpan.text("扫码登录更安全");
        $("#loginSwitch").removeClass("loginSwitch_two").addClass("loginSwitch");
    });
    //登录验证
    $(".loginForm").submit(function () {
        let yn = true;
        $(this).find(":text,:password").each(function () {
            let code = $("#code").val();
            if ($("#name").val() === "") {
                styleUtil.errorShow($("#error_message_p"), "请输入用户名！");
                yn = false;
                return yn;
            }
            if ($("#password").val() === "") {
                styleUtil.errorShow($("#error_message_p"), "请输入密码！");
                yn = false;
                return yn;
            }
            if ($("#code").val() === "") {
                styleUtil.errorShow($("#error_message_p"), "请输入验证码！");
                yn = false;
                return yn;
            }
            if (cookieUtil.getCookie("loginCode") != code) {
                styleUtil.errorShow($("#error_message_p"), "验证码错误!");
                yn = false;
                return yn;
            }
        });

        if (yn) {
            $.ajax({
                type: "POST",
                url: "/tmall/login/doLogin",
                data: {"username": $.trim($("#name").val()), "password": $.trim($("#password").val())},
                dataType: "json",
                success: function (data) {
                    $(".loginButton").val("登 录");
                    if (data.success) {
                        location.href = "/tmall";
                        cookieUtil.removeCookie("loginCode");
                    } else {
                        styleUtil.errorShow($("#error_message_p"), "用户名和密码错误！");
                    }
                },
                error: function (data) {
                    $(".loginButton").val("登 录");
                    styleUtil.errorShow($("#error_message_p"), "服务器异常，请刷新页面再试！");
                },
                beforeSend: function () {
                    $(".loginButton").val("正在登录...");
                }
            });
        }
        return false;
    });
  /*  $(".loginForm :text,.loginForm :password").focus(function () {
        styleUtil.errorHide($("#error_message_p"));
    });*/
});

//获取登录验证码
function getHomeCode() {
    $.getJSON("/tmall/login/code",function (data) {
        if(data!== null){
            $("#img_code").attr("src", data.img);
            cookieUtil.setCookie("loginCode", data.code,1);
        }
    });
}