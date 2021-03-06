(function() {
    'use strict';

    angular
        .module('amas2App')
        .controller('DepartmentMySuffixController', DepartmentMySuffixController);

    DepartmentMySuffixController.$inject = ['$scope', '$state', 'Department'];

    function DepartmentMySuffixController ($scope, $state, Department) {
        var vm = this;

        vm.departments = [];

        loadAll();

        function loadAll() {
            Department.query(function(result) {
                vm.departments = result;
                vm.searchQuery = null;
            });
        }
    }
})();
