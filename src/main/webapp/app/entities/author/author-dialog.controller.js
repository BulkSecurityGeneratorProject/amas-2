(function() {
    'use strict';

    angular
        .module('amas2App')
        .controller('AuthorDialogController', AuthorDialogController);

    AuthorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Author', 'Book'];

    function AuthorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Author, Book) {
        var vm = this;

        vm.author = entity;
        vm.clear = clear;
        vm.save = save;
        vm.books = Book.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.author.id !== null) {
                alert(vm.author.id);
                Author.update(vm.author, onSaveSuccess, onSaveError);
            } else {
                alert(vm.author.id);
                Author.save(vm.author, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('amas2App:authorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
