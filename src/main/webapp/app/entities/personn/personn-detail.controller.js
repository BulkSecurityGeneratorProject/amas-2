(function() {
    'use strict';

    angular
        .module('amas2App')
        .controller('PersonnDetailController', PersonnDetailController);

    PersonnDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Personn'];

    function PersonnDetailController($scope, $rootScope, $stateParams, previousState, entity, Personn) {
        var vm = this;

        vm.personn = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('amas2App:personnUpdate', function(event, result) {
            vm.personn = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
