var data = JSON.parse([ 
                        
      item:'41311W'
	 ,img:""
	 ,fields:[                       
{
	"inventoryid" : "SRY",
	"onhandqty" : 59,
	"onorderqty": 432
}, {
	"inventoryid" : "LLC",
    "onhandqty" : 16259,
	"onorderqty" : 132
} ]])

	 var data = JSON.parse([                         
	                        item:'41311W'
	                  	 ,img:""
	                  	 ,fields:[                       
	                  {
	                  	"inventoryid" : "SRY",
	                  	"onhandqty" : 59,
	                  	"onorderqty": 432,
	                  }, {
	                  	"inventoryid" : "LLC",
	                      "onhandqty" : 16259,
	                  	"onorderqty" : 132,
	                  } ]]);
	 
     $(#content).append(data[0].item);


		<!--creat a table-->
		var table="<table class='orderlist' border='1' style='width:100%'>";
	     table+="<tr>";
	     for (var i=0,i<itemlistth.length,i++){
				mytable=mytable+"<th>"+itemlistth[i]+"</th>";
	    	 if (i=(itemlistth.length-1)){
	    		 mytable=mytable+"</tr>";
	    	 }
	     }
	     for (var k=0,i<itemlist.length,k++){
	    	 mytable=mytable+"<tr>";
	         for(var j=0,j<itemlist[k].length,j++){
	        	 mytable="<td>"+itemlist[j]+"</td>";
	    	     if (j=(itemlist[k].length-1)){
	    	    	 mytable=mytable+"</tr>";
	      	     }
	         }
	     }
	     mytable=mytable+"</table>";
	     $("#content").append(mytable);
     
     


for (store in data[0][state][city]) {
	var request = '{address: \'' + data[country][state][city][store]['Address']
			+ ', ' + city + ', ' + state + ', ' + country + '\'}';
	$('#results')
			.append(
					'<div><b onclick="geocodeAddress('
							+ request
							+ ', 17)">'
							+ store
							+ '</b><br>\
		Address: '
							+ data[country][state][city][store]['Address']
							+ '<br>\
		City: '
							+ city
							+ ', '
							+ state
							+ ', '
							+ country
							+ ', '
							+ data[country][state][city][store]['Zip']
							+ '<br>\
		Phone: '
							+ data[country][state][city][store]['Phone']
							+ '<br>\
		Fax: '
							+ data[country][state][city][store]['Fax']
							+ '<br>\
		Email: '
							+ data[country][state][city][store]['Email']
							+ '<br>\
		Website: '
							+ data[country][state][city][store]['Website']
							+ '<br>\
		<em><a href="javascript:void(0)" onclick="geocodeAddress('
							+ request
							+ ', 17)">View map</a></em><br>\
		<img src="Kuzco-Non-Selection-Box-Code/linked-border2.jpg"></div>');
};