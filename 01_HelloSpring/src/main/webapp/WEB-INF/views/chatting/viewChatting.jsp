<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>화상채팅</title>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script>
	//WebRtc에 필요한 변수를 지정
	var webrtc_capable=true;//브라우저가 webrtc를 지원하는지여부
	var rtc_peer_connection=null;//peer간의 통신을 할 수 있게 하는 객체
	var rtc_session_description=null;//webrtc에서 유지하는 session객체
	var get_user_media=null//현재브라우저를 실행하는 컴퓨터의 video,audio디바이스 가져오는것
	var connect_stream_to_src=null;//비디오관련 stream을 video태그에 연결 
	var stun_server="stun.l.google.com:19302";//구글 공개용 stun서버
	//p2p통신을 위해 서로간의 사설ip주소를 확인시켜주는 서버
	
	//선언한 변수에 값 세팅하기!!!
	if(navigator.getUserMedia){//웹 표준
		rtc_peer_connection=RTCPeerConnection;//브라우저가 알아서 넣어줌 연결정보
		rtc_session_description=RTCSessionDescription;//session
		get_user_media=navigator.getUserMedia.bind(navigator);//로컬의 미디어 데이터를 불러옴
		connect_stream_to_src=function(media_stream,media_element){
			media_element.srcObject=media_stream;//영상을 전송하는 스트림
			//<video>태그 src속성에 값을 넣는것
			media_element.play();//video태그 실행
		};
	}else if(navigator.mozGetUserMedia){//firefox브라우저
		rtc_peer_connection=mozRTCPeerConnection;//브라우저가 알아서 넣어줌 연결정보
		rtc_session_description=mozRTCSessionDescription;//session
		get_user_media=navigator.mozGetUserMedia.bind(navigator);//로컬의 미디어 데이터를 불러옴
		connect_stream_to_src=function(media_stream,media_element){
			media_element.mozSrcObject=meda_stream;//영상을 전송하는 스트림
			//<video>태그 src속성에 값을 넣는것
			media_element.play();//video태그 실행
		};
		stun_server="74.125.31.127:19302"//firefox 공개 stun서버 DNS제공하지 않음
	}else if(navigator.webkitGetUserMedia){//크롭브라우저
		rtc_peer_connection=webkitRTCPeerConnection;//브라우저가 알아서 넣어줌 연결정보
		rtc_session_description=RTCSessionDescription;//session
		get_user_media=navigator.webkitgetUserMedia.bind(navigator);//로컬의 미디어 데이터를 불러옴
		connect_stream_to_src=function(media_stream,media_element){
			media_element.src=webkitURL.createObjectURL(meda_stream);//영상을 전송하는 스트림
			//<video>태그 src속성에 값을 넣는것
		};
	}else{
		alert("이 브라우저는 화상채팅이 불가능합니다. 다른 브라우저로 접속하세요!");
		webrtc_capable=false;
	}
