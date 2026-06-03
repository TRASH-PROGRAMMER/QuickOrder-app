# Primera Etapa – QuickOrder

## 1. Descripción de la aplicación

**QuickOrder** es una aplicación móvil que permite a un restaurante gestionar pedidos, productos, mesas y ventas de forma rápida y eficiente.

---

## 2. Presentación y temática escogida para la aplicación

La aplicación desarrollada, denominada **QuickOrder**, es una solución móvil orientada a la gestión de pedidos en restaurantes, concebida como un mini sistema de gestión de pedidos. Su propósito principal es optimizar el proceso de toma de pedidos, gestión de productos y control de ventas, facilitando el trabajo operativo dentro de pequeños y medianos establecimientos gastronómicos.

La temática de la aplicación se centra en la digitalización de procesos tradicionales en restaurantes, específicamente en aquellos negocios que aún gestionan pedidos de forma manual o mediante herramientas poco eficientes. QuickOrder busca modernizar estas operaciones mediante una interfaz intuitiva, rápida y accesible, permitiendo a los usuarios (meseros o administradores) gestionar pedidos en tiempo real desde un dispositivo móvil.

La elección de esta temática surge de la necesidad detectada en numerosos establecimientos gastronómicos que presentan problemas relacionados con errores en los pedidos, tiempos de atención prolongados y dificultades para llevar un control adecuado de las ventas. Mediante la automatización de estos procesos, la aplicación contribuye a mejorar la productividad, reducir errores y optimizar la experiencia tanto del personal como de los clientes.

---

## 3. Análisis Benchmarking

### Panorama general del mercado

Actualmente existen millones de aplicaciones disponibles en Google Play Store, generando una alta competencia en prácticamente todas las categorías. Las aplicaciones de tipo POS (Point of Sale) y gestión de pedidos han experimentado un crecimiento significativo debido a:

* Digitalización de pequeños y medianos negocios.
* Uso de dispositivos móviles como herramientas de gestión.
* Necesidad de operar sin conexión a Internet.
* Automatización de procesos administrativos y operativos.

### Aplicaciones similares

#### 1. RestauPOS

**Puntos fuertes**

* Gestión completa de pedidos.
* Control de inventario.
* Reportes de ventas.
* Interfaz profesional.

**Puntos débiles**

* Requiere curva de aprendizaje.
* Algunas funciones son de pago.
* Consumo elevado de recursos.

#### 2. RestoKeep POS

**Puntos fuertes**

* Fácil de utilizar.
* Gestión rápida de mesas y pedidos.
* Soporte para múltiples usuarios.

**Puntos débiles**

* Personalización limitada.
* Funciones estadísticas básicas.
* Dependencia de conexión para algunas características.

#### 3. Restaurant POS (Pro)

**Puntos fuertes**

* Administración integral del restaurante.
* Reportes detallados.
* Gestión de clientes y productos.

**Puntos débiles**

* Interfaz compleja.
* Costos de licencia elevados.
* Requiere capacitación inicial.

### Diferenciación de QuickOrder

QuickOrder busca diferenciarse de las aplicaciones existentes mediante:

* Funcionamiento offline.
* Gestión de pedidos en tiempo real.
* Interfaz enfocada en la experiencia de usuario (UX).
* Diseño moderno y accesible.
* Simplicidad de uso para pequeños restaurantes.
* Dashboard administrativo con métricas visuales.

---

## 4. Investigación y requisitos de usuario

### Ficha de Persona 1: Mesero Operativo

**Nombre:** Carlos Mendoza
**Edad:** 24 años
**Ocupación:** Mesero en restaurante informal
**Experiencia tecnológica:** Media

Carlos trabaja en un restaurante con alta rotación de clientes, donde la rapidez en la atención es fundamental. Actualmente toma pedidos utilizando papel y bolígrafo, lo que genera errores frecuentes durante las horas de mayor afluencia. Su principal objetivo es atender a los clientes de forma rápida y precisa, minimizando equivocaciones y retrasos.

Entre sus necesidades principales se encuentran registrar pedidos de manera eficiente, consultar el menú de forma clara, identificar promociones activas y calcular automáticamente los totales de las órdenes. También necesita una interfaz intuitiva que requiera pocos pasos para completar una tarea.

Carlos se siente frustrado cuando debe corregir pedidos incorrectos o realizar cálculos manuales. Por ello valora aplicaciones simples, rápidas y enfocadas en la productividad.

#### Escenario

