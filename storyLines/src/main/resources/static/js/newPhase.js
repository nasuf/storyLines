var newPhaseTab = Vue.component('new-phase',{
	template: '<div class="ui container" style="margin-top:20px"> \
					<form class="ui form">	\
						 <div class="field"> \
							<div class="field"> \
						        <input type="text" name="author" v-model="phase.author" placeholder="请输入作者"> \
						    </div> \
						  </div> \
						  <div class="field"> \
						    <label>正文</label> \
						    <textarea v-model="phase.content"></textarea> \
						  </div>\
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
			isContentComplete: false
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
	}
	
})