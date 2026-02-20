package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Places forts on the game map during initialization.
 * Uses FortGenerator to create fort shapes and FortValidator to ensure valid placement.
 * Attempts to place each fort at random positions until a valid location is found.
 */
public class FortPlacer {
    private static final int MAX_PLACEMENT_ATTEMPTS = 20;
    private final FortGenerator generator;
    private final FortValidator validator;
    private final Random randomNumGen;

    public FortPlacer() {
        this.generator = new FortGenerator();
        this.validator = new FortValidator();
        this.randomNumGen = new Random();
    }

    // Places N forts on the map and set their ID, returns a list of Forts for the map
    public List<Fort> placeForts(Map map, int numOfForts) throws Exception {
        List<Fort> placedForts = new ArrayList<>();

        for (int i = 0; i < numOfForts; i++) {
            // assign a letter (A, B, C,...) to each fort (set of 5 cells)
            String fortId = String.valueOf((char) ('A' + i));
            Fort fort = placeFort(map, fortId, placedForts);

            if (fort == null) {
                throw new Exception("Unable to place fort " + fortId);
            }
            placedForts.add(fort);
        }
        return placedForts;
    }

    // attempts to place one fort on the map
    private Fort placeFort(Map map, String fortId, List<Fort> existingForts) {
        int numOfAttempts = 0;

        while (numOfAttempts < MAX_PLACEMENT_ATTEMPTS) {
            numOfAttempts++;
            // 1. using FortGenerator class to generate a fort in a random shape (shapeTemplate)
            List<Cell> shapeTemplate = generator.generateFortShape(fortId);

            // 2. Select a random position to start generating from
            int startRow = randomNumGen.nextInt(map.getBoardSize());
            int startCol = randomNumGen.nextInt(map.getBoardSize());


            List<Cell> actualMapCells = translateToMapCells(shapeTemplate, startRow, startCol, map);

            if (actualMapCells == null) {
                continue; // cell that we tried to place on map is out of bounds
            }


            List<List<Cell>> existingFortCells = convertFortsToLists(existingForts);
            boolean isValid = validator.isValidPlacement(actualMapCells, existingFortCells, map.getBoardSize());

            if (!isValid) {
                // if the placement is invalid (overlap or out of bounds..) keep trying to place
                continue;
            }

            // 5. Placement is valid, create fort in this position on the map
            Fort fort = new Fort(fortId);
            for (Cell cell : actualMapCells) {
                fort.addCell(cell);
            }
            return fort;
        }
        return null;    // Failed after MAX_PLACEMENT_ATTEMPTS is reached
    }


    private List<Cell> translateToMapCells(List<Cell> shapeTemplate, int startRow, int startCol, Map map) {
        List<Cell> mapCells = new ArrayList<>();

        // for each cell in the generated fort shape, add the map's starting row and column
        for (Cell templateCell : shapeTemplate) {
            int actualRow = templateCell.getRow() + startRow;
            int actualCol = templateCell.getCol() + startCol;

            if (!map.isCellRowColValid(actualRow, actualCol)) {
                return null;    // cell is out of bounds
            }

            // find which cell is located on the map at the row and column, and add cell to mapCells
            Cell mapCell = map.getCellByRowCol(actualRow, actualCol);
            mapCells.add(mapCell);
        }
        return mapCells;
    }

    // Stream that takes a List<Fort> and converts it into a List<List<Cell>> for FortValidator
    private List<List<Cell>> convertFortsToLists(List<Fort> forts) {
        return forts.stream()
                .map(fort -> fort.getCells())
                .collect(Collectors.toList());
    }

}


