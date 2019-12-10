package org.springframework.batch.item.excel.jxl;

import jxl.Cell;
import jxl.Workbook;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link JxlUtils}.
 *
 * @author Marten Deinum
 *
 */
public class JxlUtilsTests {

    private final Cell cell1 = Mockito.mock(Cell.class);
    private final Cell cell2 = Mockito.mock(Cell.class);
    private final Cell cell3 = Mockito.mock(Cell.class);
    private final Cell cell4 = Mockito.mock(Cell.class);

    private final Workbook workbook = Mockito.mock(Workbook.class);

    @BeforeEach
    public void setup() {
        Mockito.when(this.cell1.getContents()).thenReturn("foo");
        Mockito.when(this.cell2.getContents()).thenReturn(" ");
        Mockito.when(this.cell3.getContents()).thenReturn("");
        Mockito.when(this.cell4.getContents()).thenReturn(null);
    }

    /**
     * Test the {@link JxlUtils#isEmpty(Cell)} method.
     */
    @Test
    public void checkIfCellsAreEmpty() {
        assertFalse(JxlUtils.isEmpty(this.cell1), "Cell[1] should not be empty");
        assertTrue(JxlUtils.isEmpty(this.cell2), "Cell[2] should be empty");
        assertTrue(JxlUtils.isEmpty(this.cell3), "Cell[3] should be empty");
        assertTrue(JxlUtils.isEmpty(this.cell4), "Cell[4] should be empty");
        assertTrue(JxlUtils.isEmpty((Cell) null), "[null] should be empty");
    }

    /**
     * Test the {@link JxlUtils#isEmpty(Cell[])} method.
     */
    @Test
    public void checkIfRowIsEmpty() {
        assertTrue(JxlUtils.isEmpty((Cell[]) null), "[null] should be empty");
        assertTrue(JxlUtils.isEmpty(new Cell[0]), "[null] should be empty");
        assertFalse(JxlUtils.isEmpty(new Cell[]{this.cell1, this.cell2, this.cell3}), "Cell[1] should not be empty");
        assertTrue(JxlUtils.isEmpty(new Cell[]{this.cell2, this.cell3, null}), "Cell[2] should be empty");
    }

    /**
     * Test the {@link JxlUtils#hasSheets(Workbook)} method.
     */
    @Test
    public void checkIfWorkbookHasSheets() {
        assertFalse (JxlUtils.hasSheets(null), "[null] doesn't have sheets.");

        Mockito.when(this.workbook.getNumberOfSheets()).thenReturn(5);
        assertTrue(JxlUtils.hasSheets(this.workbook), "Workbook should have sheets.");

        Mockito.when(this.workbook.getNumberOfSheets()).thenReturn(0);
        assertFalse(JxlUtils.hasSheets(this.workbook), "Workbook shouldn't have sheets.");
    }

    /**
     * Test the {@link JxlUtils#extractContents(jxl.Cell[])} method.
     */
    @Test
    public void extractContentWithEmptyCell() {
        Cell[] row = {cell1, cell2, cell3, cell4};
        String[] values = JxlUtils.extractContents(row);
        assertEquals(row.length, values.length, "Input and Output row should be equal");
    }

    @Test
    public void extractingContent() {
        assertTrue(JxlUtils.extractContents(null).length == 0, "[null] should give empty array");
    }

}
