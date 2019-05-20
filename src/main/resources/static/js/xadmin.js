$(function () {

    //加载弹出层
    layui.use(['form', 'element'], function () {
        layer = layui.layer;
        element = layui.element;
    });

    //触发事件
    var tab = {
        tabAdd: function (title, url, id) {
            //新增一个Tab项
            element.tabAdd('tab', {
                title: title
                ,
                content: '<iframe tab-id="' + id + '" frameborder="0" src="' + url + '" scrolling="yes" title="' + title + '" class="x-iframe"></iframe>'
                ,
                id: id
            })
        }
        , tabDelete: function (othis) {
            //删除指定Tab项
            othis.addClass('layui-btn-disabled');
        }
        , tabChange: function (id) {
            //切换到指定Tab项
            element.tabChange('tab', id);
        }
    };


    tableCheck = {
        init: function () {
            $(".layui-form-checkbox").click(function (event) {
                if ($(this).hasClass('layui-form-checked')) {
                    $(this).removeClass('layui-form-checked');
                    if ($(this).hasClass('header')) {
                        $(".layui-form-checkbox").removeClass('layui-form-checked');
                    }
                } else {
                    $(this).addClass('layui-form-checked');
                    if ($(this).hasClass('header')) {
                        $(".layui-form-checkbox").addClass('layui-form-checked');
                    }
                }

            });
        },
        getData: function () {
            var obj = $(".layui-form-checked").not('.header');
            var arr = [];
            obj.each(function (index, el) {
                arr.push(obj.eq(index).attr('data-id'));
            });
            return arr;
        }
    };

    //开启表格多选
    tableCheck.init();


    $('.container .left_open i').click(function (event) {
        if ($('.left-nav').css('left') == '0px') {
            $('.left-nav').animate({left: '-221px'}, 100);
            $('.page-content').animate({left: '0px'}, 100);
            $('.page-content-bg').hide();
        } else {
            $('.left-nav').animate({left: '0px'}, 100);
            $('.page-content').animate({left: '221px'}, 100);
            if ($(window).width() < 768) {
                $('.page-content-bg').show();
            }
        }
    });

    $('.page-content-bg').click(function (event) {
        $('.left-nav').animate({left: '-221px'}, 100);
        $('.page-content').animate({left: '0px'}, 100);
        $(this).hide();
    });

    $('.layui-tab-close').click(function (event) {
        $('.layui-tab-title li').eq(0).find('i').remove();
    });

    $("tbody.x-cate tr[fid!='0']").hide();

    //左侧菜单效果
    // $('#content').bind("click",function(event){
    $('.left-nav #nav li').click(function (event) {
        $(".left-nav .layui-this").removeClass('layui-this');
        $(this).first().addClass('layui-this');
        if ($(this).children('.sub-menu').length) {
            if ($(this).hasClass('open')) {
                $(this).removeClass('open');
                $(this).find('.nav_right').html('&#xe697;');
                $(this).children('.sub-menu').stop().slideUp();
                $(this).siblings().children('.sub-menu').slideUp();
            } else {
                $(this).addClass('open');
                $(this).children('a').find('.nav_right').html('&#xe6a6;');
                $(this).children('.sub-menu').stop().slideDown();
                $(this).siblings().children('.sub-menu').stop().slideUp();
                $(this).siblings().find('.nav_right').html('&#xe697;');
                $(this).siblings().removeClass('open');
            }
        } else {
            var url = $(this).children('a').attr('_href');
            var title = $(this).find('cite').html();
            var index = $('.left-nav #nav li').index($(this));

            for (var i = 0; i < $('.x-iframe').length; i++) {
                if ($('.x-iframe').eq(i).attr('tab-id') == url) {
                    tab.tabChange(url);
                    event.stopPropagation();
                    return;
                }
            }

            tab.tabAdd(title, url, url);
            tab.tabChange(url);
        }

        event.stopPropagation();

    })

});
var cateIds = [];

function getCateId(cateId) {

    $("tbody tr[fid=" + cateId + "]").each(function (index, el) {
        id = $(el).attr('cate-id');
        cateIds.push(id);
        getCateId(id);
    });
}

/**
 * 显示错误信息
 * @param msg： 错误信息
 */
function showError(msg) {
    layer.msg(msg, {icon: 2});
}

/**
 * 处理 ajax 请求结果
 * @param result： ajax 返回的结果
 * @param fn： 成功的处理函数 ( 传入data: fn(result.data) )
 */
function handlerResult(result, fn) {
    // 成功执行操作，失败提示原因
    if (result.code === 0) {
        fn(result.data);
    } else {
        showError(result.msg);
    }
}

function getWeekList() {
    var today = new Date();
    var data = [];
    for (var i = 0; i < 7; i++) {
        var temp = new Date(today.getTime() - 1000 * 60 * 60 * 24 * i);
        data[6 - i] = dateFormat("yyyy-MM-dd", temp);
    }
    return data;
}

function dateFormat(fmt, date) {
    var o = {
        "M+": date.getMonth() + 1,                      //月份
        "d+": date.getDate(),                           //日
        "h+": date.getHours(),                          //小时
        "m+": date.getMinutes(),                        //分
        "s+": date.getSeconds(),                        //秒
        "q+": Math.floor((date.getMonth() + 3) / 3),    //季度
        "S": date.getMilliseconds()                     //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function openTab(title, url) {
    var id = url;
    iframe = parent.$('.x-iframe');
    for (var i = 0; i < iframe.length; i++) {
        if (iframe.eq(i).attr('tab-id') == url) {
            parent.element.tabChange('tab', url);
            return;
        }
    }
    parent.element.tabAdd('tab', {
        title: title
        ,
        content: '<iframe tab-id="' + id + '" frameborder="0" src="' + url + '" scrolling="yes" class="x-iframe"></iframe>'
        ,
        id: id
    });
    parent.element.tabChange('tab', url);
}


function getCheckBoxValueByName(name) {
    var checkedValue = [];
    $('input[name="' + name + '"]:checked').each(function () {
        checkedValue.push($(this).val());
    });
    return checkedValue;
}

/**
 * 全局 AJAX error 处理事件.
 */
$(document).ajaxError(function(event, response){
    console.log("错误响应状态码: ",response.status);
    console.log("错误响应结果: ",response.responseJSON);
    showError(response.responseJSON.msg);
});


/**
 * 禁用 ajax 缓存
 */
$.ajaxSetup({
    cache: false
});

/**
 * 获取 URL 中查询条件部分的参数
 * @param name      参数名称
 * @returns         参数值
 */
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}