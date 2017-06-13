var storyLineTab = Vue.component('story-line', {
	
	template: ' <div id="storyLine"> \
					<div style="margin-top:20px; margin-bottom:20px" class="ui list"> \
						<div class="ui container"> \
							<h2 class="ui header" v-if="phases">{{topPhase.storyTitle}}&nbsp;&nbsp;<font color="grey" size="2px">{{ topPhase.author }}</font></h2> \
							<div class="ui piled segment" v-for="(phase, index) in phases" style="margin-top:5px; margin-bottom:15px"> \
								<pre style="overFlow-x: hidden; white-space: pre-wrap;word-wrap: break-word;" @click="checkBranches(phase, index)">{{ phase.content }}</pre> \
								<h> \
									<font color="grey">{{ formatDate(phase.createdDate) }}&nbsp;&nbsp;·&nbsp;&nbsp;{{phase.author}}</font>&nbsp;&nbsp;&nbsp;&nbsp; \
									<span class="left floated like"><font color="grey"><i class="like icon"></i> {{phase.like ? phase.like : \"\"}} Likes </font></span>&nbsp;&nbsp; \
									<span class="right floated star"><font color="grey"><i class="write icon"></i> Update </font></span>&nbsp;&nbsp; \
									<span class="right floated star" v-if="phase.branchPhases && phase.branchPhases.length > 1"><font color="grey"><i class="sitemap icon"></i> </font></span> \
								</h> \
							</div> \
						</div> \
					</div> \
					<div id="sidebar" data-transition="slide out" class="ui right demo vertical inverted sidebar labeled icon menu"> \
					  <a class="item" v-for="branchPhase in branchPhases" style="text-align: left" @click="loadBranchLine(branchPhase)"> \
							{{processContent(branchPhase.content)}} \
					  </a> \
					  <a class="item" @click="routeToNewPhaseTab()"> \
					    	<i class="block add circle icon"></i> \
					  </a> \
					</div> \
					<div id="createNew" class="ui basic modal"> \
					  <div class="header"> \
						要由此节开始续写故事线吗？ \
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
			phases: new Array(),
			branchPhases: [],
			selectedPhaseIndex: '',
			selectedPhase: '',
			topPhase: ''
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
					debugger;
					var subPhases = response.data.data;
					var originPhases = _self.phases.splice(0, _self.selectedPhaseIndex + 1);
					for (var i in subPhases) {
						originPhases.push(subPhases[i]);
					}
					_self.phases = originPhases;
					_self.toggleSideBar();
				}
			})
		},
		
		toggleSideBar: function() {
			$('#sidebar')
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
	
	created: function() {
		this.topPhase = this.$store.state.topPhase;
	},
	
	beforeCreate: function() {
		if (!this.$store.state.topPhase) {
			this.$parent.routeTo('/');
		}
		debugger;
		var sidebar = document.getElementById("sidebar");
		if (sidebar) {
			document.getElementsByTagName('body')[0].removeChild(sidebar);
		}
		debugger;
		var url = "/story/story/phases/" + this.$store.state.topPhase.id;//this.$route.query.parentPhaseId;
		var _self = this;
		this.branchPhases = [];
		axios.get(url).then(function(response) {
			debugger;
			if (response.data.status == 'success') {
				_self.phases = response.data.data;
			}
		})
	}
})
