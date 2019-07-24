'use strict';

var isChannelReady = false;
var isInitiator = false;
var isStarted = false;
var localStream;
var pc;
var remoteStream;
var turnReady;

var pcConfig = {
  'iceServers': [
    { 'urls': 'stun.l.google.com:19302' },
    { 'urls': 'stun1.l.google.com:19302' },
    { 'urls': 'stun2.l.google.com:19302' },
    { 'urls': 'stun3.l.google.com:19302' },
    { 'urls': 'stun4.l.google.com:19302' },
    { 'urls': 'stun.ekiga.net' },
    { 'urls': 'stun.ideasip.com' },
    { 'urls': 'stun.rixtelecom.se' },
    { 'urls': 'stun.schlund.de' },
    { 'urls': 'stun.stunprotocol.org:3478' },
    { 'urls': 'stun.voiparound.com' },
    { 'urls': 'stun.voipbuster.com' },
    { 'urls': 'stun.voipstunt.com' },
    { 'urls': 'stun.voxgratia.org' },
    { 'urls': '23.21.150.121:3478' },
    { 'urls': 'iphone-stun.strato-iphone.de:3478' },
    { 'urls': 'numb.viagenie.ca:3478' },
    { 'urls': 's1.taraba.net:3478' },
    { 'urls': 's2.taraba.net:3478' },
    { 'urls': 'stun.12connect.com:3478' },
    { 'urls': 'stun.12voip.com:3478' },
    { 'urls': 'stun.1und1.de:3478' },
    { 'urls': 'stun.2talk.co.nz:3478' },
    { 'urls': 'stun.2talk.com:3478' },
    { 'urls': 'stun.3clogic.com:3478' },
    { 'urls': 'stun.3cx.com:3478' },
    { 'urls': 'stun.a-mm.tv:3478' },
    { 'urls': 'stun.aa.net.uk:3478' },
    { 'urls': 'stun.acrobits.cz:3478' },
    { 'urls': 'stun.actionvoip.com:3478' },
    { 'urls': 'stun.advfn.com:3478' },
    { 'urls': 'stun.aeta-audio.com:3478' },
    { 'urls': 'stun.aeta.com:3478' },
    { 'urls': 'stun.alltel.com.au:3478' },
    { 'urls': 'stun.altar.com.pl:3478' },
    { 'urls': 'stun.annatel.net:3478' },
    { 'urls': 'stun.antisip.com:3478' },
    { 'urls': 'stun.arbuz.ru:3478' },
    { 'urls': 'stun.avigora.com:3478' },
    { 'urls': 'stun.avigora.fr:3478' },
    { 'urls': 'stun.awa-shima.com:3478' },
    { 'urls': 'stun.awt.be:3478' },
    { 'urls': 'stun.b2b2c.ca:3478' },
    { 'urls': 'stun.bahnhof.net:3478' },
    { 'urls': 'stun.barracuda.com:3478' },
    { 'urls': 'stun.bluesip.net:3478' },
    { 'urls': 'stun.bmwgs.cz:3478' },
    { 'urls': 'stun.botonakis.com:3478' },
    { 'urls': 'stun.budgetphone.nl:3478' },
    { 'urls': 'stun.budgetsip.com:3478' },
    { 'urls': 'stun.cablenet-as.net:3478' },
    { 'urls': 'stun.callromania.ro:3478' },
    { 'urls': 'stun.callwithus.com:3478' },
    { 'urls': 'stun.cbsys.net:3478' },
    { 'urls': 'stun.chathelp.ru:3478' },
    { 'urls': 'stun.cheapvoip.com:3478' },
    { 'urls': 'stun.ciktel.com:3478' },
    { 'urls': 'stun.cloopen.com:3478' },
    { 'urls': 'stun.colouredlines.com.au:3478' },
    { 'urls': 'stun.comfi.com:3478' },
    { 'urls': 'stun.commpeak.com:3478' },
    { 'urls': 'stun.comtube.com:3478' },
    { 'urls': 'stun.comtube.ru:3478' },
    { 'urls': 'stun.cope.es:3478' },
    { 'urls': 'stun.counterpath.com:3478' },
    { 'urls': 'stun.counterpath.net:3478' },
    { 'urls': 'stun.cryptonit.net:3478' },
    { 'urls': 'stun.darioflaccovio.it:3478' },
    { 'urls': 'stun.datamanagement.it:3478' },
    { 'urls': 'stun.dcalling.de:3478' },
    { 'urls': 'stun.decanet.fr:3478' },
    { 'urls': 'stun.demos.ru:3478' },
    { 'urls': 'stun.develz.org:3478' },
    { 'urls': 'stun.dingaling.ca:3478' },
    { 'urls': 'stun.doublerobotics.com:3478' },
    { 'urls': 'stun.drogon.net:3478' },
    { 'urls': 'stun.duocom.es:3478' },
    { 'urls': 'stun.dus.net:3478' },
    { 'urls': 'stun.e-fon.ch:3478' },
    { 'urls': 'stun.easybell.de:3478' },
    { 'urls': 'stun.easycall.pl:3478' },
    { 'urls': 'stun.easyvoip.com:3478' },
    { 'urls': 'stun.efficace-factory.com:3478' },
    { 'urls': 'stun.einsundeins.com:3478' },
    { 'urls': 'stun.einsundeins.de:3478' },
    { 'urls': 'stun.ekiga.net:3478' },
    { 'urls': 'stun.epygi.com:3478' },
    { 'urls': 'stun.etoilediese.fr:3478' },
    { 'urls': 'stun.eyeball.com:3478' },
    { 'urls': 'stun.faktortel.com.au:3478' },
    { 'urls': 'stun.freecall.com:3478' },
    { 'urls': 'stun.freeswitch.org:3478' },
    { 'urls': 'stun.freevoipdeal.com:3478' },
    { 'urls': 'stun.fuzemeeting.com:3478' },
    { 'urls': 'stun.gmx.de:3478' },
    { 'urls': 'stun.gmx.net:3478' },
    { 'urls': 'stun.gradwell.com:3478' },
    { 'urls': 'stun.halonet.pl:3478' },
    { 'urls': 'stun.hellonanu.com:3478' },
    { 'urls': 'stun.hoiio.com:3478' },
    { 'urls': 'stun.hosteurope.de:3478' },
    { 'urls': 'stun.ideasip.com:3478' },
    { 'urls': 'stun.imesh.com:3478' },
    { 'urls': 'stun.infra.net:3478' },
    { 'urls': 'stun.internetcalls.com:3478' },
    { 'urls': 'stun.intervoip.com:3478' },
    { 'urls': 'stun.ipcomms.net:3478' },
    { 'urls': 'stun.ipfire.org:3478' },
    { 'urls': 'stun.ippi.fr:3478' },
    { 'urls': 'stun.ipshka.com:3478' },
    { 'urls': 'stun.iptel.org:3478' },
    { 'urls': 'stun.irian.at:3478' },
    { 'urls': 'stun.it1.hr:3478' },
    { 'urls': 'stun.ivao.aero:3478' },
    { 'urls': 'stun.jappix.com:3478' },
    { 'urls': 'stun.jumblo.com:3478' },
    { 'urls': 'stun.justvoip.com:3478' },
    { 'urls': 'stun.kanet.ru:3478' },
    { 'urls': 'stun.kiwilink.co.nz:3478' },
    { 'urls': 'stun.kundenserver.de:3478' },
    { 'urls': 'stun.l.google.com:19302' },
    { 'urls': 'stun.linea7.net:3478' },
    { 'urls': 'stun.linphone.org:3478' },
    { 'urls': 'stun.liveo.fr:3478' },
    { 'urls': 'stun.lowratevoip.com:3478' },
    { 'urls': 'stun.lugosoft.com:3478' },
    { 'urls': 'stun.lundimatin.fr:3478' },
    { 'urls': 'stun.magnet.ie:3478' },
    { 'urls': 'stun.manle.com:3478' },
    { 'urls': 'stun.mgn.ru:3478' },
    { 'urls': 'stun.mit.de:3478' },
    { 'urls': 'stun.mitake.com.tw:3478' },
    { 'urls': 'stun.miwifi.com:3478' },
    { 'urls': 'stun.modulus.gr:3478' },
    { 'urls': 'stun.mozcom.com:3478' },
    { 'urls': 'stun.myvoiptraffic.com:3478' },
    { 'urls': 'stun.mywatson.it:3478' },
    { 'urls': 'stun.nas.net:3478' },
    { 'urls': 'stun.neotel.co.za:3478' },
    { 'urls': 'stun.netappel.com:3478' },
    { 'urls': 'stun.netappel.fr:3478' },
    { 'urls': 'stun.netgsm.com.tr:3478' },
    { 'urls': 'stun.nfon.net:3478' },
    { 'urls': 'stun.noblogs.org:3478' },
    { 'urls': 'stun.noc.ams-ix.net:3478' },
    { 'urls': 'stun.node4.co.uk:3478' },
    { 'urls': 'stun.nonoh.net:3478' },
    { 'urls': 'stun.nottingham.ac.uk:3478' },
    { 'urls': 'stun.nova.is:3478' },
    { 'urls': 'stun.nventure.com:3478' },
    { 'urls': 'stun.on.net.mk:3478' },
    { 'urls': 'stun.ooma.com:3478' },
    { 'urls': 'stun.ooonet.ru:3478' },
    { 'urls': 'stun.oriontelekom.rs:3478' },
    { 'urls': 'stun.outland-net.de:3478' },
    { 'urls': 'stun.ozekiphone.com:3478' },
    { 'urls': 'stun.patlive.com:3478' },
    { 'urls': 'stun.personal-voip.de:3478' },
    { 'urls': 'stun.petcube.com:3478' },
    { 'urls': 'stun.phone.com:3478' },
    { 'urls': 'stun.phoneserve.com:3478' },
    { 'urls': 'stun.pjsip.org:3478' },
    { 'urls': 'stun.poivy.com:3478' },
    { 'urls': 'stun.powerpbx.org:3478' },
    { 'urls': 'stun.powervoip.com:3478' },
    { 'urls': 'stun.ppdi.com:3478' },
    { 'urls': 'stun.prizee.com:3478' },
    { 'urls': 'stun.qq.com:3478' },
    { 'urls': 'stun.qvod.com:3478' },
    { 'urls': 'stun.rackco.com:3478' },
    { 'urls': 'stun.rapidnet.de:3478' },
    { 'urls': 'stun.rb-net.com:3478' },
    { 'urls': 'stun.refint.net:3478' },
    { 'urls': 'stun.remote-learner.net:3478' },
    { 'urls': 'stun.rixtelecom.se:3478' },
    { 'urls': 'stun.rockenstein.de:3478' },
    { 'urls': 'stun.rolmail.net:3478' },
    { 'urls': 'stun.rounds.com:3478' },
    { 'urls': 'stun.rynga.com:3478' },
    { 'urls': 'stun.samsungsmartcam.com:3478' },
    { 'urls': 'stun.schlund.de:3478' },
    { 'urls': 'stun.services.mozilla.com:3478' },
    { 'urls': 'stun.sigmavoip.com:3478' },
    { 'urls': 'stun.sip.us:3478' },
    { 'urls': 'stun.sipdiscount.com:3478' },
    { 'urls': 'stun.sipgate.net:10000' },
    { 'urls': 'stun.sipgate.net:3478' },
    { 'urls': 'stun.siplogin.de:3478' },
    { 'urls': 'stun.sipnet.net:3478' },
    { 'urls': 'stun.sipnet.ru:3478' },
    { 'urls': 'stun.siportal.it:3478' },
    { 'urls': 'stun.sippeer.dk:3478' },
    { 'urls': 'stun.siptraffic.com:3478' },
    { 'urls': 'stun.skylink.ru:3478' },
    { 'urls': 'stun.sma.de:3478' },
    { 'urls': 'stun.smartvoip.com:3478' },
    { 'urls': 'stun.smsdiscount.com:3478' },
    { 'urls': 'stun.snafu.de:3478' },
    { 'urls': 'stun.softjoys.com:3478' },
    { 'urls': 'stun.solcon.nl:3478' },
    { 'urls': 'stun.solnet.ch:3478' },
    { 'urls': 'stun.sonetel.com:3478' },
    { 'urls': 'stun.sonetel.net:3478' },
    { 'urls': 'stun.sovtest.ru:3478' },
    { 'urls': 'stun.speedy.com.ar:3478' },
    { 'urls': 'stun.spokn.com:3478' },
    { 'urls': 'stun.srce.hr:3478' },
    { 'urls': 'stun.ssl7.net:3478' },
    { 'urls': 'stun.stunprotocol.org:3478' },
    { 'urls': 'stun.symform.com:3478' },
    { 'urls': 'stun.symplicity.com:3478' },
    { 'urls': 'stun.sysadminman.net:3478' },
    { 'urls': 'stun.t-online.de:3478' },
    { 'urls': 'stun.tagan.ru:3478' },
    { 'urls': 'stun.tatneft.ru:3478' },
    { 'urls': 'stun.teachercreated.com:3478' },
    { 'urls': 'stun.tel.lu:3478' },
    { 'urls': 'stun.telbo.com:3478' },
    { 'urls': 'stun.telefacil.com:3478' },
    { 'urls': 'stun.tis-dialog.ru:3478' },
    { 'urls': 'stun.tng.de:3478' },
    { 'urls': 'stun.twt.it:3478' },
    { 'urls': 'stun.u-blox.com:3478' },
    { 'urls': 'stun.ucallweconn.net:3478' },
    { 'urls': 'stun.ucsb.edu:3478' },
    { 'urls': 'stun.ucw.cz:3478' },
    { 'urls': 'stun.uls.co.za:3478' },
    { 'urls': 'stun.unseen.is:3478' },
    { 'urls': 'stun.usfamily.net:3478' },
    { 'urls': 'stun.veoh.com:3478' },
    { 'urls': 'stun.vidyo.com:3478' },
    { 'urls': 'stun.vipgroup.net:3478' },
    { 'urls': 'stun.virtual-call.com:3478' },
    { 'urls': 'stun.viva.gr:3478' },
    { 'urls': 'stun.vivox.com:3478' },
    { 'urls': 'stun.vline.com:3478' },
    { 'urls': 'stun.vo.lu:3478' },
    { 'urls': 'stun.vodafone.ro:3478' },
    { 'urls': 'stun.voicetrading.com:3478' },
    { 'urls': 'stun.voip.aebc.com:3478' },
    { 'urls': 'stun.voip.blackberry.com:3478' },
    { 'urls': 'stun.voip.eutelia.it:3478' },
    { 'urls': 'stun.voiparound.com:3478' },
    { 'urls': 'stun.voipblast.com:3478' },
    { 'urls': 'stun.voipbuster.com:3478' },
    { 'urls': 'stun.voipbusterpro.com:3478' },
    { 'urls': 'stun.voipcheap.co.uk:3478' },
    { 'urls': 'stun.voipcheap.com:3478' },
    { 'urls': 'stun.voipfibre.com:3478' },
    { 'urls': 'stun.voipgain.com:3478' },
    { 'urls': 'stun.voipgate.com:3478' },
    { 'urls': 'stun.voipinfocenter.com:3478' },
    { 'urls': 'stun.voipplanet.nl:3478' },
    { 'urls': 'stun.voippro.com:3478' },
    { 'urls': 'stun.voipraider.com:3478' },
    { 'urls': 'stun.voipstunt.com:3478' },
    { 'urls': 'stun.voipwise.com:3478' },
    { 'urls': 'stun.voipzoom.com:3478' },
    { 'urls': 'stun.vopium.com:3478' },
    { 'urls': 'stun.voxgratia.org:3478' },
    { 'urls': 'stun.voxox.com:3478' },
    { 'urls': 'stun.voys.nl:3478' },
    { 'urls': 'stun.voztele.com:3478' },
    { 'urls': 'stun.vyke.com:3478' },
    { 'urls': 'stun.webcalldirect.com:3478' },
    { 'urls': 'stun.whoi.edu:3478' },
    { 'urls': 'stun.wifirst.net:3478' },
    { 'urls': 'stun.wwdl.net:3478' },
    { 'urls': 'stun.xs4all.nl:3478' },
    { 'urls': 'stun.xtratelecom.es:3478' },
    { 'urls': 'stun.yesss.at:3478' },
    { 'urls': 'stun.zadarma.com:3478' },
    { 'urls': 'stun.zadv.com:3478' },
    { 'urls': 'stun.zoiper.com:3478' },
    { 'urls': 'stun1.faktortel.com.au:3478' },
    { 'urls': 'stun1.l.google.com:19302' },
    { 'urls': 'stun1.voiceeclipse.net:3478' },
    { 'urls': 'stun2.l.google.com:19302' },
    { 'urls': 'stun3.l.google.com:19302' },
    { 'urls': 'stun4.l.google.com:19302' },
    { 'urls': 'stunserver.org:3478' },
    { 'urls': 'turn:turn01.hubl.in?transport=udp' },
    {
      'urls': 'turn:turn02.hubl.in?transport=tcp'
    },
    {
      'urls': 'turn:numb.viagenie.ca',
      'credential': 'muazkh',
      'username': 'webrtc@live.com',
    },
    {
      'urls': 'turn:192.158.29.39:3478?transport=udp',
      'credential': 'JZEOEt2V3Qb0y27GRntt2u2PAYA=',
      'username': '28224511:1379330808',
    },
    {
      "url": 'turn:192.158.29.39:3478?transport=tcp',
      "credential": 'JZEOEt2V3Qb0y27GRntt2u2PAYA=',
      "username": '28224511:1379330808',
    },
    {
      'urls': 'turn:turn.bistri.com:80',
      'credential': 'homeo',
      'username': 'homeo',
    },
    {
      'url': 'turn:turn.anyfirewall.com:443?transport=tcp',
      'credential': 'webrtc',
      'username': 'webrtc',
    }
  ]
};

