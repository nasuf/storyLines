var storyListTab = Vue.component('story-list', {
	
	template: ' <div style="margin-top:20px; margin-bottom:20px" class="ui list"> \
				<div class="ui container"> \
					<div class="ui secondary  menu" style="margin-top:25px"> \
					  <div class="right menu"> \
					    <a class="active ui item" @click="routeTo(\'newStoryTab\')"><i class="write icon"></i> 创建新故事线  </a> \
					  </div> \
					</div> \
					<div class="ui raised link centered card" v-for="(phase, index) in phases"> \
						  <div class="content" @click="loadStoryLine(phase.id, phase.branchPhases, phase.isStart, phase)"> \
							<span class="right floated"><font color="grey" size="2px">{{phase.isStart ? "" : phase.author + " · " }}{{formatDate(phase.createdDate)}}</font></span> \
						    <div class="header" v-if="phase.isStart">{{phase.storyTitle}}<span><font color="grey" size="2px">    / {{phase.author}}</font></span></div> \
						    <div class="description"> \
						      <pre style="overFlow-x: hidden; white-space: pre-wrap;word-wrap: break-word;">{{phase.content}}</pre> \
						    </div> \
						  </div> \
						  <div class="extra content"> \
						    <span class="right floated like"><i :id="index" class="empty heart icon" @click="toggleLikesIcon(phase.id, index)"></i>{{phase.like==0 ? "" : phase.like}} Likes </span> \
						  </div> \
					</div> \
					<div id="extendNew" class="ui basic modal"> \
					  <div class="header"> \
						该故事线尚未开启，您愿意续写吗？ \
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
					      <div class="ui ok green basic inverted button" @click="routeTo(\'newPhaseTab\', selectedPhase)"> \
					        <i class="checkmark icon"></i> \
					        Yes \
					      </div> \
					    </div> \
					  </div> \
					</div> \
				</div> \
				</div>',
	
	data: function() {
		return {
			phases: [],
			selectedPhase: ''
		}
	},
	
	methods: {
		
		toggleLikesIcon: function(phaseId, index) {
			debugger;
			var like = true;
			var attr = $('#'+index).attr('class');
			if (attr == 'empty heart icon') {
				// like
				$('#'+index).attr('class','heart icon');
				this.phases[index].like ++;
			} else {
				// dislike
				$('#'+index).attr('class','empty heart icon');
				like = false;
				if (this.phases[index].like != 0) {
					this.phases[index].like -- ;
				}
				
			}
			var url = "/story/phase/" + phaseId + "/" + like;
			axios.put(url);
		},
		
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
		
		loadStoryLine: function(parentPhaseId, branchPhases, isStart, topPhase) {
			if (branchPhases && isStart) {
				debugger;
				this.$store.commit('updateTopPhase', topPhase);
				this.$parent.routeTo("/storyLine");
			} else {
				this.selectedPhase = topPhase;
				$('#extendNew')
					.modal('show');
			}
		},
		
		routeTo: function(tabName, parentPhase) {
			//router.push(tabName)
			debugger;
			this.$store.commit('updateParentPhase', parentPhase);
			var arr = new Array();
			arr.push(parentPhase);
			this.$store.commit('updateParentPhases', arr);
			this.$parent.routeTo(tabName)
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

