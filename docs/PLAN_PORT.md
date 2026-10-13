# Plan de port — Primitive Mobs 1.2.3 (1.12.2) → Primitive Menagerie (NeoForge 1.21.1)

Fuente: `temp/original/src/main/` (141 `.java`, ~20.570 líneas, 122 png, 25 ogg, 47 json, 8 lang). Paquete original `net.daveyx0.primitivemobs`. Alcance: **port fiel completo**, con modelos vanilla (`ModelPart`/`LayerDefinition`). Dependencia obligatoria: **Mobweave Core** (`libs/`, nuestro port de Multi Mob Core). Es su primer consumidor.

## Volumen por paquete

| Paquete original | Archivos | Líneas | Contenido |
|---|---|---|---|
| `entity/monster` | 23 | 8.252 | Bebé araña, madre araña, juggernaut ardiente, brain slime, libro encantado, creeper festivo/cohete/de apoyo, escupefuego, goblin, arpía, herramienta embrujada, lily lurker, mímico, esqueleto guerrero, slime del tesoro, slime domesticable, trollager, ojo del vacío, yeti |
| `entity/passive` | 7 | 2.208 | Camaleón, dodo, lagarto ladrón, duendecillo del bosque, minero perdido, ovejero, mercader viajero |
| `entity/item` | 5 | 672 | Escupitajo de fuego, TNT, huevo de araña, bloque lanzado, proyectil base |
| `entity/ai` | 4 | 492 | Goals propios |
| `client/models` | 21 | 2.815 | Modelos `ModelBase` a mano |
| `core` | 13 | 1.664 | Eventos (496), profesiones de aldeano (460), items, registro de entidades, sonidos, loot, spawn, recetas |
| `item` | 5 | 677 | Armadura/tinte de camuflaje, maza de goblin, savia, huevos y comida |
| `config`, `crafting`, `message`, `modint`, `world/gen` | 5+2+5+2+1 | ~1.000 | Config, 2 recetas propias, mensajes de red, integraciones (**descartadas**), generación de mímicos |

## Fases (cada una se delega por separado, con compilación y `runServer` al final)

| Fase | Contenido | Hito |
|---|---|---|
| P1 | Esqueleto: clase principal, `DeferredRegister` de entidades/ítems/sonidos/atributos, config (`ModConfigSpec` COMMON), `PrimitiveMenagerieReference`, eventos de sonido, listas de ids y atributos | Compila; `runServer` limpio con las 30 entidades registradas (sin comportamiento) |
| P2 | Los 21 modelos a `ModelPart`/`LayerDefinition` + renderers y `ModelLayerLocation` registrados por evento (solo cliente) | Todas las entidades tienen modelo y renderer; compila |
| P3 | Criaturas de combate simples: creepers (primitivo, festivo, cohete, de apoyo), arañas (primitiva, bebé, madre), esqueleto guerrero, goblin, arpía | Compila; `runServer` limpio |
| P4 | Criaturas complejas: brain slime, slime domesticable, slime del tesoro, trollager, yeti, ojo del vacío, escupefuego, juggernaut ardiente, lily lurker, mímico, libro encantado, herramienta embrujada | Ídem |
| P5 | Pasivas y comercio: camaleón, dodo, lagarto ladrón, duendecillo del bosque, minero perdido, ovejero, mercader viajero; profesiones de aldeano y trades en formato 1.21 | Ídem |
| P6 | Ítems (7), recetas JSON y las 2 recetas especiales (camuflaje, savia), tablas de loot (cofres y entidades), generación de mímicos, eventos, registro de spawns con la API de Mobweave Core | Ídem |
| P7 | Recursos: texturas (122), sonidos (`sounds.json` + 25 ogg), modelos de ítem, lang (8 idiomas a `.json` minúsculas), revisión de residuos `primitivemobs`/`net.daveyx0` | `runServer` limpio y comprobación de referencias |

## Decisiones de diseño

- **Modelos**: conversión mecánica de cada `ModelBase` a `LayerDefinition` (cubos, rotaciones y UV), sin GeckoLib ni dependencias nuevas. Animaciones (`setRotationAngles`) → `setupAnim`.
- **Base de las criaturas**: usar las clases e interfaces de Mobweave Core (`IMultiMob*`, bases voladoras/nadadoras, goals y domesticación por *attachments*) donde el original dependía de Multimob; el resto sobre `Monster`/`PathfinderMob`/`Slime`/`Creeper`/`Spider`/`Villager` de 1.21.
- **IDs**: `primitive_menagerie:<nombre>` en snake_case (`skeleton_warrior`, `haunted_tool`, `support_creeper`, `treasure_slime`, `mimic`, `enchanted_book`, `traveling_merchant`, `lost_miner`, `sheepman`, `baby_spider`, `mother_spider`, `trollager`, `dodo`, `lily_lurker`, `goblin`, `flame_spewer`...). Mantener una tabla id viejo → id nuevo en `docs/IDS_PRIMITIVEMOBS.md` para el conversor de plantillas de Warhost.
- **Integraciones**: DynamicTrees y JER **no se portan**.
- **Aldeanos**: profesiones y trades con los registros de 1.21 (`VillagerProfession`, `PoiType`, `VillagerTrades` por evento).
- **Config**: `COMMON`; la GUI de config de Forge no se porta.
- **Cualquier fallo de Mobweave Core que salga aquí se corrige en su repo**, no con parches locales.

## Licencia y atribución

CC BY-NC 3.0. Cabecera `// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).` en cada fichero portado. No comercial (no inscribir en CurseForge Rewards). **Modelos de terceros** (KodaichiZero, Gibea, TehPhailed): comprobar su licencia antes de publicar. No usar ni leer los otros ports de Primitive Mobs.

## Gotchas 1.12 → 1.21.1

- Datos en carpetas singulares (`loot_table/`, `recipe/`, `tags/...`); modelos de ítem en `models/item/`; sonidos con `sounds.json` y `SoundEvent` registrado.
- `EntityType` y atributos por `EntityAttributeCreationEvent`; spawn placements por `RegisterSpawnPlacementsEvent`.
- Config COMMON: no leer valores mientras se rellenan los registros.
- Eventos de juego en `NeoForge.EVENT_BUS`; cliente solo en cliente.
- Parar los daemons de Gradle al terminar cada tanda (`./gradlew.bat --stop`).
