# QuickOrder

## Documento de Análisis y Diseño Inicial

### Versión

1.0

### Autor

Equipo de Desarrollo QuickOrder

---

# 1. Introducción

## 1.1 Descripción General

QuickOrder es una aplicación móvil para la gestión integral de pedidos en restaurantes, diseñada para optimizar los procesos de atención, administración de productos, control de mesas y seguimiento de ventas.

La solución busca reemplazar procesos manuales mediante una plataforma móvil intuitiva que permita operar tanto en línea como fuera de línea.

## 1.2 Objetivos

### Objetivo General

Desarrollar una aplicación móvil que permita gestionar eficientemente pedidos, productos, mesas y ventas dentro de establecimientos gastronómicos.

### Objetivos Específicos

* Reducir errores en la toma de pedidos.
* Mejorar los tiempos de atención.
* Facilitar el control de ventas.
* Permitir operación offline.
* Proporcionar métricas para la toma de decisiones.

---

# 2. Contexto del Problema

## 2.1 Problemática Actual

Muchos restaurantes pequeños y medianos continúan utilizando:

* Pedidos en papel.
* Control manual de ventas.
* Gestión desorganizada de productos.
* Procesos lentos de atención.

Esto genera:

* Errores frecuentes.
* Pérdidas económicas.
* Retrasos en el servicio.
* Baja productividad operativa.

## 2.2 Solución Propuesta

QuickOrder digitaliza el proceso de atención mediante:

* Gestión de pedidos.
* Administración de productos.
* Control de mesas.
* Sistema de promociones.
* Dashboard de ventas.
* Funcionamiento offline.

---

# 3. Investigación de Mercado (Benchmarking)

## 3.1 Panorama del Mercado

Las aplicaciones POS han crecido debido a:

* Transformación digital de negocios.
* Uso de dispositivos móviles.
* Necesidad de operación offline.
* Automatización de procesos.

## 3.2 Competidores Analizados

### RestauPOS

#### Fortalezas

* Gestión completa de pedidos.
* Control de inventario.
* Reportes avanzados.

#### Debilidades

* Curva de aprendizaje alta.
* Costos adicionales.
* Consumo elevado de recursos.

### RestoKeep POS

#### Fortalezas

* Facilidad de uso.
* Gestión rápida de pedidos.
* Soporte multiusuario.

#### Debilidades

* Personalización limitada.
* Estadísticas básicas.

### Restaurant POS Pro

#### Fortalezas

* Administración integral.
* Reportes detallados.
* Gestión de clientes.

#### Debilidades

* Interfaz compleja.
* Licenciamiento costoso.

## 3.3 Ventajas Competitivas de QuickOrder

* Operación offline.
* Interfaz simple.
* Dashboard visual.
* Gestión mediante QR.
* Arquitectura moderna Android.
* Sin dependencia permanente de internet.

---

# 4. Modelo de Negocio

## 4.1 Tipo de Negocio

### B2B SaaS

Software orientado a restaurantes mediante suscripción.

## 4.2 Propuesta de Valor

QuickOrder permite:

* Digitalizar pedidos.
* Controlar ventas.
* Gestionar promociones.
* Analizar métricas.

## 4.3 Cliente Objetivo

* Restaurantes pequeños.
* Restaurantes medianos.
* Cafeterías.
* Negocios gastronómicos.

---

# 5. Investigación de Usuarios

## 5.1 Persona 1 – Mesero Operativo

### Perfil

* Nombre: Carlos Mendoza
* Edad: 24 años
* Experiencia tecnológica: Media

### Necesidades

* Registrar pedidos rápidamente.
* Consultar promociones.
* Calcular totales automáticamente.
* Reducir errores.

### Escenario de Uso

Registrar pedidos de una mesa y enviarlos sin errores.

---

## 5.2 Persona 2 – Administradora

### Perfil

* Nombre: Laura Pérez
* Edad: 35 años
* Experiencia tecnológica: Alta

### Necesidades

* Supervisar ventas.
* Gestionar productos.
* Analizar métricas.
* Crear promociones.

### Escenario de Uso

