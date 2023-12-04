var vue = new Vue({
    el:"#app",
    data:{
    },
    methods:{
        login:function (){
            $.post(getServiceUrl('members') + "/userinfo/login", $("#_j_login_form").serialize(), function (res){
                if(res.code == 200){
                    console.log(res);
                    var map = res.data;
                    var accessToken = map.token;  //后续后端获取当前登录用户信息
                    var refreshToken = map.refreshToken;  //后续后端获取当前登录用户信息
                    var user = map.user;  //前端页面需要显示用户信息

                    //sessionStorage  客户端技术可以在浏览器窗口存储数据, 一但关闭窗口,
                    // 数据就没了, 是如果多个窗口, 数据无法共享

                    //localStorage  客户端技术可以在浏览器窗口存储数据, 数据操作是永久
                    localStorage.setItem("accessToken", accessToken);
                    localStorage.setItem("refreshToken", refreshToken);
                    localStorage.setItem("user", JSON.stringify(user));
                    var url = document.referrer ? document.referrer : "/";
                    if (url.indexOf("login.html") > -1 || url.indexOf("register.html") > -1) {
                        url = "/";
                    }
                    window.location.href = url;


                }else{
                    popup(res.msg);
                }
            })
        }
    },
    mounted:function () {
    }
});
