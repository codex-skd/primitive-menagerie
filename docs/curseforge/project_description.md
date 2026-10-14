<h1 align="center">&#129430; Primitive Menagerie</h1>

<p align="center"><strong>A menagerie of familiar, sometimes ridiculous creatures. A NeoForge 1.21.1 port of Primitive Mobs.</strong></p>

<p align="center">
<img src="https://img.shields.io/badge/loader-NeoForge-orange?style=plastic&logo=curseforge" alt="NeoForge">
<img src="https://img.shields.io/badge/minecraft-1.21.1-blue?style=plastic" alt="Minecraft 1.21.1">
<img src="https://img.shields.io/badge/side-client%20%2B%20server-brightgreen?style=plastic" alt="Client and Server">
<img src="https://img.shields.io/badge/license-CC%20BY--NC%203.0-lightgrey?style=plastic" alt="CC BY-NC 3.0">
</p>

<br>

---

<br>

<h2>&#10024; Overview</h2>

<table>
<tr>
<td width="65%">
<p>Primitive Menagerie fills the world with <strong>26 creatures</strong>: skeleton warriors that swap between bow and blade, creepers that throw TNT, leap like rockets or buff their friends, slimes that guard treasure, haunted tools, a dodo, a chameleon, a troll, a yeti and many more. Some are dangerous, some are shy, and some will trade with you.</p>

<p>It is a NeoForge 1.21.1 port of <strong>Primitive Mobs</strong> (1.12.2, version 1.2.3) by <strong>Daveyx0</strong>. <strong>The original work has been modified</strong>: it was ported from Minecraft 1.12.2 and adapted to current APIs. This port is unofficial and is not affiliated with or endorsed by the original author.</p>
</td>
<td width="35%" align="center">
<a href="https://codex.skdragons.com/" target="_blank"><img src="https://node-files.skdragons.com/uploads/MINECRAFT/Codex/logo_codex_stalking_dragons.png" alt="Codex Stalking Dragons" width="160"></a>
</td>
</tr>
</table>

<br>

<h2>&#128126; The creatures</h2>

<h3>&#9876;&#65039; Hostile (19)</h3>
<ul>
<li><strong>Skeleton Warrior</strong> &mdash; switches between ranged and melee weapons.</li>
<li><strong>Rocket Creeper</strong>, <strong>Festive Creeper</strong> and <strong>Support Creeper</strong> &mdash; one leaps at you, one throws primed TNT (it never explodes itself) and one buffs nearby creepers and Trollagers.</li>
<li><strong>Mother Spider</strong> and <strong>Baby Spider</strong> &mdash; a spider family; the babies can be tamed and ridden.</li>
<li><strong>Goblin</strong> &mdash; carries a Goblin Mace. <strong>Harpy</strong> &mdash; a flying menace.</li>
<li><strong>Treasure Slime</strong> and <strong>Brain Slime</strong> &mdash; the treasure slime carries an item and can rarely be tamed.</li>
<li><strong>Haunted Tool</strong> and <strong>Bewitched Tome</strong> &mdash; possessed gear and a flying enchanted book.</li>
<li><strong>Blazing Juggernaut</strong>, <strong>Flame Spewer</strong> and <strong>Trollager</strong> &mdash; the heavy hitters: a charging blaze, a lava-skinned fire shooter and a troll that can smash terrain.</li>
<li><strong>Lily Lurker</strong> &mdash; hides among lily pads. <strong>Void Eye</strong> &mdash; a teleporting watcher. <strong>Mimic</strong> and <strong>Yeti</strong>.</li>
</ul>

<h3>&#127807; Passive and neutral (7)</h3>
<ul>
<li><strong>Chameleon</strong> &mdash; changes colour to match its surroundings.</li>
<li><strong>Dodo</strong> &mdash; a clumsy bird that can turn grass into mycelium.</li>
<li><strong>Filch Lizard</strong> &mdash; a thief that may spawn holding loot.</li>
<li><strong>Grove Sprite</strong> &mdash; a tree spirit that plants the saplings it holds and gives Wonder Sap.</li>
<li><strong>Lost Miner</strong>, <strong>Sheepman</strong> and <strong>Traveling Merchant</strong> &mdash; villager-like characters with their own trades.</li>
</ul>

<p>Every creature has a spawn egg.</p>

<br>

<h2>&#127890; Items and recipes</h2>

<h3>&#129358; Camouflage Armor</h3>
<p>Combine a <strong>Camouflage Dye</strong> with a leather armour piece to get camouflage armour that takes on the colour of the block you stand on. Special recipes toggle the automatic camouflage on or off for a piece.</p>