// Set up audio and video regardless of what devices are present.
var sdpConstraints = {
  offerToReceiveAudio: true,
  offerToReceiveVideo: true
};

/////////////////////////////////////////////

var room = 'room';
// Could prompt for room name:
room = prompt('Enter room name:');

//var socket = io.connect("http://172.245.132.132:1794");
var socket = io.connect();
if (room !== '') {
  socket.emit('create or join', room);
  console.log('Attempted to create or  join room', room);
}

socket.on('created', function (room) {
  console.log('Created room ' + room);
  isInitiator = true;
});

socket.on('full', function (room) {
  console.log('Room ' + room + ' is full');
});

socket.on('join', function (room) {
  console.log('Another peer made a request to join room ' + room);
  console.log('This peer is the initiator of room ' + room + '!');
  isChannelReady = true;
});

socket.on('joined', function (room) {
  console.log('joined: ' + room);
  isChannelReady = true;
});

socket.on('log', function (array) {
  console.log.apply(console, array);
});

////////////////////////////////////////////////

function sendMessage(message) {
  console.log('Client sending message: ', message);
  socket.emit('message', message);
}

// This client receives a message
socket.on('message', function (message) {
  console.log('Client received message:', message);
  if (message === 'got user media') {
    maybeStart();
  } else if (message.type === 'offer') {
    if (!isInitiator && !isStarted) {
      maybeStart();
    }
    pc.setRemoteDescription(new RTCSessionDescription(message));
    doAnswer();
  } else if (message.type === 'answer' && isStarted) {
    pc.setRemoteDescription(new RTCSessionDescription(message));
  } else if (message.type === 'candidate' && isStarted) {
    var candidate = new RTCIceCandidate({
      sdpMLineIndex: message.label,
      candidate: message.candidate
    });
    pc.addIceCandidate(candidate);
  } else if (message === 'bye' && isStarted) {
    handleRemoteHangup();
  }
});

