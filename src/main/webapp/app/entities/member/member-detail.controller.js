(function() {
    'use strict';

    angular
        .module('amas2App')
        .controller('MemberDetailController', MemberDetailController);

    MemberDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Member', 'Asso'];

    function MemberDetailController($scope, $rootScope, $stateParams, previousState, entity, Member, Asso) {
        var vm = this;

        vm.member = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('amas2App:memberUpdate', function(event, result) {
            vm.member = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
