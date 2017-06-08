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
					  <a class="item" @click="routeToNewPhaseTab()"> \
					    	<i class="block add circle icon"></i> \
					  </a> \
					</div> \
					<div id="createNew" class="ui basic modal"> \
					  <div class="header"> \
						要重写此节并开启新的故事线吗？ \
					  </div> \
					  <div class="image content"> \
					    <div class="description"> \
					    </div> \
					  </div> \
					  <div class="actions"> \
					    <div class="two fluid ui inverted buttons"> \
					      <div class="ui cancel red basic inverted button"> \
					        <i class="remove icon"></i> \
					        No \
					      </div> \
					      <div class="ui ok green basic inverted button" @click="routeTo(\'newPhaseTab\')"> \
					        <i class="checkmark icon"></i> \
					        Yes \
					      </div> \
					    </div> \
					  </div> \
					</div> \
				</div>',
	
	data: function() {
		return {
			phases: [],
			branchPhases: [],
			selectedPhaseIndex: '',
			selectedPhase: ''
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
				this.selectedPhase = phase;
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
						_self.selectedPhaseIndex = index;
					}
				}) 
				
			} else {
				this.selectedPhase = phase;
				$('#createNew')
				  .modal('show');
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
					var originPhases = _self.phases.splice(0, _self.selectedPhaseIndex + 1);
					originPhases.push(branchPhase);
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
		},
		
		routeToNewPhaseTab() {
			this.toggleSideBar();
			$('#createNew')
			  .modal('show');
		},
		
		routeTo: function(tabName) {
			//router.push(tabName)
			debugger;
			this.$store.commit('updateParentPhase', this.selectedPhase);
			this.$parent.routeTo(tabName)
		}
		
	},
	
	beforeCreate: function() {
		debugger;
		var url = "/story/story/phases/" + this.$store.state.topPhase.id;//this.$route.query.parentPhaseId;
		var _self = this;
		axios.get(url).then(function(response) {
			debugger;
			if (response.data.status == 'success') {
				_self.phases = response.data.data;
				//_self.phases.splice(0,0,_self.$route.query.parentPhase)
				_self.phases.splice(0,0,_self.$store.state.topPhase)
			}
		})
	}
})
