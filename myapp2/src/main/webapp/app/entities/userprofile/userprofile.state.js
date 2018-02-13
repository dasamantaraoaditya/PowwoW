(function() {
    'use strict';

    angular
        .module('myapp2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('userprofile', {
            parent: 'entity',
            url: '/userprofile',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Userprofiles'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/userprofile/userprofiles.html',
                    controller: 'UserprofileController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('userprofile-detail', {
            parent: 'userprofile',
            url: '/userprofile/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Userprofile'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/userprofile/userprofile-detail.html',
                    controller: 'UserprofileDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Userprofile', function($stateParams, Userprofile) {
                    return Userprofile.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'userprofile',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('userprofile-detail.edit', {
            parent: 'userprofile-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/userprofile/userprofile-dialog.html',
                    controller: 'UserprofileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Userprofile', function(Userprofile) {
                            return Userprofile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('userprofile.new', {
            parent: 'userprofile',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/userprofile/userprofile-dialog.html',
                    controller: 'UserprofileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                profilepicture: null,
                                about: null,
                                nicname: null,
                                dateofbirth: null,
                                address: null,
                                contactno: null,
                                facebooklink: null,
                                googlepluslink: null,
                                linkedinlink: null,
                                twitterlink: null,
                                instagramlink: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('userprofile', null, { reload: 'userprofile' });
                }, function() {
                    $state.go('userprofile');
                });
            }]
        })
        .state('userprofile.edit', {
            parent: 'userprofile',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/userprofile/userprofile-dialog.html',
                    controller: 'UserprofileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Userprofile', function(Userprofile) {
                            return Userprofile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('userprofile', null, { reload: 'userprofile' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('userprofile.delete', {
            parent: 'userprofile',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/userprofile/userprofile-delete-dialog.html',
                    controller: 'UserprofileDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Userprofile', function(Userprofile) {
                            return Userprofile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('userprofile', null, { reload: 'userprofile' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
