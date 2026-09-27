# Chem Space Invaders

A small JavaFX simulation, styled after *Space Invaders*, written to generate data for a
CO₂ research paper. A player block patrols a strip at the bottom of the screen while red
blocks fall from random positions at the top. When a falling block hits the player, the
time since the round started is logged and a new round begins. Running this thousands of
times under different settings produces the collision-time data used in the paper's
calculations.

## How the simulation works

- **Window:** 1000 × 1000 px. The player is a 20 × 20 px black square at y = 805.
- **Player movement:** the player moves back and forth automatically inside the selected
  screen region, reversing direction at each edge.
- **Falling blocks:** a new 20 × 20 px red block spawns at a random x position every 50 ms,
  with at most 5 on screen at once. Blocks that leave the bottom of the screen are removed.
- **Timer:** counts in hundredths of a second and is shown as `mm:ss:hh`.
- **Collision:** the current timer value is printed to the console and appended to a raw
  data file, then the round restarts.

### Settings

| Setting | Options |
| --- | --- |
| Screen region | Left Quarter (x 0–245), Left Half (x 0–490), Right Quarter (x 730–980), Right Half (x 480–980), Full Screen (x 0–980) |
| Player speed | Slow (0.1 px/frame), Mid (0.2 px/frame), Fast (0.3 px/frame) |
| Opponent speed | Slow, Mid, Fast (player slow speed × a scaling factor of 1000) |

Pick one option from each group and press **Start Game**. Press **B** during a round to go
back to the settings screen.

## Getting started

### Requirements

- JDK 17 or newer
- [JavaFX SDK](https://gluonhq.com/products/javafx/) (the project was built against 22.0.2)

### Run from the command line

From the repository root, with `PATH_TO_FX` pointing at the JavaFX SDK's `lib` folder:

```bash
javac --module-path "$PATH_TO_FX" --add-modules javafx.controls \
      -d out "chem project/src/com/example/chem_project/"*.java

java --module-path "$PATH_TO_FX" --add-modules javafx.controls \
     -cp out com.example.chem_project.Game
```

Run it from the repository root so collision times land in the bundled `rawData` folder.
To write them somewhere else, pass `-Dchem.dataDir=/path/to/folder` to `java`.

### Run from IntelliJ IDEA

Open the repository folder as a project. The project expects the JavaFX SDK at
`~/javafx-sdk-22.0.2/lib`; if yours lives elsewhere, update the `lib` library under
**File → Project Structure → Libraries**. Create an Application run configuration for
`com.example.chem_project.Game` with these VM options:

```
--module-path <path-to-javafx-sdk>/lib --add-modules javafx.controls
```

## Data pipeline

1. **Collect** – run `Game`. Collision times are appended to `pSlow1000x.txt`,
   `pMid1000x.txt` or `pFast1000x.txt` in `rawData/`, depending on the player speed.
   Earlier datasets (see below) were collected by pointing the output at other file names.
2. **Convert** – `TimeConversion` turns `mm:ss:hh` times into decimal seconds:

   ```bash
   java -cp out com.example.chem_project.TimeConversion <input.txt> <output.txt>
   ```

3. **Filter (optional)** – `DupRemover` writes a copy of one file with every line that also
   appears in another file removed:

   ```bash
   java -cp out com.example.chem_project.DupRemover <fileToSubtract.txt> <sourceFile.txt> <output.txt>
   ```

4. **Analyse** – the converted values were pasted into the spreadsheets at the repository
   root for statistics (mean, standard deviation, median, mode) and further calculations.

## Repository layout

```
.
├── chem project/src/com/example/chem_project/
│   ├── Game.java              # the simulation (JavaFX application)
│   ├── TimeConversion.java    # mm:ss:hh -> decimal seconds
│   ├── DupRemover.java        # removes lines found in one file from another
│   ├── rawData/               # collision times as recorded (mm:ss:hh)
│   └── convertedData/         # the same times in decimal seconds
├── Datapoints and Calculations Updated.xlsx   # statistics per setting
└── OLR Calculations.xlsx                      # further calculations for the paper
```

### Data file names

Raw files use descriptive names; converted files use abbreviations.

| Raw file (`rawData/`) | Converted file (`convertedData/`) | Settings |
| --- | --- | --- |
| `p_slow o_slow.txt` | `pSlow_oSlow_decimal.txt` | player slow, opponent slow |
| `p_slow o_mid.txt` | `pSlow_oMid_decimal.txt` | player slow, opponent mid |
| `p_slow o_fast.txt` | `pSlow_oFast_decimal.txt` | player slow, opponent fast |
| `p_mid o_slow.txt` | `pmos_conv.txt` | player mid, opponent slow |
| `p_mid o_mid.txt` | `pmom_conv.txt` | player mid, opponent mid |
| `p_mid o_fast.txt` | `pmof_conv.txt` | player mid, opponent fast |
| `p_fast o_slow.txt` | `pfos_conv.txt` | player fast, opponent slow |
| `p_fast o_mid.txt` | `pfom_conv.txt` | player fast, opponent mid |
| `p_fast o_fast.txt` | `pfof_conv.txt` | player fast, opponent fast |
| `leftQMid.txt` | `lqm_conv.txt` | left quarter screen, mid speed |
| `leftHMid.txt` | `lhm_conv.txt` | left half screen, mid speed |
| `rightQuarterMid.txt` | `rqm_conv.txt` | right quarter screen, mid speed |
| `rightHalfMid.txt` | `rhm_conv.txt` | right half screen, mid speed |
| `pSlow1000x.txt` | `pSlow1000x_conv.txt` | player slow, opponent speed × 1000 |
| `pMid1000x.txt` | `pMid1000x_conv.txt` | player mid, opponent speed × 1000 |
| `pFast1000x.txt` | `pFast1000x_conv.txt` | player fast, opponent speed × 1000 |
