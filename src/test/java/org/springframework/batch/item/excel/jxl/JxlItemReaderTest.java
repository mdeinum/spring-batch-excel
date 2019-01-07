package org.springframework.batch.item.excel.jxl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.excel.mapping.PassThroughRowMapper;
import org.springframework.core.io.ClassPathResource;

/**
 * Test
 */
public class JxlItemReaderTest {

    private final Log logger = LogFactory.getLog(this.getClass());

    private JxlItemReader<String[]> itemReader;

    @Before
    public void setup() throws Exception {
        this.itemReader = new JxlItemReader<>();
        this.itemReader.setLinesToSkip(1); //First line is column names
        this.itemReader.setResource(new ClassPathResource("/MAP-ICONS.xls"));
        this.itemReader.setRowMapper(new PassThroughRowMapper());
        this.itemReader.setSkippedRowsCallback((sheet, row) -> System.out.println("Skipping: " + Arrays.toString(row)));
        this.itemReader.afterPropertiesSet();
        this.itemReader.open(new ExecutionContext());
    }

    @After
    public void after() throws Exception {
        this.itemReader.close();
    }

    @Test
    public void readExcelFile() throws Exception {
        assertEquals(5, this.itemReader.getNumberOfSheets());
        String[] row;
        do {
            row = this.itemReader.read();
            this.logger.debug("Read: "+ Arrays.toString(row));
        } while (row != null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequiredProperties() throws Exception {
        final JxlItemReader reader = new JxlItemReader();
        reader.afterPropertiesSet();
    }

}
