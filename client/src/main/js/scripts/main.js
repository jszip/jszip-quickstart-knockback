require.config({
    baseUrl: 'scripts/',
    shim:{
        "underscore":{
            exports:"_"
        },
        "backbone":{
            'deps':['underscore', 'jquery'],
            exports:"Backbone"
        }
    }
});
require(["backbone", "jquery", "underscore", "knockout", "knockback", "domReady", "hogan", "text!template.html"],
        function (Backbone, $, _, ko, kb, domready, Hogan, template) {
            //model
            var PersonModel = Backbone.Model.extend({ urlRoot:'/api/persons' });

            //viewmodel
            var PersonViewModel = function (person) {

                this.firstName = kb.observable(person, 'firstName');
                this.lastName = kb.observable(person, 'lastName');
                this.fullName = ko.computed((function () {
                    return "" + (this.firstName()) + " " + (this.lastName());
                }), this);
            };

            //model
            var PersonsModel = Backbone.Collection.extend({ model:PersonModel, url:'/api/persons' });

            //viewmodel
            var PersonsViewModel = function (persons) {
                this.persons = kb.collectionObservable(persons)
            };

            var compiledTemplate = Hogan.compile(template);
            var templateContext = { appName:"QuickStart Knockback"};
            var templatePartials = {};
            domready(function () {
                $('body').append(compiledTemplate.render(templateContext, templatePartials));

                var model = new PersonModel({ id:1 });
                var personViewModel = new PersonViewModel(model);
                model.fetch().done(function () {
                    //binding
                    ko.applyBindings(personViewModel, $('#kb_observable')[0]);
                });


                var personsModel = new PersonsModel();
                var personsViewModel = new PersonsViewModel(personsModel);
                personsModel.fetch().done(function () {
                    //binding
                    ko.applyBindings(personsViewModel, $('#kb_collection_observable')[0]);
                });
            });

        });
