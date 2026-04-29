const API_URL = "http://localhost:8181/countries";
let phoneSortState = 'default';
let allCountriesData = []; // YENİ: Tüm ülkeleri hafızada tutacağımız global değişken

// TOP 5 YÜKLEME
async function loadTop5Currencies() {
    try {
        const res = await fetch(`${API_URL}/currency/top5`);
        const json = await res.json();

        if (json.success && json.data) {
            const top5Div = document.getElementById('top5Currencies');
            top5Div.innerHTML = ""; // Yükleniyor animasyonunu temizle

            // Backend'den gelen Map objesini dönüyoruz
            for (const [code, rate] of Object.entries(json.data)) {
                top5Div.innerHTML += `
                    <div onclick="filterByCurrency('${code}')" class="bg-white/10 p-3 rounded-lg text-center backdrop-blur-sm border border-white/20 shadow-lg hover:bg-white/30 transition cursor-pointer transform hover:scale-105">
                        <div class="text-sm text-blue-200 font-semibold">${code}</div>
                        <div class="text-xl font-bold text-yellow-400">₺${rate.toFixed(2)}</div>
                    </div>
                `;
            }
        }
    } catch (e) {
        console.error("Top 5 yüklenemedi", e);
    }
}

// PARA BİRİMİNE GÖRE FİLTRELEME (Kutucuğa tıklanınca çalışır)
function filterByCurrency(currencyCode) {
    // Hafızadaki ülkelerden sadece seçilen para birimine sahip olanları ayır
    const filteredCountries = allCountriesData.filter(c => c.currency === currencyCode);
    // Tabloyu filtrelenmiş liste ile yeniden çiz
    renderTable(filteredCountries);
}

// VERİ YÜKLEME
async function loadAllCountries() {
    try {
        const res = await fetch(`${API_URL}/all`);
        const json = await res.json();
        if(json.success) {
            allCountriesData = json.data; // Veriyi global değişkene kopyala (Filtreleme için)
            renderTable(allCountriesData);
        }
    } catch (e) { alert("Bağlantı hatası!"); }
}

async function getCountry() {
    const code = document.getElementById('countryCode').value;
    if(!code) return;
    try {
        const res = await fetch(`${API_URL}/getcountry/${code}`);
        const json = await res.json();
        json.success ? renderTable([json.data]) : alert(json.message);
    } catch (e) { console.error(e); }
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
    if(json.success) renderTable(json.data);
}

// TABLO ÇİZİMİ
function renderTable(data) {
    const tbody = document.getElementById('countryTableBody');
    tbody.innerHTML = "";
    data.forEach(c => {
        const phoneDisplay = c.phone ? `+${c.phone}` : 'N/A';

        tbody.innerHTML += `
        <tr class="hover:bg-blue-50 cursor-pointer transition" onclick="showDetails('${c.code}')">
            <td class="px-5 py-5 border-b text-sm font-semibold text-blue-700">${c.name}</td>
            <td class="px-5 py-5 border-b text-sm">${c.code}</td>
            <td class="px-5 py-5 border-b text-sm font-mono">${phoneDisplay}</td>
            <td class="px-5 py-5 border-b text-sm text-right space-x-3">
                <button onclick="event.stopPropagation(); window.location.href='/edit-country.html?code=${c.code}'" class="text-blue-600 font-bold hover:underline">Düzenle</button>
                <button onclick="event.stopPropagation(); deleteCountry('${c.code}')" class="text-red-600 font-bold hover:underline">Sil</button>
            </td>
        </tr>`;
    });
}

// MODAL YÖNETİMİ
async function showDetails(code) {
    try {
        const res = await fetch(`${API_URL}/getcountry/${code}`);
        const json = await res.json();

        if(json.success) {
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

            // Arka planda kura istek at ve HTML'i güncelle
            if (c.currency) {
                const rateRes = await fetch(`${API_URL}/currency/rate?code=${c.currency}`);
                const rateJson = await rateRes.json();
                if(rateJson.success) {
                    document.getElementById('liveCurrencyRate').innerHTML = `<strong>Canlı Kur (TRY):</strong> 1 ${c.currency} = ₺${rateJson.data.toFixed(2)}`;
                } else {
                    document.getElementById('liveCurrencyRate').innerHTML = `<strong>Canlı Kur (TRY):</strong> Bulunamadı`;
                }
            }
        }
    } catch (e) {
        console.error("Detay hatası:", e);
    }
}

async function deleteCountry(code) {
    if(!confirm('Emin misiniz?')) return;
    const res = await fetch(`${API_URL}/deletecountry/${code}`, {method: 'DELETE'});
    const json = await res.json();
    alert(json.message);
    loadAllCountries();
}

function closeModal() { document.getElementById('countryModal').classList.add('hidden'); }

// Sayfa yüklendiğinde hem tabloyu hem de Top 5'i getir
window.onload = () => {
    loadAllCountries();
    loadTop5Currencies();
};