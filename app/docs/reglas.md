# Reglas del Proyecto Android - QuickOrder

## Reglas Generales

* Debes responder siempre en español.
* Debes analizar primero el contexto general del proyecto antes de proponer cambios.
* Debes respetar la arquitectura definida del proyecto.
* Debes justificar cualquier cambio arquitectónico importante.
* Debes priorizar mantenibilidad, escalabilidad y legibilidad del código.
* Debes seguir principios SOLID.
* Debes seguir Clean Code.
* Debes seguir buenas prácticas Android recomendadas por Google.
* Debes documentar decisiones arquitectónicas relevantes.
* No debes crear dependencias innecesarias.
* No debes introducir lógica de negocio dentro de las pantallas Compose.
* No debes introducir acceso directo a Room desde la UI.
* No debes duplicar lógica de negocio.
* Antes de eliminar archivos o módulos debes solicitar aprobación y explicar el motivo.

---

# Reglas de Arquitectura

## Arquitectura Base Obligatoria

Debe utilizarse:

* MVVM
* Clean Architecture
* Repository Pattern
* Dependency Injection con Hilt
* Navigation Compose
* Room Database
* DataStore
* Coroutines
* Flow / StateFlow

## Estructura Base

com.example.quickorderapp

data/
├── local/
│ ├── dao/
│ ├── database/
│ └── entities/
│
├── datastore/
│
├── repository/
│
├── remote/
│ └── firebase/

domain/
├── model/
├── repository/
└── usecase/

ui/
├── screens/
├── components/
├── navigation/
└── state/

viewmodel/

di/

ui/theme/

MainActivity.kt

---

# Reglas de UI (Jetpack Compose)

## Framework Obligatorio

* Jetpack Compose
* Material Design 3

## Diseño

* Mobile First.
* Diseño moderno.
* Diseño minimalista.
* Diseño consistente.
* Navegación intuitiva.
* Acciones importantes visibles.
* Feedback visual inmediato.
* Estados de carga claros.
* Estados vacíos claros.
* Estados de error claros.

## Composables

* Un composable debe tener una única responsabilidad.
* Los composables deben ser reutilizables.
* Los composables no deben contener lógica de negocio.
* Los composables deben recibir estado desde ViewModel.
* Los composables deben ser stateless cuando sea posible.

Ejemplo:

Correcto:

ProductCard(
product = product,
onClick = {}
)

Incorrecto:

ProductCard() {
roomDao.obtenerProductos()
}

---

# Reglas UX/UI

## Accesibilidad

La aplicación debe cumplir:

* WCAG AA
* Compatibilidad con TalkBack
* Compatibilidad con Switch Access
* Tamaños táctiles mínimos de 48dp
* Contraste adecuado
* Navegación mediante teclado cuando aplique

## Usabilidad

* Minimizar cantidad de clics.
* Evitar formularios largos.
* Mantener siempre visible el estado actual.
* Mostrar confirmaciones antes de acciones destructivas.
* Evitar carga cognitiva innecesaria.

## Experiencia de Usuario

* Mantener al usuario informado.
* Mostrar progreso de procesos largos.
* Evitar bloqueos innecesarios.
* Evitar errores prevenibles.

---

# Reglas de ViewModel

## Obligatorio

* Todo estado de UI debe venir desde ViewModel.
* Utilizar StateFlow.
* Utilizar UiState.

Ejemplo:

sealed interface ProductUiState

data object Loading

data class Success(
val products: List<Product>
)

data class Error(
val message: String
)

## Prohibido

* Acceso a Room desde Compose.
* Acceso a Firebase desde Compose.
* Lógica de negocio en Compose.

---

# Reglas de Casos de Uso

Cada acción importante debe tener su propio Use Case.

Ejemplos:

* LoginUseCase
* CreateOrderUseCase
* UpdateOrderStatusUseCase
* GetProductsUseCase
* CreatePromotionUseCase

Los ViewModel deben comunicarse únicamente con UseCases.

---

# Reglas de Room Database

## Obligatorio

* Room como fuente de verdad local.
* DAO por entidad.
* Relaciones correctamente modeladas.
* Uso de Flow para observación.

Tablas mínimas:

### Maestras

* Usuario
* Producto
* Mesa
* Promocion

### Transaccionales

* Pedido
* DetallePedido
* Venta

## Prohibido

* Queries SQL inseguras.
* Acceso directo desde UI.

---

# Reglas DataStore

Utilizar DataStore para:

* Tema oscuro.
* Configuraciones.
* Preferencias.
* Sesión local.
* Datos ligeros.

No utilizar DataStore para listas grandes o información transaccional.

---

# Reglas Firebase

## Firebase Firestore

Usar para:

* Sincronización remota.
* Dashboard en tiempo real.
* Respaldo de datos.

## Firebase Cloud Messaging

Usar para:

* Notificaciones de pedidos.
* Promociones.
* Alertas administrativas.

## Estrategia

Firestore
↓
Repository
↓
Room
↓
ViewModel
↓
Compose

Room siempre será la fuente local principal.

---

# Reglas Offline First

QuickOrder debe funcionar sin internet.

## Prioridad

1. Room
2. DataStore
3. Firebase

Flujo:

Usuario
↓
Room
↓
Sincronización
↓
Firebase

Nunca depender exclusivamente de internet.

---

# Reglas QR

Los QR deben usar Deep Links.

Ejemplo:

quickorder://menu/mesa/5

No utilizar URLs web obligatorias.

El QR debe:

* Abrir la app.
* Navegar a la mesa correspondiente.
* Cargar datos desde Room.
* Funcionar sin internet.

---

# Reglas de Navegación

Framework:

Navigation Compose

Cada módulo debe tener:

* Route
* Screen
* ViewModel

Ejemplo:

LoginScreen
ProductScreen
OrderScreen
DashboardScreen
SettingsScreen

No utilizar navegación manual mediante Intents entre pantallas Compose.

---

# Reglas de Calidad

Antes de aprobar código:

* Verificar arquitectura.
* Verificar SOLID.
* Verificar Clean Code.
* Verificar accesibilidad.
* Verificar rendimiento.
* Verificar manejo de errores.
* Verificar modo offline.
* Verificar sincronización.
* Verificar navegación.

Todo cambio debe mantener la coherencia con MVVM + Clean Architecture + Room + DataStore + Hilt + Compose.
