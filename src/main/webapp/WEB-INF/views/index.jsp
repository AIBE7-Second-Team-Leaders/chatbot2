<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chatbot 2 - 로그인</title>

    <style>
        :root {
            font-family: Arial, sans-serif;
        }

        body {
            margin: 0;
            background: #f5f7fb;
            color: #1f2937;
        }

        main {
            width: min(900px, calc(100% - 32px));
            margin: 48px auto;
        }

        h1 {
            margin-bottom: 8px;
        }

        .notice {
            color: #64748b;
            margin-bottom: 24px;
        }

        .message {
            color: #047857;
        }

        .error {
            color: #b91c1c;
        }

        .auth-container {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
        }

        .panel {
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 16px;
            padding: 20px;
            box-shadow: 0 8px 24px #0f172a0d;
        }

        label {
            display: block;
            margin: 12px 0 6px;
            font-weight: 600;
        }

        input,
        button {
            width: 100%;
            box-sizing: border-box;
            font: inherit;
            border-radius: 10px;
        }

        input {
            border: 1px solid #cbd5e1;
            padding: 11px 12px;
        }

        button {
            margin-top: 16px;
            border: 0;
            padding: 12px;
            background: #2563eb;
            color: white;
            cursor: pointer;
        }

        @media (max-width: 700px) {
            .auth-container {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>

<body>
<main>
    <h1>Chatbot 2</h1>
    <p class="notice">로그인 후 챗봇을 이용할 수 있습니다.</p>

    <p class="message">${message}</p>
    <p class="error">${error}</p>

    <div class="auth-container">
        <section class="panel">
            <h2>로그인</h2>

            <form action="${pageContext.request.contextPath}/auth/login"
                  method="post">

                <label for="loginEmail">이메일</label>
                <input
                        id="loginEmail"
                        name="email"
                        type="email"
                        autocomplete="email"
                        required
                >

                <label for="loginPassword">비밀번호</label>
                <input
                        id="loginPassword"
                        name="password"
                        type="password"
                        autocomplete="current-password"
                        required
                >

                <button type="submit">로그인</button>
            </form>
        </section>

        <section class="panel">
            <h2>회원가입</h2>

            <form action="${pageContext.request.contextPath}/auth/signup"
                  method="post">

                <label for="displayName">이름</label>
                <input
                        id="displayName"
                        name="displayName"
                        type="text"
                        required
                >

                <label for="signupEmail">이메일</label>
                <input
                        id="signupEmail"
                        name="email"
                        type="email"
                        autocomplete="email"
                        required
                >

                <label for="signupPassword">비밀번호</label>
                <input
                        id="signupPassword"
                        name="password"
                        type="password"
                        autocomplete="new-password"
                        required
                >

                <button type="submit">회원가입</button>
            </form>
        </section>
    </div>
</main>
</body>
</html>