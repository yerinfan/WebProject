let socket = new SockJS('/ws');
let stompClient = Stomp.over(socket);

function connect(roomId) {
  stompClient.connect({}, function() {
    stompClient.subscribe('/topic/rooms/' + roomId, function(msg) {
      const message = JSON.parse(msg.body);
      addMessageToList(message);
    });
  });
}

function sendMessage(roomId, author, content) {
  stompClient.send(
    '/app/chat/' + roomId,
    {},
    JSON.stringify({
      sender: author,
      content: content,
      type: 'CHAT'
    })
  );
}

function addMessageToList(message) {
  const container = document.getElementById('messages');

  // 날짜 구분은 생략하고 메시지 박스만 추가
  const bubble = document.createElement('div');
  bubble.className = 'message-bubble';

  const meta = document.createElement('div');
  meta.className = 'meta';
  meta.innerHTML = `<strong>${message.sender}</strong> <span class="timestamp">${formatTime(message.timestamp)}</span>`;
  bubble.appendChild(meta);

  if (message.type === 'IMAGE') {
    const img = document.createElement('img');
    img.src = message.content;
    img.className = 'bubble-image';
    img.onload = () => container.scrollTop = container.scrollHeight;
    bubble.appendChild(img);
  } else {
    const content = document.createElement('div');
    content.className = 'bubble-text';
    content.textContent = message.content;
    bubble.appendChild(content);
  }

  container.appendChild(bubble);
  container.scrollTop = container.scrollHeight;
}

function formatTime(isoString) {
  const date = new Date(isoString);
  const hh = String(date.getHours()).padStart(2, '0');
  const mm = String(date.getMinutes()).padStart(2, '0');
  return `${hh}:${mm}`;
}
