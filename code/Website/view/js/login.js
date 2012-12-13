/*
* Contains script which perform login and user registration 
* related operation
*
*/

/*
* Shows the new account register form and hides the login 
* screen
*
*/

function showRegisterForm(){
	
	$('#login').addClass('hide');
	$('#register').removeClass('hide');
	return false;
}

/*
* Shows the Login form hiding the register form
*
*/

function showLogin(){
	
	$('#login').removeClass('hide');
	$('#register').addClass('hide');
	return false;
}
/*
* Shows login modal popup with the Login form and hiding the register form
*
*/
function showLoginModal(){
	
	$('#login').removeClass('hide');
	$('#register').addClass('hide');
	$('#login_message').addClass('hide');
	$('#loginModal').modal('show');
	return false;
}
/*
* Handles the submit of register form event and do validation
* 
*/
$('#register_form').submit(function() {

	var isValid = false;
	
	//get the form jquery object reference,instead of creating jquery object each time
	var formRef = $(this);

	var site = new Site();
		
	site.createUserAccount(formRef);
	
	//return false to prevent form posting
    return false;
});

/*
* Handles the submit of login form event and do validation
* 
*/
$('#login_form').submit(function() {

	var isValid = false;
	
	//get the form jquery object reference,instead of creating jquery object each time
	var formRef = $(this);

	var site = new Site();
		
	site.login(formRef);
	
	//return false to prevent form posting
    return false;
});

function getLoginData(formToProcess,formObj, url){

			var data	=	{
							   type: "POST",
							   path: url,
							   message: formObj.serialize(),// serializes the form's elements.
							   success: function(data)
							   {
								   console.log(data); // show response from the php script.
								   $('#ajaxProgress').addClass('hide');	
								   $(formToProcess).removeClass('hide');
								   //if call success
								   if(data){
								
									   if(data.error){
											//error occured in server, display the message to the user
											$('#login_message').text(data.error).removeClass('hide');
											
									   }else{
											//on success
											$('.userName').each( function(){
																	if($(this).find("i")){
																		$(this).append(data.name);
																	}
																	else{
																		$(this).text(data.name);
																	}
																});
											$('#userName').parent().removeClass('hide');
											$('#loginButton').addClass('hide');
											$("#loginModal").modal("hide");
										
											 //start getting the messages for the user
											 chat.getMessages();
											 //start updating online users
											 chat.updateChatPanel();
										}
									}
									else{
										//error occured in server, display the message to the user
											$('#login_message').text("Server error occurred").removeClass('hide');
									}
							   },
							   error:function(jqXHR, textStatus, errorThrown)
							   {
										//what to do while error occurred making ajax call
										$('#ajaxProgress').addClass('hide');	
										$('#login_message').text(textStatus).removeClass('hide');

							   }
					};
					
			return data;

}