var storyLineTab = Vue.component('story-line', {
	
	template: ' <div> \
					<div style="margin-top:20px; margin-bottom:20px" class="ui list"> \
						<div class="ui container"> \
							<div class="ui piled segment" v-for="(phase, index) in phases" style="margin-top:5px; margin-bottom:15px" @click="checkBranches(phase, index)"> \
								<p>{{ phase.content }}</p> \
								<h> \
									<font color="grey">{{ formatDate(phase.createdDate) }}&nbsp;&nbsp;·&nbsp;&nbsp;{{phase.author}}</font>&nbsp;&nbsp;&nbsp;&nbsp; \
									<span class="left floated like"><font color="grey"><i class="like icon"></i> {{phase.like ? phase.like : \"\"}} Likes </font></span>&nbsp;&nbsp; \
									<span class="right floated star" v-if="phase.branchPhases && phase.branchPhases.length > 1"><font color="grey"><i class="sitemap icon"></i> </font></span> \
								</h> \
							</div> \
						</div> \
					</div> \
					<div class="ui right demo vertical inverted sidebar labeled icon menu"> \
					  <a class="item" v-for="branchPhase in branchPhases" style="text-align: left" @click="loadBranchLine(branchPhase)"> \
							{{processContent(branchPhase.content)}} \
					  </a> \
					</div> \
				</div>',
	
	data: function() {
		return {
			phases: [],
			branchPhases: [],
			clickedPhaseIndex: ''
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
		
		checkBranches: function(phase, index) {
			debugger;
			if (phase.branchPhases && phase.branchPhases.length > 1) {
				var url = "/story/phases";
				var _self = this;
				axios.get(url, {
					params: {
						phaseIds: phase.branchPhases.join(',')
					}
				}).then(function(response){
					if (response.data.status == 'success') {
						debugger;
						_self.branchPhases = response.data.data;
						_self.toggleSideBar();
						_self.clickedPhaseIndex = index;
					}
				}) 
				
			}
		},
		
		processContent: function(content) {
			debugger;
			if (content && content.length > 20) {
				return content.substr(0, 15) + '...';
			} else {
				return content;
			}
		},
		
		loadBranchLine: function(branchPhase) {
			debugger;
			var url = "/story/story/phases/" + branchPhase.id;
			var _self = this;
			axios.get(url).then(function(response) {
				debugger;
				if (response.data.status == 'success') {
					var subPhases = response.data.data;
					var originPhases = _self.phases.splice(0, _self.clickedPhaseIndex + 1);
					for (var i in subPhases) {
						originPhases.push(subPhases[i]);
					}
					_self.phases = originPhases;
					_self.toggleSideBar();
				}
			})
		},
		
		toggleSideBar: function() {
			$('.ui.labeled.icon.sidebar')
			  .sidebar('toggle');
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
				_self.phases.splice(0,0,_self.$route.query.parentPhase)
			}
		})
	}
})
