var AppSpyConfig = function(obj){

    this.params = {};

    this.params = $.extend(this.params, obj);
    var currentObject = this;    

    AppSpyConfig.prototype.bindElementsAndInitialize = function(){

        $(document).on("click","#data_monitoring",function(){
            if($("#data_monitoring:checked").length > 0){
                $("#data_monitoring_data").show();
            }else{
                $("#data_monitoring_data").hide();
            }   
        });

        $(document).on("click","#responseCodeCheck",function(){
            if($("#responseCodeCheck:checked").length > 0){
                $("#resp_code").show();
            }else{
                $("#resp_code").hide();
            }   
        });

        $(document).on("click","#responseTimeCheck",function(){
            if($("#responseTimeCheck:checked").length > 0){
                $("#resp_time").show();
            }else{
                $("#resp_time").hide();
            }   
        });

        $(document).on("change","#serviceList",function(){
            currentObject.serviceFormData();
        });

        $(document).on("click", "#serviceAddBtn", function(){
            currentObject.submitButtonClick();
        });

        $(document).on("click","#postRequestCheck",function(){
            if($("#postRequestCheck:checked").length > 0){
                $("#post_request").show();
                $("#addMorePost").show();
            }else{
                $("#post_request").hide();
                $("#addMorePost").hide();
            }   
        }); 

        $(document).on("click","#addMorePost",function(){
            $html = '<br /><br />key : <input type="text" name="postKey[]"> Value : <input type="text" name="postValue[]">';
            $("#post_request").append($html);
        });

        $(document).on("click","img.deleteService",function(){
            currentObject.deleteService($(this));
        });

        $(document).on("click","img.viewService",function(){
            currentObject.viewService($(this));
        });

        $(document).on("click","img.closeService",function(){
            hideServicesDataLayer();
        });

        

        $("#serviceList").val("-1");
        
    }

    AppSpyConfig.prototype.sendAjaxRequest = function(ajaxURL,ajaxData,isPost,isAsync,successCallback){

       var typeOfRequest = "GET";
       if(typeof(isPost) != "undefined" && isPost == true){
          typeOfRequest = "POST";
       }
       if(typeof(isAsync) == "undefined"){
          isAsync = true;
       }

        $.ajax({
            type: typeOfRequest,
            async: isAsync,
            data: ajaxData,
            url : ajaxURL,  
            success : function(response){

                if(typeof successCallback == "function"){
                    successCallback(response);  
                }
                
            }
        });
    }

    AppSpyConfig.prototype.serviceFormData = function(){
        $service = $("#serviceList").val();
        /*if($service != "-1"){
            $("#serviceAddBtn").hide();
        }else{
            $("#serviceAddBtn").show();
        }*/
        ajaxURL = base_url+"/Configure/formGeneratorForService/"+$service;
        currentObject.sendAjaxRequest(ajaxURL,{},false,false,currentObject.showForm);
    }

    AppSpyConfig.prototype.showForm = function(response){
        $("#serviceData").html(response);
    }

    AppSpyConfig.prototype.submitButtonClick = function(response){
        showLoader();
        // CHeck whether service name is selected
        var serviceName = $("#serviceList").val();

        if(serviceName == "-1"){
            $("#serviceList_error").html("Please Select the service Name");
            hideLoader();
            return;
        }

        // Check defualt form values using form params
        var result = currentObject.validateServiceForm("serviceAddForm");
        if(result == true){
            currentObject.addServiceDataToDB("serviceAddForm");
        }else{
            hideLoader();
        }
    }

    AppSpyConfig.prototype.addServiceDataToDB = function(formId){

        var form = document.getElementById(formId);
        var ajaxData = {};
        for(var i=0; i < form.elements.length; i++){
            var e = form.elements[i];
            var eJQ = $(e);
            var id = eJQ.attr('name');
            var value = eJQ.val();
            if ( eJQ.is("input") || eJQ.is("select") || eJQ.is("textarea")){
                if(eJQ.is("input:checkbox")){
                    value = eJQ.prop("checked");
                }
                if(id.indexOf("[]") != -1){
                    
                    if(typeof ajaxData[id] == "undefined"){
                        ajaxData[id] = [];
                    }
                    ajaxData[id].push(value);
                }else{
                    ajaxData[id] = value;
                }
            }
        }
        
        ajaxURL = base_url+"/Configure/addServiceDataToDB";
        currentObject.sendAjaxRequest(ajaxURL, ajaxData, true, true, currentObject.addServicesCallback);
    }

    AppSpyConfig.prototype.addServicesCallback = function(response){
        response = JSON.parse(response);
        if(typeof response.error != "undefined"){
            errorMessage = response.msg;
            $(".post_error").html(errorMessage);
        }else{
            $(".post_error").html("Service Added.. Updating service list...");
            $("#right_col").html("Updating Service List. Please wait");
            currentObject.resetForm("serviceAddForm");
            setTimeout(function(){
               currentObject.updateServiceList();
               $(".post_error").html("");
            },500);
            
        }
        hideLoader();
    }

    AppSpyConfig.prototype.updateServiceList = function(){
        ajaxURL = base_url+"/Configure/fetchListOfServices";
        currentObject.sendAjaxRequest(ajaxURL,{},false,false,currentObject.updateServiceListCallback);
    };

    AppSpyConfig.prototype.updateServiceListCallback = function(response){
        $("#right_col").html(response);
    };



    AppSpyConfig.prototype.validateServiceForm = function(formId){
        $(".error").html("");

        var finalResult = true;
        $("#"+formId+" input,textarea").each(function(index,item){
            var itemJQ = $(item);
            var validateType = $(itemJQ).attr("validate");
            var id = $(itemJQ).attr('id');
            var isVisible = $(item).is(":visible");        
            if(typeof validateType != "undefined" && isVisible){
                var r = window[validateType](itemJQ);    
                if(r != true){
                    $("#"+id+"_error").html(r);
                    return finalResult = false;
                }
            }
        });
        return finalResult;
    }

    AppSpyConfig.prototype.resetForm = function(formId){
        $("#"+formId+" input,textarea").each(function(index,item){
            var itemJQ = $(item);
            if(itemJQ.is("input:checkbox")){
                itemJQ.prop("checked",false).trigger("click");
            }else{
                itemJQ.val("");
            }
        });

        $("#"+formId+" select").each(function(index,item){
            var itemJQ = $(item);
            $(itemJQ).val("-1").trigger("change");
        });

    }

    AppSpyConfig.prototype.fetchServicesList = function(){
        showLoader();
        currentObject.sendAjaxRequest(base_url+"/Configure/fetchListOfServices",{},false,false,currentObject.fetchServicesListCallback)
    };

    AppSpyConfig.prototype.deleteService = function(obj){

        var serviceid = obj.attr('serviceId');

        if (confirm("Proceed to delete service.") == true) {
            if(serviceid == "undefined" || serviceid == ""){
                return;
            }
            ajaxURL = base_url+"/Configure/deleteService";
            currentObject.sendAjaxRequest(ajaxURL,{'id' : serviceid},true,true,currentObject.deleteServiceCallback);  
        } 
    };


   AppSpyConfig.prototype.deleteServiceCallback = function(response){
        
        if($.trim(response) == "success"){
            $("#right_col").html("Deletion success.. Updating list...");
        }else{
            $("#right_col").html("Deletion failure.. Updating list...");
        }
        setTimeout(function(){
            currentObject.updateServiceList();
        },300);
        
   };

    AppSpyConfig.prototype.viewService = function(obj){

        var serviceid = obj.attr('serviceId');

        ajaxURL = base_url+"/Configure/viewService/"+serviceid;
        currentObject.sendAjaxRequest(ajaxURL,{},true,true,currentObject.viewServiceCallback);  
        
    };

    AppSpyConfig.prototype.viewServiceCallback = function(response){
        if(response == "error"){
            alert("Some error occured");
        }else{
            showServicesDataLayer();
            $(".overlayMidContentDynamic").html(response);
        }
    };

    
}

