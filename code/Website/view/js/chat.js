/*
*   Chat related scripts are implemented in this file
*
*
*/
(function(window,undefined){
	//chat object
	var chat = function(){

	};
	var userListInterval = 3000;
	var chatInterval = 2000;
	//function to update chat panel, which displays current logged in users
	chat.updateChatPanel = function (){
		
		var site = new Site();
		site.getUserList(chat.updateUsers);
		
	};
	//call back function to handle to update user list in the chat panel
	chat.updateUsers = function (data){

			var list = $("#chat_content").find("ul");
			var userList = $("#online_users").find("ul");
			list.find('.users').remove();
			userList.find('.users').remove();
			if(data.data){
				
				$.each(data.data,function(index){
					list.append(createListItem(this));
					userList.append(createListItem(this));
				});
				//update chat panel every 20 secs
				//setTimeout(chat.updateChatPanel,userListInterval);
			}else if(data.error){
			
				if(data.error.isLoginRequired){
					showLoginModal();
				}			
			}
	};
	chat.initializeChat = function(response){
		
		if(!response.error){
			//get the chat window id from the response
			var chatWindowId = response.data.chatWindowId;
		
			//get the chat window from the dom
			var chatWindow = document.getElementById(chatWindowId);
				chatWindow = $(chatWindow);//get jquery object
			chatWindow.find("form").attr(response.data);
			chatWindow.find(".chat_messages").attr("id",response.data.chat_id);
			var data = {"message":chatWindow.find('.message').val(),"chatid":response.data.chat_id};
			var site = new Site();
			site.sendMessage(data,chat.updateUserMessage);
		}else if(response.error){
			
				if(response.error.isLoginRequired){
					showLoginModal();
				}			
		}
	};
	//get messages for the user 
	chat.getMessages = function(){
	
		var site = new Site();
		site.getMessages(chat.updateMessages);
	}
	//update user entered message 
	chat.updateMessages = function(response){
		if( !response.error && response.data){
				//get the chat window from the dom
				//updateChatPanel(response.data);
				console.log(response);
				$.each(response.data,function(){
					
					var chatMessageWindow = document.getElementById(this.chat_id);
					//see if chat message window doesn't exist then check for chat window
					if(!chatMessageWindow){
						//see if the chat already exists
						var chatWindow = document.getElementById(this.user_id);
						if(!chatWindow){
								var name = this.user_name;
								chatWindow = createChatWindow(this.user_id,name);
								chatWindow.find("form").attr(this);
								chatWindow.find(".chat_messages").attr("id",this.chat_id);
							
						}
						else{
							$(chatWindow).removeClass('hide');
						}
					}
					updateChatPanel(this,true);
				});
				setTimeout(chat.getMessages,chatInterval);
		}else if(response.error){
			if(response.error.isLoginRequired){
				showLoginModal();
			}			
		}
	};
	//update user entered message 
	chat.updateUserMessage = function(response){
		if( !response.error && response.data){
			//get the chat window from the dom
				updateChatPanel(response.data,false);
				console.log(response);
		}else if(response.error){
			if(response.error.isLoginRequired){
				showLoginModal();
			}			
		}
	};
	/*
	*	chatMessageHandler - Handles the form submit in all the chat window
	*
	*/
	chat.chatMessageHandler = function(form){
			chatId = form.getAttribute("chat_id");
					
			if(chatId){
				
				var data = {"message":$(form).find('.message').val(),"chatid":chatId};
				var site = new Site();
				site.sendMessage(data,chat.updateUserMessage);
				//clear the message after sending the message
				$(form).find('.message').val("");
			}
			else{
			
				//get the form jquery object reference,instead of creating jquery object each time
				var formObj= $(form);
					
				var site = new Site();
				var data = {"partnerId":userId};
					
				site.initChat(data,chat.initializeChat);
			}	
			//return false to prevent form posting
			return false;
	}
	
	function updateChatPanel(data,received){
	
		var chatId = data.chat_id ? data.chat_id : data.chatId;
		var chatWindow = document.getElementById(chatId);
		if(chatWindow){
		
			if(!document.getElementById(data.message_id)){
				var messagePanel = $("#chat_message_template").clone().removeClass('hide');
					messagePanel.attr("id",	data.message_id);
					messagePanel.find('.sender').text(data.user_name);
					if(received){
						messagePanel.find('.sender').addClass('pull-right');
						messagePanel.find('.message').addClass('clear');
					}
					messagePanel.find('.message').text(data.message);
					messagePanel.find('.timeStamp').text(data.timestamp);
					var chatWindow = $(chatWindow);
					//make the chat window visible
					$(chatWindow).parent().removeClass('hide');
					//append message panel to the chat window
					messagePanel.appendTo($(chatWindow));
					//scroll to the latest message
					messagePanel.parent().scrollTop(messagePanel.parent()[0].scrollHeight - messagePanel.parent()[0].clientHeight);
				}
		}
	}
	//private methods
	//Creat list item will create a single li it with users name and online status
	function createListItem(item){

		var link = document.createElement('a');
			link.href = "#";
			link.value = item.name;

			if(document.all){
				link.innerText = item.name;
			}
			else{
				link.textContent = item.name;
			}
			link.setAttribute("userId",item.id_users);
			//add start chat event listener to the list object
			if (link.addEventListener) {
					link.addEventListener('click', function(){startChat(this)}, false); 
			} else if (link.attachEvent)  {
					link.attachEvent('onclick', function(){startChat(this)});
			}
		var span = document.createElement('span');
			span.className = item.online_status;
		var li = document.createElement('li');
			li.className = "users";
			li.appendChild(span);
			li.appendChild(link);
			
		return li;
	};
	
	function startChat(item){
	
		userId = item.getAttribute("userId");
		//see if the chat already exists
		var chatWindow = document.getElementById(userId);
		if(!chatWindow){
				var name = $(item).val()
				chatWindow = createChatWindow(userId,name);
			
		}else{
			$(chatWindow).removeClass('hide');
		}
		//prevent event bubbling
		if(event){
			if(event.cancelBubble) event.cancelBubble = true;
			if (event.stopPropagation) event.stopPropagation();
		}
		return false;
	}
	
	//Function to create chat window
	function createChatWindow(userId,name){
	
			chatWindow = $('#chat_template').clone().attr('id',userId).removeClass("hide").prependTo('#chat_area');
			chatWindow.find("#chatName").text(name);
			chatWindow.find(".chat_header").toggle(function() {
							  $(this).parent().animate({
													height: "20"
												}, 500, function() {
													console.log(this);
												});
							}, function() {
							   $(this).parent().animate({
													height: "250"
												}, 500, function() {
													// Animation complete.
												});
							}).find('.closeButton').click(function(event){
							
								 $(this).parentsUntil('.chatbox').parent().parent(".chatbox:first").addClass('hide');
								 //prevent event bubbling
								if(event){
									if(event.cancelBubble) event.cancelBubble = true;
									if (event.stopPropagation) event.stopPropagation();
								}
							});
					//Attach form submit event to chat form 
					chatWindow.find("form").submit(function(){
														return chat.chatMessageHandler(this);
													});
		return chatWindow;
	}
	
	window.chat = chat;
})(window);