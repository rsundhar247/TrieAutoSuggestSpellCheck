function SpellCheck(){
	var sentence1 = document.getElementById('txtarea1').value;
	var option1 = '1';
	
	$.ajax({
        type: "POST",
        url: "SpellCheck",
        data: { 
        	sentence: sentence1,
        	option: option1,
        	access: 't'
        },
		success: function(data){
			if(data != null){
				var value = '';
				Object.keys(data).forEach(function(key) {
					if(key == 'invalid'){
						value = ''+data[key];
					}
				});
				
				$("#wrongWordsArea").empty();
				
				document.getElementById("wrongWordsArea").style.display = "block";

				if(value.indexOf(",") != -1){
					var valueArr = value.split(",");
					
					var e1 = document.createElement("Label");
			      	e1.setAttribute("for","wrongWordsArea");
			      	e1.innerHTML = "Error Words";
			      	document.getElementById("wrongWordsArea").appendChild(e1);
			      	
					var ul=document.createElement('ul');
					ul.setAttribute("id", "header");
					var li1=document.createElement('li');
					var li2=document.createElement('li');
					li1.innerHTML = "Error Words";
					li2.innerHTML = "Suggestions";
					ul.appendChild(li1);
					ul.appendChild(li2);
					document.getElementById("wrongWordsArea").appendChild(ul);
					
					for(var i = 0; i<valueArr.length; i++){
						var suggestion = ''+data[valueArr[i]];
						console.log(''+valueArr[i]+' suggested words :: '+suggestion);
						
						var ul=document.createElement('ul');
						var li1=document.createElement('li');
						var li2=document.createElement('li');
						var anchorEle = document.createElement('a');
						anchorEle.innerHTML = valueArr[i];
						li1.appendChild(anchorEle);
						li2.innerHTML = suggestion;
						ul.appendChild(li1);
						ul.appendChild(li2);
						document.getElementById("wrongWordsArea").appendChild(ul);
					}
					 
				} else if(value.length > 0){
					var e1 = document.createElement("Label");
			      	e1.setAttribute("for","wrongWordsArea");
			      	e1.innerHTML = "Error Words";
			      	document.getElementById("wrongWordsArea").appendChild(e1);
			      	
					var ul=document.createElement('ul');
					ul.setAttribute("id", "header");
					var li1=document.createElement('li');
					var li2=document.createElement('li');
					li1.innerHTML = "Error Words";
					li2.innerHTML = "Suggestions";
					ul.appendChild(li1);
					ul.appendChild(li2);
					document.getElementById("wrongWordsArea").appendChild(ul);
					
					var ul=document.createElement('ul');
					var li1=document.createElement('li');
					var li2=document.createElement('li');
					var anchorEle = document.createElement('a');
					anchorEle.innerHTML = value;
					li1.appendChild(anchorEle);
					li2.innerHTML = data[value];
					ul.appendChild(li1);
					ul.appendChild(li2);
					document.getElementById("wrongWordsArea").appendChild(ul);
					
				} else{
					console.log('No misspelled words');
					
					var ul=document.createElement('ul');
					ul.setAttribute("id", "header");
					var li1=document.createElement('li');
					li1.innerHTML = "No Misspelled Words!!";
					ul.appendChild(li1);
					document.getElementById("wrongWordsArea").appendChild(ul);
				}
			}
			
			$('#wrongWordsArea ul li:first-child a').on("click", function(){
				var text=$(this).html();
				addToTrie(text);	
			});
		}
    });
}


function AutoSuggest(){
	var word = document.getElementById('searchBox1').value;
	if(word.length > 0){
		var option = '2';
		$.ajax({
	        type: "POST",
	        url: "SpellCheck",
	        data: { 
	        	suggestion: word,
	        	option: option,
	        	access: 't'
	        },
			success: function(data){
				if(data != null){
					var value;
					var $state = $("#searchBox2");
					$state.empty();
					Object.keys(data).forEach(function(key) {
					    value = data[key];
					    console.log(value);
					    $('<li>').val(key).text(value).appendTo($state);
					});
				}
			} 
	    });
	} else{
		var $state = $("#searchBox2");
		$state.empty();
	}
}

function addToTrie(word){
	var option = '3';
	var url_string = window.location.href;
	var url = new URL(url_string);
	var user = url.searchParams.get("username");
	var pass = url.searchParams.get("password");
	
	$.ajax({
        type: "POST",
        url: "SpellCheck",
        data: { 
        	word: word,
        	option: option,
        	user: user,
        	pass: pass
        },
		success: function(data){
			if(data != null){
				if(data == "Success"){
					var element = document.getElementById("addMsg");
					element.classList.remove("err");
					element.classList.add("success");
					document.getElementById("trieAdd").innerHTML = word+"  Successfully added!!";
				} else if(data == "Exists"){
					var element = document.getElementById("addMsg");
					element.classList.remove("success");
					element.classList.add("err");
					document.getElementById("trieAdd").innerHTML = "Already Exists!!";
				} else if(data == "No Access"){
					var element = document.getElementById("addMsg");
					element.classList.remove("success");
					element.classList.add("err");
					document.getElementById("trieAdd").innerHTML = "You don't have access. Check with your administrator!!";
				} else{
					var element = document.getElementById("addMsg");
					element.classList.remove("success");
					element.classList.add("err");
					document.getElementById("trieAdd").innerHTML = "Error try again after sometime!!";
				}
				console.log(data);
			}
		},
        error: function(data){
        	var element = document.getElementById("addMsg");
			element.classList.remove("success");
			element.classList.add("err");
			document.getElementById("trieAdd").innerHTML = "Error try again after sometime!!";
        }
    });
}

function deleteFromTrie(){
	var word = document.getElementById('deleteBox').value;
	var option = '4';
	var url_string = window.location.href;
	var url = new URL(url_string);
	var user = url.searchParams.get("username");
	var pass = url.searchParams.get("password");
	
	$.ajax({
        type: "POST",
        url: "SpellCheck",
        data: { 
        	word: word,
        	option: option,
        	user: user,
        	pass: pass
        },
		success: function(data){
			if(data != null){
				if(data == "Success"){
					var element = document.getElementById("removeMsg");
					element.classList.remove("err");
					element.classList.add("success");
					document.getElementById("trieDelete").innerHTML = word+"  Successfully removed!!";
				} else if(data == "NotExist"){
					var element = document.getElementById("removeMsg");
					element.classList.remove("success");
					element.classList.add("err");
					document.getElementById("trieDelete").innerHTML = "Doesn't Exists!!";
				} else if(data == "No Access"){
					var element = document.getElementById("removeMsg");
					element.classList.remove("success");
					element.classList.add("err");
					document.getElementById("trieDelete").innerHTML = "You don't have access. Check with your teacher!!";
				} else{
					var element = document.getElementById("removeMsg");
					element.classList.remove("success");
					element.classList.add("err");
					document.getElementById("trieDelete").innerHTML = "Error try again after sometime!!";
				}
				console.log(data);
			}
		},
        error: function(data){
        	var element = document.getElementById("removeMsg");
			element.classList.remove("success");
			element.classList.add("err");
			document.getElementById("trieDelete").innerHTML = "Error try again after sometime!!";
        }
    });
}