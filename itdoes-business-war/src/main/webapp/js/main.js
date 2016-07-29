var itemlist = [];
var url = "http://localhost:8080/biz/facade/"
var inventorydata = [];
function inventory() {

	showscanbtn();
	if (inventorydata.length > 0) {
		showinventory();
	}

	$("#item_no").keypress(
			function(event) {
				var keycode = (event.keyCode ? event.keyCode : event.which);
				if (keycode == 13) {
					var itemname = $("#item_no").val();
					var url1 = "http://localhost:8080/biz/facade/" + "Part"
							+ "/search";
					var checkkey = {
						ff_partNo : itemname.toUpperCase(),
					}
					var checkresult1 = ajaxsearch(checkkey, url1);
					var url2 = "http://localhost:8080/biz/facade/"
							+ "InvCompany" + "/search";
					checkkey = {
						ff_skuNo : checkresult1[0].skuNo,
						ff_invType : "I",
					}
					var checkresult2 = ajaxsearch(checkkey, url2);
					$.each(checkresult2, function(i, n) {
						if (n.onHandQty == undefined) {
							checkresult2[i].onHandQty = 0;
						}
						if (n.onOrderQty == undefined) {
							checkresult2[i].onOrderQty = 0;
						}
					});
					inventorydata = [ {
						item : checkresult1[0].partNo,
						img : "http://kuzcolighting.com/images/"
								+ checkresult1[0].partNo + "-list.jpg",
						fields : [ {
							inventoryid : checkresult2[0].companyId,
							onhandqty : checkresult2[0].onHandQty,
							onorderqty : checkresult2[0].onOrderQty
						}, {
							inventoryid : checkresult2[1].companyId,
							onhandqty : checkresult2[1].onHandQty,
							onorderqty : checkresult2[1].onOrderQty
						} ]
					} ];

					showinventory();

				}
			})

	$("#scanner_btn").click(
			function() {
				// var barcoderesult = scanCode();
				var barcoderesult = "10691759026402"
				barcoderesult = barcoderesult.substr(
						barcoderesult.indexOf("6"), 11)

				var url1 = url + "Part" + "/search";
				var checkkey = {

					ff_barCode : barcoderesult,
				}
				var checkresult1 = ajaxsearch(checkkey, url1);
				var url2 = url + "InvCompany" + "/search";
				checkkey = {

					ff_skuNo : checkresult1[0].skuNo,
					ff_invType : "I",
				}
				var checkresult2 = ajaxsearch(checkkey, url2);
				$.each(checkresult2, function(i, n) {
					if (n.onHandQty == undefined) {
						checkresult2[i].onHandQty = 0;
					}
					if (n.onOrderQty == undefined) {
						checkresult2[i].onOrderQty = 0;
					}
				});
				inventorydata = [ {
					item : checkresult1[0].partNo,
					img : "http://kuzcolighting.com/images/"
							+ checkresult1[0].partNo + "-list.jpg",
					fields : [ {
						inventoryid : checkresult2[0].companyId,
						onhandqty : checkresult2[0].onHandQty,
						onorderqty : checkresult2[0].onOrderQty
					}, {
						inventoryid : checkresult2[1].companyId,
						onhandqty : checkresult2[1].onHandQty,
						onorderqty : checkresult2[1].onOrderQty
					} ]
				} ];

				showinventory();
				$("#item_no").val(inventorydata[0].item);
			});
}

function scanCode() {
	cordova.plugins.barcodeScanner.scan(function(result) {
		return result.text;
	}, function(error) {
		alert("Scanning failed: " + error);
	});
}

function loginWindowClose() {
	$("div#loginWindow").hide();
	$("div.wrap").show();
}

