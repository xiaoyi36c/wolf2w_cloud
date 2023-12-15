var vue = new Vue({
    el:"#app",
    data:{
        tv:{destId:''},
        dests:{},
        content:''
    },
    filters:{
        dateFormat:function(date){
            return dateFormat(date, "YYYY-MM-DD")
        }
    },
    methods:{
        chosePic:function () {
            $("#coverBtn").click();
        },
        saveOrUpdate:function (state) {
            $("#state").val(state);
            var param = $("#editForm").serialize() ;
            ajaxPost("article","/travels/saveOrUpdate", param, function (data) {
                //还缺一参数：目的地id
                var destId = $("#region").val();
                window.location.href = "/views/travel/detail.html?id=" + data.data + "&destId=" + destId;
            })
        }
    },
    mounted:function () {
        var id = getParams().id;
        //游记编辑
        /*ajaxGet("/travels/input",{id:id}, function (data) {
            var map = data.data;
            if(map.tv){//travel, 当id有值时需要进行回显
                vue.tv = map.tv;
                ue.setContent( map.tv.content);
            }
            vue.dests = map.dests;
        })*/

        ajaxGet("article","/backend/destinations/list", {}, function (data) {
            vue.dests = data.data;
        })



        if (id){
            ajaxGet("article","/travels/"+ id, {}, function (data) {
                vue.tv = data.data;
                ue.setContent( data.data.content.content);
            })
        }

        //接口设计原则:一个接口一个功能
        // 查询目的:  destinations/list
        // 数据回显:  travels/detail?id=
    },

    created:function () {
        if (!getAccessToken() || !getRefreshToken()){
            alert("请先登录");
            window.location.href = "/login.html";
        }
    },
    beforeUpdate:function () {
        var aouthId = vue.dests[0].aouthId;
        var userInfo = getUserInfo();
        if (aouthId !== userInfo.id){
            alert("不能编辑别人的游记");
            window.location.href = "/views/travel/list.html";
        }
    }



});

