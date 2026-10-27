# Gym Management — aplikacja do zarządzania siłownią

Aplikacja klient–serwer do obsługi użytkowników, trenerów personalnych, treningów indywidualnych, zajęć grupowych oraz karnetów.

Projekt zaliczeniowy z przedmiotu **Zaawansowane programowanie w Javie**.

---

## Spis treści

- [Funkcjonalności](#funkcjonalności)
- [Technologie](#technologie)
- [Wymagania](#wymagania)
- [Konfiguracja bazy danych](#konfiguracja-bazy-danych)
- [Konfiguracja aplikacji](#konfiguracja-aplikacji)
- [Uruchomienie backendu](#uruchomienie-backendu)
- [Uruchomienie frontendu](#uruchomienie-frontendu)
- [Uwierzytelnianie](#uwierzytelnianie)
- [Konta demonstracyjne](#konta-demonstracyjne)
- [Dokumentacja API](#dokumentacja-api)
- [Struktura projektu](#struktura-projektu)
- [Role i uprawnienia](#role-i-uprawnienia)
- [Scenariusz demonstracji](#scenariusz-demonstracji)
- [Testy](#testy)

---

## Funkcjonalności

### USER
- rejestracja i logowanie
- edycja własnego profilu
- przeglądanie trenerów i ich dostępności
- rezerwacja i anulowanie treningów personalnych
- zapis na zajęcia grupowe
- przeglądanie i zakup karnetów
- podgląd własnych rezerwacji i historii karnetów

### TRAINER
- zarządzanie własnymi slotami dostępności
- podgląd zaplanowanych treningów personalnych
- oznaczanie treningów jako zrealizowanych

### ADMIN
- zarządzanie użytkownikami i tworzenie kont trenerów
- tworzenie i anulowanie zajęć grupowych
- zarządzanie typami karnetów
- podgląd zapisów na zajęcia

---

## Technologie

| Warstwa | Stack |
|---------|--------|
| Backend | Java 17, Spring Boot 4.1, Spring Web, Spring Data JPA, Spring Security, JWT |
| Baza | MySQL |
| Walidacja | Bean Validation |
| Dokumentacja API | Springdoc OpenAPI / Swagger UI |
| Build | Gradle |
| Frontend | React + Vite + React Router |

---

## Wymagania

- **JDK 17+**
- **MySQL** (uruchomiony lokalnie)
- **Node.js 18+** i npm (tylko do frontendu)
- IntelliJ IDEA / inny IDE (opcjonalnie)

---

## Konfiguracja bazy danych

1. Uruchom MySQL.
2. Utwórz bazę (lub pozwól na utworzenie przez URL z `createDatabaseIfNotExist` — jeśli używasz takiego parametru):

```sql
CREATE DATABASE IF NOT EXISTS baza_java
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
