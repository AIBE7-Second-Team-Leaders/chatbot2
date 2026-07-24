<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chatbot 2</title>
    <style>
        :root { font-family: Arial, sans-serif; }
        body { margin: 0; background: #f5f7fb; color: #1f2937; }
        main { width: min(760px, calc(100% - 32px)); margin: 48px auto; }
        h1 { margin-bottom: 8px; }
        .notice { color: #64748b; margin-bottom: 24px; }
        .panel { background: white; border: 1px solid #e2e8f0; border-radius: 16px; padding: 20px; box-shadow: 0 8px 24px #0f172a0d; }
        label { display: block; margin: 12px 0 6px; font-weight: 600; }
        input, textarea, button { width: 100%; box-sizing: border-box; font: inherit; border-radius: 10px; }
        input, textarea { border: 1px solid #cbd5e1; padding: 11px 12px; }
        textarea { min-height: 120px; resize: vertical; }
        button { margin-top: 16px; border: 0; padding: 12px; background: #2563eb; color: white; cursor: pointer; }
        button:disabled { opacity: .6; cursor: wait; }
        #result { margin-top: 20px; white-space: pre-wrap; line-height: 1.6; }
        .error { color: #b91c1c; }
    </style>
</head>
<body>
<main>
    <h1>Chatbot 2</h1>
    <p class="notice">Spring Boot 4 + MySQL + Spring Data JPA + Spring AI</p>
    <section class="panel">
        <label for="userId">사용자 ID</label>
        <input id="userId" placeholder="app_users.user_id 값을 입력하세요">
        <label for="message">메시지</label>
        <textarea id="message" placeholder="메시지를 입력하세요"></textarea>
        <button id="sendButton" type="button">전송</button>
        <div id="result"></div>
    </section>
</main>
<script>
    const contextPath = '${pageContext.request.contextPath}';
    const userId = document.querySelector('#userId');
    const message = document.querySelector('#message');
    const button = document.querySelector('#sendButton');
    const result = document.querySelector('#result');

    button.addEventListener('click', async () => {
        const user = userId.value.trim();
        const text = message.value.trim();
        result.className = '';
        if (!user || !text) {
            result.className = 'error';
            result.textContent = '사용자 ID와 메시지를 입력하세요.';
            return;
        }
        button.disabled = true;
        result.textContent = '응답을 기다리는 중입니다...';
        try {
            const response = await fetch(contextPath + '/api/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json', 'X-User-Id': user },
                body: JSON.stringify({ message: text })
            });
            const body = await response.json().catch(() => ({}));
            if (!response.ok) throw new Error(body.message || '요청에 실패했습니다.');
            const answer = (body.messages || []).findLast(item => item.role === 'ASSISTANT');
            result.textContent = answer ? answer.content : '응답이 없습니다.';
        } catch (error) {
            result.className = 'error';
            result.textContent = error.message;
        } finally {
            button.disabled = false;
        }
    });
</script>
</body>
</html>
