
//var _width = window.document.body.offsetWidth;
//var _height = window.document.body.offsetHeight;
var G = {
	movetime:3000,
	score:0, //分数
	crashtimer:null //碰撞定时器
};
$(function(){
	G.width = document.body.clientWidth;
	G.height = document.body.clientHeight;
	$("#results").find("h3").hide();
	$("#results").css("top", "120px");
	//gamebegin(); //开始
});
//begin
function gamebegin(){
	initSprites();
	bindEvent();//事件
}
//初始化组件
function initSprites(){
	//alert(G.width);
	$("#results").hide();
	$("#bed1").css("left", "10px");
	$("#bed2").css("left", "250px");
	$("#bed1").css("top", G.height-80);
	$("#bed2").css("top", G.height-75);
	//$("#bed3").css("top", G.height-70);
	$("#man").css("top", "80px");
	G.score = 0;
	$("#score").html("分数:"+G.score);
	
	//运动
	/*
	setInterval(function(){
		var x = 0;
		$(".bed").each(function(i, d){
			x  = $(this).css("left").replace("px", "");
			$(this).animate({left:x+10});
		});
	}, 3000);
	*/
	//movebg();
	movebed1();
	movebed2();
	//movebed3();
	manmove();
	checkCrash(); //检测碰撞
}
//事件
function bindEvent(){
	$("#stage").click(function(){ //点击事件
		var mousex = window.event.clientX;
		var mousey = window.event.clientY;
		var manx = Number($("#man").css("left").replace("px", ""));
		
		if(mousex < manx){ //向左
			//console.log("left");
			$("#man").css("left", Number(manx)-10);
			//$("#man").animate({left:manx-20}, 10);
		}
		if(mousex > manx){ //向右
			//console.log("->:"+Number(Number(manx)+20));
			$("#man").css("left", Number(manx)+10);
			//console.log("left:"+$("#man").css("left"));
			//$("#man").animate({marginLeft:Number(Number(manx)+20)});
		}
		console.log(mousex+"|"+mousey);
	});
}
//背景移动
function movebg(){
	$("#gamebg").animate({left:200}, 30000, function(){
		
	});
}

//蹦床运动
function movebed1(){
	var bedwidth = Number($("#bed1").css("width").replace("px", ""));
	var x = $("#bed1").css("left").replace("px", "");
	var dx = G.width - bedwidth - x;
	$("#bed1").animate({left:G.width-bedwidth}, dx/0.1, function(){
		dx = G.width - bedwidth;
		$(this).animate({left:0}, dx/0.1, movebed1);
	});
}
function movebed2(){
	var bedwidth = Number($("#bed2").css("width").replace("px", ""));
	var x = $("#bed2").css("left").replace("px", "");
	var dx = G.width - bedwidth - x;
	$("#bed2").animate({left:G.width-bedwidth}, dx/0.1, function(){
		dx = G.width - bedwidth;
		$(this).animate({left:0}, dx/0.1, movebed2);
	});
}
function movebed3(){
	var bedwidth = $("#bed3").css("width").replace("px", "");
	var x = $("#bed3").css("left").replace("px", "");
	var dx = G.width - bedwidth - x;
	$("#bed3").animate({left:G.width-bedwidth}, dx/0.1, function(){
		dx = G.width - bedwidth - x;
		$(this).animate({left:0}, dx/0.1, movebed3);
	});
}
//人动
function manmove(){
	$("#man").animate({top:G.height+300}, 3000, "easeInQuad", function(){
		//$("#man").animate({top:80}, 2000, "easeOutQuint", manmove);
		//游戏结束
		//gameover();
	});
}
//游戏结束
function gameover(){
	clearInterval(G.crashtimer);
	$("#man").stop();
	$("#bed1").stop();
	$("#bed2").stop();
	$("#results").show();
	$("#results").find("h3").show();
	$("#results").animate({top:120}, 2000);
}
//replay
function replay(){
	$("#results").css("top", "600px");
	gamebegin();
}
//检测碰撞
function checkCrash(){
	G.crashtimer = setInterval(function(){
		var manbottom = G.height - $("#man").css("top").replace("px", "") - 60; //player脚到底部距离
		if(manbottom <= 80){ //开始检测
			var bed1x = Number($("#bed1").css("left").replace("px", ""));
			var bed2x = Number($("#bed2").css("left").replace("px", ""));
			var manx = Number($("#man").css("left").replace("px", ""));
			console.log("<80:"+manbottom);
			console.log(manx+">"+bed1x);
			if(manx>bed1x && manx<(bed1x+60)){ //与bed1撞
				$("#man").stop();
				$("#man").animate({top:80}, 4000, "easeOutQuad", manmove);
				G.score += 12;
				$("#score").html("分数:"+G.score);
				console.log("与bed1撞");
				return;
			}
			if(manx>bed2x && manx<(bed2x+60)){ //与bed1撞
				$("#man").stop();
				$("#man").animate({top:80}, 4000, "easeOutQuad", manmove);
				G.score += 10;
				$("#score").html("分数:"+G.score);
				console.log("与bed2撞");
				return;
			}
			if(manbottom < 50){
				gameover();
			}
		}
	}, 30);
}