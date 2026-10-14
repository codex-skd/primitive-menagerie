# CurseForge — Variables del proyecto

> Leídas por `codex-docs/scripts/curseforge-upload.ps1`. IDs verificados con la API: 9638 Client, 9639 Server, 11779 1.21.1, 10150 NeoForge. **No se guarda el token en este fichero**: la subida se hace con curl leyendo `codex-docs/secrets.md`.

project_id = 1744360
game_versions = 9638, 9639, 11779, 10150
release_type = beta
relations = mobweave-core:requiredDependency

## Proyecto

| Variable | Valor |
|----------|-------|
| `mod_id` | `primitive_menagerie` |
| `display_name` | `Primitive Menagerie` |
| `classId` | `6` (Mods) |
| `category` | Mobs (elegir en la web; verificar el nombre exacto del desplegable) |
| `license` | CC BY-NC 3.0 (Creative Commons Attribution-NonCommercial 3.0) |
| `source_url` | `https://gitlab.com/stalking-dragons/minecraft/primitive-menagerie` |

### Summary (campo *Summary* del proyecto, editable en la web)

```
A menagerie of 26 familiar, sometimes silly creatures, plus camouflage armor, mimic chests and trades. NeoForge 1.21.1 port of Primitive Mobs.
```

(143 caracteres. Límite de CurseForge: 200.)

### Alternativas de summary

- `Skeleton warriors, TNT-throwing creepers, treasure slimes, mimics, a dodo and more. A NeoForge 1.21.1 port of Daveyx0's Primitive Mobs.`
- `26 creatures, camouflage armor, mimic chests and villager trades. Unofficial NeoForge 1.21.1 port of Primitive Mobs. Requires Mobweave Core.`

### Pendiente antes de subir

- Proyecto creado en CurseForge (ID 1744360).
- **Slug de la dependencia**: `mobweave-core` es el esperado, pero **no se pudo verificar** (la ficha estaba tras Cloudflare). Confirmarlo en la ficha de Mobweave Core (ID 1743225) antes de subir; si es otro, corregir `relations`.
- Logo: generar con `logo_prompt.md`; derivar `logo_400.png` (solo local) e `icon.png` 64×64 en la raíz de `src/main/resources`, con `logoFile="icon.png"` en `neoforge.mods.toml`.
- **NO inscribir en el programa de recompensas de CurseForge (Rewards)**: obra original modificada bajo CC BY-NC 3.0, solo uso no comercial.
- Confirmar nombre y slug `primitive-menagerie` libres.
- **TODO de licencia en `NOTICE`**: confirmar que la licencia de los modelos de terceros (KodaichiZero, Gibea, TehPhailed) permite esta adaptación antes de publicar.
- Mobweave Core debe estar publicado y accesible en CurseForge antes (o a la vez) que este mod.
