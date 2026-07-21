// ===================== AUTH YARDIMCI DOSYASI =====================
// Bu dosyayı /js/auth.js olarak kaydet, tüm sayfalarda <script src="/js/auth.js"></script>
// ile en üstte (app.js'ten ÖNCE) çağır.

const AUTH_API_URL = "http://localhost:8181/api/auth";

// ---- Token'ı saklama / okuma ----
function saveToken(token, username) {
    localStorage.setItem('jwt_token', token);
    localStorage.setItem('jwt_username', username);
}

function getToken() {
    return localStorage.getItem('jwt_token');
}

function getUsername() {
    return localStorage.getItem('jwt_username');
}

function clearToken() {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('jwt_username');
}

function isLoggedIn() {
    return !!getToken();
}

// ---- Korumalı isteklerde kullanılacak header üretici ----
// fetch çağrılarında: fetch(url, { headers: authHeaders() })
function authHeaders(extra = {}) {
    const token = getToken();
    return {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...extra
    };
}

// ---- Logout ----
async function logout() {
    try {
        await fetch(`${AUTH_API_URL}/logout`, {
            method: 'POST',
            headers: authHeaders()
        });
    } catch (e) {
        console.error("Logout isteği başarısız, yine de local token temizleniyor", e);
    } finally {
        clearToken();
        window.location.href = '/login.html';
    }
}

// ---- Sayfa üstüne login durumunu yansıtan küçük bir header widget'ı ----
// index.html gibi sayfalarda, body'nin en üstüne bir <div id="authStatus"></div> koyup
// window.onload içinde renderAuthStatus() çağırman yeterli.
function renderAuthStatus() {
    const el = document.getElementById('authStatus');
    if (!el) return;

    if (isLoggedIn()) {
        el.innerHTML = `
            <span>👤 <strong>${getUsername()}</strong> olarak giriş yaptınız</span>
            <button onclick="logout()" class="btn-sm btn-delete" style="margin-left:10px;">Çıkış Yap</button>
        `;
    } else {
        el.innerHTML = `
            <a href="/login.html" class="btn btn-info">Giriş Yap</a>
            <a href="/register.html" class="btn btn-info">Kayıt Ol</a>
        `;
    }
}

// ---- 401/403 durumunda uygun uyarıyı ver ----
// 401 = giriş yapılmamış/token geçersiz -> login'e yönlendir
// 403 = giriş yapılmış AMA yetki (rol) yetersiz -> sadece bilgilendir, login'e atma
function handleAuthError(status) {
    if (status === 401) {
        alert("Bu işlem için giriş yapmanız gerekiyor.");
        clearToken(); // token geçersiz/süresi dolmuş olabilir, temizle
        window.location.href = '/login.html';
        return true;
    }
    if (status === 403) {
        alert("Bu işlemi yapmak için yetkiniz yok (Admin rolü gerekiyor).");
        return true;
    }
    return false;
}