function orderentry() {
	var testkey = 0;
	var itemarray = {};
	showscanbtn();
	if ((itemlist.length > 0) && (itemlist[0].orderNo == undefined)) {
		showitemlist();
	} else {
		itemlist = [];
	}
	$("#item_no")
			.keypress(
					function(event) {
						testkey = 0;
						var keycode = (event.keyCode ? event.keyCode
								: event.which);
						if (keycode == 13) {
							var itemname = $("#item_no").val();
							var url1 = "http://localhost:8080/biz/facade/"
									+ "Part" + "/search";
							var checkkey = {
								ff_partNo : itemname.toUpperCase(),
							}
							var checkresult1 = ajaxsearch(checkkey, url1);
							itemarray = {
								skuNo : checkresult1[0].skuNo,
								partNo : checkresult1[0].partNo,
								orderQty : 1,
								unitPrice : checkresult1[0].unitPrice,
							};
							$
									.each(
											itemlist,
											function(i, n) {
												if (n.skuNo == checkresult1[0].skuNo) {
													itemlist[i].orderQty = parseInt(itemlist[i].orderQty) + 1;
													testkey = 1;
												}
											});
							if (testkey == 0) {
								itemlist.push(itemarray);
							}
							showitemlist()
						}
					})
	$("#scanner_btn").click(function() {
		testkey = 0;
		itemarray = {};
		// var barcoderesult = scanCode();
		var barcoderesult = "10691759026402"
		barcoderesult = barcoderesult.substr(barcoderesult.indexOf("6"), 11)
		var url2 = url + "Part" + "/search";
		var checkkey = {

			ff_barCode : barcoderesult,
		}
		var checkresult1 = ajaxsearch(checkkey, url2);
		itemarray = {
			skuNo : checkresult1[0].skuNo,
			partNo : checkresult1[0].partNo,
			orderQty : 1,
			unitPrice : checkresult1[0].unitPrice,
		};
		$.each(itemlist, function(i, n) {
			if (n.skuNo == checkresult1[0].skuNo) {
				itemlist[i].orderQty = parseInt(itemlist[i].orderQty) + 1;
				testkey = 1;
			}
		});
		if (testkey == 0) {
			itemlist.push(itemarray);
		}
		showitemlist()

	});

}

function itemdelete(itemno) {
	var itemlist1 = [];
	var j = 0

	$.each(itemlist, function(i, n) {
		if (n.skuNo != itemno) {
			itemlist1[j] = itemlist[i];
			j = j + 1;
		}
	});

	itemlist = itemlist1;
	if (itemlist.length > 0) {
		showitemlist();
	} else {
		$("div#itemlistdiv").remove();
	}

}

function showscanbtn() {
	$("div#content").html("Item:")
	$("<input />", {
		"type" : "text",
		"id" : "item_no",
		"name" : "itemNo",
		"size" : "18px",
		"placeholder" : "please input...",
	}).appendTo("div#content");
	$("<input>", {
		"type" : "button",
		"id" : "scanner_btn",
		"value" : "Scan",
		"class" : "menubutton white",
	}).appendTo("div#content");
	$("#item_no").focus();
}

function showitemlist() {

	if (itemlist[0].orderNo == undefined) {
		var temporderno = "";
	} else {
		var temporderno = itemlist[0].orderNo;
	}

	var itemlistth = [ "Item", "QTY", "Price", "Del" ];
	if ($("div#itemlistdiv")) {
		$("div#itemlistdiv").remove();
	}
	var mytable = "<div id='itemlistdiv'><table class='orderlist' >";
	mytable += "<tr>";
	for (var i = 0; i < itemlistth.length; i++) {
		mytable = mytable + "<th>" + itemlistth[i] + "</th>";
		if (i == (itemlistth.length - 1)) {
			mytable = mytable + "</tr>";
		}
	}

	$.each(itemlist, function(i, n) {

		mytable = mytable + "<tr>" + "<td>" + n.partNo + "</td>"
				+ "<td><input class='itemchange' value=" + n.orderQty + ">"
				+ "</input></td>" + "<td>" + n.unitPrice + "</td>" + "<td>"
				+ "<a href='#'onclick='itemdelete(" + '\"' + n.skuNo + '\"'
				+ ")'>Del</a>" + "</td>" + "</tr>";

	});

	mytable = mytable + "</table>";
	mytable = mytable + "<button  class='menubutton white' onclick='ordersave("
			+ '\"' + temporderno + '\"' + ")'>Submit</button>";
	mytable = mytable
			+ "<button  class='menubutton white' onclick='itemcancel()'>Cancel</button></div>";
	$("div#content").append(mytable);

	$("table td ").change(function() {
		var row = $(this).parent().index();
		var col = $(this).index();
		var allinput = $("table.orderlist input");
		itemlist[row - 1].orderQty = allinput[row - 1].value;
	});

}

function showinventory() {
	if ($("#content p")) {
		$("#content p").remove();
	}

	$("#content").append(
			'<p><img src=' + '"' + inventorydata[0].img + '"' + ' height="80"'
					+ ' width="80"' + '/></p>' + '<p><b>Iitem:</b>'
					+ inventorydata[0].item + '</p>' + '<p><b>Warehouse:</b>'
					+ inventorydata[0].fields[0].inventoryid
					+ '&nbsp&nbsp<b>Onhand:</b>'
					+ inventorydata[0].fields[0].onhandqty
					+ '&nbsp&nbsp<b>Onorder:</b>'
					+ inventorydata[0].fields[0].onorderqty + '</p>'
					+ '<p><b>Warehouse:</b>'
					+ inventorydata[0].fields[1].inventoryid
					+ '&nbsp&nbsp<b>Onhand:</b>'
					+ inventorydata[0].fields[1].onhandqty
					+ '&nbsp&nbsp<b>Onorder:</b>'
					+ inventorydata[0].fields[1].onorderqty + '</p>');

}

