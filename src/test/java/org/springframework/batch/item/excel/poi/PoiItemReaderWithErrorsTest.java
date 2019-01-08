package org.springframework.batch.item.excel.poi;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.FormulaError;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.excel.mapping.PassThroughRowMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Marten Deinum
 */
public class PoiItemReaderWithErrorsTest {

	private final Log logger = LogFactory.getLog(this.getClass());

	private PoiItemReader<String[]> itemReader;

	@Before
	public void setup() throws Exception {
		this.itemReader = new PoiItemReader<>();
		this.itemReader.setResource(new ClassPathResource("errors.xlsx"));
		this.itemReader.setLinesToSkip(1); // First line is column names
		this.itemReader.setRowMapper(new PassThroughRowMapper());
		this.itemReader.setSkippedRowsCallback(rs -> logger.info("Skipping: " + Arrays.toString(rs.getCurrentRow())));
		this.itemReader.afterPropertiesSet();

		ExecutionContext executionContext = new ExecutionContext();
		this.itemReader.open(executionContext);
	}

	@Test
	public void readExcelFileWithBlankRow() throws Exception {
		assertEquals(1, this.itemReader.getNumberOfSheets());
		String[] row;
		String[] lastRow = null;
		do {
			row = this.itemReader.read();
			this.logger.debug("Read: " + Arrays.toString(row));
			if (row != null) {
				lastRow = row;
				assertEquals(3, row.length);
			}
		} while (row != null);
		int readCount = (Integer) ReflectionTestUtils.getField(this.itemReader, "currentItemCount");
		assertEquals(3, readCount);
		assertEquals(FormulaError.DIV0.getString(), lastRow[2]);
	}
}
