(function() {
    'use strict';

    angular
        .module('amas2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('asso', {
            parent: 'entity',
            url: '/asso',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amas2App.asso.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/asso/assos.html',
                    controller: 'AssoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('asso');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('asso-detail', {
            parent: 'entity',
            url: '/asso/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amas2App.asso.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/asso/asso-detail.html',
                    controller: 'AssoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('asso');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Asso', function($stateParams, Asso) {
                    return Asso.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'asso',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('asso-detail.edit', {
            parent: 'asso-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/asso/asso-dialog.html',
                    controller: 'AssoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Asso', function(Asso) {
                            return Asso.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('asso.new', {
            parent: 'asso',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/asso/asso-dialog.html',
                    controller: 'AssoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('asso', null, { reload: 'asso' });
                }, function() {
                    $state.go('asso');
                });
            }]
        })
        .state('asso.edit', {
            parent: 'asso',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/asso/asso-dialog.html',
                    controller: 'AssoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Asso', function(Asso) {
                            return Asso.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('asso', null, { reload: 'asso' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('asso.delete', {
            parent: 'asso',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/asso/asso-delete-dialog.html',
                    controller: 'AssoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Asso', function(Asso) {
                            return Asso.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('asso', null, { reload: 'asso' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