Consultar indicadores de rendimiento diarios.

---

# 6. Requerimientos del Sistema

## 6.1 Requerimientos Funcionales

### Autenticación

* Iniciar sesión.
* Registrarse.
* Recuperar acceso.

### Gestión de Productos

* Crear productos.
* Editar productos.
* Eliminar productos.
* Listar productos.

### Gestión de Pedidos

* Crear pedido.
* Modificar pedido.
* Cancelar pedido.
* Consultar estado.

### Gestión de Mesas

* Crear mesa.
* Editar mesa.
* Asociar pedidos.

### Gestión de Ventas

* Registrar venta.
* Consultar historial.
* Generar métricas.

### Promociones

* Crear promociones.
* Aplicar descuentos.
* Gestionar vigencia.

### Administración

* Dashboard.
* Estadísticas.
* Configuración.

---

## 6.2 Requerimientos No Funcionales

### Rendimiento

* Tiempo de carga menor a 2 segundos.

### Disponibilidad

* Funcionamiento offline.

### Seguridad

* Gestión de sesiones.
* Persistencia segura.

### Usabilidad

* Diseño intuitivo.
* Flujo simplificado.

### Accesibilidad

* Contraste adecuado.
* Compatibilidad con TalkBack.
* Componentes accesibles.

---

# 7. Flujos de Usuario

## 7.1 Flujo del Comensal

1. Escanear QR.
2. Abrir menú.
3. Seleccionar productos.
4. Confirmar pedido.
5. Consultar estado.

## 7.2 Flujo del Mesero

1. Recibir pedido.
2. Gestionar atención.
3. Actualizar estado.
4. Finalizar servicio.

## 7.3 Flujo del Administrador

1. Gestionar catálogo.
2. Gestionar promociones.
3. Supervisar ventas.
4. Analizar métricas.

---

# 8. Diseño UX/UI

## Principios Aplicados

### Diseño Centrado en el Usuario

* Simplicidad.
* Eficiencia.
* Consistencia.

### Usabilidad

* Navegación clara.
* Acciones rápidas.
* Feedback visual.

### Accesibilidad

* WCAG.
* TalkBack.
* Contraste AA.

### Referencia Visual

Los diseños se basarán en los prototipos definidos dentro de:

docs/prototipos

---

# 9. Arquitectura del Sistema

## Patrón Arquitectónico

MVVM + Clean Architecture

## Tecnologías

* Kotlin
* Jetpack Compose
* Room
* DataStore
* Hilt
* Navigation Compose
* Firebase (sincronización)

## Estructura del Proyecto

data/
├── local/
├── datastore/
└── repository/

domain/
├── model/
├── repository/
└── usecase/

ui/
├── screens/
├── components/
└── navigation/

viewmodel/
di/
ui/theme/

---

# 10. Persistencia de Datos

## Base de Datos Local

Room Database

### Tablas Maestras

* Productos
* Mesas

### Tabla Transaccional

* Pedidos

## Persistencia Ligera

DataStore

Configuraciones:

* Tema.
* Preferencias.
* Sesión.

---

# 11. Sistema QR y Operación Offline

## Objetivo

Permitir acceso al menú sin conexión permanente a internet.

## Solución

QR + Deep Links + Room Database

### Flujo

Administrador
↓
Carga menú
↓
Room Database
↓
Genera QR por mesa

Comensal
↓
Escanea QR
↓
Deep Link
↓
MenuScreen
↓
Consulta Room
↓
Visualiza menú

## Beneficios

* Acceso rápido.
* Menor dependencia de internet.
* Mejor experiencia de usuario.

---

# 12. Sincronización de Datos

## Estrategia

Firebase Firestore + Room

### Room

Fuente local de datos.

### Firebase

Sincronización remota.

### Beneficios

* Operación offline.
* Sincronización automática.
* Dashboard en tiempo real.

---

# 13. Conclusiones

QuickOrder propone una solución moderna para la gestión de restaurantes mediante una arquitectura escalable, soporte offline, experiencia de usuario optimizada y sincronización en tiempo real, permitiendo mejorar la productividad operativa y la calidad del servicio.