Carlos recibe a un grupo de clientes en la mesa 5. Desde QuickOrder selecciona la mesa correspondiente, agrega los productos solicitados, aplica una promoción vigente y visualiza automáticamente el total del pedido. Finalmente envía la orden sin errores y procesa el pago de manera eficiente.

---

### Ficha de Persona 2: Administradora del Restaurante

**Nombre:** Laura Pérez
**Edad:** 35 años
**Ocupación:** Administradora de restaurante
**Experiencia tecnológica:** Alta

Laura es responsable de supervisar las operaciones del restaurante y controlar las ventas diarias. Actualmente utiliza hojas de cálculo y registros manuales, dificultando el acceso a información actualizada y la toma de decisiones estratégicas.

Sus necesidades incluyen gestionar productos y precios, crear promociones, revisar ventas diarias y analizar indicadores de desempeño. También busca reducir pérdidas operativas y mejorar la rentabilidad del negocio mediante decisiones basadas en datos.

Laura valora sistemas organizados, visuales y centralizados que permitan administrar todos los procesos desde una única aplicación.

#### Escenario

Al finalizar la jornada laboral, Laura accede al dashboard de QuickOrder para revisar las ventas del día, identificar los productos más vendidos y analizar el impacto de las promociones. Con esta información realiza ajustes estratégicos orientados a mejorar los resultados del restaurante.

---

## 5. Requisitos funcionales

### Requisitos funcionales generales

1. Iniciar sesión.
2. Registrarse.
3. Escanear código QR.
4. CRUD de productos.
5. Gestionar promociones y descuentos.
6. Enlistar productos.
7. CRUD de pedidos.
8. CRUD de mesas.
9. Gestionar ventas.
10. Visualizar estados del pedido.
11. Operar en modo offline.
12. Configuración de la aplicación.
13. Visualizar menú con promociones.
14. Dashboard administrativo.
15. Splash Screen.

### Requisitos funcionales por rol

#### Administrador

* Iniciar sesión.
* Registrarse.
* Gestionar productos.
* Gestionar promociones.
* Gestionar descuentos.
* Gestionar pedidos.
* Gestionar ventas.
* Configurar la aplicación.
* Consultar métricas.
* Visualizar gráficos estadísticos.

#### Mesero

* Iniciar sesión.
* Visualizar productos.
* Consultar promociones.
* Crear pedidos.
* Modificar pedidos.
* Eliminar pedidos.
* Asociar pedidos a mesas.
* Finalizar pedidos.

#### Comensal (Cliente)

* Registrarse (opcional).
* Visualizar menú.
* Consultar promociones.
* Realizar pedidos.
* Modificar pedidos.
* Finalizar pedidos.

---

## 6. Validación del flujo actual

### Flujo del Cliente

1. Escanea código QR.
2. Descarga o accede a la aplicación.
3. Selecciona la mesa.
4. Visualiza el menú.
5. Realiza el pedido.
6. Consulta el estado en tiempo real.

### Flujo del Mesero

1. Recibe pedidos.
2. Coordina con cocina.
3. Actualiza el estado del pedido.
4. Gestiona la entrega.

### Flujo del Administrador

1. Gestiona productos y promociones.
2. Gestiona mesas.
3. Supervisa pedidos.
4. Analiza estadísticas.
5. Revisa métricas de ventas.

El flujo propuesto es coherente con las necesidades identificadas en el proceso de atención de restaurantes, permitiendo optimizar tiempos, reducir errores y mejorar la experiencia de usuarios y clientes.

## 6. Diseño UX/ui
aplicar diceño centrado al usuario 
aplicar usabilida y acesibilidad
guiarse con el prototipo que esta en com/example/quickorderapp/docs/prototipos
## 7.arquitectura base
base funcional completa (esqueleto real) alineada con MVVM + Clean Architecture + Room + DataStore + DI (Hilt).
estructura de  la arquitectura :
com.example.quickorderapp
├── data
│    ├── local
│    │     ├── dao
│    │     ├── database
│    │     └── entities
│    │
│    ├── datastore
│    └── repository
│
├── domain
│    ├── model
│    ├── repository
│    └── usecase
│
├── ui
│    ├── screens
│    ├── components
│    └── navigation
│
├── viewmodel 
├── di
│
├── ui.theme  
└── MainActivity.kt 

## 8. data base
Registro de usuario en la base de datos
Pantalla de configuración para persistencia local ligera (DataStore)
CRUD del proceso principal de la aplicacion en la base de datos local con el patron MVVM y ROOM (mínimo 2
tablas maestras y 1 tabla transaccional)
