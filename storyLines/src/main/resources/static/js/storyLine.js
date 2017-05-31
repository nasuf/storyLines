var storyLineTab = Vue.component('story-line', {
	
	template: ' <div style="margin-top:20px; margin-bottom:20px" class="ui list"> \
					<div class="ui container"> \
						<div class="ui piled segment" v-for="phase in phases" style="margin-top:5px; margin-bottom:15px"> \
							<p>{{ phase.content }}</p> \
							<h> \
								<font color="grey">{{ formatDate(phase.createdDate) }}&nbsp;&nbsp;·&nbsp;&nbsp;{{phase.author}}</font>&nbsp;&nbsp;&nbsp;&nbsp; \
								<span class="left floated like"><font color="grey"><i class="like icon"></i> {{phase.like ? phase.like : \"\"}} Likes </font></span>&nbsp;&nbsp; \
	    						<span class="right floated star"><font color="grey"><i class="write icon"></i> Follow </font></span> \
							</h> \
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
		},
		
		loadStoryLine: function() {
			
		}
		
	},
	
	beforeCreate: function() {
		debugger;
		var url = "/story/story/phases/" + this.$route.query.parentPhaseId;
		var _self = this;
		axios.get(url).then(function(response) {
			debugger;
			if (response.data.status == 'success') {
				_self.phases = response.data.data;
			}
		})
	}
})
