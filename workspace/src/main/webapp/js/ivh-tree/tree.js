     
    gdkmApp.factory('tree', tree);
    
    function tree() {
 
        var self =this;
        //label: description of node
        //hasFakechild: The reason of this parameter is use to create a dummy "loading..."
        //              node, to tell the ivh-treeview to show "Expanded" icon because we're not sure
        //              where every node has child
        var makeNode = function (label, hasFakechild) {
            var node;
            node ={
                label: label.name,
                id: label.taxonId,
                type: label.parentTaxonId,
                children: []
            };
 
            //Create dummy for speed up loading & make library show "Expand" icon
            //child nodes only load when 'leaf element click in directive
            
            if (hasFakechild) {
                node.children.push({
                    label: 'Loading...',
                    id: 0,
                    type: 'DEL',  //Use for determine whether to load from server or delete the node
                    children: []
                });
            }
 
            return node;
        };
 
        var makeChild = function (node) {
            return {
                label: label.name,
                id: label.taxonId,
                type: label.parentTaxonId,
                children: []
            };
        };
        //list: json data getting from server
        //parent: parent node
        //hasFakechild: The reason of this parameter is use to create a dummy "loading..."
        //              node, to tell the ivh-treeview to show "Expanded" icon because we're not sure
        //              where every node has child
        self.genNode =  function (list, parent, hasFakechild) {
            var node = [],   
                nodes = [];
 
             while (list !== null && list.length) {
                 
                node = makeNode(list.shift(), hasFakechild);
                
                node.selected = parent === null ? node.selected : parent.selected;
                if (parent !== null) {                    
                    if (parent.children[0].type == 'DEL') {//Remove the dummy node
                        parent.children.splice(0); 
                    }
                    parent.children.push(node);
                } else {
                    nodes.push(node);
 
                } 
            } 
  
            if (parent !== null) {
                return parent;
            } else {
                return nodes;
            }
        } 
        
    return self;
}