function showmenu() {
	$("div#mainmenu").html("");
	var mymenu = "";
	for (i = 0; i < menudata.length; i++) {
		mymenu = mymenu + "<button class='menubutton white' onclick='"
				+ menudata[i].onclick + "'>" + menudata[i].menuname
				+ "</button>";

	}
	$("div#mainmenu").append(mymenu);
}

function itemcancel() {
	itemlist = [];
	$("div#itemlistdiv").remove();
}

function logout() {
	$("div.wrap").hide();
	itemlist = [];
	inventorydata = [];
	$("div#content").html("");
	$(document).ready(function() {
		$("div#loginWindow").show();
	});

}

function ordercheck() {
	showorderlist();

}

function showorderlist() {
	$("#content").empty();
	companyId: 221
	var url1 = url + "OrderHeader" + "/search";
	var checkkey = {
		ff_companyId : 221,
	}
	var checkresult1 = ajaxsearch(checkkey, url1);

	$.each(checkresult1, function(i, n) {

		$("#content").append(
				"<a href='#' class='a_style'  onclick='showorderdetail(" + '"'
						+ n.orderNo + '"' + ")'>" + n.orderNo + "</a>");
		$("<input>", {
			"type" : "button",
			"id" : "order_btn",
			"value" : "Modify",
			"class" : "menubutton white",
			"onclick" : "#",
		}).appendTo("div#content");
		$("#content").append("<hr class='separator' />");
	});
}

function showorderdetail(order) {
	var checkresult2 = [];
	$("#content").empty();
	var temporderno = order;
	var url1 = url + "OrderDetail" + "/search";
	var url2 = url + "Part" + "/search";
	var checkkey = {
		ff_orderNo : temporderno,
	}
	var checkresult1 = ajaxsearch(checkkey, url1);

	$.each(checkresult1, function(i, n) {

		var checkkey1 = {
			ff_skuNo : n.skuNo,
		};
		checkresult2 = ajaxsearch(checkkey1, url2);
		checkresult1[i].unitPrice = checkresult2[0].unitPrice;
		checkresult1[i].partNo = checkresult1[i].partNO;
	});
	itemlist = checkresult1
	showitemlist(temporderno);

}

function ordersave(order) {
	var checkresult = [];
	if (order == "") {
		var orderno = prompt("Please enter OrderNo:");
		var url1 = url + "OrderHeader" + "/post";
		var checkkey = {
			orderNo : orderno,
			companyId : 221,
			entryId : 56,
		}
		ajaxcreate(checkkey, url1);
		var url2 = url + "OrderDetail" + "/post";

		$.each(itemlist, function(i, n) {
			var checkkey = {
				orderNo : orderno,
				skuNo : n.skuNo,
				partNO : n.partNo,
				orderQty : n.orderQty,
			}
			ajaxcreate(checkkey, url2);
		});
	} else {
		var orderno = order;
		var url1 = url + "OrderDetail" + "/search";
		var url2 = url + "OrderDetail" + "/put/";
		var url3 = url + "OrderDetail" + "/delete/";
		var checkkey = {
			ff_orderNo : orderno,
		}

		checkresult = ajaxsearch(checkkey, url1);
		var changekey = 0;
		$.each(checkresult, function(i, n) {
			changekey = 0;
			$.each(itemlist, function(j, m) {
				if (n.orderDetailId == m.orderDetailId) {
					changekey = 1;
					if ((n.orderQty != m.orderQty)&&( m.orderQty!=0)) {

						var checkkey = {
							orderQty : m.orderQty,
						}
						ajaxput(checkkey, (url2 + n.orderDetailId));
					}
                   if(m.orderQty==0){
                	   changekey = 0;
                   }
				}
			});
			if (changekey == 0) {
				ajaxdelete(url3 + n.orderDetailId);
			}
		});
		checkresult = ajaxsearch(checkkey, url1);
		if(checkresult.length==0){

			var checkkey = {
				ff_orderNo : orderno,
			}
			checkresult = ajaxsearch(checkkey, (url + "OrderHeader" + "/search"));
			ajaxdelete((url + "OrderHeader" + "/delete/")+ checkresult[0].orderId)
		};
	}

	itemcancel()
}