function validateStr(fieldObject){

        var str = $.trim($(fieldObject).val());
        var r = validateRequired(fieldObject);
        caption = $(fieldObject).attr("caption");        
        if(!r){
            return "Please fill the "+caption;
        }

        str = str.replace(/[^\x20-\x7E]/g,'');
        var strArray = str.split(" ");
        for(var strArrayCount = 0; strArrayCount < strArray.length; strArrayCount++) {
            if((strArray[strArrayCount].length > 32) && (strArray[strArrayCount].search(/(http|https)?(:\/\/)/) == -1)) {
                return caption + " cannot contain any word exceeding 32 characters.";
            }
        }
        return true;
}

function validateUrl(fieldObject) {
    var url = $(fieldObject).val();
    var r = validateRequired(fieldObject);
    caption = $(fieldObject).attr("caption");        
    if(!r){
        return "Please fill the "+caption;
    }
    /*var filter = /^(([\w]+:)?\/\/)?(([\d\w]|%[a-fA-f\d]{2,2})+(:([\d\w]|%[a-fA-f\d]{2,2})+)?@)?([\d\w][-\d\w]{0,253}[\d\w]\.)+[\w]{2,4}(:[\d]+)?(\/([-+_~.\d\w!#]|%[a-fA-f\d]{2,2})*)*(\?(&?([-+_~.\d\w\[\]\|`<>:{}!\^\*\(\)\/]|%[a-fA-f\d]{2,2})=?)*)?(#([-+_~.\d\w]|%[a-fA-f\d]{2,2})*)?$/;
    if(url == '') {
        return  "Please enter the "+ caption +".";
    }else if(!filter.test(url)){
        return "Please enter "+ caption +" in correct format.";
    }*/
    return true;
}

function validateInt(fieldObject) {
        var r = validateRequired(fieldObject);
        caption = $(fieldObject).attr("caption");        
        if(!r){
            return "Please fill the "+caption;
        }
        number = $(fieldObject).val();
        if(number == "") return true;
        var filter = /^(\d)+$/;
        if(!filter.test(number)){
            return 'Please fill the '+ caption +' with correct numeric value';
        }

        var minValue = $(fieldObject).attr('minValue');
        var maxValue = $(fieldObject).attr('maxValue');
        // check min value

        if(typeof minValue != "undefined" && number < minValue){
            return "Min value for "+caption+" is "+minValue;
        }

        // check max value
        if(typeof maxValue != "undefined" && number > maxValue){
            return "Max value for "+caption+" is "+minValue;
        }



        return true;
    }

function validateRequired(fieldObject){
    var requiredFlag = $(fieldObject).attr('mandatory');
    var value = $.trim($(fieldObject).val());
    if(typeof requiredFlag == "undefined"){
        return true;
    }else if(requiredFlag == "false"){
        return true;
    }else if(requiredFlag == "true" && value == ""){
        return false;
    }
    return true;
    
}

function showLoader(){
    $(".overlay").show();
    $(".overlayMidContent").show();
}


function hideLoader(){
    $(".overlay").hide();
    $(".overlayMidContent").hide();
}


function showServicesDataLayer(){
    $(".overlay").show();
    $(".overlayMidContentDynamic").show();
}

function hideServicesDataLayer(){
    $(".overlay").hide();
    $(".overlayMidContentDynamic").hide();
}

function isJson(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}