# Primitive Menagerie — Logo de CurseForge

> Prompt para generar el avatar del proyecto en CurseForge (400×400). Misma familia visual que los logos del ecosistema: fondo violeta con nebulosa, marco dorado, pixel-art y un único color de acento por mod (aquí, **verde lima ácido**, distinto del verde té de Steeped, el turquesa de Enchanter's Bench y el naranja brasa de Mobweave Core).

## Prompt

```
Pixel-art mod icon for a Minecraft NeoForge creature mod called "Primitive Menagerie". 400x400 square, perfectly centered.

SUBJECT: a cheerful group of four creatures posing together like a menagerie portrait, shoulder to shoulder:
a skeleton warrior in the back left holding a small sword and round shield, a support creeper in the back right
with a friendly glowing aura, a treasure slime in front left (a translucent cube with a few gold coins and a gem
floating inside it), and a plump dodo bird in front right with a funny little tuft of feathers. Acid-lime green
sparks and a soft lime glow link the four of them, as if they were a quirky team.

COMPOSITION: the four creatures form a tight, balanced cluster that fills most of the frame; the skeleton and the
creeper stand slightly behind, the slime and the dodo in front, all readable as strong silhouettes. They stand on a
small patch of grass and dark stone so nothing floats except the sparks.

FRAME: an ornate golden border with filigree corners, echoing a gilded frame. A few small warm sparkles and
four-pointed star glints drift around the scene.

BACKGROUND: deep near-black violet with a soft purple nebula, darker at the corners so the gold frame, the
creatures and the lime glow all read clearly.

PALETTE: gold #d4af6a for the frame, acid lime #b6f03c through #6fae1a for the glow, sparks and the creeper's
aura, bone white #e8e4d4 for the skeleton, muted creeper green-grey #5f8a52 for the creeper, translucent
lime-tinted gel with gold #e6c14a coins for the slime, cream #f1e3c6 and warm tan #b98a55 for the dodo, violet
#2a1a3e for the background. One accent colour only: acid lime green.

STYLE: crisp pixel art, large readable pixels, limited palette, strong contrast, 16-bit and 32-bit pixel art
feel. Readable at 64x64 - silhouette first, detail second.

AVOID: photorealism, 3D render, smooth gradients, anti-aliasing, thin hairlines, text, letters, UI elements,
human characters, realistic animals, orange, turquoise or tea-green accents, cluttered background.
```

## Por qué cada elemento está pedido

| Elemento | Razón |
|---|---|
| Grupo de cuatro criaturas | "Menagerie" = colección de animales; el retrato de grupo lo comunica de un vistazo |
| Esqueleto guerrero, creeper de apoyo, slime del tesoro, dodo | Cuatro criaturas muy reconocibles del mod, con siluetas distintas (alto, vertical, cúbica, redondeada) |
| Tono alegre y algo ridículo | El mod es "familiar y a veces ridículo", no sombrío |
| Verde lima ácido como único acento | Regla de la familia (un acento por mod); distinto de verde té, turquesa y naranja brasa |
| Marco dorado + nebulosa violeta | Anclan el logo a la familia de logos publicados |
| "Readable at 64x64" | CurseForge muestra el avatar en tarjetas de ~100 px |

## Después de generarla

1. Comprobar que es 400×400 y que se lee a 100 px (las cuatro siluetas reconocibles).
2. Subirla como avatar del proyecto en CurseForge.
3. Opcionalmente subirla a `node-files.skdragons.com/uploads/MINECRAFT/Codex/`.
4. No versionarla en el repo (el `.gitignore` excluye `docs/curseforge/logo_*.png`).
5. Para el icono dentro del juego (`icon.png` 64×64 en la **raíz** de `src/main/resources`, y `logoFile="icon.png"` en `neoforge.mods.toml`), derivar una versión sin marco, como en Mobweave Core.
