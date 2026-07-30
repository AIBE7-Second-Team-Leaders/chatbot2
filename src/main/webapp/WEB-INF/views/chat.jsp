<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chatbot 2</title>
    <style>
        :root {
            font-family: Inter, Pretendard, "Noto Sans KR", Arial, sans-serif;
            color: #172033;
            background: #f7f8fc;
        }

        * { box-sizing: border-box; }
        body { margin: 0; min-height: 100vh; background: #f7f8fc; }
        button, textarea, select { font: inherit; }
        button { cursor: pointer; }

        .app-shell { display: flex; min-height: 100vh; }
        .sidebar {
            width: 292px;
            flex: 0 0 292px;
            display: flex;
            flex-direction: column;
            padding: 18px 14px;
            color: #e8edf7;
            background: #182235;
        }
        .brand-row, .user-row, .chat-header { display: flex; align-items: center; }
        .brand-row { justify-content: space-between; padding: 2px 8px 20px; }
        .brand { color: white; font-size: 20px; font-weight: 800; letter-spacing: -.03em; }
        .new-chat, .sidebar-action {
            border: 1px solid #3b4961;
            border-radius: 10px;
            color: #eef3fb;
            background: #243149;
        }
        .new-chat {
            width: 100%;
            padding: 12px 14px;
            text-align: left;
            font-weight: 700;
        }
        .new-chat:hover, .sidebar-action:hover, .conversation-item:hover { background: #2c3b56; }
        .section-label { margin: 26px 8px 9px; color: #91a0b8; font-size: 12px; font-weight: 700; text-transform: uppercase; letter-spacing: .08em; }
        .conversation-list { flex: 1; overflow-y: auto; padding-right: 2px; }
        .conversation-item {
            display: flex;
            align-items: center;
            gap: 8px;
            width: 100%;
            margin-bottom: 4px;
            padding: 10px 9px;
            border: 0;
            border-radius: 9px;
            color: #e8edf7;
            background: transparent;
            text-align: left;
        }
        .conversation-item.active { background: #30415e; }
        .conversation-title { overflow: hidden; flex: 1; white-space: nowrap; text-overflow: ellipsis; font-size: 14px; }
        .conversation-actions { display: none; gap: 2px; }
        .conversation-item:hover .conversation-actions, .conversation-item.active .conversation-actions { display: flex; }
        .icon-button { width: 26px; height: 26px; padding: 0; border: 0; border-radius: 6px; color: #bdc9dc; background: transparent; }
        .icon-button:hover { color: white; background: #415473; }
        .sidebar-bottom { margin-top: 14px; padding-top: 14px; border-top: 1px solid #34425a; }
        .user-row { justify-content: space-between; gap: 10px; padding: 8px; }
        .user-name { overflow: hidden; white-space: nowrap; text-overflow: ellipsis; font-size: 14px; font-weight: 700; }
        .logout-button { padding: 7px 10px; border: 0; border-radius: 7px; color: #cbd5e1; background: #2c3b56; font-size: 12px; }

        .main { display: flex; flex: 1; min-width: 0; flex-direction: column; }
        .chat-header { justify-content: space-between; min-height: 78px; padding: 18px 30px; border-bottom: 1px solid #e5e9f1; background: rgba(255,255,255,.78); }
        .header-title { min-width: 0; }
        .header-title h1 { overflow: hidden; margin: 0; white-space: nowrap; text-overflow: ellipsis; font-size: 20px; }
        .header-title p { margin: 5px 0 0; color: #7a879b; font-size: 13px; }
        .model-select { padding: 9px 12px; border: 1px solid #d8dfeb; border-radius: 9px; color: #334155; background: white; }
        .mobile-menu { display: none; padding: 7px 10px; border: 1px solid #d8dfeb; border-radius: 8px; background: white; }
        .messages { width: min(900px, calc(100% - 48px)); flex: 1; margin: 0 auto; padding: 28px 0 150px; }
        .empty-state { padding: 18vh 20px 0; color: #8090a5; text-align: center; }
        .empty-state h2 { margin: 0 0 10px; color: #243149; font-size: 26px; }
        .message { display: flex; gap: 14px; margin: 0 0 24px; }
        .avatar { flex: 0 0 34px; width: 34px; height: 34px; padding-top: 8px; border-radius: 50%; color: white; background: #3267e8; font-size: 12px; font-weight: 800; text-align: center; }
        .message.assistant .avatar { background: #17a673; }
        .message-body { min-width: 0; flex: 1; }
        .message-role { margin: 2px 0 6px; color: #738198; font-size: 12px; font-weight: 800; }
        .message-content { white-space: pre-wrap; line-height: 1.75; overflow-wrap: anywhere; }
        .composer-wrap { position: fixed; right: 0; bottom: 0; left: 292px; padding: 18px 30px 24px; background: linear-gradient(transparent, #f7f8fc 22%); }
        .composer { display: flex; align-items: flex-end; gap: 10px; width: min(900px, 100%); margin: 0 auto; padding: 10px; border: 1px solid #d8dfeb; border-radius: 15px; background: white; box-shadow: 0 8px 30px rgba(40, 58, 90, .08); }
        textarea { min-height: 46px; max-height: 150px; flex: 1; padding: 11px 12px; resize: none; border: 0; outline: 0; line-height: 1.5; }
        .send-button { width: 46px; height: 42px; border: 0; border-radius: 10px; color: white; background: #2864e8; font-weight: 800; }
        .send-button:disabled { opacity: .55; cursor: wait; }
        .error { width: min(900px, calc(100% - 48px)); margin: -125px auto 100px; color: #c0392b; font-size: 14px; }

        @media (max-width: 760px) {
            .sidebar { position: fixed; z-index: 10; top: 0; bottom: 0; left: -292px; transition: left .2s ease; }
            .sidebar.open { left: 0; box-shadow: 12px 0 30px #18223555; }
            .chat-header { padding: 14px 16px; gap: 10px; }
            .mobile-menu { display: block; }
            .model-select { max-width: 130px; }
            .messages { width: calc(100% - 32px); padding-top: 22px; }
            .composer-wrap { left: 0; padding: 12px 16px 16px; }
            .error { width: calc(100% - 32px); }
        }
    </style>
</head>
<body>
<div class="app-shell">
    <aside class="sidebar" id="sidebar">
        <div class="brand-row">
            <div class="brand">Chatbot 2</div>
            <button class="sidebar-action" id="closeMenu" type="button" aria-label="메뉴 닫기">×</button>
        </div>
        <button class="new-chat" id="newChat" type="button">＋ 새 대화</button>
        <div class="section-label">이전 대화</div>
        <nav class="conversation-list" id="conversationList" aria-label="이전 대화 목록"></nav>
        <div class="sidebar-bottom">
            <div class="user-row">
                <span class="user-name">${sessionScope.loginUserName}님</span>
                <form action="${pageContext.request.contextPath}/auth/logout" method="post">
                    <button class="logout-button" type="submit">로그아웃</button>
                </form>
            </div>
        </div>
    </aside>

    <main class="main">
        <header class="chat-header">
            <button class="mobile-menu" id="openMenu" type="button" aria-label="메뉴 열기">☰</button>
            <div class="header-title">
                <h1 id="conversationHeading">새 대화</h1>
                <p>원하는 AI 모델을 선택해 대화를 시작하세요.</p>
            </div>
            <select class="model-select" id="model" aria-label="AI 모델 선택">
                <option value="groq">Groq</option>
                <option value="gemini">Google Gemini</option>
                <option value="nim">NVIDIA NIM</option>
            </select>
        </header>

        <section class="messages" id="messages">
            <div class="empty-state" id="emptyState">
                <h2>무엇을 도와드릴까요?</h2>
                <p>메시지를 입력하면 여기에 대화가 표시됩니다.</p>
            </div>
        </section>
        <div class="error" id="error" hidden></div>

        <div class="composer-wrap">
            <div class="composer">
                <textarea id="message" rows="1" placeholder="메시지를 입력하세요..." aria-label="메시지 입력"></textarea>
                <button class="send-button" id="sendButton" type="button" aria-label="메시지 전송">↑</button>
            </div>
        </div>
    </main>
</div>

<script>
    const contextPath = '${pageContext.request.contextPath}';
    const list = document.querySelector('#conversationList');
    const messages = document.querySelector('#messages');
    const emptyState = document.querySelector('#emptyState');
    const heading = document.querySelector('#conversationHeading');
    const messageInput = document.querySelector('#message');
    const sendButton = document.querySelector('#sendButton');
    const errorBox = document.querySelector('#error');
    const sidebar = document.querySelector('#sidebar');
    let currentConversationId = null;
    let conversations = [];

    async function request(path, options) {
        const response = await fetch(contextPath + path, options || {});
        const body = await response.json().catch(() => null);
        if (!response.ok) {
            throw new Error(body && (body.detail || body.message || body.title) || '요청에 실패했습니다.');
        }
        return body;
    }

    function showError(error) {
        errorBox.textContent = error.message || String(error);
        errorBox.hidden = false;
    }

    function clearError() { errorBox.hidden = true; errorBox.textContent = ''; }

    function renderConversations() {
        list.textContent = '';
        if (!conversations.length) {
            const empty = document.createElement('div');
            empty.style.cssText = 'padding:12px 8px;color:#91a0b8;font-size:13px;';
            empty.textContent = '아직 대화가 없습니다.';
            list.appendChild(empty);
            return;
        }

        conversations.forEach(conversation => {
            const item = document.createElement('div');
            item.className = 'conversation-item' + (conversation.conversationId === currentConversationId ? ' active' : '');
            item.title = conversation.title;

            const title = document.createElement('span');
            title.className = 'conversation-title';
            title.textContent = conversation.title;
            item.appendChild(title);

            const actions = document.createElement('span');
            actions.className = 'conversation-actions';

            const rename = document.createElement('button');
            rename.className = 'icon-button';
            rename.type = 'button';
            rename.title = '제목 수정';
            rename.textContent = '✎';
            rename.addEventListener('click', event => { event.stopPropagation(); renameConversation(conversation); });

            const remove = document.createElement('button');
            remove.className = 'icon-button';
            remove.type = 'button';
            remove.title = '대화 삭제';
            remove.textContent = '🗑';
            remove.addEventListener('click', event => { event.stopPropagation(); deleteConversation(conversation); });

            actions.append(rename, remove);
            item.appendChild(actions);
            item.addEventListener('click', () => openConversation(conversation.conversationId));
            list.appendChild(item);
        });
    }

    function renderMessages(items) {
        messages.textContent = '';
        if (!items || !items.length) {
            messages.appendChild(emptyState);
            emptyState.hidden = false;
            return;
        }
        items.forEach(item => {
            const row = document.createElement('article');
            row.className = 'message ' + (item.role === 'ASSISTANT' ? 'assistant' : 'user');
            const avatar = document.createElement('div');
            avatar.className = 'avatar';
            avatar.textContent = item.role === 'ASSISTANT' ? 'AI' : '나';
            const body = document.createElement('div');
            body.className = 'message-body';
            const role = document.createElement('div');
            role.className = 'message-role';
            role.textContent = item.role === 'ASSISTANT' ? (item.modelName || 'AI') : '나';
            const content = document.createElement('div');
            content.className = 'message-content';
            content.textContent = item.content;
            body.append(role, content);
            row.append(avatar, body);
            messages.appendChild(row);
        });
        scrollToLatestMessage();
    }

    function scrollToLatestMessage() {
        const latestMessage = messages.querySelector('.message:last-child');
        if (latestMessage) {
            latestMessage.scrollIntoView({ behavior: 'smooth', block: 'end' });
        }
    }

    async function loadConversations(selectFirst) {
        conversations = await request('/api/conversations');
        renderConversations();
        if (selectFirst && conversations.length) await openConversation(conversations[0].conversationId);
    }

    async function openConversation(id) {
        clearError();
        try {
            const response = await request('/api/conversations/' + encodeURIComponent(id));
            currentConversationId = response.conversation.conversationId;
            heading.textContent = response.conversation.title;
            renderMessages(response.messages);
            renderConversations();
            sidebar.classList.remove('open');
        } catch (error) { showError(error); }
    }

    function startNewChat() {
        currentConversationId = null;
        heading.textContent = '새 대화';
        renderMessages([]);
        clearError();
        messageInput.focus();
        renderConversations();
        sidebar.classList.remove('open');
    }

    async function renameConversation(conversation) {
        const title = window.prompt('새 대화 제목을 입력하세요.', conversation.title);
        if (title === null || !title.trim()) return;
        try {
            await request('/api/conversations/' + encodeURIComponent(conversation.conversationId), {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ title: title.trim() })
            });
            await loadConversations(false);
            if (currentConversationId === conversation.conversationId) heading.textContent = title.trim().slice(0, 200);
        } catch (error) { showError(error); }
    }

    async function deleteConversation(conversation) {
        if (!window.confirm('이 대화를 삭제할까요?')) return;
        try {
            await request('/api/conversations/' + encodeURIComponent(conversation.conversationId), { method: 'DELETE' });
            if (currentConversationId === conversation.conversationId) startNewChat();
            await loadConversations(false);
        } catch (error) { showError(error); }
    }

    async function sendMessage() {
        const text = messageInput.value.trim();
        if (!text || sendButton.disabled) return;
        clearError();
        sendButton.disabled = true;
        try {
            const response = await request('/api/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ message: text, conversationId: currentConversationId, model: document.querySelector('#model').value })
            });
            currentConversationId = response.conversation.conversationId;
            heading.textContent = response.conversation.title;
            messageInput.value = '';
            renderMessages(response.messages);
            await loadConversations(false);
        } catch (error) { showError(error); }
        finally { sendButton.disabled = false; messageInput.focus(); }
    }

    sendButton.addEventListener('click', sendMessage);
    messageInput.addEventListener('keydown', event => {
        if (event.key === 'Enter' && !event.shiftKey) { event.preventDefault(); sendMessage(); }
    });
    document.querySelector('#newChat').addEventListener('click', startNewChat);
    document.querySelector('#openMenu').addEventListener('click', () => sidebar.classList.add('open'));
    document.querySelector('#closeMenu').addEventListener('click', () => sidebar.classList.remove('open'));

    loadConversations(true).catch(showError);
</script>
</body>
</html>