</script>
<!-- 여기까지 webRtc를 적용하기 위한 기본설정 -->
<script>
<!-- 실제화상통신을 구현하는 script -->
	var call_token="${loginMember.userId}";//사용자를 구분하기 위한 변수
	var signaling_server;//화상채팅시 정보를 주고받게할 서버 *websocket으로 활용! 이게 가장좋음
	var peer_connect;//실질적인 연결정보를 저장할 객체
	
	//페이지가 로드되면 변수들을 세팅하는 함수구현
	//peer_connect, signalingserver, 페이지에 필요한 정보를 설정
	function start(){
		peer_connect=new rtc_peer_connection({
			"iceServers" : [
				{
					"url":"stun:"+stun_server
				}
			]	
		});
		//p2p간의 접속할 수 있는 경로보내는데 그것을 처리하는 함수
		peer_connect.onicecandidate=function(ice_event){
			if(ice_event.candidate){
				signaling_server.send(JSON.stringify({
					token:call_token,
					type:"new_ice_candidate",
					candidate:ice_event.candidate
				}));
			}
		};
		//자신(local)의 영상을 브라우저에 세팅
		setup_video();
		//원격peer(화상채팅의 상대방)의 영상을 세팅
		peer_connect.onaddstream=function(event){
			connect_stream_to_src(event.stream,document.getElementById("remote_video"));
			$("#loading_state").hide();
			$("#open_call_state").show();
		}
		//메세지를 주고받게 해주는 서버등록(websocket)
		signaling_server=new WebSocket("wss://192.168.120.174:8443/spring/viewChatting");
		
		//시그널링 서버설정  *onmessage함수를 등록
		signaling_server.onopen=function(){
			//메세지처리함수
			signaling_server.onmessage=caller_signal_handler;
			//접속한것을 시그널리서버에 알림
			signaling_server.send(JSON.stringify({
				token:call_token,
				type:"join"
			}));
		}
		document.title="연결대기중";
		$("#loading_state").html("영상통화대기중....");
	}//기본환경설정끝
	var connected=true;//요청이 왔는지 확인하는 flag
	
	//signalingserver에서 온 메세지처리함수 설정
	function caller_signal_handler(event){
		var signal=JSON.parse(event.data);//서버에서 보낸메세지 파싱
		if(signal.type=="callee_arrived"){//화상연결요청이 들어왔을때 처리
			//서로연결을 위한 sdp객체를 생성해서 전송
			//sdp(description) 미디어설정값이 들어가 있음, 해상도, 코딩값...등등
			//createOffer() 상대방에게 연결하기 위한 값을 생성하고 생성이 정상적으로 이뤄지면
			//첫번째 매개변수에 함수를 실행함, 
			//정상적으로 생성되지않으면 error가발생하면 두번째 함수를 실행 *두가지다 callback함수
			peer_connect.createOffer(new_description_created,log_error);
		}else if(signal.type=="new_ice_candidate"){
			peer_connect.addIceCandidate(new RTCIceCandidate(
					signal.candidate));
		}else if(signal.type=="new_description"){//p2p간 연결하는 로직
			peer_connect.setRemoteDescription(new rtc_session_description(
				signal.sdp),function(){
					if(peer_connect.remoteDescription.type=="answer"){
						if(connected){
							if(confirm("화상채팅요청이 들어왔습니다. 허용하시겠습니까?")){
								sendArrived();
							}
							connected=false;
						}
					}
					if(peer_connect.remoteDescription.type=="offer"){
						peer_connect.createAnswer(new_description_created,
							function(){	}		
						);
					}
				},log_error	
			)
		}else if(signal.type=="member"){//현재 접속자를 출력
			var membercontainer=$(".members");
			membercontainer.html("");
			for(var i=0;i<signal.members.length;i++){
				var li=$("<li>");
				var h=$("<h3>").html(signal.members[i]).css("color","gray");
				if(call_token!=signal.members[i]){
					h.css("color","green");
					h.click(function(){
						if(confirm($(this).html()+"과 연결하시겠습니까?")){
							sendArrived();
						}
					});
				}
				li.html(h);
				membercontainer.append(li);
			}
		}
	}
	
	//description, sdp를 만들어주는 함수
	function new_description_created(description) {
		peer_connect.setLocalDescription(description, function() {
			signaling_server.send(JSON.stringify({
				token:call_token,
				type: "new_description",
				sdp: description
			}));
		}, log_error);
	}
	
	//스트림처리하기
	function setup_video(){
		get_user_media({
			"audio":true,//브라우저가 컴퓨터의 sound, video디바이스를 가져오기
			"video":true
		},function(local_stream){
			//로컬스트림 연결
			connect_stream_to_src(local_stream,document.getElementById("local_video"));
			//peer_connect에 로컬스트림을 추가
			peer_connect.addStream(local_stream);
		},log_error)
	}
	function log_error(error){
		console.log(error);
	}
	function sendArrived(){
		signaling_server.send(JSON.stringify({
			token:call_token,
			type:"callee_arrived"
		}))
	}
</script>
<style>
	html, body {
		padding: 0px;
		margin: 0px;
		font-family: "Arial", "Helvetica", sans-serif;
	}
	
	#loading_state {
		position: absolute;
		top: 45%;
		left: 0px;
		width: 100%;
		font-size: 20px;
		text-align: center;
	}
	
	#open_call_state {
		display: none;
	}
	
	#local_video {
		position: absolute;
		top: 10px;
		left: 10px;
		width: 160px;
		height: 120px;
		background: #333333;
	}
	
	#remote_video {
		position: absolute;
		top: 0px;
		left: 0px;
		width: 1024px;
		height: 768px;
		background: #999999;
	}
	#membercontainer{
		position: absolute;
		top:20px;
		left:70%;
	}
	
	#membercontainer>ul{
		list-style-type:none;
	}
	#membercontainer>ul>li>h3{
		cursor:pointer;
	}
</style>
</head>
<body onload="start()">
		<div id="membercontainer">
			<ul class="members">
			</ul>
		</div>
	<div id="loading_state">
	</div>
	<div id="open_call_state">
		<video id="remote_video" controls></video>
		<video id="local_video"></video>
		<div id="membercontainer">
			<ul class="members">
			</ul>
		</div>
	</div>
</body>
</html>








