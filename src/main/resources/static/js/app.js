const API_URL = "http://localhost:8181/countries";
let phoneSortState = 'default';
let allCountriesData = []; //Tüm ülkeleri hafızada tutacağımız global değişken

// TOP 5 YÜKLEME
async function loadTop5Currencies() {
    try {
        const res = await fetch(`${API_URL}/currency/top5`);
        const json = await res.json();

        if (json.success && json.data) {
            const top5Div = document.getElementById('top5Currencies');
            top5Div.innerHTML = ""; // Yükleniyor animasyonunu temizle

            for (const [code, rate] of Object.entries(json.data)) {
                top5Div.innerHTML += `
                <div onclick="filterByCurrency('${code}')" class="currency-card">
                    <span class="code">${code}</span>
                    <span class="rate">₺${rate.toFixed(2)}</span>
                </div>
                `;
            }
        }
    } catch (e) {
        console.error("Top 5 yüklenemedi", e);
    }
}

// PARA BİRİMİNE GÖRE FİLTRELEME
function filterByCurrency(currencyCode) {
    const filteredCountries = allCountriesData.filter(c => c.currency === currencyCode);
    renderTable(filteredCountries);
}

// VERİ YÜKLEME (herkese açık, token gerekmiyor)
async function loadAllCountries() {
    try {
        const res = await fetch(`${API_URL}/all`);
        const json = await res.json();
        if (json.success) {
            allCountriesData = json.data;
            renderTable(allCountriesData);
        }
    } catch (e) {
        alert("Bağlantı hatası!");
    }
}

async function getCountry() {
    const code = document.getElementById('countryCode').value;
    if (!code) return;
    try {
        const res = await fetch(`${API_URL}/getcountry/${code}`);
        const json = await res.json();
        json.success ? renderTable([json.data]) : alert(json.message);
    } catch (e) {
        console.error(e);
    }
}

// SIRALAMA
async function togglePhoneSort() {
    const icon = document.getElementById('sortIcon');
    if (phoneSortState === 'default') {
        phoneSortState = 'asc'; icon.innerHTML = ' ↑';
        await fetchSorted('asc');
    } else if (phoneSortState === 'asc') {
        phoneSortState = 'desc'; icon.innerHTML = ' ↓';
        await fetchSorted('desc');
    } else {
        phoneSortState = 'default'; icon.innerHTML = '';
        await loadAllCountries();
    }
}

async function fetchSorted(order) {
    const res = await fetch(`${API_URL}/phoneCodes?order=${order}`);
    const json = await res.json();
    if (json.success) renderTable(json.data);
}

// TABLO ÇİZİMİ
function renderTable(data) {
    const tbody = document.getElementById('countryTableBody');
    tbody.innerHTML = "";

    // Sadece giriş yapmış kullanıcılar düzenle/sil butonlarını görsün.
    // (Not: Bu sadece arayüz kozmetiği - asıl güvenlik zaten backend'deki @PreAuthorize'da.
    //  Butonu göstermesen de saklamasan da, token'sız/yetkisiz istek backend'de reddedilir.)
    const canManage = isLoggedIn();

    data.forEach(c => {
        const phoneDisplay = c.phone ? `+${c.phone}` : 'N/A';

        const actionButtons = canManage ? `
            <button onclick="event.stopPropagation(); window.location.href='/edit-country.html?code=${c.code}'" 
                    class="btn-sm btn-edit">Düzenle</button>
            <button onclick="event.stopPropagation(); deleteCountry('${c.code}')" 
                    class="btn-sm btn-delete">Sil</button>
        ` : `
            <span class="text-xs text-gray-400 italic">Düzenlemek için giriş yapın</span>
        `;

        tbody.innerHTML += `
        <tr onclick="showDetails('${c.code}')">
            <td>${c.name}</td>
            <td>${c.code}</td>
            <td class="font-mono">${phoneDisplay}</td>
            <td class="text-right">
                <div class="actions-cell">
                    ${actionButtons}
                </div>
            </td>
        </tr>`;
    });
}

