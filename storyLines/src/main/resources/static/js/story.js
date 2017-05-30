var storyListTab = Vue.component('story-list', {
	
	template: ' <div style="margin-top:20px" class="ui list"> \
					<div class="ui top attached tabular menu transparent"> \
						<div class="right menu"> \
							 <div class="item"> \
							    <div class="ui transparent icon input"> \
									<span class="right floated star" @click="routeTo(\'newStoryTab\')"><i class="write icon"></i> 开启新故事线 </span> \
							    </div> \
							 </div> \
						</div> \
					</div> \
					<div class="ui raised link centered card" v-for="phase in phases"> \
						  <div class="content" @click="loadStoryLine(phase.id, phase.branchPhases)"> \
						    <span class="right floated"><font color="grey" size="2px">{{formatDate(phase.createdDate)}}</font></span> \
						    <div class="header">{{phase.storyTitle}}<span><font color="grey" size="2px">    / {{phase.author}}</font></span></div> \
						    <div class="description"> \
						      <p>{{phase.content}}</p> \
						    </div> \
						  </div> \
						  <div class="extra content"> \
						    <span class="left floated like"><i class="like icon"></i> Like </span> \
						    <span class="right floated star"><i class="write icon"></i> Follow </span> \
						  </div> \
					</div> \
					<div class="ui basic modal"> \
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
					      <div class="ui ok green basic inverted button"> \
					        <i class="checkmark icon"></i> \
					        Yes \
					      </div> \
					    </div> \
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
		
		loadStoryLine: function(parentPhaseId, branchPhases) {
			if (branchPhases) {
				var url = "/story/story/phases/" + parentPhaseId;
				var _self = this;
				axios.get(url).then(function(response) {
					debugger;
					if (response.data.status == 'success') {
						_self.phases = response.data.data;
					}
				})
			} else {
				$('.ui.basic.modal')
				  .modal('show')
				;
			}
		},
		
		routeTo: function(tabName) {
			router.push(tabName)
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

var newStoryTab = Vue.component('new-story',{
	template: '<div class="ui container" style="margin-top:20px"> \
					<form class="ui form">	\
						 <div class="field"> \
						    <div class="two fields"> \
						      <div class="field"> \
						        <input type="text" name="title" v-model="phase.storyTitle" placeholder="请输入标题"> \
						      </div> \
						      <div class="field"> \
						        <input type="text" name="author" v-model="phase.author" placeholder="请输入作者"> \
						      </div> \
						    </div> \
						  </div> \
						  <div class="field"> \
						    <label>正文</label> \
						    <textarea v-model="phase.content"></textarea> \
						  </div> \
						  <div class="ui submit button" @click="createStoryLine()">创建</div> \
						  <div class="ui submit button" @click="routeTo(\'/\')">取消</div> \
					</form> \
				</div> \
		       </div>',
	
    data: function() {
		return {
			phase: {
				storyTitle: '',
				author: '',
				content: '',
				isStart: true,
				isEnd: true,
				level: 1,
				like: 0,
				dislike: 0
			}
		}
	},
	
	methods: {
		createStoryLine: function() {
			var url = "/story/story?isNewStory=true";
			var _self = this;
			axios.post(url, this.phase).then(function(response){
				debugger;
				if (response.data.status = "success") {
					router.push("/");
				}
			})
		},
		routeTo: function(tabName) {
			router.push(tabName)
		}
	}
	
})