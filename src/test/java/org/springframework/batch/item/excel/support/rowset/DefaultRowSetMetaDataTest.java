package org.springframework.batch.item.excel.support.rowset;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.item.excel.Sheet;

/**
 * Tests for {@link DefaultRowSetMetaData}
 *
 * @author Marten Deinum
 * @since 0.5.0
 */
public class DefaultRowSetMetaDataTest {

    private static final String[] COLUMNS = {"col1", "col2", "col3"};

    private DefaultRowSetMetaData rowSetMetaData;

    private Sheet sheet;
    private ColumnNameExtractor columnNameExtractor;

    @BeforeEach
    public void setup() {
        sheet = Mockito.mock(Sheet.class);
        columnNameExtractor = Mockito.mock(ColumnNameExtractor.class);
        rowSetMetaData = new DefaultRowSetMetaData(sheet, columnNameExtractor);
    }

    @Test
    public void shouldReturnColumnsFromColumnNameExtractor() {

        when(columnNameExtractor.getColumnNames(sheet)).thenReturn(COLUMNS);

        String[] names = rowSetMetaData.getColumnNames();

        assertEquals(3, names.length);
        assertArrayEquals(new String[] {"col1", "col2", "col3"}, names);

        verify(columnNameExtractor, times(1)).getColumnNames(sheet);
        verifyNoMoreInteractions(sheet, columnNameExtractor);
    }

    @Test
    public void shouldGetAndReturnNameOfTheSheet() {

        when(sheet.getName()).thenReturn("testing123");

        String name = rowSetMetaData.getSheetName();

        assertEquals("testing123", name);
        verify(sheet, times(1)).getName();
        verifyNoMoreInteractions(sheet);
    }
}