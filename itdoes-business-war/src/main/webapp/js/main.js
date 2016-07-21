var itemlist = [];
var inventorydata = [];

function inventory() {

	showscanbtn();
	if (inventorydata.length > 0) {
		showinventory();
	}

	$("#item_no").keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == 13) {
			inventorydata = [ {
				item : '41311W',
				img : "http://kuzcolighting.com/images/41311W-list.jpg",
				fields : [ {
					inventoryid : "SRY",
					onhandqty : 59,
					onorderqty : 432
				}, {
					inventoryid : "LLC",
					onhandqty : 159,
					onorderqty : 132
				} ]
			} ];

			showinventory();

		}
	})

	$("#scanner_btn").click(function() {
		// var barcoderesult = scanCode();
		inventorydata = [ {
			item : '41311W',
			img : "http://kuzcolighting.com/images/41311W-list.jpg",
			fields : [ {
				inventoryid : "SRY",
				onhandqty : 59,
				onorderqty : 432
			}, {
				inventoryid : "LLC",
				onhandqty : 159,
				onorderqty : 132
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

	showscanbtn();
	if (itemlist.length > 0) {
		showitemlist();
	}
	$("#item_no")
			.keypress(
					function(event) {
						var keycode = (event.keyCode ? event.keyCode
								: event.which);
						if (keycode == 13) {
							var itemarray = [];
							itemarray[0] = [ "41311W", 1, 12.34,
									"<a href='#'onclick='itemdelete(\"41311W\")'>Del</a>" ];
							itemlist[itemlist.length] = itemarray[0];
							itemarray[0] = [ "52022BN", 1, 12.34,
									"<a href='#'onclick='itemdelete(\"52022BN\")'>Del</a>" ];
							itemlist[itemlist.length] = itemarray[0];
							itemarray[0] = [ "601001CH-LED", 1, 12.34,
									"<a href='#'onclick='itemdelete(\"601001CH-LED\")'>Del</a>" ];
							itemlist[itemlist.length] = itemarray[0];
							showitemlist()

						}
					})
	$("#scanner_btn")
			.click(
					function() {
						var itemarray = [];
						// var barcoderesult = scanCode();
						itemarray[0] = [ "41311W", 1, 12.34,
								"<a href='#'onclick='itemdelete(\"41311W\")'>Del</a>" ];
						itemlist[itemlist.length] = itemarray[0];
						itemarray[0] = [ "52022BN", 1, 12.34,
								"<a href='#'onclick='itemdelete(\"52022BN\")'>Del</a>" ];
						itemlist[itemlist.length] = itemarray[0];
						itemarray[0] = [ "601001CH-LED", 1, 12.34,
								"<a href='#'onclick='itemdelete(\"601001CH-LED\")'>Del</a>" ];
						itemlist[itemlist.length] = itemarray[0];
						showitemlist()

					});

}

function itemdelete(itemno) {
	var itemlist1 = [];
	var j = 0

	for (i = 0; i < itemlist.length; i++) {
		if ($.inArray(itemno, itemlist[i]) == -1) {
			itemlist1[j] = itemlist[i];
			j++;
		}
	}

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
		"class" : "hwq2button white",
		"placeholder" : "please input...",
	}).appendTo("div#content");
	$("#item_no").focus();
}

function showitemlist() {
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
	for (var k = 0; k < itemlist.length; k++) {
		mytable = mytable + "<tr>";
		for (var j = 0; j < itemlist[k].length; j++) {
			mytable = mytable + "<td>" + itemlist[k][j] + "</td>";
			if (j == (itemlist[k].length - 1)) {
				mytable = mytable + "</tr>";
			}
		}
	}
	mytable = mytable + "</table>";
	mytable = mytable
			+ "<button  class='hwq2button white' onclick='#'>Submit</button>";
	mytable = mytable
			+ "<button  class='hwq2button white' onclick='itemcancel()'>Cancel</button></div>";
	$("div#content").append(mytable);
	$("table td ").click(function() {
		if ($(this).index() == 1) {
			var row = $(this).parent().index();
			var col = $(this).index();
			var num = prompt("please input QTY:");

			$(this).text(num);
			itemlist[row - 1][col] = num;
		}
	});

}
function showinventory() {
	if ($("#content p")) {
		$("#content p").remove();
	}

	$("#content").append(
			'<p><img src=' + '"' + inventorydata[0].img + '"' + ' height="80"'
					+ ' width="80"' + '/></p>' + '<p>' + '<b>Iitem:</b>'
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
		mymenu = mymenu + "<button class='hwq2button white' onclick='"
				+ menudata[i].onclick + "'>" + menudata[i].menuname
				+ "</button>";

	}
	$("div#mainmenu").append(mymenu);
}

function itemcancel() {
	itemlist = [];
	orderentry();
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
 	$("#content").empty();
 	alert("show order list");
}