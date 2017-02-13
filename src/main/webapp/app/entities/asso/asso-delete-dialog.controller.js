(function() {
    'use strict';

    angular
        .module('amas2App')
        .controller('AssoDeleteController',AssoDeleteController);

    AssoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Asso'];

    function AssoDeleteController($uibModalInstance, entity, Asso) {
        var vm = this;

        vm.asso = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Asso.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