////////////////////////////////////////////////////

var localVideo = document.querySelector('#localVideo');
var remoteVideo = document.querySelector('#remoteVideo');

navigator.mediaDevices.getUserMedia({
  audio: true,
  video: true
})
  .then(gotStream)
  .catch(function (e) {
    console.log('getUserMedia() error: ' + e);
  });


// try {
//   localVideo.srcObject = stream;
// } catch (error) {
//   localVideo.src = URL.createObjectURL(stream);
// }

function gotStream(stream) {
  console.log('Adding local stream.');
  localStream = stream;
  localVideo.srcObject = stream;
  sendMessage('got user media');
  if (isInitiator) {
    maybeStart();
  }
}

var constraints = {
  video: true
};

console.log('Getting user media with constraints', constraints);

if (location.hostname !== 'localhost') {
  requestTurn(
    'https://computeengineondemand.appspot.com/turn?username=41784574&key=4080218913'
  );
}

function maybeStart() {
  console.log('>>>>>>> maybeStart() ', isStarted, localStream, isChannelReady);
  if (!isStarted && typeof localStream !== 'undefined' && isChannelReady) {
    console.log('>>>>>> creating peer connection');
    createPeerConnection();
    pc.addStream(localStream);
    isStarted = true;
    console.log('isInitiator', isInitiator);
    if (isInitiator) {
      doCall();
    }
  }
}

