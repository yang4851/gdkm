gdkmApp.controller('DemoCtrl', DemoCtrl);
 
//setup  expanded and collapse icos
gdkmApp.config(function (ivhTreeviewOptionsProvider) {
    ivhTreeviewOptionsProvider.set({
        twistieCollapsedTpl: '<leaf><span class="fa fa-arrow-circle-right"></span></leaf> ',
        twistieExpandedTpl: '<leaf><span class="fa fa-arrow-circle-down"></span></leaf>',
        twistieLeafTpl: '<leaf><span class="fa fa-circle"></span></leaf>'
 
    });
});
     
DemoCtrl.$inject = ['$scope', 'taxonTreeService','$window','tree' ];
 
function DemoCtrl($scope,taxonTreeService,$window,tree ) {
  var self = this;
  self.data = [];    
 
  //Simulate parameter needed to pass to server
  var config = {
      params: {
          id: 0
      }
  }; 
    
  
   //Call server to get data
  $scope.init = function(){
	  $scope.progressBar = true;
	  taxonTreeService.get('taxonomies', config,
			  CategoryLoadCompleted,
			  CategoryLoadFailed);  
	  $scope.progressBar = false;
	  
  }
  function CategoryLoadCompleted(result) {
      if (result.status == 200) { 
          self.data = tree.genNode(result.data.list,null,true); 
      } else {
          $window.alert('Category loaded failed' + result.error);
      }  
  }
 
  function CategoryLoadFailed(result) {
    $window.alert('Category loaded failed'); 
  }
  
  self.showTree = true;
         
}