// MODAL YÖNETİMİ
async function showDetails(code) {
    try {
        const res = await fetch(`${API_URL}/getcountry/${code}`);
        const json = await res.json();

        if (json.success) {
            const c = json.data;
            const langs = (c.languages && c.languages.length > 0) ? c.languages.map(l => l.code).join(' - ') : 'Bilgi yok';

            let currencyValue = '<span class="text-gray-500 italic">Hesaplanıyor...</span>';

            document.getElementById('modalCountryName').innerText = c.name;
            document.getElementById('modalContent').innerHTML = `
            <div class="space-y-2 border-t pt-4">
                <p><strong>Yerel Ad:</strong> ${c.nativeName || '-'}</p>
                <p><strong>Ülke Kodu:</strong> <span class="bg-blue-100 px-2 py-1 rounded text-blue-800">${c.code}</span></p>
                <p><strong>Telefon Kodu:</strong> <span class="font-mono text-green-700">+${c.phone}</span></p>
                <p><strong>Başkent:</strong> ${c.capital || '-'}</p>
                <p><strong>Kıta:</strong> ${c.continent || '-'}</p>
                <p><strong>Diller:</strong> ${langs}</p>
                <p><strong>Para Birimi:</strong> <span class="font-bold text-gray-800">${c.currency || '-'}</span></p>
                <p class="text-purple-700 font-bold bg-purple-50 p-2 rounded shadow-inner" id="liveCurrencyRate">
                    <strong>Canlı Kur (TRY):</strong> ${currencyValue}
                </p>
                <p class="text-3xl mt-4"><strong>Bayrak:</strong> ${c.flag || '-'}</p>
            </div>
        `;
            document.getElementById('countryModal').classList.remove('hidden');

            if (c.currency) {
                const cleanCurrency = c.currency.split(',')[0].trim();

                try {
                    const rateRes = await fetch(`${API_URL}/currency/rate?code=${cleanCurrency}`);
                    const rateJson = await rateRes.json();

                    if (rateJson.success) {
                        document.getElementById('liveCurrencyRate').innerHTML = `
                <strong>Canlı Kur (TRY):</strong> 1 ${cleanCurrency} = ₺${rateJson.data.toFixed(2)}
            `;
                    } else {
                        document.getElementById('liveCurrencyRate').innerHTML = `
                <strong>Canlı Kur (TRY):</strong> Kur bilgisi alınamadı (${cleanCurrency})
            `;
                    }
                } catch (e) {
                    console.error("Kur çekme hatası:", e);
                    document.getElementById('liveCurrencyRate').innerHTML = `<strong>Canlı Kur (TRY):</strong> Hata oluştu`;
                }
            }
        }
    } catch (e) {
        console.error("Detay hatası:", e);
    }
}

// SİLME — token gerektiren korumalı işlem
async function deleteCountry(code) {
    if (!confirm('Emin misiniz?')) return;

    if (!isLoggedIn()) {
        alert("Bu işlem için giriş yapmanız gerekiyor.");
        window.location.href = '/login.html';
        return;
    }

    try {
        const res = await fetch(`${API_URL}/deletecountry/${code}`, {
            method: 'DELETE',
            headers: authHeaders()
        });

        if (handleAuthError(res.status)) return;

        const json = await res.json();
        alert(json.message);
        loadAllCountries();
    } catch (e) {
        alert("Silme işlemi sırasında bir hata oluştu.");
    }
}

function closeModal() { document.getElementById('countryModal').classList.add('hidden'); }

// Sayfa yüklendiğinde hem tabloyu hem de Top 5'i getir, üst kısımdaki auth durumunu göster
window.onload = () => {
    renderAuthStatus();
    loadAllCountries();
    loadTop5Currencies();
};