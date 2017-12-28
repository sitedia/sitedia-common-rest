var myApp = angular.module('myApp', ['ng-admin']);

// declare a function to run when the module bootstraps (during the 'config' phase)
myApp.config(['NgAdminConfigurationProvider', function (nga) {
    // create an admin application
    var admin = nga.application('My Todo list').baseApiUrl('/api/v1.0/');

    // Users
    var userEntity = nga.entity('users');
    userEntity.listView()
    .title('Utilisateurs :')
    .sortField('lastName')
    .sortDir('ASC')
    .perPage(15)
    .filters([nga.field('lastName').label('Nom'), nga.field('firstName').label('Prénom'), nga.field('role').label('Rôle')])
    .fields([
    	nga.field('lastName').label('Nom').validation({ required: true }).isDetailLink(true).detailLinkRoute('show'),
    	nga.field('firstName').label("Prénom").validation({ required: true }),
    	nga.field('dateOfBirth', 'date').label('Date de naissance'),
    	nga.field('role', 'choice').label('Rôle').validation({ required: true }).choices([{ value: 'ROLE_USER', label: 'Utilisateur' }, { value: 'ROLE_ADMIN', label: 'Administrateur' }]),
        nga.field('mail', 'email').label('Adresse mail').validation({ required: true }),
    ]);
    
    userEntity.showView()
    .title('Fiche utilisateur :')
    .fields([
    	nga.field('id'),
    	nga.field('lastName').label('Nom').validation({ required: true }),
    	nga.field('firstName').label("Prénom").validation({ required: true }),
    	nga.field('dateOfBirth', 'date').label('Date de naissance'),
    	nga.field('role', 'choice').label('Rôle').validation({ required: true }).choices([{ value: 'ROLE_USER', label: 'Utilisateur' }, { value: 'ROLE_ADMIN', label: 'Administrateur' }]),
        nga.field('mail', 'email').label('Adresse mail').validation({ required: true }),
    ]);

    userEntity.creationView()
    .title('Ajouter un utilisateur :')
    .fields([
    	nga.field('lastName').label('Nom').validation({ required: true }),
    	nga.field('firstName').label("Prénom").validation({ required: true }),
    	nga.field('dateOfBirth', 'date').label('Date de naissance'),
    	nga.field('role', 'choice').label('Rôle').validation({ required: true }).choices([{ value: 'ROLE_USER', label: 'Utilisateur' }, { value: 'ROLE_ADMIN', label: 'Administrateur' }]),
        nga.field('mail', 'email').label('Adresse mail').validation({ required: true }),
        nga.field('password', 'password').label('Mot de passe').validation({ required: true }),
    ])
    .prepare((entry) => {
        entry.values.userId = localStorage.getItem('userId');
    });
    
    userEntity.editionView()
    .title('Modifier la fiche utilisateur :')
    .fields([
    	nga.field('lastName').label('Nom').validation({ required: true }),
    	nga.field('firstName').label("Prénom").validation({ required: true }),
    	nga.field('dateOfBirth', 'date').label('Date de naissance'),
    	nga.field('role', 'choice').label('Rôle').validation({ required: true }).choices([{ value: 'ROLE_USER', label: 'Utilisateur' }, { value: 'ROLE_ADMIN', label: 'Administrateur' }]),
        nga.field('mail', 'email').label('Adresse mail').validation({ required: true }),
        nga.field('password', 'password').label('Mot de passe'),
    ]);
    
    userEntity.deletionView()
    .title("Supprimer l'utilisateur '{{ entry.values.firstName }} {{ entry.values.lastName }}' :")

    admin.addEntity(userEntity);

    // Categories
    var categoryEntity = nga.entity('categories');
    categoryEntity.listView()
    .title('Mes catégories :')
    .sortField('name')
    .sortDir('ASC')
    .perPage(15)
    .permanentFilters({ "userId" : localStorage.getItem('userId') })
    .filters([nga.field('name').label('Nom')])
    .fields([
        nga.field('name').isDetailLink(true).detailLinkRoute('show'),
    ]);
    
    categoryEntity.showView()
    .title('Détails de la catégorie :')
    .fields([
    	nga.field('id'),
        nga.field('name'),
    ]);

    categoryEntity.creationView()
    .title('Ajouter une catégorie :')
    .fields([
        nga.field('name')
    ])
    .prepare((entry) => {
        entry.values.userId = localStorage.getItem('userId');
    });
    
    categoryEntity.editionView()
    .title('Modifier la catégorie :')
    .fields([
        nga.field('name'),
    ]);
    
    categoryEntity.deletionView()
    .title("Supprimer la catégorie '{{ entry.values.name }}' :")

    admin.addEntity(categoryEntity);
   
    // Todos
    var todoEntity = nga.entity('todos');
    todoEntity.listView()
    .title('Liste des tâches :')
    .sortField('title')
    .sortDir('ASC')
    .perPage(15)
    .permanentFilters({ "userId" : localStorage.getItem('userId') })
    .filters([
    	nga.field('title').label('Titre'),
        nga.field('categoryId', 'reference').label("Catégories").targetEntity(admin.getEntity('categories')).targetField(nga.field('name')).permanentFilters({ 'userId' : localStorage.getItem('userId') }).sortField('name'),
    ])
    .fields([
        nga.field('title').label("Titre").isDetailLink(true).detailLinkRoute('show'),
        nga.field('categories', 'reference_many').label("Catégories").targetEntity(admin.getEntity('categories')).targetField(nga.field('name')).permanentFilters({ 'userId' : localStorage.getItem('userId') }).sortField('name'),
    ]);
    
    todoEntity.showView()
    .title('Détail de la tâche :')
    .fields([
    	nga.field('id').label("Identifiant"),
        nga.field('title').label("Titre"),
        nga.field('categories', 'reference_many').label("Catégories").targetEntity(admin.getEntity('categories')).targetField(nga.field('name')).permanentFilters({ 'userId' : localStorage.getItem('userId') }).sortField('name'),
        nga.field('description', 'wysiwyg').label("Description"),
    ]);

    todoEntity.creationView()
    .title('Ajouter une tâche :')
    .fields([
        nga.field('title').label("Titre"),
        nga.field('categories', 'reference_many').label("Catégories").targetEntity(admin.getEntity('categories')).targetField(nga.field('name')).permanentFilters({ 'userId' : localStorage.getItem('userId') }).sortField('name'),
        nga.field('description', 'wysiwyg').label("Description"),
    ])
    .prepare((entry) => {
        entry.values.userId = localStorage.getItem('userId');
    });
    
    todoEntity.editionView()
    .title('Modifier la tâche :')
    .fields([
        nga.field('title').label("Titre"),
        nga.field('categories', 'reference_many').label("Catégories").targetEntity(admin.getEntity('categories')).targetField(nga.field('name')).permanentFilters({ 'userId' : localStorage.getItem('userId') }).sortField('name'),
        nga.field('description', 'wysiwyg').label("Description"),
    ]);
    
    todoEntity.deletionView()
    .title("Supprimer la tâche '{{ entry.values.title }}' :")

    admin.addEntity(todoEntity);
    
    
    
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
