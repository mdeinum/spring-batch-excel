package org.springframework.batch.extensions.item.excel.poi;

import org.springframework.batch.extensions.item.excel.AbstractExcelItemReader;
import org.springframework.batch.extensions.item.excel.AbstractExcelItemReaderTests;
import org.springframework.core.io.ClassPathResource;

public class PoiItemReaderXlsxTests extends AbstractExcelItemReaderTests {

	@Override
	protected void configureItemReader(AbstractExcelItemReader<String[]> itemReader) {
		itemReader.setResource(new ClassPathResource("player.xlsx"));
	}

	@Override
    protected AbstractExcelItemReader<String[]> createExcelItemReader() {
        return new PoiItemReader<>();
    }
}
