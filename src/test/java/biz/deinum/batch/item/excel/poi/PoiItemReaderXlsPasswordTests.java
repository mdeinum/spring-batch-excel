package biz.deinum.batch.item.excel.poi;

import biz.deinum.batch.item.excel.AbstractExcelItemReader;
import biz.deinum.batch.item.excel.AbstractExcelItemReaderTests;
import org.springframework.core.io.ClassPathResource;

public class PoiItemReaderXlsPasswordTests extends AbstractExcelItemReaderTests {

	@Override
	protected void configureItemReader(AbstractExcelItemReader<String[]> itemReader) {
		itemReader.setResource(new ClassPathResource("player_with_password.xls"));
		itemReader.setPassword("readme");
	}

	@Override
    protected AbstractExcelItemReader<String[]> createExcelItemReader() {
        return new PoiItemReader<>();
    }
}
