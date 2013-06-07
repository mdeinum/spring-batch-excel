package org.springframework.batch.item.excel.poi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.excel.RowCallbackHandler;
import org.springframework.batch.item.excel.Sheet;
import org.springframework.batch.item.excel.mapping.PassThroughRowMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

public class PoiItemReaderXlsxTests {

    private final Log logger = LogFactory.getLog(this.getClass());

    private PoiItemReader itemReader;

    @Before
    public void setup() throws Exception {
        this.itemReader = new PoiItemReader();
        this.itemReader.setLinesToSkip(1); //First line is column names
        this.itemReader.setResource(new ClassPathResource("/MAP-ICONS.xlsx"));
        this.itemReader.setRowMapper(new PassThroughRowMapper());
        this.itemReader.setSkippedRowsCallback(new RowCallbackHandler() {

            public void handleRow(final Sheet sheet, final String[] row) {
                System.out.println("Skipping: " + row);
            }
        });
        this.itemReader.afterPropertiesSet();
        this.itemReader.open(new ExecutionContext());
    }

    @After
    public void after() throws Exception {
        this.itemReader.close();
    }

    @Test
    public void readExcelFile() throws Exception {
        String[] row = null;
        do {
            row = (String[]) this.itemReader.read();
            this.logger.debug("Read: "+ StringUtils.arrayToCommaDelimitedString(row));
        } while (row != null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequiredProperties() throws Exception {
        final PoiItemReader reader = new PoiItemReader();
        reader.afterPropertiesSet();
    }

}
