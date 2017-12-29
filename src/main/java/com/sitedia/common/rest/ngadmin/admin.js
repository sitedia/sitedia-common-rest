var myApp = angular.module('myApp', ['ng-admin']);

// declare a function to run when the module bootstraps (during the 'config' phase)
myApp.config(['NgAdminConfigurationProvider', function (nga) {
    // create an admin application
    var admin = nga.application('My Todo list').baseApiUrl('/api/v1.0/');
	
	admin.dashboard(nga.dashboard()
	    .template("")
	);

    
    
    nga.configure(admin);
}]);


myApp.config(function(RestangularProvider) {
    RestangularProvider.setDefaultHeaders({
        'Content-Type' : 'application/json'
    });
    RestangularProvider.setDefaultHttpFields({
    	'withCredentials':'true'
    });
    
    RestangularProvider.setErrorInterceptor(function(response,
        deferred, responseHandler) {
        if (response.status === 401) {
            if ((window.location+'').indexOf('login.html') < 0) {
                window.location = 'login.html';
                response.message = "Vous n'avez pas les droits nécessaires pour accéder à cette page";
                return true;
            }
            response.message = "Authentification requise :";
            return true;
        } else {
        	if (response.data.errors !=null ) {
        		response.data.message = "<ul>";
        		for (var i in response.data.errors) {
        			response.data.message += "<li>" + response.data.errors[i] + "</li>";
        		}
        		response.data.message += "<ul>";
        	} else {
        		response.data.message = response.data.description;
        	}
            return true;
        }
    });
    
    RestangularProvider.addFullRequestInterceptor(function(element, operation, what, url, headers, params, httpConfig) {
        if (operation == 'getList') {
            if (params._filters) {
                for (var filter in params._filters) {
                    params[filter] = params._filters[filter];
                }
                delete params._filters;
            }
        }
        return { params: params };
    });

});

myApp.controller('LoginController', function($http, $scope, Restangular, $location, $window, notification) {
    $http.defaults.withCredentials = true;
    
    $scope.login = function(e) {
        if (e) {
            e.preventDefault();
        }
        var form = "username=" + $scope.username  + "&password=" + $scope.password;
        $http({url: "/login.html",
        		method : 'POST',
        		data: form,
        		withCredentials : true,
        		headers : {
            'Content-Type' : 'application/x-www-form-urlencoded'
        }}).then(function(data) {
        	if (data.status == 200) {
	            $http.get('/api/v1.0/sessions/userInfo')
	            .then(function(response) {
	            	localStorage.setItem('userId', response.data.id);
	                $window.location = "ui.html";
	                return false;
	                
	            }).error(function(data, status, headers) {
	                console.log(response)
	            });
        	} else {
        		notification.log(`Votre adresse mail ou votre mot de passe est incorrect.`, { addnCls: 'humane-flatty-error' });
        	}

        }, function() {
             notification.log(`Votre adresse mail ou votre mot de passe est incorrect.`, { addnCls: 'humane-flatty-error' });

        });
    }
});


myApp.config(['$translateProvider', function ($translateProvider) {
    $translateProvider.translations('en', {
        'STATE_FORBIDDEN_ERROR' : '{{ message }}'
    });
}]);
