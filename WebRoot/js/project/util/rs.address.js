/**
 * 地址选择器
 */
		var _addr = new RSAddress();
		function RSAddress(){}
		RSAddress.prototype.position = {address:"",province:"", city:"",area:""};
		RSAddress.prototype.show = function(callback){
			if($("#addressblock").length>0){
				$("#addressblock").show();
				return false;
			}
			$("body").append('<div id="addressblock"></div>');
			$("#addressblock").append('<div class="addrhead"><a class="back" href="javascript:$(\'#addressblock\').hide()" ><i class="icon-chevron-left icon-1x" ></i>取消</a><h3>选择地址</h3></div>');
			$("#addressblock").append('<ul class="provincelist"></ul><ul class="citylist"></ul><ul class="arealist"></ul>');
			post("/region/query", {pid:1}, function(msg){
				if(msg.success){
					var provincelist = strToJson(msg.data);
					var html = [];
					$(provincelist).each(function(i, d){
						html.push('<li><a value="'+d[0]+'">'+d[1]+'</a></li>');
					});
					$(".provincelist").html(html.join(""));
					$(".provincelist a").bind("click", function(){
						$(".provincelist a").removeClass("active");
						$(this).addClass("active");
						$(".arealist").html("");
						_addr.position.province = $(this).text();
						post("/region/query", {pid:$(this).attr("value")}, function(msg){
							if(msg.success){
								var citylist = strToJson(msg.data);
								var html = [];
								$(citylist).each(function(i, d){
									html.push('<li><a value="'+d[0]+'">'+d[1]+'</a></li>');
								});
								$(".citylist").html(html.join(""));
								$(".citylist a").bind("click", function(){
									$(".citylist a").removeClass("active");
									$(this).addClass("active");
									_addr.position.city = $(this).text();
									post("/region/query", {pid:$(this).attr("value")}, function(msg){
										if(msg.success){
											var arealist = strToJson(msg.data);
											var html = [];
											$(arealist).each(function(i, d){
												html.push('<li><a value="'+d[0]+'">'+d[1]+'</a></li>');
											});
											$(".arealist").html(html.join(""));
											$(".arealist a").bind("click", function(){
												_addr.position.area = $(this).text();
												_addr.position.address = _addr.position.province+_addr.position.city+_addr.position.area;
												//alert($(this).text());
												$("#addressblock").hide();
												if(callback) callback(_addr.position);
											});
										}
									});
								});
							}
						});
					});
				}
			});	
		};
	//加载css
	   var fileref = document.createElement('link');fileref.setAttribute("rel","stylesheet");fileref.setAttribute("type","text/css");fileref.setAttribute("href", "/js/ronsai/util/rs.address.css");
	   if(typeof fileref != "undefined"){document.getElementsByTagName("head")[0].appendChild(fileref);} 