window.onbeforeunload = function () {
  sendMessage('bye');
};

/////////////////////////////////////////////////////////

function createPeerConnection() {
  try {
    pc = new RTCPeerConnection(pcConfig);
    pc.onicecandidate = handleIceCandidate;
    pc.onaddstream = handleRemoteStreamAdded;
    pc.onremovestream = handleRemoteStreamRemoved;
    console.log('Created RTCPeerConnnection');
  } catch (e) {
    console.log('Failed to create PeerConnection, exception: ' + e.message);
    alert('Cannot create RTCPeerConnection object.');
    return;
  }
}

function handleIceCandidate(event) {
  console.log('icecandidate event: ', event);
  if (event.candidate) {
    sendMessage({
      type: 'candidate',
      label: event.candidate.sdpMLineIndex,
      id: event.candidate.sdpMid,
      candidate: event.candidate.candidate
    });
  } else {
    console.log('End of candidates.');
  }
}

function handleCreateOfferError(event) {
  console.log('createOffer() error: ', event);
}

function doCall() {
  console.log('Sending offer to peer');
  pc.createOffer(setLocalAndSendMessage, handleCreateOfferError);
}

function doAnswer() {
  console.log('Sending answer to peer.');
  pc.createAnswer().then(
    setLocalAndSendMessage,
    onCreateSessionDescriptionError
  );
}

