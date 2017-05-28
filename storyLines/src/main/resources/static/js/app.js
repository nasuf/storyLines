var routes = [
	{
		path: '/',
		components: {
			content: storyListTab
		}
	}
];

var routeBeforeEach = function(to, from, next,b,d) {
	if(to.path != "/"){
		if(isEmptySession()){
			next("/");
		} else {
			next();
		}
	} else {
		next();
	}
};

routes.forEach(function(route){
	route.beforeEnter = routeBeforeEach;
	if(route.children){
		route.children.forEach(function(children){
			children.beforeEnter = routeBeforeEach;
		});
	}
});

var router = new VueRouter({
	routes: routes,
	beforeEach: routeBeforeEach
});

var app = new Vue({
	
	router: router,
	
	el: "#app",
	
	data: function() {
		return {}
	}
})