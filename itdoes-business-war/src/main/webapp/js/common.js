function ajaxcreate(savedata, url1) {
	$.ajax({
		type : "POST",
		url : url1,
		data : savedata,
		async : false,
		success : function(result) {

		},
		timeout : 3000,
		error : function(xhr) {
			var obj = jQuery.parseJSON( xhr.responseText );
		    alert(obj.message);
			alert("error： " + xhr.status + " " + xhr.statusText);
		},

	});
}

function ajaxsearch(checkkey, url1) {
	var checklist = [];
	$.ajax({
		type : "GET",
		url : url1,
		data : checkkey,
		async : false,
		success : function(result) {
			if (result.data == undefined) {
				alert("no result");
			} else {
				$.each(result.data, function(i, n) {
					checklist.push(result.data[i]);
				});
			}
		},
		timeout : 3000,
		error : function(xhr) {
			var obj = jQuery.parseJSON( xhr.responseText );
		    alert(obj.message);
			alert("error： " + xhr.status + " " + xhr.statusText);
		},
	});
	return (checklist);
}

function ajaxdelete(url1) {
	$.ajax({
		type : "GET",
		url : url1,
		async : false,
		success : function(result) {
			alert("DELETE OK ");
		},
		timeout : 3000,
		error : function(xhr) {
			var obj = jQuery.parseJSON( xhr.responseText );
		    alert("delete message:"+obj.message);
			alert("error： " + xhr.status + " " + xhr.statusText);
		},

	});
}
function ajaxput(putdata,url1) {
	$.ajax({
		type : "POST",
		url : url1,
		data : putdata,
		async : false,
		success : function(result) {
			alert("PUT OK ");
		},
		timeout : 3000,
		error : function(xhr) {
			var obj = jQuery.parseJSON( xhr.responseText );
		    alert("put message:"+obj.message);
			alert("error： " + xhr.status + " " + xhr.statusText);
		},

	});
}
function test( ) {
       var testlist = {};
 for (var i=0 ;i<1;i++){

     testlist.skuNo=166; //Part id ,InvCompany
     testlist.partNo="41311W";//Part
     testlist.barCode=69175900005;//Part
     testlist.img="part_img_31105B.jpg";//Part
     testlist.unitPrice=i;//Part
     testlist.inventoryId=i;//InvCompany id
     testlist.onHandQty=i;//InvCompany
     testlist.onOrderQty=i;//InvCompany
     testlist.orderId=i;//OrderHeader id 
     testlist.orderNo=i;//OrderHeader
     testlist.companyIdy=i;//OrderHeader      
     testlist.pono=i;//OrderHeader
     testlist.entryDate=i;//OrderHeader    
     testlist.orderDetailId=i;//OrderDetail id 
     testlist.orderQty=i;//OrderDetail	
 
     itemlist.push(testlist);
alert(" ");
 }
 
 }