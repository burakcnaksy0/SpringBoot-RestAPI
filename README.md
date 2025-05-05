### 🌐 Spring Boot REST API - İl Yönetimi Projesi

#### 1. Temel Bileşenler ve İşleyiş

**Entity (`Il.java`)**

* `@Entity`: Veritabanı tablosu temsilidir.
* `@Id`: Primary key alanıdır.
* `@NotNull`: `name` alanı boş bırakılamaz.
* **Lombok Anotasyonları**: `@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`

**Repository (`ILRepository.java`)**

* `JpaRepository`: CRUD işlemlerini sağlar.
* `findByName`, `findAllByName`: İsme göre filtreleme yapılır.

**Service (`ILService.java`)**

* İş kuralları burada uygulanır.
* `createIl`: Aynı isimde il varsa `IlAlreadyExistsException` fırlatır.
* `getIlById`: İl bulunamazsa `IlNotFoundException` fırlatır.
* `@AllArgsConstructor`: `ILRepository` otomatik enjekte edilir.

**Controller (`ILController.java`)**

* `GET /iller`: Tüm illeri ya da filtreli listeyi döner (`@RequestParam name`).
* `GET /iller/{id}`: Belirli ID'ye sahip ili getirir.
* `POST /iller`: Yeni il oluşturur (`@Valid` ile validasyon).
* `PUT /iller/{id}`: İl bilgisini günceller.
* `DELETE /iller/{id}`: İli siler.

**Exception Handling**

* `@ExceptionHandler`: Controller içinde hata yakalama.
* `@ControllerAdvice` (`ILControllerExceptionHandler`): Global hata yönetimi sağlar (ör. `IlNotFoundException`).

#### 2. Senaryo Akışı

**Yeni İl Ekleme (POST /iller)**

1. Kullanıcı JSON gönderir: `{ "id": "06", "name": "Ankara" }`
2. `@Valid` → `@NotNull` kontrolü.
3. Service `createIl` çağrılır:

   * Aynı isimde il varsa → `IlAlreadyExistsException` (şu an 404, ancak 409 CONFLICT daha uygundur).
   * Boş name → `ILNameValueCannotNullException` → 400 BAD\_REQUEST
   * Kayıt yoksa → `ilRepository.save()`

**İl Getirme (GET /iller/{id})**

1. Controller → `ILService.getIlById("06")`
2. Repository → `findById("06")`
3. Bulunamazsa → `IlNotFoundException` → 404 NOT\_FOUND (global handler yakalar)

#### 3. Kritik Noktalar

**Validation**

* `@NotNull` + `@Valid` + Service seviyesinde manuel kontrol

**Hata Yönetimi**

* Lokal (`@ExceptionHandler`) ve global (`@ControllerAdvice`) birlikte kullanılmış.
* `IlAlreadyExistsException` için 409 CONFLICT kullanılmalı (şu an 404).

**HTTP Durum Kodları**

* `POST` başarı: 201 CREATED
* `PUT/DELETE` başarı: 200 OK
* Hatalar: 404, 400, (veya 409)

#### 4. Teknik Detaylar

* **Lombok**: Kod tekrarını azaltır
* **Spring Data JPA**: CRUD işlemleri kolaylaştırır
* **Katmanlı Mimari**: Controller → Service → Repository

> Bu yapı, Spring Boot uygulamalarının standartlarına uygun, genişletilebilir ve sürdürülebilir bir temel sağlar.

# Spring Boot - ResponseEntity Kullanımı

## 🔍 ResponseEntity Nedir?

Spring Boot'ta `ResponseEntity`, HTTP yanıtlarını daha **esnek** ve **kontrollü** şekilde dönebilmemizi sağlayan bir yapıdır. Sadece veri (`body`) değil, aynı zamanda:

* HTTP durum kodu (`status code`)
* Header (başlık bilgileri)
* Body (gövde verisi)

üzerinde tam kontrol sunar.

---

## ✅ Neden ResponseEntity Kullanılır?

### 1. HTTP Durum Kodunu Belirleyebilme

Örneğin:

```java
return new ResponseEntity<>(user, HttpStatus.OK);
```

Duruma göre `200 OK`, `404 NOT_FOUND`, `201 CREATED`, `400 BAD_REQUEST` gibi özel kodlar dönebiliriz.

---

### 2. Header (Başlık) Ekleme

```java
HttpHeaders headers = new HttpHeaders();
headers.add("Custom-Header", "Değer");

return new ResponseEntity<>(user, headers, HttpStatus.OK);
```

Özellikle JWT, CORS, veya özel metadata durumlarında kullanışlıdır.

---

### 3. Anlamlı Hatalar ve Boş Yanıtlar

Eğer istenen veri bulunamadıysa:

