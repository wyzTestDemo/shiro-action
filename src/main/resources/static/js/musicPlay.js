var myAudio ;
function changeMusicStatus() {
    if (myAudio.paused) {
        myAudio.play();
    } else {
        myAudio.pause();
    }
}
/*一个歌曲切换判断值*/
var statusId;
/*音乐id*/
var MusicId ;
/*音乐类型*/
var musicType;
function MusicInit(data) {
    MusicId = data.id;
    musicType = data.musicType;
}
var playBtn = $("#play");
/*修改播放状态*/
function changeMusicPlay() {
    if(myAudio.paused){
        myAudio.play();
    }else{
        myAudio.pause();
    }
    
}
function myPlay() {
    var currentFile = 'http://192.168.100.9:8650/myResources/'+MusicId+"."+musicType;
    if (MusicId != statusId) {
        if (myAudio.src.length > 0) {
            myAudio.pause();
        }
        myAudio.src = currentFile;
        myAudio.load();
        statusId = MusicId;
    }
    changeMusicPlay();
}

//判断浏览器是否支持audio
if (!window.HTMLAudioElement) {
    alert('您的浏览器不支持audio标签');
} else {
    myAudio= $('#myAudio')[0];
    myAudio.volume=0.6;
    //监听事件
    myAudio.oncanplay = function () {
       /* auTotal.html(transTime(myAudio.duration));*/
        console.info('进入可播放状态,音频总长度:' + myAudio.duration);
    }
    myAudio.onplay = function () {
        playBtn.removeClass("layui-icon-play");
        playBtn.addClass("layui-icon-pause");
        console.info('开始播放：' + myAudio.currentTime);
    }
    myAudio.onpause = function () {
        playBtn.removeClass("layui-icon-pause");
        playBtn.addClass("layui-icon-play");
        console.info(statusId + '暂停播放：' + myAudio.currentTime);
    }
    //结束事件
    myAudio.onended = function () {
    }
    myAudio.onprogress = function () {
    }
    myAudio.ontimeupdate = function (e) {
    }
}

function updateProgress(audio) {
    console.info("-----------------------------------++++++++++++++++++++");
    var value = audio.currentTime / audio.duration;
    proBar.css('width', value * 100 + '%');
    proDot.css('left', value * 100 + '%');
    curTime.html(transTime(audio.currentTime));
}

function transTime(value) {
    var time = "";
    var h = parseInt(value / 3600);
    value %= 3600;
    var m = parseInt(value / 60);
    var s = parseInt(value % 60);
    if (h > 0) {
        time = formatTime(h + ":" + m + ":" + s);
    } else {
        time = formatTime(m + ":" + s);
    }

    return time;
}

function formatTime(value) {
    var time = "";
    var s = value.split(':');
    var i = 0;
    for (; i < s.length - 1; i++) {
        time += s[i].length == 1 ? ("0" + s[i]) : s[i];
        time += ":";
    }
    time += s[i].length == 1 ? ("0" + s[i]) : s[i];

    return time;
}