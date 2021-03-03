package org.springframework.batch.extensions.item.excel.poi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.FormulaError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.extensions.item.ReflectionTestUtils;
import org.springframework.batch.extensions.item.excel.mapping.PassThroughRowMapper;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Marten Deinum
 */
public class PoiItemReaderWithErrorsTest {

	private final Log logger = LogFactory.getLog(this.getClass());

	private PoiItemReader<String[]> itemReader;

	@BeforeEach
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
		Integer readCount = (Integer) ReflectionTestUtils.getField(this.itemReader, "currentItemCount");
		assertEquals(3, readCount);
		assertEquals(FormulaError.DIV0.getString(), lastRow[2]);
	}
}
