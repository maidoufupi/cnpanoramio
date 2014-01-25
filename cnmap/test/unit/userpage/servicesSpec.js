'use strict';

/* jasmine specs for services go here */

describe('User Page Service', function() {

    beforeEach(function(){
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    beforeEach(module('userPageApp'));
    beforeEach(module('userPageServices'));

    describe('userPageServices', function(){
        var scope, ctrl, $httpBackend;
        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectGET('api/rest/photos/2.json').
                respond({photos: [{name: 'Nexus S'}, {name: 'Motorola DROID'}]});

            scope = $rootScope.$new();
            ctrl = $controller('UserPhotoCtrl', {$scope: scope});
        }));

        it('should create "phones" model with 2 phones fetched from xhr', function() {
            expect(scope.photos).toEqualData([]);
            $httpBackend.flush();

            expect(scope.photos).toEqualData(
                [{name: 'Nexus S'}, {name: 'Motorola DROID'}]);
        });
    })
});