```java
return new ResponseEntity<>("Kullanıcı bulunamadı", HttpStatus.NOT_FOUND);
```

---

### 4. RESTful Standartlara Uygunluk

`ResponseEntity`, REST API geliştirirken HTTP standartlarına uygun, daha profesyonel yapılar oluşturmamıza yardımcı olur.

---

## ✨ Karşılaştırma Tablosu

| Dönüş Tipi              | Esneklik | Durum Kodu Kontrolü  | Header Kontrolü |
| ----------------------- | -------- | -------------------- | --------------- |
| `return Object;`        | Düşük    | Hayır (otomatik 200) | Hayır           |
| `return ResponseEntity` | Yüksek   | Evet                 | Evet            |

---

## 📃 Örnek Kullanım:

```java
@GetMapping("/kullanici/{id}")
public ResponseEntity<?> getUser(@PathVariable Long id) {
    Optional<User> user = userService.findById(id);
    if (user.isPresent()) {
        return ResponseEntity.ok(user.get());
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
    }
}
```

---

## 📌 Spring Boot'ta Kullanılan Diğer Önemli Annotation'lar

### 1. `@PathVariable`

`@PathVariable`, URL içinde tanımlı değişkenleri metot parametresi olarak almamızı sağlar.

📌 Örnek:

```java
@GetMapping("/users/{id}")
public User getUserById(@PathVariable Long id) {
    return userService.findById(id);
}
```

📝 Açıklama: `/users/5` çağrıldığında, `id` parametresi `5` olur.

---

### 2. `@RequestParam`

`@RequestParam`, URL üzerindeki query parametrelerini almak için kullanılır.

📌 Örnek:

```java
@GetMapping("/search")
public List<User> searchUsers(@RequestParam String name) {
    return userService.searchByName(name);
}
```

📝 Açıklama: `/search?name=Ali` çağrıldığında, `name` parametresi `Ali` olur.

İsteğe bağlı yapmak için `required = false` kullanılabilir:

```java
@RequestParam(required = false) String name
```

---

### 3. `@ExceptionHandler`

`@ExceptionHandler`, uygulama boyunca oluşabilecek belirli türdeki hataları yakalayarak özel yanıtlar döndürmemizi sağlar.

📌 Örnek:

```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
}
```

📝 Açıklama: Eğer `ResourceNotFoundException` fırlatılırsa, bu metod çalışır ve anlamlı bir hata mesajı döner.

Global hata yönetimi için `@ControllerAdvice` ile birlikte kullanılır:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>("Bir hata oluştu", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

---

## 🌐 HTTP Status Kodları (Durum Kodları)

HTTP durum kodları, istemciye (client) yapılan isteğin sonucunu ifade eder. Spring Boot'ta `HttpStatus` enum'ları ile kolayca kullanılabilir.

### ✅ 1xx - Bilgilendirme

* `100 CONTINUE`: İstek alındı, işlemeye devam et.

### ✅ 2xx - Başarılı Yanıtlar

* `200 OK`: İstek başarıyla işlendi.
* `201 CREATED`: Yeni bir kaynak başarıyla oluşturuldu.
* `204 NO_CONTENT`: Başarılı fakat içerik dönülmüyor.

### ❗ 3xx - Yönlendirmeler

* `301 MOVED_PERMANENTLY`: Kaynak kalıcı olarak başka yere taşındı.
* `302 FOUND`: Geçici yönlendirme.

### ❌ 4xx - İstemci Hataları

* `400 BAD_REQUEST`: Hatalı istek.
* `401 UNAUTHORIZED`: Kimlik doğrulama gerekiyor.
* `403 FORBIDDEN`: Erişim yasak.
* `404 NOT_FOUND`: Kaynak bulunamadı.

### 🛑 5xx - Sunucu Hataları

* `500 INTERNAL_SERVER_ERROR`: Sunucuda beklenmeyen hata.
* `502 BAD_GATEWAY`: Geçersiz yanıt alındı.
* `503 SERVICE_UNAVAILABLE`: Sunucu geçici olarak kullanılamıyor.

### 🔧 Kullanım Örneği:

```java
return new ResponseEntity<>("Veri başarıyla kaydedildi", HttpStatus.CREATED);
```

Spring Boot içinde genellikle `HttpStatus` enum sınıfı ile birlikte `ResponseEntity` içinde kullanılır.

---

## ✉️ Sonuç

`ResponseEntity`, hem basit hem de karmaşık API senaryoları için esnek, standartlara uygun ve anlamlı yanıtlar oluşturmamıza yardımcı olur. `@PathVariable`, `@RequestParam` ve `@ExceptionHandler` gibi annotation'lar da REST API'lerde veri alma ve hata yönetimi açısından çok önemlidir. HTTP durum kodları ise client ile doğru iletişim kurmanın temelini oluşturur.
