
/* *************************************************
   Class: 
			Site										
   Description: 
			   Using the javascript prototype, you 
			   can make site classes. This allows objects to be 
			   made to perform the major common operations     
			   in the site.

   Operations:
			
			1. CreateUserAccount.
			2. Login user
			3. Make Ajax call
			4. Validate Email
			5. Validate Password
			6. Error handle

******************************************************/	
   
    
function Site(){
		this.baseURL = "http://localhost/rfid/";
		this.registerUserURL = this.baseURL + "index.php?s=login&action=register";
		this.loginURL = this.baseURL + "index.php?s=login";
		this.userListURL =  this.baseURL + "index.php?s=chat&action=getUserList";
		this.initChatURL =  this.baseURL + "index.php?s=chat&action=initChat&type=json";
		this.sendMessageURL = this.baseURL + "index.php?s=chat&action=sendMessage&type=json";
		this.getMessagesURL = this.baseURL + "index.php?s=chat&action=getMessages&type=json";
		this.getPackageURL = this.baseURL + "index.php?s=package&type=json";
		this.getLatestPackageURL = this.baseURL + "index.php?s=package&type=json&action=latest";
};
	
Site.prototype = {

	/*
	* function to create user account
	*/
	createUserAccount:function(formObj){
		
		if( this.validateRegisterForm(formObj) ){
			
			var formToProcess = "#register";
			
			 $('#ajaxProgress').removeClass('hide');
			 $(formToProcess).addClass('hide');
			
			 this.makeAjaxCall(getLoginData(formToProcess,formObj,this.registerUserURL));
		}
	},
	//function to login the user
	login:function(formObj){
		
		if( this.validateLogin(formObj) ){
			
			var formToProcess = "#login";
			
			 $('#ajaxProgress').removeClass('hide');
			 $(formToProcess).addClass('hide');
			
			 this.makeAjaxCall(getLoginData(formToProcess,formObj,this.loginURL));
		}
	},
	//validate to validate login form
	validateLogin:function(formObj){
		
		var isValid = false;
		
		//get the form input values
		var email    = formObj.find('.email').val();
		var password = formObj.find('.password').val();
		
		//validate email
		if(this.validateEmail(email)){

			//validate password
			if(this.validatePassword(password)){
				isValid = true;//validation successfull
				//restore the error messages
				formObj.find('.password').removeClass('input-error').next().addClass('hide');
				formObj.find('.email').removeClass('input-error').next().addClass('hide');	
			}
			else{
				//show error messages
				formObj.find('.email').removeClass('input-error').next().addClass('hide');
				formObj.find('.password').addClass('input-error').next().removeClass('hide');				
			}		
		}
		else{
			formObj.find('.email').addClass('input-error').next().removeClass('hide');
		}

		return isValid;
	},
	//function to validate user register form
	validateRegisterForm:function(formObj){
		
		var isValid = false;
		//get the form input values
		var name     = formObj.find('.name').val()
		var email    = formObj.find('.email').val();
		var password = formObj.find('.password').val();

		
		//validate name
		if(this.validateAlphaString(name)){
			
			//validate email
			if(this.validateEmail(email)){
			
				//validate password
				if(this.validatePassword(password)){
					isValid = true;//validation successfull
					//restore the error messages
					formObj.find('.password').removeClass('input-error').next().addClass('hide');	
					formObj.find('.name').removeClass('input-error').next().addClass('hide');
					formObj.find('.email').removeClass('input-error').next().addClass('hide');	
				}
				else{
					//show error messages
					formObj.find('.name').removeClass('input-error').next().addClass('hide');
					formObj.find('.email').removeClass('input-error').next().addClass('hide');
					formObj.find('.password').addClass('input-error').next().removeClass('hide');				
				}		
			}
			else{
				formObj.find('.name').removeClass('input-error').next().addClass('hide');
				formObj.find('.email').addClass('input-error').next().removeClass('hide');
			}
		}
		else{
			formObj.find('.name').addClass('input-error').next().removeClass('hide');
		}

		return isValid;
	},
	//validate the alpha string
	validateAlphaString:function(name){

		var alphaReg = /^[A-Za-z]+\s*[A-Za-z]+$/
		
		return alphaReg.test(name);
	},
	//validate the site password
	validatePassword:function(password){

		 var isValid = false;
		 
		if(password != '' && password.length >= 8){
			
			isValid = true;
		}
		
		return isValid;
	},
	//function to validate email address
	validateEmail: function(email){

		var emailReq = /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;

		return emailReq.test(email);
	},
	//get user list execpt for online users
	getUserList:function(callBack){
		this.makeAjaxCall({ type: "GET",
							   path: this.userListURL,
								 success:callBack});
	},
	//get Package Details
	getPackageDetail: function(callBack){
		this.makeAjaxCall({ type: "GET",
							   path: this.getPackageURL,
								 success:callBack});
	},
	getLatestPackage : function(callBack){
		this.makeAjaxCall({ type: "GET",
							   path: this.getLatestPackageURL,
								 success:callBack});
		
	},
	//initalize chat window
	initChat:function(data,callBack){
	
		this.makeAjaxCall({ type: "POST",
							path: this.initChatURL,
							message:data,
							success:callBack
						 });
		
	},
	//initalize chat window
	sendMessage:function(data,callBack){
	
		this.makeAjaxCall({ type: "POST",
							path: this.sendMessageURL,
							message:data,
							success:callBack
						 });
		
	},
	//get latest message
	getMessages:function(callBack){
		
		this.makeAjaxCall({ type: "POST",
							path: this.getMessagesURL,
							message:"",
							success:callBack
						 });
	},
	//function to make ajax call
	makeAjaxCall:function(data){
		
		var errorCallBack = (data.error) ? data.error: this.errorHandler;

		$.ajax({
			type:data.type,
			async:true,
			cache:false,
			data:data.message,
			url:data.path,
			dataType:'json',
			success:data.success,
			error:errorCallBack
		});
	},
	//function to handle error for the whole site
	errorHandler:function(jqXHR, textStatus, errorThrown){	
			//Initialize error to default message
			var errorMessage = "error";
			
			if(textStatus){
					errorMessage = textStatus;
			}
			else{
				
				errorMessage = jqXHR.statusText;
			}
			$("#error_message").text(errorMessage);
			$("#errorModal").modal('show');
	}		
}