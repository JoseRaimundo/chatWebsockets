new function() {
	var ws = null;
	var connected = false;

	var serverUrl;
	var connectionStatus;
	var sendMessage;

	var connectButton;
	var disconnectButton;
	var sendButton;

	var open = function() {
		var url = serverUrl.val();
		ws = new WebSocket(url);
		ws.onopen = onOpen;
		ws.onclose = onClose;
		ws.onmessage = onMessage;
		ws.onerror = onError;

		connectionStatus.text('ABRINDO...');
		serverUrl.attr('disabled', 'disabled');
		connectButton.hide();
		disconnectButton.show();
	}

	var close = function() {
		if (ws) {
			console.log('PARANDO...');
			ws.close();
		}
		connected = false;
		connectionStatus.text('PARADO');

		serverUrl.removeAttr('disabled');
		connectButton.show();
		disconnectButton.hide();
		sendMessage.attr('disabled', 'disabled');
		sendButton.attr('disabled', 'disabled');
	}

	var clearLog = function() {
		$('#messages').html('');
	}

	var onOpen = function(event) {
		console.log('OPENED: ' + serverUrl.val());
		connected = true;
		connectionStatus.text('EXECUTANDO');
		sendMessage.removeAttr('disabled');
		sendButton.removeAttr('disabled');

    var data = event.data;
		addMessage(data);
	};

	var onClose = function() {
		console.log('CLOSED: ' + serverUrl.val());
		ws = null;
	};

	var onMessage = function(event) {
		var data = event.data;
		addMessage(data);
	};

	var onError = function(event) {
		alert('Ocorreu um erro: ' +event.data+ '. Tente novamente.');
	}

	var addMessage = function(data, type) {
		var msg = $('<pre>').text(data);
		if (type === 'SENT') {
			msg.addClass('sent');
		}

		var messages = $('#messages');
		var historico = $('#log');

		if(data.charAt(0)==='$'){
			$('#onlineUsers').html('');
			$('#onlineUsers').html(msg);
		} else if(data.charAt(0)==='-'){
			messages.append(msg);
			var msgBox = messages.get(0);
			while (msgBox.childNodes.length > 1000) {
				msgBox.removeChild(msgBox.firstChild);
			}
			msgBox.scrollTop = msgBox.scrollHeight;
		} else {
			historico.append(msg);
		}
	}

	WebSocketClient = {
		init: function() {
			serverUrl = $('#serverUrl');
			connectionStatus = $('#connectionStatus');
			sendMessage = $('#sendMessage');

			connectButton = $('#connectButton');
			disconnectButton = $('#disconnectButton');
			sendButton = $('#sendButton');

			connectButton.click(function(e) {
				close();
				open();
			});

			disconnectButton.click(function(e) {
				close();
			});

			sendButton.click(function(e) {
				var msg = $('#sendMessage').val();
				addMessage(msg, 'SENT');
				ws.send(msg);
			});

			$('#clearMessage').click(function(e) {
				clearLog();
			});

			var isCtrl;
			sendMessage.keyup(function (e) {
				if(e.which == 17) isCtrl=false;
			}).keydown(function (e) {
				if(e.which == 17) isCtrl=true;
				if(e.which == 13 && isCtrl == true) {
					sendButton.click();
					return false;
				}
			});
		}
	};
}

$(function() {
	WebSocketClient.init();
});
