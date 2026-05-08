# Laboratorio: Quality Gates y Automatización con GitHub Actions
### Unidad 10 — Post-Contenido 2 | Ingeniería de Software

---

## Descripción

Este proyecto implementa un pipeline completo de **análisis estático de código** usando **SonarQube** y **GitHub Actions**. El laboratorio demuestra cómo un Quality Gate obliga al equipo a escribir código limpio y cómo la automatización previene la regresión de calidad en cada push.

---

## Qué resolvió la Refactorización

El código original del `UsuarioService` tenía **5 problemas críticos** detectados por SonarQube:

| # | Problema Original | Severidad | Solución Aplicada |
|---|---|---|---|
| 1 | Contraseña hardcoded `"password123"` | 🔴 Security Hotspot | Eliminada — el método `validarPassword` ahora recibe ambas cadenas como parámetros |
| 2 | Variables sin usar (`contadorNoUsado`, `mensajeDebugging`, `tempPwd`) | 🟡 Code Smell | Eliminadas completamente del código |
| 3 | Alta complejidad ciclomática (7 niveles de `if/else` anidados) | 🟡 Code Smell | Refactorizado con `switch expression` (Java 21) y métodos privados auxiliares `clasificarAdmin()` y `clasificarEstudiante()` |
| 4 | Bloque `catch` vacío sin log | 🔴 Bug | Reemplazado por `log.warn(...)` usando **SLF4J Logger** |
| 5 | Comparación de Strings con `==` | 🔴 Bug | Reemplazado por `.equals()` correctamente |

---

## Métricas Antes vs Después

| Métrica | Antes | Después |
|---|---|---|
| Bugs | 2 | **0** ✅ |
| Code Smells | 8+ | **0** ✅ |
| Security Hotspots | 1 | **0** ✅ |
| Cobertura (JaCoCo) | ~30% | **>70%** ✅ |
| Quality Gate | ❌ FAILED | **✅ PASSED** |

---

## Quality Gate Configurado: `Udes-Quality-Gate`

Se creó un Quality Gate personalizado en SonarQube con las siguientes condiciones de fallo:

- **Bugs > 0** → El proyecto falla automáticamente si introduce cualquier bug
- **Code Smells > 5** → No se permite más de 5 olores de código acumulados

> Ver evidencia: [`docs/config_quality_gate.png`](docs/config_quality_gate.png)

---

## Cómo Ver el Reporte desde Cualquier Push

### 1. Dashboard Local (SonarQube)
```
http://localhost:9000/dashboard?id=calidad-sonarqube
```
Requiere que el contenedor Docker de SonarQube esté corriendo:
```bash
docker start sonarqube
```

### 2. GitHub Actions (Automático en cada push)

Cada vez que haces `git push origin main`, el workflow `.github/workflows/sonar-analysis.yml` se dispara automáticamente y:

1. Configura Java 21 en el runner de Ubuntu
2. Cachea dependencias Maven y SonarQube para mayor velocidad
3. Ejecuta `mvn clean verify sonar:sonar` — corre las 24 pruebas unitarias y envía el reporte

Ver el estado en: `https://github.com/DevvBP/Plata-post2-u10/actions`

---

## Ejecutar el Análisis Manualmente

```bash
mvn clean verify sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=admin \
  -Dsonar.password=admin123
```

---

## Estructura del Proyecto

```
calidad-sonarqube/
├── .github/
│   └── workflows/
│       └── sonar-analysis.yml     ← Pipeline CI automático
├── docs/
│   ├── config_quality_gate.png    ← Evidencia Quality Gate
│   ├── dashboard_passed.png       ← Dashboard en VERDE
│   └── github_actions_success.png ← Actions ejecutándose
├── src/
│   ├── main/java/.../
│   │   ├── model/Usuario.java
│   │   ├── service/UsuarioService.java   ← Clase refactorizada
│   │   └── controller/UsuarioController.java
│   └── test/java/.../
│       └── UsuarioServiceTest.java       ← 24 tests JUnit 5
└── pom.xml                               ← JaCoCo + SonarQube configurados
```

---

## Evidencias del Laboratorio

| Captura | Descripción |
|---|---|
| `docs/config_quality_gate.png` | Quality Gate `Udes-Quality-Gate` configurado |
| `docs/dashboard_passed.png` | Proyecto en **PASSED** con 0 bugs en SonarQube |
| `docs/github_actions_success.png` | Workflow ejecutado con éxito en GitHub |

---

*Laboratorio completado — Unidad 10, Post-Contenido 2*
