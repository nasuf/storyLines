var newPhaseTab = Vue.component('new-phase',{
	template: '<div class="ui container" style="margin-top:20px"> \
					<form class="ui form">	\
						  <div class="ui piled segment" style="margin-top:5px; margin-bottom:15px"> \
							<label><b><font size="5px">前文</font></b></label> \
							<pre style="overFlow-x: hidden; overFlow-y: scroll; white-space: pre-wrap;word-wrap: break-word;" :style="{maxHeight: height}">{{ parentContent }}</pre> \
						  </div> \
						  <div class="field"> \
						    <label><b><font size="5px">正文</font></b></label> \
						    <textarea v-model="phase.content"></textarea> \
						  </div>\
						  <div class="field"> \
							<div class="field"> \
						        <input type="text" name="author" v-model="phase.author" placeholder="请输入作者"> \
						    </div> \
						  </div> \
						  <div class="ui submit button" @click="alertModal(\'create\')">创建</div> \
						  <div class="ui submit button" @click="alertModal(\'cancel\')">取消</div><br/> \
					</form><br> \
					<div id="alert-author" class="ui bottom floating hidden negative message"> <i class="close icon"></i>\
						作者尚未填写 \
				  	</div> \
					<div id="alert-content" class="ui bottom floating hidden negative message"> <i class="close icon"></i>\
						内容尚未填写 \
				  	</div> \
					<div id="create" class="ui basic modal"> \
					  <div class="header"> \
						确定创建新片段吗？ \
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
					      <div class="ui ok green basic inverted button" @click="createPhase()"> \
					        <i class="checkmark icon"></i> \
					        Yes \
					      </div> \
					    </div> \
					  </div> \
					</div> \
					<div id="cancel" class="ui basic modal"> \
					  <div class="header"> \
						确定取消吗？ \
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
					      <div class="ui ok green basic inverted button" @click="routeTo(\'/\')"> \
					        <i class="checkmark icon"></i> \
					        Yes \
					      </div> \
					    </div> \
					  </div> \
					</div> \
		       </div>',
	
    data: function() {
		return {
			phase: {
				storyTitle: '',
				author: '',
				content: '',
				isStart: false,
				isEnd: true,
				level: '',
				like: 0,
				dislike: 0
			},
			isAuthorComplete: false,
			isContentComplete: false,
			parentPhases: [],
			parentContent: '',
			height: window.screen.height*0.45 + 'px'
		}
	},
	
	methods: {
		
		alertModal: function(action) {
			if (action == 'create') {
				debugger;
				if (!this.phase.author) {
					$('#alert-author').css('display','block');
					return;
				} else {
					$('#alert-author').css('display','none');
				}
				if (!this.phase.content) {
					$('#alert-content').css('display','block');
					return;
				} else {
					$('#alert-content').css('display','none');
				}
				$('#create')
					.modal('show')
				;
			} else {
				$('#cancel')
					.modal('show')
				;
			}
			
		},
		createPhase: function() {
			debugger;
			//var parentPhase = this.$route.query.parentPhase;
			var parentPhase = this.$store.state.parentPhase;
			var url = "/story/story?isNewStory=false&storyId=" + parentPhase.storyId + 
				"&parentPhaseId=" + parentPhase.id + "&isNewStory=false";
			this.phase.level = parentPhase.level + 1;
			var _self = this;
			axios.post(url, this.phase).then(function(response){
				debugger;
				if (response.data.status = "success") {
					_self.$parent.routeBack();
				}
			})
		},
		routeTo: function(tabName) {
			this.$parent.routeTo(tabName)
		}
	},
	
	beforeCreate: function() {
		if (!this.$store.state.parentPhase) {
			this.$parent.routeTo('/');
		}
	},
	
	created: function() {
		debugger;
		this.parentPhases = this.$store.state.parentPhases;
		for (var i in this.parentPhases) {
			if (this.parentPhases[i].content) {
				this.parentContent += this.parentPhases[i].content + "\n\n";
			}
		}
	}
	
})