<h3>&#9874;&#65039; Goblin Mace</h3>
<p>A mace made of goblin metal that does extra damage to armour. Goblins can spawn carrying one.</p>

<h3>&#127811; Wonder Sap</h3>
<p>Dropped by Grove Sprites. It remembers the log it came from and is tinted with its colour, and a recipe converts it back.</p>

<h3>&#129370; Eggs and dodo food</h3>
<p>Throwable <strong>Dodo Egg</strong> and <strong>Spider Egg</strong> (which drops a Spider Eggshell), three <strong>Mystery Eggs</strong> that hatch baby creepers, and <strong>raw and cooked Dodo</strong> meat.</p>

<h3>&#128302; Mimic Orb</h3>
<p>Use it on an empty chest to turn it into a Mimic. Chests that hide mimics can also appear in caves &mdash; open one and it may bite, spring a trap, or turn out to hold treasure.</p>

<br>

<h2>&#128717; Villagers and trading</h2>
<p>Six villager professions are added: Miner, Merchant, Fake Merchant and three Sheepman types (scavenger, alchemist and thief). The Lost Miner, Sheepman and Traveling Merchant use them, and their trades are added through the standard villager trade system. Optionally, the Miner profession can also spawn in villages.</p>

<br>

<h2>&#9881;&#65039; Configuration</h2>
<p>One common config file. You can enable or disable each creature (a disabled creature simply stops spawning, so existing worlds do not break) and tune special settings, such as:</p>
<ul>
<li>Maximum Baby Spiders per Mother Spider, the Treasure Slime tameable chance and the Filch Lizard loot chance.</li>
<li>Whether the Festive Creeper's TNT harms terrain, whether the Trollager can destroy terrain and whether Rocket Creepers always jump.</li>
<li>Mimic chest rate in caves, the Lost Miner emerald reward, the Haunted Tool durability drop, and whether Dodos convert grass and Grove Sprites plant saplings.</li>
</ul>
<p>Spawn entries are registered through Mobweave Core's spawn system.</p>

<br>

<h2>&#128220; Requirements</h2>

<table>
<tr>
<th align="left">Mod</th>
<th align="left">Type</th>
<th align="left">Version</th>
</tr>
<tr>
<td>Minecraft</td>
<td>&mdash;</td>
<td>1.21.1</td>
</tr>
<tr>
<td>NeoForge</td>
<td>Required</td>
<td>21.1.249+</td>
</tr>
<tr>
<td>Java</td>
<td>&mdash;</td>
<td>21</td>
</tr>
<tr>
<td>Mobweave Core</td>
<td>Required</td>
<td>0.0.0-beta.1+</td>
</tr>
</table>

<p><strong>Mobweave Core is required</strong>: it is our port of Multi Mob Core and provides the creature bases, AI, taming and the spawn system. Install it in the same <code>mods</code> folder.</p>

<br>

---

<br>

<h2>&#128173; Notes</h2>

<ul>
<li><strong>License: Creative Commons Attribution-NonCommercial 3.0 (CC BY-NC 3.0)</strong>, the same as the original. Non-commercial use only. Credit to <strong>Daveyx0</strong> as the original author. <strong>The original work has been modified.</strong></li>
<li>Original mod: <a href="https://www.curseforge.com/minecraft/mc-mods/primitive-mobs"><strong>Primitive Mobs</strong></a>. This port is unofficial.</li>
<li>Not ported: the original Dynamic Trees and Just Enough Resources integrations, and the Forge config GUI.</li>
<li>Some behaviours are simplified compared with the original (for example, the Camouflage Armor colour changes instantly, the Void Eye beam is simpler and the Traveling Merchant uses the standard trade system).</li>
<li><strong>Beta:</strong> the mod builds and the dedicated server starts cleanly, but it has not been played in-game yet. Expect rough edges in creature AI, rendering, spawns and trading, and please report anything odd.</li>
</ul>

<br>

---

<br>

<h2>&#128214; Credits</h2>
<p>Original mod <a href="https://www.curseforge.com/minecraft/mc-mods/primitive-mobs"><strong>Primitive Mobs</strong></a> by Daveyx0 (CC BY-NC 3.0). The original work has been modified. Models credited by the original mod: <strong>KodaichiZero</strong> (spider family, creepers and brain slime), <strong>Gibea</strong> (traveling merchant) and <strong>TehPhailed</strong> (blazing juggernaut). Port by Stalking Dragons.</p>

<p align="center">
<a href="https://codex.skdragons.com/" target="_blank"><img src="https://node-files.skdragons.com/uploads/MINECRAFT/Codex/logo_codex_stalking_dragons.png" alt="Codex Stalking Dragons" width="120"></a><br>
<a href="https://codex.skdragons.com/">codex.skdragons.com</a>
</p>
