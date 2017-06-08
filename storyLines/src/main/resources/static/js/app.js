var routes = [
	{
		path: '/',
		components: {
			content: storyListTab
		}
	},
	{
		path: '/newStoryTab',
		components: {
			content: newStoryTab
		}
	},
	{
		path: '/storyLine',
		components: {
			content: storyLineTab
		}
	},
	{
		path: '/newPhaseTab',
		components: {
			content: newPhaseTab
		}
	}
];

var routeBeforeEach = function(to, from, next,b,d) {
	next();
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

var store = new Vuex.Store({
	state: {
		parentPhase: '',
		topPhase: ''
	},
	mutations: {
		updateParentPhase (state, parentPhase) {
			state.parentPhase = parentPhase;
		},
		updateTopPhase (state, topPhase) {
			state.topPhase = topPhase;
		}
	}
});

var app = new Vue({
	
	router: router,
	
	store,
	
	el: "#app",
	
	data: function() {
		return {}
	},
	
	methods: {
		routeTo: function(tabName, query) {
			router.push(tabName, query)
		},
		routeBack: function(){
			router.back();
		}
	}
})