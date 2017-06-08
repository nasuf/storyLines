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
						  </div>\
						  <div class="ui submit button" @click="alertModal(\'create\')">创建</div> \
						  <div class="ui submit button" @click="alertModal(\'cancel\')">取消</div><br/> \
					</form><br> \
					<div id="alert-title" class="ui bottom floating hidden negative message"> <i class="close icon"></i>\
						故事标题尚未填写 \
				  	</div> \
					<div id="alert-author" class="ui bottom floating hidden negative message"> <i class="close icon"></i>\
						作者尚未填写 \
				  	</div> \
					<div id="alert-content" class="ui bottom floating hidden negative message"> <i class="close icon"></i>\
						内容尚未填写 \
				  	</div> \
					<div id="create" class="ui basic modal"> \
					  <div class="header"> \
						确定创建新故事线吗？ \
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
					      <div class="ui ok green basic inverted button" @click="createStoryLine()"> \
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
				isStart: true,
				isEnd: true,
				level: 1,
				like: 0,
				dislike: 0
			},
			isTitleComplete: false,
			isAuthorComplete: false,
			isContentComplete: false
		}
	},
	
	methods: {
		
		alertModal: function(action) {
			if (action == 'create') {
				debugger;
				if (!this.phase.storyTitle) {
					$('#alert-title').css('display','block');
					return;
				} else {
					$('#alert-title').css('display','none');
				}
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
		createStoryLine: function() {
			var url = "/story/story?isNewStory=true";
			var _self = this;
			axios.post(url, this.phase).then(function(response){
				debugger;
				if (response.data.status = "success") {
					//router.push("/");
					_self.$parent.routeTo("/");
				}
			})
		},
		routeTo: function(tabName) {
			//router.push(tabName)
			this.$parent.routeTo(tabName);
		}
	}
	
})