function setLocalAndSendMessage(sessionDescription) {
  pc.setLocalDescription(sessionDescription);
  console.log('setLocalAndSendMessage sending message', sessionDescription);
  sendMessage(sessionDescription);
}

function onCreateSessionDescriptionError(error) {
  trace('Failed to create session description: ' + error.toString());
}

function requestTurn(turnURL) {
  var turnExists = false;
  for (var i in pcConfig.iceServers) {
    if (pcConfig.iceServers[i].urls.substr(0, 5) === 'turn:') {
      turnExists = true;
      turnReady = true;
      break;
    }
  }
  if (!turnExists) {
    console.log('Getting TURN server from ', turnURL);
    // No TURN server. Get one from computeengineondemand.appspot.com:
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
      if (xhr.readyState === 4 && xhr.status === 200) {
        var turnServer = JSON.parse(xhr.responseText);
        console.log('Got TURN server: ', turnServer);
        pcConfig.iceServers.push({
          'urls': 'turn:' + turnServer.username + '@' + turnServer.turn,
          'credential': turnServer.password
        });
        turnReady = true;
      }
    };
    xhr.open('GET', turnURL, true);
    xhr.send();
  }
}

function handleRemoteStreamAdded(event) {
  console.log('Remote stream added.');
  remoteStream = event.stream;
  remoteVideo.srcObject = remoteStream;
}

function handleRemoteStreamRemoved(event) {
  console.log('Remote stream removed. Event: ', event);
}

function hangup() {
  console.log('Hanging up.');
  stop();
  sendMessage('bye');
}

function handleRemoteHangup() {
  console.log('Session terminated.');
  stop();
  isInitiator = false;
}

function stop() {
  isStarted = false;
  pc.close();
  pc = null;
}
