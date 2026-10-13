# Flujo de trabajo — Primitive Menagerie (NeoForge)

> **Versión del workflow**: 1.21.0 (codex-docs)
> Este archivo pertenece al proyecto **Primitive Menagerie**. Cambios aquí solo afectan a este proyecto.
> **Trabaja directamente con este archivo**: es el workflow operativo del mod, autocontenido. No leas `codex-docs/WORKFLOW_AGENT.md` ni `WORKFLOW_GENERIC.md` de forma rutinaria.
> On-demand: `codex-docs/reference/CURSEFORGE.md`, `codex-docs/reference/GRAPHIFY.md`, `codex-docs/reference/REPO_SETUP.md` (setup único de repo — pendiente), `codex-docs/reference/WIKI.md`.

## Específico del mod

| Campo | Valor |
|---|---|
| **Mod ID** | `primitive_menagerie` |
| **Clase principal** | `com.skd.primitivemenagerie.PrimitiveMenagerie` |
| **Display name** | `Primitive Menagerie` |
| **Versión Minecraft** | `1.21.1` |
| **Versión NeoForge** | `21.1.249` (no subir sin pedirlo) |
| **Rama de trabajo** | `minecraft/1.21.1/neoforge-21.1.249/production` |
| **Repositorio GitLab** | `https://gitlab.com/stalking-dragons/minecraft/primitive-menagerie.git` |
| **Paquete base** | `com.skd.primitivemenagerie` |
| **Licencia** | **CC BY-NC 3.0** (port de Primitive Mobs, Daveyx0). Solo uso no comercial |
| **Dependencia obligatoria** | `mobweave_core` (nuestro port de Multi Mob Core), jar en `libs/` |
| **CurseForge project id** | pendiente |

## Qué es

Port fiel de **Primitive Mobs 1.2.3** (1.12.2, Daveyx0, CC BY-NC 3.0) a NeoForge 1.21.1: 30 criaturas, ítems, tablas de loot, recetas, profesiones de aldeano. Fuente original en `temp/original/` (gitignored, solo lectura). Cuarto paso del bloque 1 de `warhost/docs/PLAN_DE_RUTA.md`. **Es el primer consumidor de Mobweave Core**, así que ejercita la librería: los fallos que aparezcan en ella se corrigen en su repo.

## Restricciones activas (viajan con cualquier delegación a OpenCode)

- **Nunca subir versiones** de dependencias, mods ni NeoForge.
- **Nunca borrar ficheros** sin que se pida; `temp/original/` no se toca.
- OpenCode no hace `git commit/push` ni sube nada a CurseForge. **Nunca usar `cd` ni rutas fuera del proyecto en los prompts a OpenCode** (el sandbox las rechaza y corta el turno).
- **Obligaciones de la licencia CC BY-NC 3.0**: atribución a Daveyx0 en README, NOTICE y `neoforge.mods.toml`; etiquetar que la obra ha sido modificada; misma licencia; **no uso comercial** (no inscribir en CurseForge Rewards); no implicar respaldo del autor. Cabecera de aviso en cada fichero portado.
- **No usar ni leer** los otros ports existentes (Astryxion, Misanthropy, Wondersapped): se trabaja solo desde el original de Daveyx0. Id distinto (`primitive_menagerie`).
- Sin residuos del original: paquete `net.daveyx0.primitivemobs` → `com.skd.primitivemenagerie`, namespace `primitivemobs` → `primitive_menagerie`, sin `net.minecraftforge`.
- Los modelos de terceros (KodaichiZero, Gibea, TehPhailed) mantienen su crédito; **comprobar su licencia antes de publicar**.

## Convenciones, ramas, versionado, commits

Las del grupo (`WORKFLOW_GENERIC`): snake_case `primitive_menagerie`, clase `PrimitiveMenagerie`, Title Case `Primitive Menagerie`. Rama única `production`. Versión `0.0.0-beta.X`; JAR `primitive_menagerie-1.21.1-neoforge-21.1.249-<version>.jar`. Commits en inglés con `v<version>`. Sin tags.

## Flujo por tarea

1. Desarrollo → `./gradlew.bat build` → commit → push.
2. Verificar con `runServer` puro (`printf 'stop\n' | ./gradlew.bat runServer`). Parar los daemons de Gradle al terminar (`./gradlew.bat --stop`).
3. CurseForge solo con confirmación expresa.
4. Graphify tras cada push cuando haya código.

## Idioma

Código, logs, commits, README y CurseForge en inglés. docs/ y CHANGELOG en castellano.
