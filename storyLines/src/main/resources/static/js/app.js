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

var router = new VueRouter({
	routes: routes
});

var store = new Vuex.Store({
	state: {
		parentPhase: '',
		topPhase: '',
		parentPhases: []
	},
	mutations: {
		updateParentPhase (state, parentPhase) {
			state.parentPhase = parentPhase;
		},
		updateTopPhase (state, topPhase) {
			state.topPhase = topPhase;
		},
		updateParentPhases (state, parentPhases) {
			state.parentPhases = parentPhases;
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
	},
	
	created: function() {
		$('.ui.container')
		  .transition('scale');
	}
})