var storyListTab = Vue.component('story-list', {
	
	template: ' <div style="margin-top:20px" class="ui list"> \
					<div class="ui centered card" v-for="phase in phases"> \
						  <div class="content"> \
						    <span class="right floated"><font color="grey" size="2px">{{formatDate(phase.createdDate)}}</font></span> \
						    <div class="header">{{phase.storyTitle}}<span><font color="grey" size="2px">    / {{phase.author}}</font></span></div> \
						    <div class="description"> \
						      <p>{{phase.content}}</p> \
						    </div> \
						  </div> \
						  <div class="extra content"> \
						    <span class="left floated like"><i class="like icon"></i> Like </span> \
						    <span class="right floated star"><i class="comment icon"></i> Comment </span> \
						  </div> \
					</div> \
				</div>',
	
	data: function() {
		return {
			phases: []
		}
	},
	
	methods: {
		formatDate: function(value) {
			var date = new Date();
			date.setTime(value);
			var year = date.getYear() + 1900;
			var month = date.getMonth() + 1;
			if(month < 10) month = '0' + month;
			var day = date.getDate();
			if(day < 10) day = '0' + day;
			return year + "-" + month + "-" + day;
		}
	},
	
	beforeCreate: function() {
		var url = "/story/story/"
		var _self = this;
		axios.get(url).then(function(response) {
			debugger;
			if (response.data.status == 'success') {
				_self.phases = response.data.data;
			}
		})
	}
})