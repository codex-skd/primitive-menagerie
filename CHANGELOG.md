# Changelog — Primitive Menagerie

## 0.0.0-beta.1 (sin publicar)

- Port de Primitive Mobs 1.2.3 (1.12.2, Daveyx0, CC BY-NC 3.0) a NeoForge 1.21.1, ~16.300 líneas de Java sobre Mobweave Core: **30 criaturas** (19 hostiles y 7 pasivas/neutrales, más 4 proyectiles y el huevo de araña), 21 modelos vanilla (`ModelPart`/`LayerDefinition`), renderers y capas, 7 ítems reales (armadura de camuflaje con color, tinte, maza de goblin, savia, huevos, orbe del mímico, comida de dodo), 9 recetas (2 especiales), tablas de loot, 29 entradas de spawn, profesiones y comercio de aldeanos, generación de cofres mímico, red como payloads, sonidos, texturas y lang en 7 idiomas.
- `build` y `runServer` verificados (el servidor arranca limpio con todos los datos). **No probado in-game**: IA, renders, spawns, comercio y generación quedan pendientes de prueba en cliente.
- No portado: integraciones con Dynamic Trees y JER, GUI de config de Forge, texturas de bloques "cinder" que el original no usaba, iconos de entidad del original. Ver `docs/PORT_NOTES.md` para las simplificaciones por criatura.
- Tabla de ids viejos → nuevos en `docs/IDS_PRIMITIVEMOBS.md`.
