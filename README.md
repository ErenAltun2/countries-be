# 🌍 Ülke Bilgi Sistemi (Spring Boot & JPA)

Ülke verilerinin, dillerin ve anlık canlı döviz kurlarının takip edildiği; rol bazlı yetkilendirme (RBAC) ve güvenli kimlik doğrulama altyapısına sahip RESTful backend ve yönetim sistemi.

---

### 📖 Docker & Veritabanı Mimarisi

[![Medium](https://img.shields.io/badge/Medium-Docker%20&%20PostgreSQL-12100E?style=for-the-badge&logo=medium&logoColor=white)](https://medium.com/@eren.alltun/docker-compose-ile-s%C4%B1f%C4%B1rdan-postgresql-kurulumu-ve-pgadmin-intellij-ba%C4%9Flant%C4%B1s%C4%B1-a0fabb8e6b72)

> **Not:** Projenin Docker Compose orkestrasyonu, PostgreSQL container ayağa kaldırma adımları ve IDE veritabanı bağlantısı rehberi için yukarıdaki Medium yazımı inceleyebilirsiniz.

---

### 🖥️ Uygulama Arayüzü & Özellikler

#### Ana Dashboard & Canlı Kurlar
Ülke arama (ISO kodu), listeleme ve canlı döviz kurları paneli:

![Ana Sayfa](images/ana-sayfa.png)

#### Kimlik Doğrulama (Auth Modülü)
Kullanıcı giriş ve kayıt formları:

| Giriş Yap | Kayıt Ol |
| :---: | :---: |
| <img src="images/login-page.png" width="360" alt="Giriş Sayfası" /> | <img src="images/register-page.png" width="360" alt="Kayıt Sayfası" /> |

---

### 🗄️ Veritabanı Yapısı & Güvenlik

#### İlişkisel Tablo Yapısı
Sistemde ülke, dil ve yetkilendirme yönetimini sağlayan tablolar (`Many-to-Many` ilişki için `country_languages` junction tablosu):

![Veritabanı Tabloları](images/db-tables.png)

#### Şifreleme & Rol Bazlı Yetkilendirme (BCrypt & RBAC)
Kullanıcı parolaları veritabanında **BCrypt** algoritmasıyla hash'lenerek saklanır; yetkilendirme `USER` ve `ADMIN` rolleri üzerinden yürütülür:

![BCrypt Parola Hashleme](images/password-hash.png)

---

### 🛠️ Teknolojiler

* **Backend:** Java, Spring Boot, Spring Data JPA, Spring Security
* **Veritabanı:** PostgreSQL
* **Konteynerizasyon:** Docker, Docker Compose
* **Güvenlik:** BCrypt Password Hashing, Rol Bazlı Erişim Kontrolü (RBAC)
